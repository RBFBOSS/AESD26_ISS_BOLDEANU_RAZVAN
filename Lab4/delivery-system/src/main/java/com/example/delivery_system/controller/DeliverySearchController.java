package com.example.delivery_system.controller;
import com.example.delivery_system.dto.*;
import com.example.delivery_system.service.DeliverySearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
@RestController
@RequestMapping({"/api/v2/deliveries", "/api/deliveries"})
public class DeliverySearchController {
    private final DeliverySearchService service;
    public DeliverySearchController(DeliverySearchService service) { this.service = service; }
    @GetMapping("/search")
    public Page<DeliveryResponseDTO> search(@RequestParam(required=false) Long customerId,
        @RequestParam(required=false) String area, @RequestParam(required=false) Integer minQuantity,
        @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="20") int size) {
        if (page < 0 || size < 1 || size > 200) throw new IllegalArgumentException("page must be >= 0 and size 1..200");
        return service.search(new DeliverySearchDTO(customerId, area, minQuantity, date), PageRequest.of(page, size));
    }
}
