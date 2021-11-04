package ar.edu.unq.solotravel.backend.api;

import ar.edu.unq.solotravel.backend.api.dtos.CreateTripDto;
import ar.edu.unq.solotravel.backend.api.dtos.TokenResponseDto;
import ar.edu.unq.solotravel.backend.api.dtos.TravelAgencyLoginDto;
import ar.edu.unq.solotravel.backend.api.dtos.TravelAgencyRegisterDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/data.sql"})
class BackendSoloTravelApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper writer;
	private String createTripDtoJSON;
	private String agencyLoginDtoJSON;
	private CreateTripDto createTripDto;

	@BeforeEach
	void setUp() throws JsonProcessingException {
		writer =new ObjectMapper();
		writer.registerModule(new JavaTimeModule());
		writer.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		LocalDate tripDtoStartDate = LocalDate.of(2021,10,10);
		LocalDate tripDtoEndDate = LocalDate.of(2021,10,11);
		createTripDto = new CreateTripDto("trip", "destination", "image", "description", 200.0, tripDtoStartDate, tripDtoEndDate);
		createTripDtoJSON = writer.writeValueAsString(createTripDto);

		TravelAgencyLoginDto agencyLoginDto = new TravelAgencyLoginDto("guestTravelAgent1@gmail.com", "guest");
		agencyLoginDtoJSON = writer.writeValueAsString(agencyLoginDto);
	}

	//--------------------------------------TRIP CONTROLLER--------------------------------------

	@Test
	void getAllTrips() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/trips"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips[0].name").value("Gualeguaychú con navegación por el río: para desconectar"));
	}

	@Test
	void getAllTripsPassingNameFilter() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/trips")
				.param("name", "salta"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips[0].name").value("Salta: una aventura única"));
	}

	@Test
	void getTripById() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/trips/{tripId}", -1)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Mendoza, más cerca de las nubes"));
	}

	@Test
	void getTripByIdOfInexistentId() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/trips/{tripId}", 200)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No Trip with Id: 200"));
	}

	@Test
	void getAllTripsForGivenUser() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", -1)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips[0].name").value("Gualeguaychú con navegación por el río: para desconectar"));
	}

	@Test
	void getAllTripsForGivenUserPassingWrongUserId() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", 200)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().is(404))
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No User with Id: 200"));
	}

	@Test
	void getAllTripsPassingForGivenUserNameFilter() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", -1)
				.param("name", "salta")
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips[0].name").value("Salta: una aventura única"));
	}

	//--------------------------------------AGENCY CONTROLLER--------------------------------------

	@Test
	void getAgencyTrips() throws Exception {

		String token = getAgencyToken();

		mockMvc.perform(MockMvcRequestBuilders.get("/agencies/{agencyId}/trips", -2)
				.header("Authorization", token))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips").isArray());
	}

	@Test
	void createTrip() throws Exception {

		String token = getAgencyToken();

		mockMvc.perform(MockMvcRequestBuilders.post("/agencies/{agencyId}/new", -2)
				.header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(createTripDtoJSON))
				.andDo(print())
				.andExpect(status().isOk());
	}


	@Test
	void updateTrip() throws Exception {
		String token = getAgencyToken();

		mockMvc.perform(MockMvcRequestBuilders.post("/agencies/{agencyId}/new", -2)
				.header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(createTripDtoJSON))
				.andDo(print())
				.andExpect(status().isOk());

		createTripDto.setDescription("description2");
		createTripDtoJSON = writer.writeValueAsString(createTripDto);

		mockMvc.perform(MockMvcRequestBuilders.put("/agencies/{agencyId}/edition/{tripId}",-2, 1)
				.header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(createTripDtoJSON))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	void updateInexistentTrip() throws Exception {
		String token = getAgencyToken();

		mockMvc.perform(MockMvcRequestBuilders.put("/agencies/{agencyId}/edition/{tripId}",-2, 1)
				.header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(createTripDtoJSON))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No Trip with Id: 1"));
	}

	@Test
	void deleteTrip() throws Exception{
		String token = getAgencyToken();

		mockMvc.perform(MockMvcRequestBuilders.post("/agencies/{agencyId}/new", -2)
				.header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(createTripDtoJSON))
				.andDo(print())
				.andExpect(status().isOk());

		mockMvc.perform(MockMvcRequestBuilders.delete("/agencies/{agencyId}/deletion/{tripId}",-2, 1)
				.header("Authorization", token))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	void deleteInexistentTrip() throws Exception{
		String token = getAgencyToken();

		mockMvc.perform(MockMvcRequestBuilders.delete("/agencies/{agencyId}/deletion/{tripId}",-2, 1)
				.header("Authorization", token))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No Trip with Id: 1"));
	}

	//--------------------------------------TRAVELER CONTROLLER--------------------------------------

	@Test
	void addTripToUserFavouritesTrips() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}/favorites/{tripId}", -1, -1)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	void addInexistentTripToUserFavouritesTrips() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}/favorites/{tripId}", -1, 100)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().is(404))
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No Trip with Id: 100"));
	}

	@Test
	void addTripToInexistentUserFavouritesTrips() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}/favorites/{tripId}", 100, -1)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No User with Id: 100"));
	}

	@Test
	void removeTripFromUserFavouritesTrips() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}/favorites/{tripId}", -1, -1)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().isOk());

		mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}/favorites/{tripId}", -1, -1)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	void removeInexistentTripToUserFavouritesTrips() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}/favorites/{tripId}", -1, 100)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().is(404))
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No Trip with Id: 100"));
	}

	@Test
	void removeTripToInexistentUserFavouritesTrips() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}/favorites/{tripId}", 100, 1)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().is(404))
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No User with Id: 100"));
	}

	@Test
	void getAllFavouritesTripsFromAUser() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}/favorites/{tripId}", -1, -1)
				.header("Authorization", "Bearer mockJwtToken"));

		mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}/favorites", -1)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips[0].name").value("Mendoza, más cerca de las nubes"));
	}

	@Test
	void getAllFavouritesTripsFromAnInexistentUser() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}/favorites", 100)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().is(404))
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No User with Id: 100"));
	}

	//--------------------------------------REGISTRATION CONTROLLER--------------------------------------

	@Test
	void travelAgencyRegistration() throws Exception {

		TravelAgencyRegisterDto travelAgencyRegisterDto = new TravelAgencyRegisterDto("TravelAgencyName", "mail@gmail.com", "password",
				"fiscalName", 20222222222L, "province", "city", "street", 123,
				"firstName", "surname", 11111111, 20222222222L);
		String travelAgencyRegisterDtoJSON = writer.writeValueAsString(travelAgencyRegisterDto);

		mockMvc.perform(MockMvcRequestBuilders.post("/register/travelAgency")
				.contentType(MediaType.APPLICATION_JSON)
				.content(travelAgencyRegisterDtoJSON))
				.andDo(print())
				.andExpect(status().isOk());
	}

	//--------------------------------------PRIVATE CONTROLLER--------------------------------------

	private String getAgencyToken() throws Exception {
		String responseJson = mockMvc.perform(MockMvcRequestBuilders.post("/authentication/login/internal")
				.contentType(MediaType.APPLICATION_JSON)
				.content(agencyLoginDtoJSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		TokenResponseDto tokenResponse = writer.readValue(responseJson, TokenResponseDto.class);
		return tokenResponse.getToken();
	}
}
