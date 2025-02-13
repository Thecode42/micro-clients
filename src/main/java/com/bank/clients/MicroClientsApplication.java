package com.bank.clients;

import com.bank.clients.mensajes.MessagesClient;
import com.bank.clients.mensajes.MessagesException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({MessagesException.class, MessagesClient.class})
public class MicroClientsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroClientsApplication.class, args);
	}

}
