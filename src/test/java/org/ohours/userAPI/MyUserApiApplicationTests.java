package org.ohours.userAPI;

import org.junit.jupiter.api.Test;
import org.ohours.userAPI.controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MyUserApiApplicationTests {

	@Autowired
	private UserController userController;

	@Test
	void contextLoads() {
		assertThat(userController).isNotNull();
	}

}
