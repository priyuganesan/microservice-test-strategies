package com.cac.microservice.provider;

import com.cac.microservice.provider.config.DBConnectionProvider;
import com.cac.microservice.provider.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {ProviderApplication.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
class ProviderApplicationTests {

	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
			.withDatabaseName("test") // Optionally, specify a database name
			.withUsername("test")
			.withPassword("test");

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;


	@BeforeEach
	void setUp() {

		DBConnectionProvider connectionProvider = new DBConnectionProvider(
				postgres.getJdbcUrl(),
				postgres.getUsername(),
				postgres.getPassword()
		);
		// Create the user table
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS public.user (id VARCHAR(255) PRIMARY KEY, name VARCHAR(255))");
	}

	@Test
	void testInsertAndRetrieve() {
		jdbcTemplate.execute("INSERT INTO public.user (id, name) VALUES ('1', 'Test Name')");
		String name = jdbcTemplate.queryForObject("SELECT name FROM public.user WHERE id = '1'", String.class);

		// Verify the record
		assertThat(name).isEqualTo("Test Name");
	}

	@Test
	void testCreateUser() {
		// Create a User object
		User user = new User("1", "Test User");

		// Construct the full URL using the injected random port
		String url = "http://localhost:" + port + "/users";

		// Send POST request to /users endpoint
		ResponseEntity<User> response = restTemplate.postForEntity(url, user, User.class);

		// Assert that the response status is 200 OK
		assertThat(response.getStatusCodeValue()).isEqualTo(201);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getId()).isEqualTo("1");
		assertThat(response.getBody().getName()).isEqualTo("Test User");
	}

}
