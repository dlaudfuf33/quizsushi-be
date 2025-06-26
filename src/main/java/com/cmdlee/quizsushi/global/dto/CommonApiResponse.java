package com.cmdlee.quizsushi.global.tmp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> CommonApiResponse<T> ok(T data, String message) {
        return new CommonApiResponse<>(true, message, data);
    }

    public static <T> CommonApiResponse<T> fail(String message) {
        return new CommonApiResponse<>(false, message, null);
    }
}