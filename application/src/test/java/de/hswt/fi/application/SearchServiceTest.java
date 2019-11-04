package de.hswt.fi.application;

import de.hswt.fi.search.service.mass.search.model.Entry;
import de.hswt.fi.search.service.mass.search.model.SearchParameter;
import de.hswt.fi.search.service.search.api.CompoundSearchService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationContext.class)
@Profile("test")
public class SearchServiceTest {

	@Autowired
	private CompoundSearchService searchService;

	@Before
	public void setUp() {

	}

	@Test
	public void autowired() {
		assertNotNull(searchService);
	}

	@Test
	public void testIndexSearchWithIndexO() {
		assertIndexSearch(new SearchParameter('O'), 2, "SI00000005", "SI00000008");
	}
	
	@Test
	public void testIndexSearchWithIndexT() {
		assertIndexSearch(new SearchParameter('T'), 7, "SI00000008", "SI00000028");
	}

//	@Test
	public void testDefaultSearchByMassAndPh() {
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setAccurateMass(255.0d);
		searchParameter.setPpm(1000d);
		searchParameter.setLogP(1.5d);
		searchParameter.setLogPDelta(0.6d);
		assertDefaultSearch(searchParameter, 9, "SI00002808", "SI00003856");
	}
	
	@Test
	public void testDefaultSearchByFormula() {
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setElementalFormula("C15H24O");
		assertDefaultSearch(searchParameter, 2, "SI00000026", "SI00000027");
	}
	
	@Test
	public void testDefaultSearchByName() {
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setName("benz");
		assertDefaultSearch(searchParameter, 11, "SI00000001", "SI00000028");
	}
	
	private void assertIndexSearch(SearchParameter searchParameter, int count, String firstId, String lastId) {
		List<Entry> results = searchService.searchByNameFirstCharacter(searchParameter.getIndexChar());
		results.sort(Comparator.comparing(Entry::getPublicID));
		assertEquals(count, results.size());
		assertEquals(firstId, new ArrayList<>(results).get(0).getPublicID());
		assertEquals(lastId, new ArrayList<>(results).get(results.size() - 1).getPublicID());
	}
	
	private void assertDefaultSearch(SearchParameter searchParameter, int count, String firstId, String lastId) {
		List<Entry> results = searchService.searchDynamic(searchParameter);
		results.sort(Comparator.comparing(Entry::getPublicID));
		assertEquals(count, results.size());
		assertEquals(firstId, results.get(0).getPublicID());
		assertEquals(lastId, results.get(results.size() - 1).getPublicID());
	}
}
