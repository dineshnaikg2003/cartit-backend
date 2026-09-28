package com.cartit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Requires active PostgreSQL database")
class ApplicationTests {

	@Test
	@Disabled("Requires active PostgreSQL instance")
	void contextLoads() {
	}

}
