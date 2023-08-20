package com.pu.programus.global.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultResponse {

    String msg;

    public void setResponse(HttpServletResponse response, HttpStatus status) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String obj = objectMapper.writeValueAsString(this);
        response.getWriter().write(obj);
    }
}
