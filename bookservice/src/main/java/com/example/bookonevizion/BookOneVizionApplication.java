package com.example.bookonevizion;

import com.example.bookonevizion.dto.BookDTO;
import com.example.bookonevizion.repository.BookRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookOneVizionApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookOneVizionApplication.class, args);
	}

	@Bean
	public ApplicationRunner dataLoader(BookRepository repository) {
		return args -> {
			if(repository.findAllOrderByTitleDesc().isEmpty()) {
				repository.save(BookDTO.builder().title("Crime and Punishment").author("F. Dostoevsky").build());
				repository.save(BookDTO.builder().title("Anna Karenina").author("L. Tolstoy").build());
				repository.save(BookDTO.builder().title("The Brothers Karamazov").author("F. Dostoevsky").build());
				repository.save(BookDTO.builder().title("War and Peace").author("L. Tolstoy").build());
				repository.save(BookDTO.builder().title("Dead Souls").author("N. Gogol").build());
			}
		};
	}

}
