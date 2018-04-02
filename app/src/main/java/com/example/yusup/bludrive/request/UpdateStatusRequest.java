package com.example.yusup.bludrive.request;

/**
 * Created by Asus on 23/11/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateStatusRequest {
    @SerializedName("status")
    @Expose
    public String status;

    public UpdateStatusRequest(String status) {
        this.status = status;
    }
}
