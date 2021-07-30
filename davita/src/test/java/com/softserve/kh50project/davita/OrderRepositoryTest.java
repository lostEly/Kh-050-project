package com.softserve.kh50project.davita;

import com.softserve.kh50project.davita.model.*;
import com.softserve.kh50project.davita.model.Order;
import com.softserve.kh50project.davita.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@TestPropertySource("classpath:application-test.properties")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ProcedureRepository procedureRepository;

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
            procedure.setDuration(LocalTime.of(i * 10, i * 10, i * 10));
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
        doctorsInit(5);
        proceduresInit(5);
        patientsInit(5);
        ordersInit(5);
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
        Order orderFirst = orderRepository.findById(1L).get();
        Order orderLast = orderRepository.findById(5L).get();
        assertEquals(orderFirst, procedures.get(0));
        assertEquals(orderLast, procedures.get(4));
    }

    @Test
    @DisplayName("find orders: all")
    void findAll() {
        List<Order> foundOrders = orderRepository.findAll();
        assertEquals(5, foundOrders.size());
        assertEquals(100.0, foundOrders.get(0).getCost());
        assertEquals(300.0, foundOrders.get(2).getCost());
        assertEquals(500.0, foundOrders.get(4).getCost());
    }

    @Test
    @DisplayName("find orders: all, DB is empty")
    void findAll1() {
        for (Order order : orders) {
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
        assertEquals(foundOrders.size(), procedures.size());
        assertEquals(foundOrders.get(3), procedures.get(3));
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
        assertEquals(foundOrders.get(0).getDoctor().getUserId(), 1);
    }

    @Test
    @DisplayName("find orders: all, specification (startAllEquals, null, null, null, null)")
    void getProcedureQueryStart2() {
        //2021-07-01T10:15:00   2021-07-01T10:30:00
        LocalDateTime startDate = LocalDateTime.parse("2021-07-01T10:15:00");
        for (Order order : orders){
            order.setStart(startDate);
            entityManager.persistAndFlush(order);
        }
        Specification<Order> specification = orderRepository.getOrderQuery(
                startDate,
                null,
                null,
                null,
                null
        );
        List<Order> foundOrders = orderRepository.findAll(specification);
        assertEquals(foundOrders.size(), 5);
        assertEquals(foundOrders.get(0).getStart(), startDate);
        assertEquals(foundOrders.get(4).getStart(), startDate);
        assertNotEquals(foundOrders.get(0).getFinish(), foundOrders.get(4).getFinish());
    }

    @Test
    @DisplayName("find orders: all, specification (null,finish, null, null, null)")
    void getProcedureQuery3() {
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
        assertEquals(foundOrders.get(3), procedures.get(3));
    }

    @Test
    @DisplayName("find orders: all, specification (null, null, procedure(0), null, null)")
    void getProcedureQuery4() {
        //2021-07-01T10:15:00   2021-07-06T10:15:00
//        Specification<Order> specification = orderRepository.getOrderQuery(null, null, 0,0,0);
//        List<Order> foundOrders = orderRepository.findAll(specification);
//        assertEquals(foundOrders.size(), procedures.size());
//        assertEquals(foundOrders.get(3), procedures.get(3));
    }

    @Test
    @DisplayName("find orders: all, specification (null, null, null, patient(0), null)")
    void getProcedureQuery5() {
        //2021-07-01T10:15:00   2021-07-06T10:15:00
//        Specification<Order> specification = orderRepository.getOrderQuery(null, null, 0,0,0);
//        List<Order> foundOrders = orderRepository.findAll(specification);
//        assertEquals(foundOrders.size(), procedures.size());
//        assertEquals(foundOrders.get(3), procedures.get(3));
    }

    @Test
    @DisplayName("find orders: all, specification (null, null, null, null, doctor(0)")
    void getProcedureQuery6() {
        //2021-07-01T10:15:00   2021-07-06T10:15:00
//        Specification<Order> specification = orderRepository.getOrderQuery(null, null, 0,0,0);
//        List<Order> foundOrders = orderRepository.findAll(specification);
//        assertEquals(foundOrders.size(), procedures.size());
//        assertEquals(foundOrders.get(3), procedures.get(3));
    }

    @Test
    @DisplayName("find orders: all, specification (start,finish,procedure(0),patient(0),doctor(0))")
    void getProcedureQuery7() {
        //2021-07-01T10:15:00   2021-07-06T10:15:00
//        Specification<Order> specification = orderRepository.getOrderQuery(null, null, 0,0,0);
//        List<Order> foundOrders = orderRepository.findAll(specification);
//        assertEquals(foundOrders.size(), procedures.size());
//        assertEquals(foundOrders.get(3), procedures.get(3));
    }

}