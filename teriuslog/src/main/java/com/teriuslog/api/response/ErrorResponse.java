package com.teriuslog.api.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 *     "code":"400"
 *     "message":"bad request"
 *     "validation":{
 *         "title":"값을 입력해주세요"
 *     }
 * }
 */
@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final String code;
    private final String message;
    private final Map<String,String> validation = new HashMap<>();

    public void addValidation(String fiedlName, String errorMassage){
        validation.put(fiedlName,errorMassage);
    }
}
