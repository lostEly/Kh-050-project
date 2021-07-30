package com.softserve.kh50project.davita.service;

import com.softserve.kh50project.davita.model.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Order readById(Long id);
    List<Order> read(String start, String finish, long procedureId, long patientId, long doctorId);

    Order create(Order order);
    Order update(Order order, Long id);
    Order patch(Map<String, Object> fields, Long id);

    void delete(Long id);
}
