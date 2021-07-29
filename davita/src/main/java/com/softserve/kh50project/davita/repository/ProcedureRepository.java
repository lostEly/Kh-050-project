package com.softserve.kh50project.davita.repository;


import com.softserve.kh50project.davita.model.Procedure;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
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

    @Transactional
    @Modifying
    @Query(
            value = "update procedure set equipment_id = :equipmentId where procedure_id = :procedureId",
            nativeQuery = true)
    void registerEquipment(@Param("procedureId") Long procedureId, @Param("equipmentId") Long equipmentId);
}
