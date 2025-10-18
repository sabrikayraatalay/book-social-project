package com.KayraAtalay.base_code;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = com.KayraAtalay.starter.BaseCodeApplicationStarter.class)
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
})
class BaseCodeApplicationStarterTests {

	@Test
	void contextLoads() {
	}

}
