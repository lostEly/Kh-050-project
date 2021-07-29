package com.softserve.kh50project.davita.repository;

import com.softserve.kh50project.davita.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}
