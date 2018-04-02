package com.example.yusup.bludrive.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Asus on 04/12/2017.
 */

public class TolakRequest {
    @SerializedName("status")
    @Expose
    public String status;

    public TolakRequest(String status){
        this.status = status;
    }


}
