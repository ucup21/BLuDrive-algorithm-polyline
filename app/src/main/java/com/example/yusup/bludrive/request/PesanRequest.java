package com.example.yusup.bludrive.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Asus on 16/11/2017.
 */

public class PesanRequest {
    @SerializedName("id_mobil")
    @Expose
    public int idMobil;
    @SerializedName("posisi")
    @Expose
    public String posisi;
    @SerializedName("posisi_tujuan")
    @Expose
    public String posisiTujuan;
    @SerializedName("tanggal_pemesanan")
    @Expose
    public String tanggalPemesanan;
    @SerializedName("waktu_pemesanan")
    @Expose
    public String waktuPemesanan;
    @SerializedName("keterangan_user")
    @Expose
    public String keteranganUser;

    public PesanRequest(int idMobil, String posisi, String posisiTujuan, String tanggalPemesanan, String waktuPemesanan, String keteranganUser) {
        this.idMobil = idMobil;
        this.posisi = posisi;
        this.posisiTujuan = posisiTujuan;
        this.tanggalPemesanan = tanggalPemesanan;
        this.waktuPemesanan = waktuPemesanan;
        this.keteranganUser = keteranganUser;
    }
}
