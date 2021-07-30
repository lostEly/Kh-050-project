package com.softserve.kh50project.davita.controller;

import com.softserve.kh50project.davita.model.Order;
import com.softserve.kh50project.davita.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    @Qualifier(value = "OrderServiceImpl")
    private final OrderService orderService;

    /**
     * Getting order by id
     *
     * @param id order id
     * @return the order by id
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Order> readById(@PathVariable Long id) {
        System.out.println(id);
        Order order = orderService.readById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * Getting orders by param
     *
     * @param start       optional, procedure name
     * @param finish      optional, procedure cost
     * @param procedureId optional, procedure duration
     * @param patientId   optional, procedure duration
     * @param doctorId    optional, procedure duration
     * @return the list of doctors
     */
    @GetMapping
    public ResponseEntity<List<Order>> read(@RequestParam(value = "start", required = false) String start,
                                            @RequestParam(value = "finish", required = false) String finish,
                                            @RequestParam(value = "procedure_id", required = false) int procedureId,
                                            @RequestParam(value = "patient_id", required = false) int patientId,
                                            @RequestParam(value = "doctor_id", required = false) int doctorId) {
        List<Order> orders = orderService.read(start, finish, procedureId, patientId, doctorId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    /**
     * Creating a new order
     *
     * @param order which should be create
     * @return the created order
     */
    @PostMapping
    public ResponseEntity<Order> create(@RequestBody Order order) {
        Order createdOrder = orderService.create(order);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    /**
     * Updating the order
     *
     * @param order which should be update
     * @param id
     * @return the updated order
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<Order> update(@RequestBody Order order, @PathVariable Long id) {
        Order updatedOrder = orderService.update(order, id);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    /**
     * Partial updating the order
     *
     * @param fields is map where keys are updated fields and values are field values
     * @return partly updated order
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<Order> patch(@RequestBody Map<String, Object> fields, @PathVariable Long id) {
        Order patchedOrder = orderService.patch(fields, id);
        return new ResponseEntity<>(patchedOrder, HttpStatus.OK);
    }

    /**
     * Deleting the order
     *
     * @param id order id
     * @return ResponseEntity with status OK
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        System.out.println(id);
        orderService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
