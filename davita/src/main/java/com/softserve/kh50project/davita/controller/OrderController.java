package com.softserve.kh50project.davita.controller;

import com.softserve.kh50project.davita.model.Doctor;
import com.softserve.kh50project.davita.model.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/orders")
public class OrderController {
//rjjlasdkfjlasdkjfalsdfjaslkfdj

    /**
     * Getting order by id
     *
     * @param id order id
     * @return the order by id
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Order> readById(@PathVariable Long id) {
        Order order = new Order();
        order.setOrderId(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

//    /**
//     * Getting all orders or orders with some param
//     *
//     * @param specialization optional, doctor specialization
//     * @return the list of doctors
//     */
//    @GetMapping
//    public ResponseEntity<List<Doctor>> read(@RequestParam(value = "specialization", required = false) String specialization) {
//        Doctor doctor = new Doctor();
//        Doctor doctor1 = new Doctor();
//        Doctor doctor2 = new Doctor();
//
//        if (Objects.isNull(specialization)) {
//            return new ResponseEntity<>(List.of(doctor, doctor1, doctor2), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(List.of(doctor, doctor2), HttpStatus.OK);
//
//    }

    /**
     * Creating an order
     *
     * @param order which should be create
     * @return the created order
     */
    @PostMapping
    public ResponseEntity<Order> create(@RequestBody Order order) {
        return new ResponseEntity<>(order, HttpStatus.CREATED);
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
        //you need find order there
        Order findOrder = new Order();
        findOrder.setStart(order.getStart());
        findOrder.setFinish(order.getFinish());
        findOrder.setDoctor(order.getDoctor());
        findOrder.setPatient(order.getPatient());
        findOrder.setProcedure(order.getProcedure());
        findOrder.setCost(order.getCost());
        return new ResponseEntity<>(findOrder, HttpStatus.OK);
    }

    /**
     * Partial updating the order
     *
     * @param fields is map where keys are updated fields and values are field values
     * @return partly updated order
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<Order> patch(@RequestBody Map<String, Object> fields, @PathVariable Long id) {
        //update order there
        return new ResponseEntity<>(new Order(), HttpStatus.OK);
    }

    /**
     * Deleting the order
     *
     * @param id order id
     * @return ResponseEntity with status OK
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}
