package com.musalasoftdroneservice.reponse;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class APIResponse<T> {

    private String message;
    private LocalDateTime time;
    private T dto;
}
