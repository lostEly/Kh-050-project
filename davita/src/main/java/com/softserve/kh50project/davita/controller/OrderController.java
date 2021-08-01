package com.softserve.kh50project.davita.controller;

import com.softserve.kh50project.davita.dto.OrderDto;
import com.softserve.kh50project.davita.model.Order;
import com.softserve.kh50project.davita.service.OrderService;
import com.softserve.kh50project.davita.service.ProcedureService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    @Qualifier(value = "OrderServiceImpl")
    private final OrderService orderService;

    //TODO add doctor, patient services
//    @Qualifier(value = "DoctorServiceImpl")
//    private final DoctorService doctorService;
//
//    @Qualifier(value = "PatientServiceImpl")
//    private final PatientService patientService;

    @Qualifier(value = "ProcedureServiceImpl")
    private final ProcedureService procedureService;


    /**
     * Getting order by id
     *
     * @param orderDto order id
     * @return the order by id
     */
    @GetMapping(value = "/find_by_id")
    public ResponseEntity<OrderDto> readById(@RequestBody OrderDto orderDto) {
        Order findOrder = orderService.readById(orderDto.getId());
        OrderDto newOrderDto = new OrderDto(
                findOrder.getOrderId(),
                findOrder.getStart().toString(),
                findOrder.getFinish().toString(),
                findOrder.getCost(),
                findOrder.getProcedure().getProcedureId(),
                findOrder.getDoctor().getUserId(),
                findOrder.getPatient().getUserId()
        );
        return new ResponseEntity<>(newOrderDto, HttpStatus.OK);
    }

    /**
     * Getting orders by param
     *
     * @param orderDto optional, OrderDto
     * @return the list of orders
     */
    @GetMapping(value = "/find_by_filter")
    public ResponseEntity<List<OrderDto>> read(@RequestBody OrderDto orderDto) {
        List<Order> orders = orderService.read(
                LocalDateTime.parse(orderDto.getStart()),
                LocalDateTime.parse(orderDto.getFinish()),
                (orderDto.getProcedureId()>0) ? procedureService.readById(orderDto.getProcedureId()) : null,
                //TODO add patien and doctor
                null, //(orderDto.getDoctorId()>0) ? doctorService.readById(orderDto.getDoctorId()) : null,
                null); //(orderDto.getPatientId()>0) ? patientService.readById(orderDto.getPatientId()) : null,
        List<OrderDto> ordersDto = new ArrayList<>();
        for (Order curOrder : orders) {
            ordersDto.add(new OrderDto(
                            curOrder.getOrderId(),
                            curOrder.getStart().toString(),
                            curOrder.getFinish().toString(),
                            curOrder.getCost(),
                            curOrder.getProcedure().getProcedureId(),
                            curOrder.getDoctor().getUserId(),
                            curOrder.getPatient().getUserId()
                    )
            );
        }
        return new ResponseEntity<>(ordersDto, HttpStatus.OK);
    }

    /**
     * Creating a new order
     *
     * @param orderDto optional, OrderDto
     * @return the created new order
     */
    @PostMapping
    public ResponseEntity<OrderDto> create(@RequestBody OrderDto orderDto) {
        Order newOrder = new Order();
        newOrder.setStart(LocalDateTime.parse(orderDto.getStart()));
        newOrder.setFinish(LocalDateTime.parse(orderDto.getFinish()));
        newOrder.setCost(orderDto.getCost());
        newOrder.setProcedure(procedureService.readById(orderDto.getProcedureId()));
        //TODO add patien and doctor
//        newOrder.setDoctor(doctorService.readById(orderDto.getDoctorId()));
//        newOrder.setPatient(patientService.readById(orderDto.getPatientId()));
        newOrder = orderService.create(newOrder);
        orderDto.setId(newOrder.getOrderId());
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    /**
     * Updating the order
     *
     * @param orderDto which should be update
     * @param id
     * @return the updated order
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<OrderDto> update(@RequestBody OrderDto orderDto, @PathVariable Long id) {
        Order newOrder = new Order();
        newOrder.setStart(LocalDateTime.parse(orderDto.getStart()));
        newOrder.setFinish(LocalDateTime.parse(orderDto.getFinish()));
        newOrder.setCost(orderDto.getCost());
        newOrder.setProcedure(procedureService.readById(orderDto.getProcedureId()));
        //TODO add patien and doctor
//        newOrder.setDoctor(doctorService.readById(orderDto.getDoctorId()));
//        newOrder.setPatient(patientService.readById(orderDto.getPatientId()));
        Order updatedOrder = orderService.update(newOrder, id);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    /**
     * Partial updating the order
     *
     * @param fields is map where keys are updated fields and values are field values
     * @return partly updated order
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<OrderDto> patch(@RequestBody Map<String, Object> fields, @PathVariable Long id) {
        Order patchedOrder = orderService.patch(fields, id);
        OrderDto orderDto = new OrderDto(
                patchedOrder.getOrderId(),
                patchedOrder.getStart().toString(),
                patchedOrder.getFinish().toString(),
                patchedOrder.getCost(),
                patchedOrder.getProcedure().getProcedureId(),
                patchedOrder.getDoctor().getUserId(),
                patchedOrder.getPatient().getUserId()
        );
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    /**
     * Deleting the order
     *
     * @param id order id
     * @return ResponseEntity with status OK
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        orderService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
