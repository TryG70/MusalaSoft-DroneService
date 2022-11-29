package com.musalasoftdroneservice.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class APIResponse<T> {

    private String message;
    private LocalDateTime time;
    private T dto;
}
