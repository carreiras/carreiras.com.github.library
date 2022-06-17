package com.ewecarreira.library;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@SpringBootApplication
public class LibraryApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}

	@Scheduled(cron = "0 54 9 1/1 * ?")
	public void testAgendamentoTarefas() {
		System.out.println("AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO!");
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
