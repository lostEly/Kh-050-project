package com.softserve.kh50project.davita.repository;

import com.softserve.kh50project.davita.model.Procedure;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public interface ProcedureRepository extends JpaRepository<Procedure, Long>, JpaSpecificationExecutor<Procedure> {

    default Specification<Procedure> getProcedureQuery(String name, Double cost, String duration) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!Objects.isNull(name)) {
                predicates.add(criteriaBuilder.equal(root.get("name"), name));
            }
            if (!Objects.isNull(cost)) {
                predicates.add(criteriaBuilder.equal(root.get("cost"), cost));
            }
            if (!Objects.isNull(duration)) {
                LocalTime convertedDuration = LocalTime.parse(duration);
                predicates.add(criteriaBuilder.equal(root.get("duration"), convertedDuration));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
