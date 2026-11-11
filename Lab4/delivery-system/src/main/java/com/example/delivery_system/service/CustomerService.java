
package com.example.delivery_system.service;

import com.example.delivery_system.entity.Customer;
import com.example.delivery_system.repository.CustomerRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public List<Customer> findAll() {
        return repository.findAll();
    }

    public Optional<Customer> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<Customer> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(cacheNames="customerRead", key="#customer.id", allEntries=true)
    public Customer save(Customer customer) {
        return repository.save(customer);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(cacheNames="customerRead", key="#id")
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
