package com.softserve.kh50project.davita.dto;

import com.softserve.kh50project.davita.model.Order;
import lombok.AllArgsConstructor;
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

//    public OrderDto(Order order){
//        if (order == null){
//            throw new NullPointerException();
//        }
//        this.id = order.getOrderId();
//        this.start = order.getStart().toString();
//        this.finish = order.getFinish().toString();
//        this.cost = order.getCost();
//        this.procedureId = order.getProcedure().getProcedureId();
//        this.doctorId = order.getDoctor().getUserId();
//        this.patientId = order.getPatient().getUserId();
//    }
}