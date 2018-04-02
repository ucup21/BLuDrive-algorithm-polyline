package com.example.yusup.bludrive.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Asus on 16/11/2017.
 */

public class PesanResponse {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;

    private String idMobil;
    private String posisi;
    private String posisiTujuan;
    private String tanggalPemesanan;
    private String waktuPemesanan;
    private String keteranganUser;

    public PesanResponse(String idMobil, String posisi, String posisiTujuan, String tanggalPemesanan, String waktuPemesanan, String keteranganUser) {
        this.idMobil = idMobil;
        this.posisi = posisi;
        this.posisiTujuan = posisiTujuan;
        this.tanggalPemesanan = tanggalPemesanan;
        this.waktuPemesanan = waktuPemesanan;
        this.keteranganUser = keteranganUser;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
