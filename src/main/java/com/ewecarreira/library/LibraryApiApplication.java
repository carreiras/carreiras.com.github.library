package com.ewecarreira.library;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

// @EnableScheduling
@SpringBootApplication
public class LibraryApiApplication {

	// @Autowired
	// private EmailService emailService;

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	// @Bean
	// public CommandLineRunner runner() {
	// 	return args -> {
	// 		List<String> emails = Arrays.asList("48b9750bf6-8cac98+1@inbox.mailtrap.io");
	// 		emailService.sendEmails("Testando o serviço de emails", emails);
	// 		System.out.println("E-mails enviados!!!!");
	// 	};
	// }

	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}

}
