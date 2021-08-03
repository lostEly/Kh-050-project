package com.softserve.kh50project.davita.service.impl;

import com.softserve.kh50project.davita.dto.DoctorDto;
import com.softserve.kh50project.davita.dto.OrderDto;
import com.softserve.kh50project.davita.dto.PatientDto;
import com.softserve.kh50project.davita.dto.ProcedureDto;
import com.softserve.kh50project.davita.exceptions.ResourceNotFoundException;
import com.softserve.kh50project.davita.model.Order;
import com.softserve.kh50project.davita.repository.DoctorRepository;
import com.softserve.kh50project.davita.repository.OrderRepository;
import com.softserve.kh50project.davita.repository.PatientRepository;
import com.softserve.kh50project.davita.repository.ProcedureRepository;
import com.softserve.kh50project.davita.service.OrderService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProcedureRepository procedureRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Override
    public OrderDto readById(Long id) {
        Order findOrder = orderRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        return convertOrderToDto(findOrder);
    }

    @Override
    public List<OrderDto> read(LocalDateTime start, LocalDateTime finish, ProcedureDto procedureDto, DoctorDto doctorDto, PatientDto patientDto) {
        Specification<Order> specification = orderRepository.getOrderQuery(
                start,
                finish,
                procedureRepository.getById(procedureDto.getProcedureId()),
                doctorRepository.getById(doctorDto.getUserId()),
                patientRepository.getById(patientDto.getUserId())
        );
        List<Order> orderList = orderRepository.findAll(specification);
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (Order curOrder : orderList) {
            orderDtoList.add(convertOrderToDto(curOrder));
        }
        return orderDtoList;
    }

    @Override
    public OrderDto create(OrderDto orderDto) {
        Order newOrder = convertDtoToOrder(orderDto);
        newOrder = orderRepository.save(newOrder);
        orderDto.setOrderId(newOrder.getOrderId());
        return orderDto;
    }

    @Override
    public OrderDto update(OrderDto orderDto, Long id) {
        Order findOrder = orderRepository.getById(id);
        findOrder.setStart(LocalDateTime.parse(orderDto.getStart()));
        findOrder.setFinish(LocalDateTime.parse(orderDto.getFinish()));
        findOrder.setCost(orderDto.getCost());
        findOrder.setProcedure(procedureRepository.getById(orderDto.getProcedureId()));
        findOrder.setDoctor(doctorRepository.getById(orderDto.getDoctorId()));
        findOrder.setPatient(patientRepository.getById(orderDto.getPatientId()));
        findOrder = orderRepository.save(findOrder);
        return orderDto;
    }

    @Override
    public OrderDto patch(Map<String, Object> fields, Long id) {
        Order order = orderRepository.getById(id);
        for (String fieldName : fields.keySet()) {
            switch (fieldName) {
                case "start":
                    order.setStart(LocalDateTime.parse((String) fields.get("start")));
                    break;
                case "finish":
                    order.setFinish(LocalDateTime.parse((String) fields.get("finish")));
                    break;
                case "cost":
                    order.setCost((Double) fields.get("cost"));
                    break;
                case "procedureId":
                    order.setProcedure(procedureRepository.getById((Long) fields.get("procedureId")));
                    break;
                case "doctorId":
                    order.setDoctor(doctorRepository.getById((Long) fields.get("doctorId")));
                    break;
                case "patientId":
                    order.setPatient(patientRepository.getById((Long) fields.get("patientId")));
                    break;
            }
        }
//        fields.forEach((k, v) -> {
//            Field field = ReflectionUtils.findField(Order.class, k);
//            if (field != null) {
//                field.setAccessible(true);
//                ReflectionUtils.setField(field, order, v);
//            }
//        });
        order = orderRepository.save(order);
        return convertOrderToDto(order);
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<OrderDto> findAllFreeOrdersByProcedure(Long procedureId) {
        return orderRepository.findAllFreeOrdersByProcedure(procedureId).stream()
                .map(this::convertOrderToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> findAllPatientOrders(Long patientId) {
        return orderRepository.findAllPatientOrders(patientId).stream()
                .map(this::convertOrderToDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<OrderDto> findAllDoctorOrders(Long doctorId) {
        return orderRepository.findAllDoctorOrders(doctorId).stream()
                .map(this::convertOrderToDto)
                .collect(Collectors.toList());
    }


    private Order convertDtoToOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setOrderId(orderDto.getOrderId());
        order.setStart(LocalDateTime.parse(orderDto.getStart()));
        order.setFinish(LocalDateTime.parse(orderDto.getFinish()));
        order.setCost(orderDto.getCost());
        order.setProcedure(procedureRepository.getById(orderDto.getProcedureId()));
        order.setDoctor(doctorRepository.getById(orderDto.getDoctorId()));
        order.setPatient(patientRepository.getById(orderDto.getPatientId()));
        return order;
    }

    private OrderDto convertOrderToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getOrderId());
        orderDto.setStart(String.valueOf(order.getStart()));
        orderDto.setFinish(String.valueOf(order.getFinish()));
        orderDto.setCost(order.getCost());
        if (nonNull(order.getProcedure())) {
            orderDto.setProcedureId(order.getProcedure().getProcedureId());
        }
        if (nonNull(order.getDoctor())) {
            orderDto.setDoctorId(order.getDoctor().getUserId());
        }
        if (nonNull(order.getPatient())) {
            orderDto.setPatientId(order.getPatient().getUserId());
        }
        return orderDto;
    }
}
