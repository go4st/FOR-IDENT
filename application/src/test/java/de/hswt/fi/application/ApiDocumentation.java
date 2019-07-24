package de.hswt.fi.application;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestApplicationContext.class)
@ActiveProfiles("test")
public class ApiDocumentation {

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@Before
	public void setUp() {

		RestDocumentationResultHandler document = document("{method-name}", preprocessResponse(prettyPrint()));

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).alwaysDo(document)
				.apply(documentationConfiguration(this.restDocumentation).uris().withScheme("https")
						.withHost("water.for-ident.org").withPort(443))
				.build();
	}

	@Test
	public void substanceRootEntryPointIsAvailable() throws Exception {
		mockMvc.perform(get("/api/substances")).andExpect(status().isUnauthorized());
	}

	@Test
	public void inchiKeysRootEntryPointIsAvailable() throws Exception {
		mockMvc.perform(get("/api/substances/inchiKeys?page=1&size=5")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(document("{method-name}", preprocessResponse(prettyPrint()),
						requestParameters(parameterWithName("page").description("The page to retrieve"),
								parameterWithName("size").description("Entries per page"))));
	}

	@Test
	public void idsRootEntryPointIsAvailable() throws Exception {
		mockMvc.perform(get("/api/substances/ids?page=1&size=5")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(document("{method-name}", preprocessResponse(prettyPrint()),
						requestParameters(parameterWithName("page").description("The page to retrieve"),
								parameterWithName("size").description("Entries per page"))));
	}

	@Test
	public void inchiKeysAndIdsRootEntryPointIsAvailable() throws Exception {
		mockMvc.perform(get("/api/substances/inchiKeysAndIds?page=1&size=5")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(document("{method-name}", preprocessResponse(prettyPrint()),
						requestParameters(parameterWithName("page").description("The page to retrieve"),
								parameterWithName("size").description("Entries per page"))));
	}

	@Test
	public void listSubstanceByElementalFormula() throws Exception {
		mockMvc.perform(get("/api/substances?elementalFormula=C16H12Cl2N4O4&page=0&size=1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(document("{method-name}", preprocessResponse(prettyPrint()),
						requestParameters(
								parameterWithName("elementalFormula").description("The formula to search for"),
								parameterWithName("page").description("The page to retrieve"),
								parameterWithName("size").description("Entries per page"))));
	}

	@Test
	public void listSubstanceByMassRange() throws Exception {
		mockMvc.perform(get("/api/substances?accurateMassMin=100&accurateMassMax=300&page=1&size=1"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(document("{method-name}", preprocessResponse(prettyPrint()),
						requestParameters(parameterWithName("accurateMassMin").description("The lower bound"),
								parameterWithName("accurateMassMax").description("The upper bound"),
								parameterWithName("page").description("The page to retrieve"),
								parameterWithName("size").description("Entries per page"))));
	}

	@Test
	public void listSubstanceById() throws Exception {
		mockMvc.perform(get("/api/substances?id=SI00000001&id=SI00000002&id=PI00000001&id=PI00000002"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(document("{method-name}", preprocessResponse(prettyPrint()),
						requestParameters(parameterWithName("id")
								.description("The STOFF-IDENT id. Limited to 100 per request"))));
	}

	@Test
	public void listSubstanceByInchiKey() throws Exception {
		mockMvc.perform(get("/api/substances?inchiKey=MXWJVTOOROXGIU-UHFFFAOYSA-N")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(document("{method-name}", preprocessResponse(prettyPrint()),
						requestParameters(parameterWithName("inchiKey").description("The Inchi Key to search for"))));
	}
}
