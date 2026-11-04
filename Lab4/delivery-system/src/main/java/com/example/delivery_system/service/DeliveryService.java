package com.example.delivery_system.service;

import com.example.delivery_system.dto.DeliveryRequestDTO;
import com.example.delivery_system.dto.DeliveryResponseDTO;
import com.example.delivery_system.entity.Customer;
import com.example.delivery_system.entity.Delivery;
import com.example.delivery_system.exception.ResourceNotFoundException;
import com.example.delivery_system.repository.CustomerRepository;
import com.example.delivery_system.repository.DeliveryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final CustomerRepository customerRepository;

    public DeliveryService(
            DeliveryRepository deliveryRepository,
            CustomerRepository customerRepository) {
        this.deliveryRepository = deliveryRepository;
        this.customerRepository = customerRepository;
    }

    private DeliveryResponseDTO toDTO(Delivery delivery) {
        return new DeliveryResponseDTO(
                delivery.getId(),
                delivery.getCustomer().getId(),
                delivery.getCustomer().getName(),
                delivery.getQuantity(),
                delivery.getNotes(),
                delivery.getTimestamp()
        );
    }

    private Delivery getDelivery(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Delivery not found: " + id));
    }

    public List<DeliveryResponseDTO> findAll() {
        return deliveryRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public DeliveryResponseDTO findById(Long id) {
        return toDTO(getDelivery(id));
    }

    @Transactional
    public DeliveryResponseDTO create(DeliveryRequestDTO dto) {

        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found: " + dto.customerId()));

        Delivery delivery = new Delivery(
                customer,
                dto.quantity(),
                dto.notes()
        );

        return toDTO(deliveryRepository.save(delivery));
    }

    @Transactional
    public DeliveryResponseDTO update(Long id, DeliveryRequestDTO dto) {

        Delivery delivery = getDelivery(id);

        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found: " + dto.customerId()));

        delivery.setCustomer(customer);
        delivery.setQuantity(dto.quantity());
        delivery.setNotes(dto.notes());

        return toDTO(deliveryRepository.save(delivery));
    }

    @Transactional
    public void delete(Long id) {
        deliveryRepository.delete(getDelivery(id));
    }

    public List<DeliveryResponseDTO> findByCustomerId(Long customerId) {
        return deliveryRepository.findByCustomerId(customerId)
                .stream()
                .map(this::toDTO)
                .toList();
    }
}