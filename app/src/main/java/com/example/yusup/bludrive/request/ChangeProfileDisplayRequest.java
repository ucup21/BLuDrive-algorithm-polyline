package com.example.yusup.bludrive.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Asus on 29/09/2017.
 */

public class ChangeProfileDisplayRequest {
    @SerializedName("foto")
    @Expose
    public String foto;

    public ChangeProfileDisplayRequest(String foto) {
        this.foto = foto;
    }
}
