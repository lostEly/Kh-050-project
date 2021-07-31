package com.softserve.kh50project.davita.service.impl;

import com.softserve.kh50project.davita.exceptions.ResourceNotFoundException;
import com.softserve.kh50project.davita.model.Order;
import com.softserve.kh50project.davita.repository.DoctorRepository;
import com.softserve.kh50project.davita.repository.OrderRepository;
import com.softserve.kh50project.davita.repository.PatientRepository;
import com.softserve.kh50project.davita.repository.ProcedureRepository;
import com.softserve.kh50project.davita.service.OrderService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import lombok.AllArgsConstructor;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProcedureRepository procedureRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Override
    public Order readById(Long id) {
        return orderRepository
                .findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public List<Order> read(String start, String finish, long procedureId, long patientId, long doctorId) {
        Specification<Order> specification = orderRepository.getOrderQuery(
                LocalDateTime.parse(start),
                LocalDateTime.parse(finish),
                procedureRepository.getById(procedureId),
                patientRepository.getById(patientId),
                doctorRepository.getById(doctorId));
        return orderRepository.findAll(specification);
    }

    @Override
    public Order create(Order procedure) {
        return orderRepository.save(procedure);
    }

    @Override
    public Order update(Order newProcedure, Long id) {
        newProcedure.setOrderId(id);
        return orderRepository.save(newProcedure);
    }

    @Override
    public Order patch(Map<String, Object> fields, Long id) {
        Order order = readById(id);
        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Order.class, k);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, order, v);
            }
        });
        return orderRepository.save(order);
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}
