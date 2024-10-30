package org.jullaene.walkmong_back.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BasicResponse<T>{
    private String message;
    private Integer statusCode;
    private T data;

    private static final String SUCCESS = "Success";
    public static <T> BasicResponse<T> of(HttpStatus statusCode, String message, T data) {
        return new BasicResponse<>(message, statusCode.value(), data);
    }

    public static <T> BasicResponse<T> ofSuccess(T data){
        return new BasicResponse<>(SUCCESS, HttpStatus.OK.value(), data);
    }

    public static <T> BasicResponse<T> ofCreateSuccess(T data){
        return new BasicResponse<>(SUCCESS, HttpStatus.CREATED.value(), data);
    }

}
