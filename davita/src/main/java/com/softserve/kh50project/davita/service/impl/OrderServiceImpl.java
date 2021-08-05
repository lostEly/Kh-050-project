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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProcedureRepository procedureRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Override
    public List<OrderDto> findAll() {
        return orderRepository.findAll().stream()
                .map(this::convertOrderToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto findById(Long orderId) {
        Order findOrder = orderRepository.findById(orderId)
                .orElseThrow(ResourceNotFoundException::new);
        return convertOrderToDto(findOrder);
    }

    @Override
    public List<OrderDto> find(LocalDateTime start, LocalDateTime finish, ProcedureDto procedureDto, DoctorDto doctorDto, PatientDto patientDto) {
        Specification<Order> specification = orderRepository.getOrderQuery(
                start,
                finish,
                (procedureDto != null) ? procedureRepository.getById(procedureDto.getProcedureId()) : null,
                (doctorDto != null) ? doctorRepository.getById(doctorDto.getUserId()) : null,
                (patientDto != null) ? patientRepository.getById(patientDto.getUserId()) : null
        );
        return orderRepository.findAll(specification).stream()
                .map(this::convertOrderToDto)
                .collect(Collectors.toList());
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
        Order findOrder = orderRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        findOrder.setStart(orderDto.getStart() != null ? LocalDateTime.parse(orderDto.getStart()) : null);
        findOrder.setFinish(orderDto.getFinish() != null ? LocalDateTime.parse(orderDto.getFinish()) : null);
        findOrder.setCost(orderDto.getCost());
        findOrder.setProcedure(orderDto.getProcedureId() != null ? procedureRepository.getById(orderDto.getProcedureId()) : null);
        findOrder.setDoctor(orderDto.getDoctorId() != null ? doctorRepository.getById(orderDto.getDoctorId()) : null);
        findOrder.setPatient(orderDto.getPatientId() != null ? patientRepository.getById(orderDto.getPatientId()) : null);
        orderRepository.save(findOrder);
        return orderDto;
    }

    @Override
    public OrderDto patch(Map<String, Object> fields, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(ResourceNotFoundException::new);
        for (String fieldName : fields.keySet()) {
            switch (fieldName) {
                case "start":
                    order.setStart(fields.get("start") != null ? LocalDateTime.parse((String) fields.get("start")) : null);
                    break;
                case "finish":
                    order.setFinish(fields.get("finish") != null ? LocalDateTime.parse((String) fields.get("finish")) : null);
                    break;
                case "cost":
                    order.setCost((Double) fields.get("cost"));
                    break;
                case "procedureId":
                    order.setProcedure(fields.get("procedureId") != null ? procedureRepository.getById((Long) fields.get("procedureId")) : null);
                    break;
                case "doctorId":
                    order.setDoctor(fields.get("doctorId") != null ? doctorRepository.getById((Long) fields.get("doctorId")) : null);
                    break;
                case "patientId":
                    order.setPatient(fields.get("patientId") != null ? patientRepository.getById((Long) fields.get("patientId")) : null);
                    break;
            }
        }
        order = orderRepository.save(order);
        return convertOrderToDto(order);
    }

    @Override
    public void delete(Long id) {
        orderRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        orderRepository.deleteById(id);
    }

    //************************
    // Dasha Tkachenko
    //************************
    @Override
    public List<OrderDto> findAllFreeOrdersByProcedure(Long procedureId) {
        procedureRepository.findById(procedureId)
                .orElseThrow(ResourceNotFoundException::new);
        return orderRepository.findAllByDoctorUserIdIsNotNullAndPatientUserIdIsNullAndProcedureProcedureIdAndStartGreaterThan(procedureId, LocalDateTime.now())
                .stream()
                .map(this::convertOrderToDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<OrderDto> findAllPatientOrders(Long patientId) {
        patientRepository.findById(patientId)
                .orElseThrow(ResourceNotFoundException::new);
        return orderRepository.findAllByDoctorUserIdIsNotNullAndPatientUserId(patientId).stream()
                .map(this::convertOrderToDto)
                .collect(Collectors.toList());
    }
    // end Dasha Tkachenko


    @Override
    public List<OrderDto> findAllFreeOrdersForDoctor() {
        return orderRepository.findAllFreeOrdersForDoctor(LocalDateTime.now()).stream()
                .map(this::convertOrderToDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<OrderDto> findAllDoctorOrders(Long doctorId) {
        doctorRepository.findById(doctorId)
                .orElseThrow(ResourceNotFoundException::new);
        return orderRepository.findAllDoctorOrders(doctorId).stream()
                .map(this::convertOrderToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> findDoctorCalendar(Long doctorId) {
        doctorRepository.findById(doctorId)
                .orElseThrow(ResourceNotFoundException::new);
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime finishDate = startDate.plusDays(1).withHour(23).withMinute(59).withSecond(59); //next day end
        return orderRepository.findDoctorCalendar(doctorId, startDate, finishDate).stream()
                .map(this::convertOrderToDto)
                .collect(Collectors.toList());
    }

    private Order convertDtoToOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setCost(orderDto.getCost());
        if (orderDto.getStart() != null)
            order.setStart(LocalDateTime.parse(orderDto.getStart()));
        if (orderDto.getStart() != null)
            order.setFinish(LocalDateTime.parse(orderDto.getFinish()));
        if (orderDto.getProcedureId() != null)
            order.setProcedure(procedureRepository.getById(orderDto.getProcedureId()));
        if (orderDto.getDoctorId() != null)
            order.setDoctor(doctorRepository.getById(orderDto.getDoctorId()));
        if (orderDto.getPatientId() != null)
            order.setPatient(patientRepository.getById(orderDto.getPatientId()));
        return order;
    }

    private OrderDto convertOrderToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getOrderId());
        orderDto.setCost(order.getCost());
        if (order.getStart() != null)
            orderDto.setStart(order.getStart().toString());
        if (order.getFinish() != null)
            orderDto.setFinish(order.getFinish().toString());
        if (order.getProcedure() != null)
            orderDto.setProcedureId(order.getProcedure().getProcedureId());
        if (order.getDoctor() != null)
            orderDto.setDoctorId(order.getDoctor().getUserId());
        if (order.getPatient() != null)
            orderDto.setPatientId(order.getPatient().getUserId());
        return orderDto;
    }
}
