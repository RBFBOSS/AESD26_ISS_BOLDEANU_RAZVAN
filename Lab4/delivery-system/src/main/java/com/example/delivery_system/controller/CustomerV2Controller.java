package com.example.delivery_system.controller;
import com.example.delivery_system.dto.CachedCustomerDTO;
import com.example.delivery_system.service.CustomerReadService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
@RestController
@RequestMapping("/api/v2/customers")
public class CustomerV2Controller {
    private final CustomerReadService service;
    public CustomerV2Controller(CustomerReadService service) { this.service=service; }
    private EntityModel<CachedCustomerDTO> model(CachedCustomerDTO c) {
        return EntityModel.of(c,
            linkTo(methodOn(CustomerV2Controller.class).getOne(c.id())).withSelfRel(),
            linkTo(methodOn(CustomerV2Controller.class).getAll()).withRel("customers"),
            linkTo(methodOn(DeliverySearchController.class).search(c.id(),null,null,null,0,20)).withRel("deliveries"));
    }
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CachedCustomerDTO>> getOne(@PathVariable Long id) {
        CachedCustomerDTO c=service.cached(id);
        return c==null ? ResponseEntity.notFound().build() : ResponseEntity.ok(model(c));
    }
    @GetMapping
    public CollectionModel<EntityModel<CachedCustomerDTO>> getAll() {
        List<EntityModel<CachedCustomerDTO>> items=service.all().stream().map(this::model).toList();
        return CollectionModel.of(items, linkTo(methodOn(CustomerV2Controller.class).getAll()).withSelfRel());
    }
}
