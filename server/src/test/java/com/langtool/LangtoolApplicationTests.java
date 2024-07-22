package com.langtool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.langtool.object.Definition;
import com.langtool.dto.WordDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class LangtoolApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void testGetWords() throws Exception {
		mockMvc.perform(get("/words"))
				.andExpect(status().isOk());
	}

	@Test
	void testAddWord() throws Exception {
		WordDto word = new WordDto();
		word.setOrigin("example");

		mockMvc.perform(post("/words")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(word)))
				.andExpect(status().isCreated());
	}

	@Test
	void testAddWordAndGetWords() throws Exception {
		// Add a word
		WordDto word = new WordDto();
		word.setOrigin("example");

		mockMvc.perform(post("/words")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(word)))
				.andExpect(status().isCreated());

		// Get the words
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/words",
				String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		List<WordDto> words = objectMapper.readValue(response.getBody(), List.class);
		assertThat(words.size()).isGreaterThan(0);
	}

	@Test
	void testSubmitDefinition() throws Exception {
		WordDto word = new WordDto();
		word.setOrigin("parler");

		mockMvc.perform(post("/words")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(word)))
				.andExpect(status().isCreated());

		Definition submission = new Definition();
		submission.setDefinition("to speak or to tell");

		mockMvc.perform(post("/submit")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(submission)))
				.andExpect(status().isCreated());

		// todo: check that response rating is 4.5 out of 5 or better. Robot should also
		// say nothing is missing.
	}
}
