package com.softserve.kh50project.davita.repository;

import com.softserve.kh50project.davita.model.*;
import com.softserve.kh50project.davita.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test-order.properties")
class OrderRepositoryTest {

    final static int TEST_LIST_SIZE = 5;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager entityManager;

    private List<Order> orders;
    private List<Doctor> doctors;
    private List<Patient> patients;
    private List<Procedure> procedures;

    void doctorsInit(int count) {
        doctors = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Doctor doctor = new Doctor();
            doctor.setLogin("Login" + i);
            doctor.setPassword("Password" + i);
            doctor.setName("DoctorName" + i);
            doctor.setLastName("DoctorLastName" + i);
            doctor.setPhone("050505050" + i);
            doctor.setEmail("doctor" + i + "@gmail.com");
            doctor.setSpecialization("Specialization" + i);
            doctor.setCertificateNumber("0000000000" + i);
            doctor = entityManager.persistAndFlush(doctor);
            doctors.add(doctor);
        }
    }

    void patientsInit(int count) {
        patients = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Patient patient = new Patient();
            patient.setLogin("Login" + i);
            patient.setPassword("Password" + i);
            patient.setName("patientName" + i);
            patient.setLastName("patientLastName" + i);
            patient.setPhone("050999999" + i);
            patient.setEmail("patient" + i + "@gmail.com");
            patient.setInsuranceNumber("111111110" + i);
            patient = entityManager.persistAndFlush(patient);
            patients.add(patient);
        }
    }

    void proceduresInit(int count) {
        procedures = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Procedure procedure = new Procedure();
            procedure.setName("procedureName" + i);
            procedure.setCost(100.0 * (1 + i));
            procedure.setDuration(LocalTime.of(i, i * 5, i * 5));
            procedure = entityManager.persistAndFlush(procedure);
            procedures.add(procedure);
        }
    }

    void ordersInit(int count) {
        orders = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Order order = new Order();
            //2021-07-01T10:15:00   2021-07-06T10:15:00
            order.setStart(LocalDateTime.parse("2021-07-0" + (i + 1) + "T10:15:00"));
            order.setFinish(LocalDateTime.parse("2021-07-0" + (i + 1) + "T10:30:00"));
            order.setCost(100.0 * (i + 1));
            order.setDoctor(doctors.get(i));
            order.setPatient(patients.get(i));
            order.setProcedure(procedures.get(i));
            order = entityManager.persistAndFlush(order);
            orders.add(order);
        }
    }

    @BeforeEach
    void setUp() {
        doctorsInit(TEST_LIST_SIZE);
        proceduresInit(TEST_LIST_SIZE);
        patientsInit(TEST_LIST_SIZE);
        ordersInit(TEST_LIST_SIZE);
    }

    @AfterEach
    void tearDown() {
        doctors = null;
        patients = null;
        procedures = null;
        orders = null;
    }

    @Test
    @DisplayName("find order: by id")
    void findById() {
        Order orderFirst = orderRepository.findById(orders.get(0).getOrderId()).get();
        Order orderLast = orderRepository.findById(orders.get(TEST_LIST_SIZE - 1).getOrderId()).get();
        assertEquals(orderFirst, orders.get(0));
        assertEquals(orderLast, orders.get(TEST_LIST_SIZE - 1));
    }

    @Test
    @DisplayName("find orders: all")
    void findAll() {
        List<Order> foundOrders = orderRepository.findAll();
        assertEquals(TEST_LIST_SIZE, foundOrders.size());
    }

    @Test
    @DisplayName("find orders: all, DB is empty")
    void findAll1() {
        for (Order order : orders) {
            System.out.println(order.getOrderId());
            entityManager.remove(order);
        }
        List<Order> foundOrders = orderRepository.findAll();
        assertEquals(0, foundOrders.size());
    }

    @Test
    @DisplayName("find orders: all, specification (null, null, null, null, null)")
    void getProcedureQuery() {
        Specification<Order> specification = orderRepository.getOrderQuery(
                null,
                null,
                null,
                null,
                null
        );
        List<Order> foundOrders = orderRepository.findAll(specification);
        assertEquals(foundOrders.size(), orders.size());
    }

    @Test
    @DisplayName("find orders: all, specification (start, null, null, null, null)")
    void getProcedureQueryStart() {
        //2021-07-01T10:15:00   2021-07-01T10:30:00
        LocalDateTime startDate = LocalDateTime.parse("2021-07-01T10:15:00");
        Specification<Order> specification = orderRepository.getOrderQuery(
                startDate,
                null,
                null,
                null,
                null
        );
        List<Order> foundOrders = orderRepository.findAll(specification);
        assertEquals(foundOrders.size(), 1);
        assertEquals(foundOrders.get(0).getStart(), startDate);

        for (Order order : orders) {
            order.setStart(startDate);
            entityManager.persistAndFlush(order);
        }
        specification = orderRepository.getOrderQuery(
                startDate,
                null,
                null,
                null,
                null
        );
        foundOrders = orderRepository.findAll(specification);
        assertEquals(foundOrders.size(), orders.size());
    }

    @Test
    @DisplayName("find orders: all, specification (null, finish, null, null, null)")
    void getProcedureQueryFinish() {
        //2021-07-01T10:15:00   2021-07-01T10:30:00
        LocalDateTime finishDate = LocalDateTime.parse("2021-07-01T10:30:00");
        Specification<Order> specification = orderRepository.getOrderQuery(
                null,
                finishDate,
                null,
                null,
                null
        );
        List<Order> foundOrders = orderRepository.findAll(specification);
        assertEquals(foundOrders.size(), 1);
        assertEquals(foundOrders.get(0), orders.get(0));

        for (Order order : orders) {
            order.setFinish(finishDate);
            entityManager.persistAndFlush(order);
        }
        specification = orderRepository.getOrderQuery(
                null,
                finishDate,
                null,
                null,
                null
        );
        foundOrders = orderRepository.findAll(specification);
        assertEquals(foundOrders.size(), orders.size());
    }

    @Test
    @DisplayName("find orders: all, specification (null, null, procedure, null, null)")
    void getProcedureQueryProcedure() {
        Specification<Order> specification = orderRepository.getOrderQuery(
                null,
                null,
                procedures.get(0),
                null,
                null);
        List<Order> foundOrders = orderRepository.findAll(specification);
        assertEquals(foundOrders.size(), 1);

        Procedure curProc = procedures.get(0);
        for (Order order : orders) {
            order.setProcedure(curProc);
        }
        specification = orderRepository.getOrderQuery(
                null,
                null,
                curProc,
                null,
                null);
        foundOrders = orderRepository.findAll(specification);
        assertEquals(foundOrders.size(), orders.size());

    }

    @Test
    @DisplayName("find orders: all, specification (null, null, null, patient, null)")
    void getProcedureQueryPatient() {
        Specification<Order> specification = orderRepository.getOrderQuery(
                null,
                null,
                null,
                patients.get(0),
                null);
        List<Order> foundOrders = orderRepository.findAll(specification);
        assertEquals(foundOrders.size(), 1);

        Patient curPatient = patients.get(0);
        for (Order order : orders) {
            order.setPatient(curPatient);
        }
        specification = orderRepository.getOrderQuery(
                null,
                null,
                null,
                curPatient,
                null);
        foundOrders = orderRepository.findAll(specification);
        assertEquals(foundOrders.size(), orders.size());
    }

    @Test
    @DisplayName("find orders: all, specification (null, null, null, null, doctor)")
    void getProcedureQueryDoctor() {
        Specification<Order> specification = orderRepository.getOrderQuery(
                null,
                null,
                null,
                null,
                doctors.get(0));
        List<Order> foundOrders = orderRepository.findAll(specification);
        assertEquals(foundOrders.size(), 1);

        Doctor curDoctor = doctors.get(0);
        for (Order order : orders) {
            order.setDoctor(curDoctor);
        }
        specification = orderRepository.getOrderQuery(
                null,
                null,
                null,
                null,
                curDoctor);
        foundOrders = orderRepository.findAll(specification);
        assertEquals(foundOrders.size(), 5);
    }

    @Test
    @DisplayName("find orders: all, specification (start,finish,procedure(0),patient(0),doctor(0))")
    void getProcedureQueryAllParam() {
        //2021-07-01T10:15:00   2021-07-06T10:15:00
        LocalDateTime startDate = LocalDateTime.parse("2021-07-01T10:15:00");
        LocalDateTime finishDate = LocalDateTime.parse("2021-07-01T10:30:00");
        Specification<Order> specification = orderRepository.getOrderQuery(
                startDate,
                finishDate,
                procedures.get(0),
                patients.get(0),
                doctors.get(0));
        List<Order> foundOrders = orderRepository.findAll(specification);
        assertEquals(foundOrders.size(), 1);
    }

    @Test
    @DisplayName("delete order: by id")
    void deleteById() {
        long firstId = orders.get(0).getOrderId();
        orderRepository.deleteById(firstId);

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            Order orderFirst = orderRepository.findById(firstId).get();
        });

        String expectedMessage = "No value present";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

}