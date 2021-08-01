package com.softserve.kh50project.davita.service;

import com.softserve.kh50project.davita.model.Doctor;
import com.softserve.kh50project.davita.model.Order;
import com.softserve.kh50project.davita.model.Patient;
import com.softserve.kh50project.davita.model.Procedure;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderService {
    Order readById(Long id);
    List<Order> read(LocalDateTime start, LocalDateTime finish, Procedure procedure, Doctor doctor, Patient patient);

    Order create(Order order);
    Order update(Order order, Long id);
    Order patch(Map<String, Object> fields, Long id);

    void delete(Long id);
}
