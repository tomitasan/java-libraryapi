package com.cursodsousa.libraryapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class LibraryApiApplication {

	//retorna uma instância do ModelMapper para o contexto Spring
	//cria um singleton do ModelMapper para servir toda a aplicação - onde precisar, é só "injetar"
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Scheduled(cron = "0 44 14 1/1 * ?")
	public void testeAgendamentoTarefas(){
		System.out.println("AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO!");
	}

	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}
}
