package com.softserve.kh50project.davita.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class HandlerResponseDto {

    private int status;
    private String message;
    private LocalDateTime timestamp;

}
