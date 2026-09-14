package com.delivery.habsida.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(

        int status,
        List<String> errors,
        LocalDateTime timeStamp,
        String path
) {
}
