package com.example.delivery_system.service;
import com.example.delivery_system.dto.CachedCustomerDTO;
import com.example.delivery_system.entity.Customer;
import com.example.delivery_system.repository.CustomerRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class CustomerReadService {
    private final CustomerRepository repository;
    public CustomerReadService(CustomerRepository repository) { this.repository = repository; }
    @Transactional(readOnly=true)
    @Cacheable(cacheNames="customerRead", key="#id", unless="#result == null")
    public CachedCustomerDTO cached(Long id) { return uncached(id); }
    @Transactional(readOnly=true)
    public CachedCustomerDTO uncached(Long id) { return repository.findById(id).map(this::map).orElse(null); }
    @Transactional(readOnly=true)
    public List<CachedCustomerDTO> all() { return repository.findAll().stream().map(this::map).toList(); }
    private CachedCustomerDTO map(Customer c) { return new CachedCustomerDTO(c.getId(),c.getName(),c.getEmail(),c.getPhone(),c.getAddress(),c.getCoordinates()); }
}
