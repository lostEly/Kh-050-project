package com.softserve.kh50project.davita.service;

import com.softserve.kh50project.davita.dto.DoctorDto;
import com.softserve.kh50project.davita.dto.OrderDto;
import com.softserve.kh50project.davita.dto.PatientDto;
import com.softserve.kh50project.davita.dto.ProcedureDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderDto findById(Long id);
    List<OrderDto> findAll();
    List<OrderDto> findAllFreeOrdersByProcedure(Long procedureId);
    List<OrderDto> findAllFreeOrdersForDoctor();
    List<OrderDto> findAllPatientOrders(Long patientId);
    List<OrderDto> findAllDoctorOrders(Long doctorId);
    List<OrderDto> findDoctorCalendar(Long doctorId);
    List<OrderDto> find(LocalDateTime start, LocalDateTime finish, ProcedureDto procedureDto, DoctorDto doctorDto, PatientDto patientDto);

    OrderDto create(OrderDto orderDto);
    OrderDto update(OrderDto orderDto, Long id);
    OrderDto patch(Map<String, Object> fields, Long id);
    void delete(Long id);
}
