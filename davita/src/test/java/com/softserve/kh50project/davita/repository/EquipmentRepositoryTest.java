package com.softserve.kh50project.davita.repository;


import com.softserve.kh50project.davita.dto.EquipmentDto;
import com.softserve.kh50project.davita.model.Equipment;
import com.softserve.kh50project.davita.model.Procedure;
import com.softserve.kh50project.davita.service.impl.EquipmentServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("classpath:application-test.properties")
public class EquipmentRepositoryTest {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private TestEntityManager entityManager;

    private List<Equipment> equipmentList;

    private static final String ROOT_URL = "http://localhost:8080/ss-ita-davita-api";

    RestTemplate restTemplate = new RestTemplate();

    @BeforeEach
    void setUp() {
        equipmentList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Equipment equipment = new Equipment();
            equipment.setName("equipment " + i);
            equipment = entityManager.persistAndFlush(equipment);
            equipmentList.add(equipment);
        }
    }


    @AfterEach
    void tearDown() {
        equipmentList = null;
    }





    @MockBean
    EquipmentServiceImpl equipmentService;

    @Test
    @Order(1)
    @DisplayName("find by id returns correct object")
    void findById() {
        Equipment equipment = equipmentRepository.findById(4L).get();
        assertEquals(equipment, equipmentList.get(3));
    }


    @Test
    @DisplayName("find all")
    void findAll() {
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertEquals(10, equipmentList.size());
        assertEquals("equipment 0", equipmentList.get(0).getName());
    }

    @Test
    @DisplayName("find all returns an empty list if db is empty")
    void findAll1() {
        for (Equipment equipment : equipmentList) {
            entityManager.remove(equipment);
        }
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertEquals(0, equipmentList.size());
    }


    @Test
    @DisplayName("create adds object in database")
    void create() {
        Equipment equipment = createEquipment();
        Equipment FoundEquipment = equipmentRepository.findById(equipment.getEquipmentId()).get();
        assertEquals(equipment.getEquipmentId(), FoundEquipment.getEquipmentId());
        assertEquals(equipment.getName(), FoundEquipment.getName());
    }


    @Test
    @DisplayName("update changes the object in database")
    void update() {
        Equipment equipment = createEquipment();
        String newName = "changedName";
        equipment.setName(newName);
        equipmentRepository.save(equipment);
        Equipment foundEquipment = equipmentRepository.findById(equipment.getEquipmentId()).get();
        assertEquals(equipment.getEquipmentId(), foundEquipment.getEquipmentId());
        assertEquals(newName, foundEquipment.getName());
    }

    @Test
    @DisplayName("delete removes the object in database")
    void delete() {
        Equipment equipment = createEquipment();
        equipmentRepository.delete(equipment);
        assertFalse(equipmentRepository.findById(equipment.getEquipmentId()).isPresent());
    }


    @Test
    @DisplayName("testGetAllPosts using URL")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:dataForTests/data-equipment.sql")
    public void testGetAllPosts() {
        ResponseEntity<Equipment[]> responseEntity =
                restTemplate.getForEntity(ROOT_URL + "/equipment", Equipment[].class);
        List<Equipment> posts = Arrays.asList(responseEntity.getBody());
        assertNotNull(posts);
    }


    @Test
    @DisplayName("testCreatePost using URL")
    public void testCreatePost() {
        Equipment equipment = new Equipment();
        equipment.setName("Defibrillators");
        ResponseEntity<Equipment> postResponse =
                restTemplate.postForEntity(ROOT_URL + "/equipment", equipment, Equipment.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
    }

    @Test
    @DisplayName("testGetPostById using URL")
    public void testGetPostById() {
        Equipment equipment = restTemplate.getForObject(ROOT_URL + "/equipment/17", Equipment.class);
        assertNotNull(equipment);
    }





    @Test
    @DisplayName("testUpdatePost using URL")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:dataForTests/data-equipment.sql")
    public void testUpdatePost() {
        int id = 17;
        Equipment post = restTemplate.getForObject(ROOT_URL + "/equipment/" + id, Equipment.class);
        post.setName("Updated Equipment");
        restTemplate.put(ROOT_URL + "/equipment/" + id, post);
        Equipment updatedPost = restTemplate.getForObject(ROOT_URL + "/equipment/" + id, Equipment.class);
        assertNotNull(updatedPost);
    }





    private Equipment createEquipment() {
        Equipment equipment = new Equipment();
        equipment.setName("Pressure mattresses");
        return equipmentRepository.save(equipment);
    }

}