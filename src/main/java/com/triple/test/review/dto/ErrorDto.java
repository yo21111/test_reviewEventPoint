package com.triple.test.review.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ErrorDto {
    private Exception e;
    private String msg;
    private LocalDate date;

    public ErrorDto(Exception e, String msg, LocalDate date) {
        this.e = e;
        this.msg = msg;
        this.date = date;
    }
}
