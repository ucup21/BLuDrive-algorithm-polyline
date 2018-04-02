package com.example.yusup.bludrive.response;

/**
 * Created by Asus on 12/01/2018.
 */

import com.google.android.gms.common.api.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileResponse {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("nama")
    @Expose
    public String nama;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("tanggal_lahir")
    @Expose
    public String tanggalLahir;
    @SerializedName("jenis_kelamin")
    @Expose
    public String jenisKelamin;
    @SerializedName("alamat")
    @Expose
    public String alamat;
    @SerializedName("foto")
    @Expose
    public String foto;
    @SerializedName("telepon")
    @Expose
    public String telepon;
    @SerializedName("ni")
    @Expose
    public String ni;
    @SerializedName("id_status")
    @Expose
    public String idStatus;
    @SerializedName("id_otorisasi")
    @Expose
    public String idOtorisasi;
    @SerializedName("created_at")
    @Expose
    public Object createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("status")
    @Expose
    public Status status;

    public ProfileResponse(Integer id, String username, String nama, String email, String tanggalLahir, String jenisKelamin,
                           String alamat, String foto, String telepon, String ni, String idStatus, String idOtorisasi,
                           Object createdAt, String updatedAt){

        this.id = id;
        this.username = username;
        this.nama = nama;
        this.email = email;
        this.tanggalLahir = tanggalLahir;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
        this.foto = foto;
        this.telepon = telepon;
        this.ni = ni;
        this.idStatus = idStatus;
        this.idOtorisasi = idOtorisasi;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Created by Asus on 16/11/2017.
     */

    public static class status {
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("nama_status")
        @Expose
        public String namaStatus;
        @SerializedName("created_at")
        @Expose
        public Object createdAt;
        @SerializedName("updated_at")
        @Expose
        public Object updatedAt;

        public status(Integer id, String namaStatus, Object createdAt, Object updatedAt){
            this.id = id;
            this.namaStatus = namaStatus;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public Integer getId() {
            return id;
        }

        public String getNamaStatus() {
            return namaStatus;
        }

        public Object getCreatedAt() {
            return createdAt;
        }

        public Object getUpdatedAt() {
            return updatedAt;
        }
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getFoto() {
        return foto;
    }

    public String getTelepon() {
        return telepon;
    }

    public String getNi() {
        return ni;
    }

    public String getIdStatus() {
        return idStatus;
    }

    public String getIdOtorisasi() {
        return idOtorisasi;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Status getStatus() {
        return status;
    }
}

