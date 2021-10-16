package ar.edu.unq.solotravel.backend.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/data.sql"})
class BackendSoloTravelApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void getAllTrips() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/trips/{userId}", 1))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips[0].name").value("Mendoza, más cerca de las nubes"));
	}

	@Test
	void getAllTripsPassingWrongUserId() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/trips/{userId}", 200))
				.andDo(print())
				.andExpect(status().is(404))
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No User with Id: 200"));
	}

	@Test
	void getAllTripsPassingNameFilter() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/trips/{userId}", 1)
				.param("name", "salta"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips[0].name").value("Salta: una aventura única"));
	}

	@Test
	void addTripToUserFavouritesTrips() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}/favorites/{tripId}", 1, 1))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	void addInexistentTripToUserFavouritesTrips() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}/favorites/{tripId}", 1, 100))
				.andDo(print())
				.andExpect(status().is(404))
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No Trip with Id: 100"));
	}

	@Test
	void addTripToInexistentUserFavouritesTrips() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}/favorites/{tripId}", 100, 1))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No User with Id: 100"));
	}

	@Test
	void removeTripFromUserFavouritesTrips() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}/favorites/{tripId}", 1, 1))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	void removeInexistentTripToUserFavouritesTrips() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}/favorites/{tripId}", 1, 100))
				.andDo(print())
				.andExpect(status().is(404))
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No Trip with Id: 100"));
	}

	@Test
	void removeTripToInexistentUserFavouritesTrips() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}/favorites/{tripId}", 100, 1))
				.andDo(print())
				.andExpect(status().is(404))
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No User with Id: 100"));
	}

	@Test
	void getAllFavouritesTripsFromAUser() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}/favorites/{tripId}", 1, 1));

		mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}/favorites", 1))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips").isArray())
				.andExpect(MockMvcResultMatchers.jsonPath("$.trips[0].name").value("Mendoza, más cerca de las nubes"));
	}

	@Test
	void getAllFavouritesTripsFromAnInexistentUser() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}/favorites", 100))
				.andDo(print())
				.andExpect(status().is(404))
				.andExpect(content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No User with Id: 100"));
	}
}
