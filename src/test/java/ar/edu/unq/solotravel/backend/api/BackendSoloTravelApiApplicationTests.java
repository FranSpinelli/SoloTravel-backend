package ar.edu.unq.solotravel.backend.api;

import ar.edu.unq.solotravel.backend.api.dtos.*;
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
	private String updateTripDtoJSON;
	private String agencyLoginDtoJSON;
	private CreateTripDto createTripDto;
	private UpdateTripDto updateTripDto;

	@BeforeEach
	void setUp() throws JsonProcessingException {
		writer =new ObjectMapper();
		writer.registerModule(new JavaTimeModule());
		writer.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		LocalDate tripDtoStartDate = LocalDate.of(2021,12,10);
		LocalDate tripDtoEndDate = LocalDate.of(2021,12,11);
		createTripDto = new CreateTripDto("trip", "destination", "image", "description", 200.0, tripDtoStartDate, tripDtoEndDate, 10);
		createTripDtoJSON = writer.writeValueAsString(createTripDto);

		updateTripDto = new UpdateTripDto(1, "trip", "destination", "image", "description", 200.0, tripDtoStartDate, tripDtoEndDate, 10);
		updateTripDtoJSON = writer.writeValueAsString(updateTripDto);

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
				.param("destination", "salta"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips[0].name").value("Salta: una aventura única"));
	}

	@Test
	void getTripById() throws Exception {

		String token = getAgencyToken();
		mockMvc.perform(MockMvcRequestBuilders.get("/trips/{tripId}", -1)
				.header("Authorization", token))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Mendoza, más cerca de las nubes"));
	}

	@Test
	void getTripByIdOfInexistentId() throws Exception {

		String token = getAgencyToken();
		mockMvc.perform(MockMvcRequestBuilders.get("/trips/{tripId}", 200)
				.header("Authorization", token))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No Trip with Id: 200"));
	}

	@Test
	void getAllTripsForGivenUser() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/travelers/{userId}", -1)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips[0].name").value("Gualeguaychú con navegación por el río: para desconectar"));
	}

	@Test
	void getAllTripsForGivenUserPassingWrongUserId() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/travelers/{userId}", 200)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().is(404))
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No User with Id: 200"));
	}

	@Test
	void getAllTripsPassingForGivenUserNameFilter() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/travelers/{userId}", -1)
				.param("destination", "salta")
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

		updateTripDto.setDescription("description2");
		updateTripDtoJSON = writer.writeValueAsString(updateTripDto);

		mockMvc.perform(MockMvcRequestBuilders.put("/agencies/{agencyId}/edition/{tripId}",-2, 1)
				.header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(updateTripDtoJSON))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	void updateInexistentTrip() throws Exception {
		String token = getAgencyToken();

		mockMvc.perform(MockMvcRequestBuilders.put("/agencies/{agencyId}/edition/{tripId}",-2, 1)
				.header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(updateTripDtoJSON))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The Agency does not contain a trip with Id: 1"));
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
		mockMvc.perform(MockMvcRequestBuilders.put("/travelers/{userId}/favorites/{tripId}", -1, -1)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	void addInexistentTripToUserFavouritesTrips() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/travelers/{userId}/favorites/{tripId}", -1, 100)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().is(404))
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No Trip with Id: 100"));
	}

	@Test
	void addTripToInexistentUserFavouritesTrips() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/travelers/{userId}/favorites/{tripId}", 100, -1)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No User with Id: 100"));
	}

	@Test
	void removeTripFromUserFavouritesTrips() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/travelers/{userId}/favorites/{tripId}", -1, -1)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().isOk());

		mockMvc.perform(MockMvcRequestBuilders.delete("/travelers/{userId}/favorites/{tripId}", -1, -1)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	void removeInexistentTripToUserFavouritesTrips() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/travelers/{userId}/favorites/{tripId}", -1, 100)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().is(404))
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No Trip with Id: 100"));
	}

	@Test
	void removeTripToInexistentUserFavouritesTrips() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/travelers/{userId}/favorites/{tripId}", 100, 1)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().is(404))
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No User with Id: 100"));
	}

	@Test
	void getAllFavouritesTripsFromAUser() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/travelers/{userId}/favorites/{tripId}", -1, -1)
				.header("Authorization", "Bearer mockJwtToken"));

		mockMvc.perform(MockMvcRequestBuilders.get("/travelers/{userId}/favorites", -1)
				.header("Authorization", "Bearer mockJwtToken"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips[0].name").value("Mendoza, más cerca de las nubes"));
	}

	@Test
	void getAllFavouritesTripsFromAnInexistentUser() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/travelers/{userId}/favorites", 100)
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
				"firstName", "surname", "11111111", 20222222222L);
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
		return "Bearer " + tokenResponse.getToken();
	}
}
