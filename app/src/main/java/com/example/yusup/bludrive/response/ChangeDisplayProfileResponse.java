package com.example.yusup.bludrive.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Asus on 29/09/2017.
 */

public class ChangeDisplayProfileResponse {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
