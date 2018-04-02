package com.example.yusup.bludrive.response;

/**
 * Created by Asus on 08/08/2017.
 */
public class LoginResponse {
    private String status;
    private String message;
    private String token;
    private String id_otorisasi;

    public LoginResponse() {

    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public String getId_otorisasi() {
        return id_otorisasi;
    }
}
