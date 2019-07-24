package de.hswt.fi.ui.vaadin.views.components.processing;

import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import de.hswt.fi.calculation.service.api.CalculationService;
import de.hswt.fi.common.ValueFormatUtil;
import de.hswt.fi.model.Peak;
import de.hswt.fi.msms.service.model.MsMsCandidateFragment;
import de.hswt.fi.processing.service.model.ProcessCandidate;
import de.hswt.fi.ui.vaadin.CustomValoTheme;
import de.hswt.fi.ui.vaadin.UIMessageKeys;
import de.hswt.fi.ui.vaadin.mschart.MsChart;
import de.hswt.fi.ui.vaadin.mschart.MsChartConfig;
import de.hswt.fi.ui.vaadin.mschart.MsSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.i18n.I18N;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@SpringComponent
@PrototypeScope
public class MsMsDetailsComponent extends CssLayout {

    private static final long serialVersionUID = 1L;

    private static final int PRECURSOR_IMAGE_WIDTH = 300;

    private static final int PRECURSOR_IMAGE_HEIGHT = 300;

    private static final int FRAGMENT_IMAGE_WIDTH = 200;

    private static final int FRAGMENT_IMAGE_HEIGHT = 200;

    private final I18N i18n;

    private final CalculationService calculationService;

    private ProcessCandidate candidate;

    private Map<Peak, MsMsCandidateFragment> peakStructures;

    private CssLayout currentFragementImageLayout;

    private MsChart chart;

    private ArrayList<Peak> matchingPeaks;

    private List<Peak> fragmentPeaks;

    private List<Peak> targetPeaks;

    private Peak selectedPeak;

    @Autowired
    public MsMsDetailsComponent(I18N i18n, CalculationService calculationService) {
        peakStructures = new HashMap<>();
        this.i18n = i18n;
        this.calculationService = calculationService;
    }

    public void init(ProcessCandidate candidate) {

        // Enable layout update on window resize
        Page.getCurrent().addBrowserWindowResizeListener(new BrowserWindowResizeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void browserWindowResized(BrowserWindowResizeEvent event) {
                initLayout();
            }
        });

        this.candidate = candidate;

        fragmentPeaks = new ArrayList<>();
        candidate.getMsMsCandidate().getFragments().forEach(
                f -> fragmentPeaks.add(f.getPeak()));

        targetPeaks = candidate.getMsMsCandidate().getTargetPeaks();

        initLayout();
    }

    private void initLayout() {

        removeAllComponents();

        setWidth("100%");

        // Top layout containing chart, candidate and current selection
        CssLayout topLayout = new CssLayout();
        topLayout.setWidth("100%");
        topLayout.setHeight("50%");
        topLayout.addStyleName(CustomValoTheme.MSMS_SECTION_MARGIN);

        // Chart
        MsChartConfig config = new MsChartConfig()
                .withAxisCaptionX("m/z")
                .withAxisCaptionY("rel. Intensity")
                .withBarStrokeWidth(3)
                .withAxisColor("#474747")
                .withSelectionColor("#f4bc42")
                .withHoverColor("#9c9c9c")
                .withTextHoverColor("#383838");

        chart = new MsChart(new ArrayList<>(), config);
        chart.setSizeFull();
        chart.addValueChangeListener(this::valueChanged);

        drawChart(targetPeaks, fragmentPeaks);


        CssLayout chartWrapper = new CssLayout();
        chartWrapper.addStyleName(CustomValoTheme.MARGIN_TOP);
        chartWrapper.addStyleName(CustomValoTheme.PADDING_TOP);
        chartWrapper.addStyleName(CustomValoTheme.PADDING_RIGHT);
        chartWrapper.setWidth("48%");
        chartWrapper.setHeight("100%");
        chartWrapper.addComponent(chart);
        topLayout.addComponent(chartWrapper);

        // Candidate
        CssLayout candidateImageLayout = new CssLayout();
        candidateImageLayout.addStyleName(CustomValoTheme.MSMS_IMAGE_MARGIN_LEFT);
        candidateImageLayout.setWidth("25%");
        candidateImageLayout.setHeight("100%");

        Label label = new Label(candidate.getEntry().getName().getValue());
        label.setWidth("100%");
        label.setHeight("15%");
        label.addStyleName(CustomValoTheme.PADDING_VERTICAL);
        label.addStyleName(CustomValoTheme.TEXT_CENTER);
        label.addStyleName(CustomValoTheme.LABEL_OVERFLOW_DOTTED);
        candidateImageLayout.addComponent(label);

        if (candidate.getMsMsCandidate().getSmiles() != null) {
            CssLayout candidateImage = getImage(candidate.getEntry().getElementalFormula().getValue(),
                    candidate.getEntry().getAccurateMass().getValue(), candidate.getEntry().getSmiles().getValue(),
                    PRECURSOR_IMAGE_WIDTH, PRECURSOR_IMAGE_HEIGHT);
            candidateImage.setWidth("100%");
            candidateImage.setHeight("75%");
            candidateImageLayout.addComponent(candidateImage);
        }

        topLayout.addComponent(candidateImageLayout);

        // Current selected
        CssLayout wrapperLayout = new CssLayout();
        wrapperLayout.addStyleName(CustomValoTheme.MSMS_IMAGE_MARGIN_LEFT);
        wrapperLayout.setWidth("25%");
        wrapperLayout.setHeight("100%");
        topLayout.addComponent(wrapperLayout);

        Label currentSelectedLabel = new Label(
                i18n.get(UIMessageKeys.MS_MS_DETAILS_CURRENT_SELECTION_CAPTION));
        currentSelectedLabel.setWidth("100%");
        currentSelectedLabel.setHeight("15%");
        currentSelectedLabel.addStyleName(CustomValoTheme.TEXT_CENTER);
        currentSelectedLabel.addStyleName(CustomValoTheme.PADDING_VERTICAL);
        wrapperLayout.addComponent(currentSelectedLabel);

        currentFragementImageLayout = new CssLayout();
        currentFragementImageLayout.setWidth("100%");
        currentFragementImageLayout.setHeight("75%");
        wrapperLayout.addComponent(currentFragementImageLayout);

        // Bottom layout containing fragment images
        CssLayout imageLayout = new CssLayout();
        imageLayout.setWidth("100%");
        // 48% because of layout bug
        imageLayout.setHeight("48%");
        imageLayout.addStyleName(CustomValoTheme.CSS_LAYOUT_SCROLLBAR);

        // add images in reverse order
        LinkedList<MsMsCandidateFragment> imageList = new LinkedList<>(candidate.getMsMsCandidate().getFragments());

        for (int i = 0; i < imageList.size(); i++) {

            // Add images in reverse order
            int index = imageList.size() - 1 - i;
            MsMsCandidateFragment f = imageList.get(index);

            CssLayout image = getImage(f, FRAGMENT_IMAGE_WIDTH, FRAGMENT_IMAGE_HEIGHT);
            image.addStyleName(CustomValoTheme.MSMS_IMAGE);
            image.addStyleName(CustomValoTheme.MSMS_IMAGE_MARGIN_BOTTOM);

            // Add margins for all non edge images (edge images: first image in each row, 5 images in a row
            // -> 0,5,10...)

            if ((i % 5) != 0) {
                image.addStyleName(CustomValoTheme.MSMS_IMAGE_MARGIN_LEFT);
            }

            image.addLayoutClickListener(event -> {
                if (event.getButton() == MouseEventDetails.MouseButton.LEFT) {
                    setCurrentFragmentSelection(f.getPeak());
                }
            });
            imageLayout.addComponent(image);
            peakStructures.put(f.getPeak(), f);
        }

        addComponent(topLayout);
        addComponent(imageLayout);

        if (selectedPeak != null) {
            setCurrentFragmentSelection(selectedPeak);
        }
    }

    private void valueChanged() {

		MsSeries.Point value = chart.getValue();

		if (value != null) {
			Optional<Peak> match = matchingPeaks.stream().filter(peak -> peak.getMz() == value.getX()).findFirst();
            match.ifPresent(this::setCurrentFragmentSelection);
		}
	}

	private void drawChart(List<Peak> peaks, List<Peak> fragments) {

        List<Peak> nonMatchingPeaks = peaks.stream()
                .filter(p -> fragments.stream().noneMatch(f -> f.getMz() == p.getMz()))
                .collect(Collectors.toList());

        matchingPeaks = new ArrayList<>(peaks);
        matchingPeaks.removeAll(nonMatchingPeaks);

		addSeries("match with in silico", matchingPeaks, "#7AB800");
		addSeries("ms data", nonMatchingPeaks, "#0058BC");
	}

	private void addSeries(String seriesName, List<Peak> peaks, String color) {
		MsSeries series = new MsSeries(seriesName);
		series.setColor(color);
		peaks.forEach(peak -> series.addPoint(new MsSeries.Point(peak.getMz(), peak.getRelativeIntensity())));
		chart.addSeries(series);
	}

    private CssLayout getImage(MsMsCandidateFragment fragment, int width, int height) {
        return getImage(fragment.getFormula(), fragment.getMass(), fragment.getSmiles(), width, height);
    }

    private CssLayout getImage(String formula, double mass, String smiles, int width, int height) {

        CssLayout layout = new CssLayout();
        layout.addStyleName(CustomValoTheme.BORDER_COLOR_ALT1);

        CssLayout captionLayout = new CssLayout();
        captionLayout.setWidth("100%");
        captionLayout.setHeight("30%");
        captionLayout.addStyleName(CustomValoTheme.PADDING);

        layout.addComponent(captionLayout);

        Label formulaLabel = new Label(formula);
        formulaLabel.addStyleName(CustomValoTheme.LABEL_VERY_BOLD);
        formulaLabel.addStyleName(CustomValoTheme.TEXT_CENTER);
        captionLayout.addComponent(formulaLabel);

        Label massLabel = new Label(ValueFormatUtil.formatForMass(mass));
        massLabel.addStyleName(CustomValoTheme.TEXT_CENTER);
        captionLayout.addComponent(massLabel);

        byte[] createdImage = calculationService.getSmilesAsImage(smiles, width,
                height);

        if (createdImage == null) {
            return layout;
        }
        StreamSource imageSource = new RenderedImageSource(createdImage);
        StreamResource resource = new StreamResource(imageSource, System.currentTimeMillis() + "");

        CssLayout wrapper = new CssLayout();
        wrapper.setWidth("100%");
        wrapper.setHeight("70%");

        Image image = new Image(null, resource);
        image.addStyleName(CustomValoTheme.IMAGE_CENTER);
        image.addStyleName(CustomValoTheme.MAX_WIDTH_100_PERCENT);
        image.addStyleName(CustomValoTheme.MAX_HEIGHT_100_PERCENT);
        wrapper.addComponent(image);
        layout.addComponent(wrapper);

        return layout;
    }

    private void setCurrentFragmentSelection(Peak peak) {

        selectedPeak = peak;

		currentFragementImageLayout.removeAllComponents();

        if (!peakStructures.containsKey(peak)) {
            return;
        }

        CssLayout image = getImage(peakStructures.get(peak), PRECURSOR_IMAGE_WIDTH,
                PRECURSOR_IMAGE_HEIGHT);
        image.setSizeFull();
        chart.select(chart.findValue(peak.getMz(), peak.getRelativeIntensity()));
        currentFragementImageLayout.addComponent(image);
    }

    public class RenderedImageSource implements StreamSource {

        private static final long serialVersionUID = 1L;

        private byte[] imageData;

        RenderedImageSource(byte[] imageData) {
            this.imageData = imageData;
        }

        @Override
        public InputStream getStream() {
            return new ByteArrayInputStream(imageData);
        }
    }
}
