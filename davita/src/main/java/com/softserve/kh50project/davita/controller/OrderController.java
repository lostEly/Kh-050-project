package com.softserve.kh50project.davita.controller;

import com.softserve.kh50project.davita.dto.OrderDto;
import com.softserve.kh50project.davita.service.DoctorService;
import com.softserve.kh50project.davita.service.OrderService;
import com.softserve.kh50project.davita.service.PatientService;
import com.softserve.kh50project.davita.service.ProcedureService;
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
     * Getting order by id
     *
     * @param id Long
     * @return The OrderDto by id
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderDto> readById(@PathVariable Long id) {
        OrderDto findOrderDto = orderService.readById(id);
        return new ResponseEntity<>(findOrderDto, HttpStatus.OK);
    }

    /**
     * Getting orders by param
     *
     * @param fields Map<String, Object> (start,finish,procedureId,doctorId,patientId)
     * @return The List<OrderDto> of orders. If all fields == null -> return all Orders
     */
    @GetMapping
    public ResponseEntity<List<OrderDto>> read(@RequestBody Map<String, Object> fields) {
        List<OrderDto> ordersDto = orderService.read(
                (fields.get("start") != null) ? LocalDateTime.parse((String) fields.get("start")) : null,
                (fields.get("finish") != null) ? LocalDateTime.parse((String) fields.get("finish")) : null,
                (fields.get("procedureId") != null) ? procedureService.readById(Long.parseLong(fields.get("procedureId").toString())) : null,
                (fields.get("doctorId") != null) ? doctorService.readById(Long.parseLong(fields.get("doctorId").toString())) : null,
                (fields.get("patientId") != null) ? patientService.readById(Long.parseLong(fields.get("patientId").toString())) : null
        );
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
}
