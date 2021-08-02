package com.softserve.kh50project.davita.service;

import com.softserve.kh50project.davita.dto.DoctorDto;
import com.softserve.kh50project.davita.dto.OrderDto;
import com.softserve.kh50project.davita.dto.PatientDto;
import com.softserve.kh50project.davita.dto.ProcedureDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderDto readById(Long id);
    List<OrderDto> read(LocalDateTime start, LocalDateTime finish, ProcedureDto procedureDto, DoctorDto doctorDto, PatientDto patientDto);

    OrderDto create(OrderDto orderDto);
    OrderDto update(OrderDto orderDto, Long id);
    OrderDto patch(Map<String, Object> fields, Long id);

    void delete(Long id);

    List<OrderDto> findAllFreeOrdersByProcedure(Long procedureId);

    List<OrderDto> findAllPatientOrders(Long patientId);
}
