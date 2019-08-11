package org.launchcode.rallymvc;

import org.launchcode.rallymvc.controllers.UserPicController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class RallyMvcApplication {

	public static void main(String[] args) {
		new File(UserPicController.uploadDirectory).mkdir();
		SpringApplication.run(RallyMvcApplication.class, args);
	}

}
