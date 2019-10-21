package de.hswt.fi.search.service.index.henry;

import de.hswt.fi.model.Feature;
import de.hswt.fi.model.Score;
import de.hswt.fi.search.service.henry.model.HenrySearchResult;
import de.hswt.fi.search.service.index.api.IndexSearchService;
import de.hswt.fi.search.service.index.model.IndexJob;
import de.hswt.fi.search.service.index.model.IndexSearchResult;
import de.hswt.fi.search.service.index.model.IndexSearchResults;
import de.hswt.fi.search.service.index.model.IndexSettings;
import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.search.service.mass.search.model.properties.NumberSearchProperty;
import de.hswt.fi.search.service.search.api.CompoundSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class HenrySearchService implements IndexSearchService {

	private static final int TIMEOUT = 10000;

	private static final Logger LOGGER = LoggerFactory.getLogger(HenrySearchService.class);

	private List<HenrySearchResult> candidates;

	@Override
	public IndexSearchResults executeJob(IndexJob job, List<CompoundSearchService> searchServices) {
		LOGGER.debug("enter getResults with processing data {}", job);

		candidates = new ArrayList<>();
		IndexSettings settings = job.getSettings();

		if (!searchServices.isEmpty()) {
			ExecutorService executor = Executors.newCachedThreadPool();

			LOGGER.debug("starting RTI search for {} targets",
					job.getFeatureSet().getFeatures().size());

			for (Feature feature : job.getFeatureSet().getFeatures()) {
				double ppm = settings.getPpm();
				if (feature.isMassCalculated()) ppm = settings.getFormulaDerivedMassesPpm();
				executor.execute(new Evaluator(ppm, feature, searchServices));
			}

			LOGGER.debug("search for all targets data started");
			executor.shutdown();
			try {
				LOGGER.debug("waiting for RTI search to finish");
				executor.awaitTermination(TIMEOUT, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				LOGGER.debug("waiting search to finish failed: {}", e.getMessage());
				Thread.currentThread().interrupt();
			}
		} else {
			LOGGER.debug("No search service available, RTI search not performed");
		}

		Comparator<HenrySearchResult> byTarget = Comparator.comparing(HenrySearchResult::getTargetIdentifier);
		Comparator<HenrySearchResult> byScore = Comparator.comparing(t -> t.getScore().getScoreValue());

		candidates = candidates.stream().sorted(byTarget.thenComparing(byScore.reversed()))
				.collect(Collectors.toList());

		LOGGER.debug("RTI search finished, return {} entries", candidates.size());

		Set<String> locatedFeatureIds = new HashSet<>();
		candidates.forEach(r -> locatedFeatureIds.add(r.getTargetIdentifier()));

		return new IndexSearchResults<>(candidates);
	}

	@Override
	public IndexSearchResult getDummyResult() {
		return new HenrySearchResult("", null, Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
	}

	/**
	 * The Class Evaluator is the container for each thread (runnable).
	 */
	private class Evaluator implements Runnable {

		private static final double SCORE_MAX_HENRY_DELTA = 5;
		private final List<CompoundSearchService> searchServices;

		private double ppm;

		private Feature feature;

		/**
		 * Instantiates a new evaluator.
		 *
		 * @param ppm             the ppm
		 * @param feature         the target feature
		 * @param searchServices
		 */
		private Evaluator(double ppm, Feature feature, List<CompoundSearchService> searchServices) {
			this.ppm = ppm;
			this.feature = feature;
			this.searchServices = searchServices;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			double searchValue = feature.getNeutralMass();

			ArrayList<HenrySearchResult> tempEntries = new ArrayList<>();
			List<Entry> searchResult = searchServices.stream()
					.map(searchService -> searchService.searchByAccurateMass(new NumberSearchProperty(searchValue, ppm)))
					.flatMap(List::stream)
					.collect(Collectors.toList());

			for (Entry entry : searchResult) {
				tempEntries.add(new HenrySearchResult(feature.getIdentifier(), entry,
						feature.getRetentionTime(), searchValue, ppm, feature.getRetentionTimeIndex(), feature.getRetentionTimeSignal()));
			}

			calculateScore(tempEntries);
			findBestMatch(tempEntries);

			synchronized (candidates) {
				candidates.addAll(tempEntries);
			}
		}

		private void calculateScore(ArrayList<HenrySearchResult> entries) {
			if (entries == null || entries.isEmpty()) {
				return;
			}

			for (HenrySearchResult entry : entries) {
				Score scoreSummery;
				if (entry.isAvailable()) {
					double henry = entry.getEntry().getHenryBond().getValue();
					double diff = Math.abs(entry.getRetentionTimeSignal() - henry);
					double score = diff < SCORE_MAX_HENRY_DELTA ? (1.0 - diff / SCORE_MAX_HENRY_DELTA)
							: 0.0;
					scoreSummery = new Score(score, 1.0, score);
				} else {
					scoreSummery = new Score(Double.NaN, 1.0, Double.NaN);
				}
				entry.setScore(scoreSummery);
			}
		}

		private void findBestMatch(ArrayList<HenrySearchResult> entries) {

			if (entries == null || entries.isEmpty()) {
				return;
			}

			Optional<HenrySearchResult> optional = entries.stream()
					.max(Comparator.comparingDouble(entry -> entry.getScore().getScoreValue()));

			HenrySearchResult rtiSearchResult = optional.get();
			rtiSearchResult.setFirst(true);

			optional = entries.stream().min(Comparator.comparingDouble(e -> e.getScore().getScoreValue()));

			if (optional.isPresent()) {
                HenrySearchResult r = optional.get();
				r.setLast(true);
			}
		}
	}
}
