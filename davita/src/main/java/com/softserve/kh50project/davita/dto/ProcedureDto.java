package com.softserve.kh50project.davita.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
public class ProcedureDto {
    private static final DateTimeFormatter timeFormat
            = DateTimeFormatter.ofPattern("HH:mm:ss");

    private Long procedureId;

    private String name;

    private String duration;

    private Double cost;

    public LocalTime convertStringToLocalTime() {
        return LocalTime.parse(this.duration);
    }

    public void convertLocalTimeToString(LocalTime duration) {
        this.duration = duration.format(timeFormat);
    }

}
