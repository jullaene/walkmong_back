package org.jullaene.walkmong_back.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ResponseMessage<T> {
    private String message;
    private Integer statusCode;
    private T data;

    public static <T> ResponseMessage<T> of(HttpStatus statusCode, String message, T data) {
        return new ResponseMessage(message, statusCode.value(), data);
    }

    public static ResponseMessage ofSuccess(){
        return new ResponseMessage("Success", HttpStatus.CREATED.value(), ofSuccess().getData());
    }
}
