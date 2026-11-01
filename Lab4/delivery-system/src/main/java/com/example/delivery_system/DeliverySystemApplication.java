package com.example.delivery_system;

import com.example.delivery_system.entity.Customer;
import com.example.delivery_system.entity.Depot;
import com.example.delivery_system.entity.Vehicle;
import com.example.delivery_system.repository.CustomerRepository;
import com.example.delivery_system.repository.DepotRepository;
import com.example.delivery_system.repository.VehicleRepository;
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
	CommandLineRunner testRepositories(
			CustomerRepository customerRepository,
			DepotRepository depotRepository,
			VehicleRepository vehicleRepository) {

		return args -> {
			// Create or retrieve a customer.
			String email = "alice@example.com";

			Customer customer = customerRepository.findByEmail(email)
					.orElseGet(() -> customerRepository.save(
							new Customer(
									"Alice Popescu",
									email,
									"0712345678",
									"Strada Exemplului 10, Iasi",
									"47.1585,27.6014"
							)
					));

			System.out.println("Customer: " + customer.getName());

			// Create or retrieve a depot.
			Depot depot = depotRepository.findByName("Main Depot")
					.orElseGet(() -> depotRepository.save(
							new Depot("Main Depot", "47.1585,27.6014")
					));

			// Create a vehicle associated with that depot.
			Vehicle vehicle = vehicleRepository.findById(1L)
					.orElseGet(() -> vehicleRepository.save(
							new Vehicle("Van 1", 1000, depot)
					));

			System.out.println("Depot: " + depot.getName());
			System.out.println("Vehicle: " + vehicle.getName());
			System.out.println("Vehicle depot: "
					+ vehicle.getDepot().getName());

			// Test the derived repository query.
			System.out.println("Available vehicles:");
			vehicleRepository.findByAvailableTrue()
					.forEach(v -> System.out.println(v.getName()));

			// Test the explicit JPQL query.
			System.out.println("Vehicles at Main Depot:");
			vehicleRepository.findVehiclesByDepotName("Main Depot")
					.forEach(v -> System.out.println(v.getName()));

			System.out.println("Total customers: "
					+ customerRepository.count());
		};
	}
}