package com.teriuslog.api.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class TeriusWebException extends RuntimeException{

    public final Map<String,String> validation = new HashMap<>();

    public TeriusWebException(String message) {
        super(message);
    }

    public TeriusWebException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message){
        validation.put(fieldName,message);
    }
}
