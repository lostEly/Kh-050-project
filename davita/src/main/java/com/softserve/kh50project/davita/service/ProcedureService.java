package com.softserve.kh50project.davita.service;

import com.softserve.kh50project.davita.model.Procedure;

import java.util.List;
import java.util.Map;

public interface ProcedureService {
    Procedure readById(Long id);

    List<Procedure> read(String name, Double cost, String duration);

    Procedure create(Procedure procedure);

    Procedure update(Procedure procedure, Long id);

    Procedure patch(Map<String, Object> fields, Long id);

    void delete(Long id);
}
