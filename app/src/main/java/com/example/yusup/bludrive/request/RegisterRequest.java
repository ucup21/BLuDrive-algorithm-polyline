package com.example.yusup.bludrive.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Asus on 19/01/2018.
 */

public class RegisterRequest {
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("nama")
    @Expose
    public String nama;
    @SerializedName("telepon")
    @Expose
    public String telepon;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("token")
    @Expose
    public String token;

    public String getUsername() {
        return username;
    }

    public RegisterRequest(String username, String email, String nama, String telepon, String password, String token){

    this.username = username;
    this.email = email;
    this.nama = nama;
    this.telepon = telepon;
    this.password = password;
    this.token = token;
    }
}
