
package com.example.delivery_system;

import com.example.delivery_system.entity.Customer;
import com.example.delivery_system.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DeliverySystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliverySystemApplication.class, args);
	}

	@Bean
	CommandLineRunner testCustomerRepository(
			CustomerRepository customerRepository) {

		return args -> {
			String email = "alice@example.com";

			Customer customer = customerRepository
					.findByEmail(email)
					.orElseGet(() -> customerRepository.save(
							new Customer(
									"Alice Popescu",
									email,
									"0712345678",
									"Strada Exemplului 10, Iasi",
									"47.1585,27.6014"
							)
					));

			System.out.println("Saved or found customer:");
			System.out.println(customer);

			System.out.println("All customers:");
			customerRepository.findAll()
					.forEach(System.out::println);

			System.out.println("Total customers: "
					+ customerRepository.count());
		};
	}
}
