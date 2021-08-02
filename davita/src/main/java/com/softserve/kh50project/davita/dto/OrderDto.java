package com.softserve.kh50project.davita.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderDto {
    private Long orderId;
    private String start;
    private String finish;
    private Double cost;
    private Long procedureId;
    private Long doctorId;
    private Long patientId;
}