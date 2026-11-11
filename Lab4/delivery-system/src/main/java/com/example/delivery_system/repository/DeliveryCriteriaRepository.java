package com.example.delivery_system.repository;
import com.example.delivery_system.dto.DeliverySearchDTO;
import com.example.delivery_system.entity.Delivery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DeliveryCriteriaRepository {
    @PersistenceContext private EntityManager em;

    public Page<Delivery> search(DeliverySearchDTO filters, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Delivery> cq = cb.createQuery(Delivery.class);
        Root<Delivery> root = cq.from(Delivery.class);
        List<Predicate> predicates = predicates(cb, root, filters);
        cq.select(root).where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(root.get("id")));
        List<Delivery> results = em.createQuery(cq)
                .setFirstResult(Math.toIntExact(pageable.getOffset()))
                .setMaxResults(pageable.getPageSize()).getResultList();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Delivery> countRoot = countQuery.from(Delivery.class);
        countQuery.select(cb.count(countRoot)).where(predicates(cb, countRoot, filters).toArray(new Predicate[0]));
        long total = em.createQuery(countQuery).getSingleResult();
        return new PageImpl<>(results, pageable, total);
    }

    private List<Predicate> predicates(CriteriaBuilder cb, Root<Delivery> root, DeliverySearchDTO f) {
        List<Predicate> ps = new ArrayList<>();
        if (f.customerId() != null) ps.add(cb.equal(root.get("customer").get("id"), f.customerId()));
        if (f.area() != null && !f.area().isBlank()) {
            ps.add(cb.like(cb.lower(root.get("customer").get("address")), "%" + f.area().toLowerCase(java.util.Locale.ROOT) + "%"));
        }
        if (f.minQuantity() != null) ps.add(cb.greaterThan(root.get("quantity"), f.minQuantity()));
        if (f.date() != null) {
            LocalDateTime start = f.date().atStartOfDay();
            ps.add(cb.greaterThanOrEqualTo(root.get("timestamp"), start));
            ps.add(cb.lessThan(root.get("timestamp"), start.plusDays(1)));
        }
        return ps;
    }
}
