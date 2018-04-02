package com.example.yusup.bludrive.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Asus on 14/08/2017.
 */
public class ChangePasswordRequest {
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("password_konfirmasi")
    @Expose
    public String passwordKonfirmasi;

    public ChangePasswordRequest(String password, String passwordKonfirmasi) {
        this.password = password;
        this.passwordKonfirmasi = passwordKonfirmasi;
    }
}
