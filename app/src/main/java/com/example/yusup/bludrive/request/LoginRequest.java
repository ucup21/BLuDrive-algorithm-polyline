package com.example.yusup.bludrive.request;

/**
 * Created by Asus on 08/08/2017.
 */
public class LoginRequest {
    private String username;
    private String password;
    private String token;

    public LoginRequest(String username, String password, String token) {
        this.username = username;
        this.password = password;
        this.token    = token;
    }
}
