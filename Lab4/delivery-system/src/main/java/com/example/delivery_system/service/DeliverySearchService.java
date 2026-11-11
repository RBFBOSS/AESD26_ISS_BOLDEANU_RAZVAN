package com.example.delivery_system.service;
import com.example.delivery_system.dto.*;
import com.example.delivery_system.entity.Delivery;
import com.example.delivery_system.repository.DeliveryCriteriaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class DeliverySearchService {
    private final DeliveryCriteriaRepository repository;
    public DeliverySearchService(DeliveryCriteriaRepository repository) { this.repository = repository; }
    @Transactional(readOnly = true)
    public Page<DeliveryResponseDTO> search(DeliverySearchDTO f, Pageable pageable) {
        return repository.search(f, pageable).map(this::dto);
    }
    private DeliveryResponseDTO dto(Delivery d) {
        return new DeliveryResponseDTO(d.getId(), d.getCustomer().getId(), d.getCustomer().getName(), d.getQuantity(), d.getNotes(), d.getTimestamp());
    }
}
