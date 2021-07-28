package com.softserve.kh50project.davita;


import com.softserve.kh50project.davita.model.Equipment;
import com.softserve.kh50project.davita.repository.EquipmentRepository;
import com.softserve.kh50project.davita.service.impl.EquipmentServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("classpath:application-test.properties")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class EquipmentRepositoryTest {


    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private TestEntityManager entityManager;

    private List<Equipment> equipmentList;

    private static final String ROOT_URL = "http://localhost:8080";

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


    @Test
    @Order(1)
    @DisplayName("find by id returns correct object")
    void findById() {
        Equipment procedure = equipmentRepository.findById(9L).get();
        assertEquals(procedure, equipmentList.get(8));
    }


    @MockBean
    EquipmentServiceImpl equipmentService;


    @Test
    public void givenMovieId_whenMakingGetRequestToMovieEndpoint_thenReturnMovie() {

        Equipment testEquipment = new Equipment();
        testEquipment.setName("defibrillators");
        when(equipmentService.readById(16L)).thenReturn(testEquipment);

        get(ROOT_URL + "/equipment/" + testEquipment.getEquipmentId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(testEquipment.getEquipmentId()))
                .body("name", equalTo(testEquipment.getName()));

        Equipment result = get(ROOT_URL + "/equipment/" + testEquipment.getEquipmentId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(Equipment.class);
        assertThat(result).isEqualTo(testEquipment);

        String responseString = get(ROOT_URL + "/equipment/" + testEquipment.getEquipmentId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .asString();
        assertThat(responseString).isNotEmpty();
    }




    @Test
    @Order(2)
    @DisplayName("find all")
    void findAll() {
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertEquals(10, equipmentList.size());
        assertEquals("equipment 0", equipmentList.get(0).getName());
    }

    @Test
    @Order(3)
    @DisplayName("find all returns an empty list if db is empty")
    void findAll1() {
        for (Equipment equipment : equipmentList) {
            entityManager.remove(equipment);
        }
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertEquals(0, equipmentList.size());
    }


    @Test
    @Order(4)
    @DisplayName("create adds object in database")
    void create() {
        Equipment equipment = createEquipment();
        Equipment FoundEquipment = equipmentRepository.findById(equipment.getEquipmentId()).get();
        assertEquals(equipment.getEquipmentId(), FoundEquipment.getEquipmentId());
        assertEquals(equipment.getName(), FoundEquipment.getName());
    }


    @Test
    @Order(5)
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
    @Order(6)
    @DisplayName("delete removes the object in database")
    void delete() {
        Equipment equipment = createEquipment();
        equipmentRepository.delete(equipment);
        assertFalse(equipmentRepository.findById(equipment.getEquipmentId()).isPresent());
    }


    @Test
    @Order(7)
    @DisplayName("testGetAllPosts using URL")
    public void testGetAllPosts() {
        ResponseEntity<Equipment[]> responseEntity =
                restTemplate.getForEntity(ROOT_URL + "/equipment", Equipment[].class);
        List<Equipment> posts = Arrays.asList(responseEntity.getBody());
        assertNotNull(posts);
    }


    @Test
    @Order(8)
    @DisplayName("testCreatePost using URL")
    public void testCreatePost() {
        Equipment equipment = new Equipment();
        equipment.setName("Defibrillators");
        ResponseEntity<Equipment> postResponse =
                restTemplate.postForEntity(ROOT_URL + "/equipment/add-equipment", equipment, Equipment.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
    }

    @Test
    @Order(9)
    @DisplayName("testGetPostById using URL")
    public void testGetPostById() {
        Equipment equipment = restTemplate.getForObject(ROOT_URL + "/equipment/24", Equipment.class);
        assertNotNull(equipment);
    }





    @Test
    @Order(10)
    @DisplayName("testUpdatePost using URL")
    public void testUpdatePost() {
        int id = 27;
        Equipment post = restTemplate.getForObject(ROOT_URL + "/equipment/" + id, Equipment.class);
        post.setName("This my updated post1 content");
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
