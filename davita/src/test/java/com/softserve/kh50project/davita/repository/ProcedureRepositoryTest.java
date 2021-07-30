package com.softserve.kh50project.davita.repository;

import com.softserve.kh50project.davita.model.Equipment;
import com.softserve.kh50project.davita.model.Procedure;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("classpath:application-test.properties")
class ProcedureRepositoryTest {

    @Autowired
    private ProcedureRepository procedureRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private TestEntityManager entityManager;

    private List<Procedure> procedures;

    @BeforeEach
    void setUp() {
        procedures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Procedure p = new Procedure();
            p.setName("procedure" + i);
            p.setCost((double) i);
            p.setDuration(LocalTime.of(i, i, i));
            p = entityManager.persistAndFlush(p);
            procedures.add(p);
        }
    }

    @AfterEach
    void tearDown() {
        procedures = null;
    }

    @Test
    @Order(1)
    @DisplayName("find by id returns correct object")
    void findById() {
        Procedure procedure = procedureRepository.findById(3L).get();
        assertEquals(procedure, procedures.get(2));
    }

    @Test
    @Order(2)
    @DisplayName("find all works properly")
    void findAll() {
        List<Procedure> foundProcedures = procedureRepository.findAll();
        assertEquals(5, foundProcedures.size());
        assertEquals("procedure0", foundProcedures.get(0).getName());
    }

    @Test
    @Order(3)
    @DisplayName("find all returns an empty list if db is empty")
    void findAll1() {
        for (Procedure procedure : procedures) {
            entityManager.remove(procedure);
        }
        List<Procedure> foundProcedures = procedureRepository.findAll();
        assertEquals(0, foundProcedures.size());
    }

    @Test
    @Order(4)
    @DisplayName("procedure query creates proper query with all null parameters")
    void getProcedureQuery() {
        Specification<Procedure> specification = procedureRepository.getProcedureQuery(null, null, null);
        List<Procedure> foundProcedures = procedureRepository.findAll(specification);
        assertEquals(foundProcedures.size(), procedures.size());
        assertEquals(foundProcedures.get(3), procedures.get(3));
    }

    @Test
    @Order(5)
    @DisplayName("procedure query creates proper query with single parameter")
    void getProcedureQuery1() {
        Specification<Procedure> specification = procedureRepository.getProcedureQuery("procedure1", null, null);
        List<Procedure> foundProcedures = procedureRepository.findAll(specification);
        assertEquals(1, foundProcedures.size());
        assertEquals(foundProcedures.get(0), procedures.get(1));
    }

    @Test
    @Order(6)
    @DisplayName("procedure query creates proper query with two parameters")
    void getProcedureQuery2() {
        Specification<Procedure> specification = procedureRepository.getProcedureQuery("procedure1", 1.0, null);
        List<Procedure> foundProcedures = procedureRepository.findAll(specification);
        assertEquals(1, foundProcedures.size());
        assertEquals(foundProcedures.get(0), procedures.get(1));
    }

    @Test
    @Order(7)
    @DisplayName("procedure query creates proper query with all parameters")
    void getProcedureQuery3() {
        Specification<Procedure> specification = procedureRepository.getProcedureQuery("procedure1", 1.0, "01:01:01");
        List<Procedure> foundProcedures = procedureRepository.findAll(specification);
        assertEquals(1, foundProcedures.size());
        assertEquals(foundProcedures.get(0), procedures.get(1));
    }

    @Test
    @Order(8)
    @DisplayName("create adds object in database")
    void create() {
        Procedure p = createProcedure();
        Procedure foundProcedure = procedureRepository.findById(p.getProcedureId()).get();
        assertEquals(p.getProcedureId(), foundProcedure.getProcedureId());
        assertEquals(p.getName(), foundProcedure.getName());
    }

    @Test
    @Order(9)
    @DisplayName("update changes the object in database")
    void update() {
        Procedure p = createProcedure();
        String newName = "changedName";
        p.setName(newName);
        procedureRepository.save(p);
        Procedure foundProcedure = procedureRepository.findById(p.getProcedureId()).get();
        assertEquals(p.getProcedureId(), foundProcedure.getProcedureId());
        assertEquals(newName, foundProcedure.getName());
    }

    @Test
    @Order(10)
    @DisplayName("delete removes the object in database")
    void delete() {
        Procedure p = createProcedure();
        procedureRepository.delete(p);
        assertFalse(procedureRepository.findById(p.getProcedureId()).isPresent());
    }

    @Test
    @Order(11)
    @DisplayName("register equipment works properly")
    void registerEquipment() {
        Procedure procedure = createProcedure();
        Equipment equipment = new Equipment();
        equipment.setName("equipment");
        equipment.addProcedure(procedure);
        equipmentRepository.save(equipment);
        procedureRepository.registerEquipment(procedure.getProcedureId(), equipment.getEquipmentId());
        Procedure procedure1 = procedureRepository.findById(procedure.getProcedureId()).get();
        Equipment equipment1 = procedure1.getEquipment();
        assertNotNull(equipment1);
        String equipmentName = equipment1.getName();
        assertEquals("equipment", equipmentName);
    }


    private Procedure createProcedure() {
        Procedure procedure = new Procedure();
        procedure.setName("procedure");
        procedure.setCost(1.0);
        procedure.setDuration(LocalTime.of(1, 1, 1));
        return procedureRepository.save(procedure);
    }
}