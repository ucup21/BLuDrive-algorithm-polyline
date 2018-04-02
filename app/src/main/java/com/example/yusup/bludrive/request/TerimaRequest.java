package com.example.yusup.bludrive.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Asus on 04/12/2017.
 */

public class TerimaRequest {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("id_driver")
    @Expose
    public String idDriver;

    public TerimaRequest(String status, String id_driver){
        this.status = status;
        this.idDriver = id_driver;
    }
}
