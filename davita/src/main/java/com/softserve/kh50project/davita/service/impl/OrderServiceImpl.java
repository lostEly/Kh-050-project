package com.softserve.kh50project.davita.service.impl;

import com.softserve.kh50project.davita.exceptions.ResourceNotFoundException;
import com.softserve.kh50project.davita.model.Doctor;
import com.softserve.kh50project.davita.model.Order;
import com.softserve.kh50project.davita.model.Patient;
import com.softserve.kh50project.davita.model.Procedure;
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
    public List<Order> read(LocalDateTime start, LocalDateTime finish, Procedure procedure, Doctor doctor, Patient patient ) {
        Specification<Order> specification = orderRepository.getOrderQuery(start,finish,procedure,doctor,patient);
        return orderRepository.findAll(specification);
    }

    @Override
    public Order create(Order procedure) {
        return orderRepository.save(procedure);
    }

    @Override
    public Order update(Order updatedOrder, Long id) {
        Order findOrder = readById(id);
        findOrder.setStart(updatedOrder.getStart());
        findOrder.setFinish(updatedOrder.getFinish());
        findOrder.setCost(updatedOrder.getCost());
        findOrder.setProcedure(updatedOrder.getProcedure());
        findOrder.setPatient(updatedOrder.getPatient());
        findOrder.setDoctor(updatedOrder.getDoctor());
        return orderRepository.save(findOrder);
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
