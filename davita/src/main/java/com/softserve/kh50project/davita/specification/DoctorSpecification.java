package com.softserve.kh50project.davita.specification;

import com.softserve.kh50project.davita.model.Doctor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class DoctorSpecification implements Specification<Doctor> {

    private final String specialization;

    private DoctorSpecification(String specialization) {
        this.specialization = specialization;
    }

    public static DoctorSpecification create(String specification) {
        return new DoctorSpecification(specification);
    }

    @Override
    public Predicate toPredicate(Root<Doctor> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return new DoctorPredicateBuilder(criteriaBuilder, root)
                .specialization(specialization)
                .build();
    }

    private static class DoctorPredicateBuilder {

        private final List<Predicate> predicates;
        private final CriteriaBuilder criteriaBuilder;
        private final Root<Doctor> root;

        DoctorPredicateBuilder(CriteriaBuilder criteriaBuilder, Root<Doctor> root) {
            this.predicates = new ArrayList<>();
            this.criteriaBuilder = criteriaBuilder;
            this.root = root;
        }

        DoctorPredicateBuilder specialization(String specialization) {
            if (nonNull(specialization)) {
                predicates.add(criteriaBuilder.equal(root.get("specialization"), specialization));
            }

            return this;
        }

        @SuppressWarnings("all")
        Predicate build() {
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }

    }
}
