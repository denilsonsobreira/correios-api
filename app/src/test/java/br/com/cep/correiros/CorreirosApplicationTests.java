package br.com.cep.correiros;

import br.com.cep.correiros.model.Address;
import br.com.cep.correiros.service.CorreiosService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.springtest.MockServerTest;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;


@MockServerTest({"correios.base.url=http://localhost:${mockServerPort}/ceps.csv"})
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
class CorreirosApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private CorreiosService correiosService;
	private MockServerClient mockServerClient;


	@Test
	@Order(1)
	public void testGetZipcodeWhenNotReady() throws Exception{
		mockMvc.perform(get("/zipcode/63508270")).andExpect(status().isServiceUnavailable());
	}

	@Test
	@Order(2)
	public void testSetupNotOk() throws Exception{
		String bodyStr = "SP,São Paulo,Vila Formosa,3358150,Rua Ituri,,,,,,,,,,";

		mockServerClient.when(request()
						.withPath("/ceps.csv")
						.withMethod("Get"))
				.respond(response()
						.withBody("ERROR").withStatusCode(500));

		assertThrows(Exception.class, () -> correiosService.setup());
	}

	@Test
	@Order(3)
	public void testSetupOk() throws Exception{
		String bodyStr = "SP,São Paulo,Vila Formosa,3358150,Rua Ituri,,,,,,,,,,";

		mockServerClient.when(request()
						.withPath("/ceps.csv")
						.withMethod("Get"))
				.respond(response()
						.withBody(bodyStr).withStatusCode(200));

		correiosService.setup();
	}

	@Test
	@Order(4)
	public void testGetZipcodeThatDoenstExists() throws Exception{
			mockMvc.perform(get("/zipcode/12345678")).andExpect(status().isNoContent());
	}

//	@Test
//	public void testGetZipcodeOk() throws Exception {
//		MvcResult mockResult= mockMvc.perform(get("/zipcode/63508270")).andExpect(status().isOk()).andReturn();
//
//		String resultStr = mockResult.getResponse().getContentAsString();
//
//		String addressToCompare = new ObjectMapper().writeValueAsString(
//				Address.builder()
//				.zipcode("63508270")
//				.street("Rua Mauro Maia")
//				.district("Areias II")
//				.city("Iguatu")
//				.state("CE")
//				.build());
//
////		String jsonToCompare = new ObjectMapper().writeValueAsString(addressToCompare);
//
//		JSONAssert.assertEquals(addressToCompare, resultStr, false);
//
//
//		/*{
//			"zipcode": "63508270",
//			"street": "Rua Mauro Maia",
//			"district": "Areias II",
//			"city": "Iguatu",
//			"state": "CE"
//		}*/
//	}

}
