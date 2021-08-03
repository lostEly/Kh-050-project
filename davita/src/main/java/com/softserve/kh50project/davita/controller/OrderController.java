package com.softserve.kh50project.davita.controller;

import com.softserve.kh50project.davita.dto.OrderDto;
import com.softserve.kh50project.davita.service.DoctorService;
import com.softserve.kh50project.davita.service.OrderService;
import com.softserve.kh50project.davita.service.PatientService;
import com.softserve.kh50project.davita.service.ProcedureService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    @Qualifier(value = "OrderServiceImpl")
    private final OrderService orderService;

    @Qualifier(value = "DoctorServiceImpl")
    private final DoctorService doctorService;

    @Qualifier(value = "PatientServiceImpl")
    private final PatientService patientService;

    @Qualifier(value = "ProcedureServiceImpl")
    private final ProcedureService procedureService;

    private final ModelMapper modelMapper;

    /**
     * Getting order by id
     *
     * @param id Long orderDto.orderId
     * @return The orderDto by id
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderDto> readById(@PathVariable Long id) {
        OrderDto findOrderDto = orderService.readById(id);
        return new ResponseEntity<>(findOrderDto, HttpStatus.OK);
    }

    /**
     * Getting orders by param
     *
     * @param orderDto Map(String,Object)={orderId, start, finish, cost, procedureId, doctorId, patienId}
     * @return The list of orders
     */
    @GetMapping
    public ResponseEntity<List<OrderDto>> read(@RequestBody OrderDto orderDto) {
        List<OrderDto> ordersDto = orderService.read(
                LocalDateTime.parse(orderDto.getStart()),
                LocalDateTime.parse(orderDto.getFinish()),
                procedureService.readById(orderDto.getOrderId()),
                doctorService.readById(orderDto.getDoctorId()),
                patientService.readById(orderDto.getPatientId())
        );
        return new ResponseEntity<>(ordersDto, HttpStatus.OK);
    }

    @GetMapping("/free")
    public ResponseEntity<List<OrderDto>> findAllFreeOrdersByProcedure(@RequestParam Long procedureId) {
        List<OrderDto> ordersDto = orderService.findAllFreeOrdersByProcedure(procedureId);
        return new ResponseEntity<>(ordersDto, HttpStatus.OK);
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<OrderDto>> findAllPatientOrders(@RequestParam Long patientId) {
        List<OrderDto> ordersDto = orderService.findAllPatientOrders(patientId);
        return new ResponseEntity<>(ordersDto, HttpStatus.OK);
    }

    @GetMapping("/doctor-appointments")
    public ResponseEntity<List<OrderDto>> findAllDoctorOrders(@RequestParam Long doctorId) {
        List<OrderDto> ordersDto = orderService.findAllDoctorOrders(doctorId);
        return new ResponseEntity<>(ordersDto, HttpStatus.OK);
    }

    /**
     * Creating a new order
     *
     * @param orderDto
     * @return The new orderDto
     */
    @PostMapping
    public ResponseEntity<OrderDto> create(@RequestBody OrderDto orderDto) {
        orderDto = orderService.create(orderDto);
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    /**
     * Updating the order
     *
     * @param orderDto which should be update
     * @param id orderDto.orderId
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
     * @param fields is map of orderOdt Map(String,Object)={orderId, start, finish, cost, procedureId, doctorId, patienId}
     * @return partly updated order
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<OrderDto> patch(@RequestBody Map<String, Object> fields, @PathVariable Long id) {
        OrderDto orderDto = orderService.patch(fields, id);
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
