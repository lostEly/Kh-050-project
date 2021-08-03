package com.softserve.kh50project.davita.repository;

import com.softserve.kh50project.davita.model.Doctor;
import com.softserve.kh50project.davita.model.Order;
import com.softserve.kh50project.davita.model.Patient;
import com.softserve.kh50project.davita.model.Procedure;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    default Specification<Order> getOrderQuery(LocalDateTime start, LocalDateTime finish, Procedure procedure, Doctor doctor, Patient patient) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!Objects.isNull(start)) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("start"), start));
            }
            if (!Objects.isNull(finish)) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("finish"), finish));
            }
            if (!Objects.isNull(procedure)) {
                predicates.add(criteriaBuilder.equal(root.get("procedure"), procedure));
            }
            if (!Objects.isNull(doctor)) {
                predicates.add(criteriaBuilder.equal(root.get("doctor"), doctor));
            }
            if (!Objects.isNull(patient)) {
                predicates.add(criteriaBuilder.equal(root.get("patient"), patient));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
