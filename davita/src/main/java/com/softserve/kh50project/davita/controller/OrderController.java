package com.softserve.kh50project.davita.controller;

import com.softserve.kh50project.davita.dto.OrderDto;
import com.softserve.kh50project.davita.service.DoctorService;
import com.softserve.kh50project.davita.service.OrderService;
import com.softserve.kh50project.davita.service.PatientService;
import com.softserve.kh50project.davita.service.ProcedureService;
import com.softserve.kh50project.davita.service.impl.OrderServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    @Qualifier(value = "OrderServiceImpl")
    private final OrderService orderService;

    @Qualifier(value = "ProcedureServiceImpl")
    private final ProcedureService procedureService;

    @Qualifier(value = "DoctorServiceImpl")
    private final DoctorService doctorService;

    @Qualifier(value = "PatientServiceImpl")
    private final PatientService patientService;

    /**
     * Getting All orders
     *
     * @return The List<OrderDto> of all orders
     */
    @GetMapping
    public ResponseEntity<List<OrderDto>> readAll() {
        List<OrderDto> ordersDto = orderService.findAll();
        return new ResponseEntity<>(ordersDto, HttpStatus.OK);
    }

    /**
     * Getting order by id
     *
     * @param orderId Long
     * @return The OrderDto by id
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderDto> readById(@PathVariable Long orderId) {
        OrderDto findOrderDto = orderService.findById(orderId);
        return new ResponseEntity<>(findOrderDto, HttpStatus.OK);
    }

    /**
     * Getting orders by param
     *
     * @param start         String
     * @param finish        String
     * @param procedureId   Long
     * @param doctorId      Long
     * @param patientId     Long
     * @return The List<OrderDto> of orders. If all fields == null -> return all Orders
     */
    @GetMapping("/filter")
    public ResponseEntity<List<OrderDto>> read(@RequestParam(defaultValue = "") String start,
                                               @RequestParam(defaultValue = "") String finish,
                                               @RequestParam(defaultValue = "0") Long procedureId,
                                               @RequestParam(defaultValue = "0") Long doctorId,
                                               @RequestParam(defaultValue = "0") Long patientId) {
        List<OrderDto> ordersDto = orderService.find(
                (start.length()>0) ? LocalDateTime.parse(start) : null,
                (finish.length()>0) ? LocalDateTime.parse(finish) : null,
                (procedureId>0) ? procedureService.readById(procedureId) : null,
                (doctorId>0) ? doctorService.readById(doctorId) : null,
                (patientId>0) ? patientService.readById(patientId) : null
        );
        return new ResponseEntity<>(ordersDto, HttpStatus.OK);
    }

    /**
     * Getting free order to patient
     *
     * @param procedureId Long
     * @return The OrderDto by id
     */
    @GetMapping("/free")
    public ResponseEntity<List<OrderDto>> findAllFreePatientOrders(@RequestParam Long procedureId) {
        List<OrderDto> ordersDto = orderService.findAllFreeOrdersForPatient(procedureId);
        return new ResponseEntity<>(ordersDto, HttpStatus.OK);
    }

   /**
     * Getting free order to doctor
     *
     * @param procedureId Long
     * @return The OrderDto by id
     */
    @GetMapping("/doctor-free")
    public ResponseEntity<List<OrderDto>> findAllFreeDoctorOrders(@RequestParam Long procedureId) {
        List<OrderDto> ordersDto = orderService.findAllFreeOrdersForDoctor(procedureId);
        return new ResponseEntity<>(ordersDto, HttpStatus.OK);
    }

    /**
     * Getting all patient's orders
     *
     * @param patientId Long
     * @return The OrderDto by id
     */
    @GetMapping("/appointments")
    public ResponseEntity<List<OrderDto>> findAllPatientOrders(@RequestParam Long patientId) {
        List<OrderDto> ordersDto = orderService.findAllPatientOrders(patientId);
        return new ResponseEntity<>(ordersDto, HttpStatus.OK);
    }

    /**
     * Getting all doctor's orders
     *
     * @param doctorId Long
     * @return The OrderDto by id
     */
    @GetMapping("/doctor-appointments")
    public ResponseEntity<List<OrderDto>> findAllDoctorOrders(@RequestParam Long doctorId) {
        List<OrderDto> ordersDto = orderService.findAllDoctorOrders(doctorId);
        return new ResponseEntity<>(ordersDto, HttpStatus.OK);
    }

    /**
     * Getting all doctor's orders
     *
     * @param doctorId Long
     * @return The OrderDto by id
     */
    @GetMapping("/doctor-calendar")
    public ResponseEntity<List<OrderDto>> findAllDoctorCalendar(@RequestParam Long doctorId) {
        List<OrderDto> ordersDto = orderService.findDoctorCalendar(doctorId);
        return new ResponseEntity<>(ordersDto, HttpStatus.OK);
    }

    /**
     * Creating new order
     *
     * @param orderDto OrderDto
     * @return The new OrderDto
     */
    @PostMapping
    public ResponseEntity<OrderDto> create(@RequestBody OrderDto orderDto) {
        orderDto = orderService.create(orderDto);
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    /**
     * Updating the order
     *
     * @param orderDto OrderDto
     * @param id       Long
     * @return The updated order
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<OrderDto> update(@RequestBody OrderDto orderDto, @PathVariable Long id) {
        orderDto = orderService.update(orderDto, id);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    /**
     * Partial updating the order
     *
     * @param fields Map<String, Object>
     * @param id     Long
     * @return partly updated order
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<OrderDto> patch(@RequestBody Map<String, Object> fields, @PathVariable Long id) {
        OrderDto orderDto = orderService.patch(fields, id);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    /**
     * Getting orders without doctor from start to finish
     *
     * @param idList   List<Long>
     * @param doctorId Long
     * @return The List<OrderDto> of orders
     */
    @PatchMapping(value = "/bookOrdersForDoctor/{doctorId}")
    public ResponseEntity<List<OrderDto>> bookOrders(@RequestBody List<Long> idList, @PathVariable Long doctorId) {
        List<OrderDto> ordersDto = new ArrayList<>();
        Map<String, Object> patchFields = new HashMap<>();
        patchFields.put("doctorId", doctorId);
        for (Long idOrder : idList) {
            ordersDto.add(orderService.patch(patchFields, idOrder));
        }
        return new ResponseEntity<>(ordersDto, HttpStatus.OK);
    }

    /**
     * Deleting the order
     *
     * @param id Long
     * @return ResponseEntity with status OK
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        orderService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
