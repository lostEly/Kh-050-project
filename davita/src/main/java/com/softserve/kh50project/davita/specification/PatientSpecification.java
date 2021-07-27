package com.softserve.kh50project.davita.specification;

import com.softserve.kh50project.davita.model.Patient;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class PatientSpecification implements Specification<Patient>{

    private final String name;
    private final String lastName;
    private final LocalDate dateOfBirth;

    private PatientSpecification(String name, String lastName, LocalDate dateOfBirth) {
        this.name = name;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public static PatientSpecification create(String name, String lastName, LocalDate dateOfBirth) {
        return new PatientSpecification(name, lastName, dateOfBirth);
    }

    @Override
    public Predicate toPredicate(Root<Patient> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return new DoctorPredicateBuilder(criteriaBuilder, root)
                .name(name)
                .lastName(lastName)
                .dateOfBirth(dateOfBirth)
                .build();
    }

    private static class DoctorPredicateBuilder {

        private final List<Predicate> predicates;
        private final CriteriaBuilder criteriaBuilder;
        private final Root<Patient> root;

        DoctorPredicateBuilder(CriteriaBuilder criteriaBuilder, Root<Patient> root) {
            this.predicates = new ArrayList<>();
            this.criteriaBuilder = criteriaBuilder;
            this.root = root;
        }

        DoctorPredicateBuilder name(String name) {
            if (nonNull(name)) {
                predicates.add(criteriaBuilder.equal(root.get("name"), name));
            }

            return this;
        }

        DoctorPredicateBuilder lastName(String lastName) {
            if (nonNull(lastName)) {
                predicates.add(criteriaBuilder.equal(root.get("lastName"), lastName));
            }

            return this;
        }

        DoctorPredicateBuilder dateOfBirth(LocalDate dateOfBirth) {
            if (nonNull(dateOfBirth)) {
                predicates.add(criteriaBuilder.equal(root.get("dateOfBirth"), dateOfBirth));
            }

            return this;
        }

        @SuppressWarnings("all")
        Predicate build() {
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }

    }
}
