package com.FVSS.numisis.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse<T> {

    private String message;
    private T dado;

    public AuthResponse(String message) {
        this.message = message;
    }

    public AuthResponse(T dado){
        this.dado = dado;
    }

    public AuthResponse(String message, T dado) {
        this.message = message;
        this.dado = dado;
    }


}