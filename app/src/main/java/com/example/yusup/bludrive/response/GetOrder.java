package com.example.yusup.bludrive.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Asus on 13/11/2017.
 */

public class GetOrder {
    @SerializedName("current_page")
    @Expose
    public Integer currentPage;
    @SerializedName("data")
    @Expose
    public List<Datum> data = null;
    @SerializedName("from")
    @Expose
    public Integer from;
    @SerializedName("last_page")
    @Expose
    public Integer lastPage;
    @SerializedName("next_page_url")
    @Expose
    public Object nextPageUrl;
    @SerializedName("path")
    @Expose
    public String path;
    @SerializedName("per_page")
    @Expose
    public Integer perPage;
    @SerializedName("prev_page_url")
    @Expose
    public Object prevPageUrl;
    @SerializedName("to")
    @Expose
    public Integer to;
    @SerializedName("total")
    @Expose
    public Integer total;

    /**
     * Created by Asus on 13/11/2017.
     */

    public static class Datum {
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("id_user")
        @Expose
        public String idUser;
        @SerializedName("id_mobil")
        @Expose
        public String idMobil;
        @SerializedName("id_driver")
        @Expose
        public String idDriver;
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
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("keterangan")
        @Expose
        public Object keterangan;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("mobil_drive")
        @Expose
        public MobilDrive mobilDrive;
        @SerializedName("user")
        @Expose
        public User user;
        @SerializedName("driver")
        @Expose
        public Driver driver;


        public Datum(Integer id, String idUser, String idMobil, String idDriver, String posisi, String posisiTujuan,
                     String tanggalPemesanan, String waktuPemesanan, String status, Object keterangan, String createdAt, String updatedAt) {

            this.id = id;
            this.idUser = idUser;
            this.idMobil = idMobil;
            this.idDriver = idDriver;
            this.posisi = posisi;
            this.posisiTujuan = posisiTujuan;
            this.tanggalPemesanan = tanggalPemesanan;
            this.waktuPemesanan = waktuPemesanan;
            this.status = status;
            this.keterangan = keterangan;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public Integer getId() {return id;}
        public String getIdUser() {return idUser;}
        public String getIdMobil() {return idMobil;}
        public String getIdDriver() {return idDriver;}
        public String getPosisi() {return posisi;}
        public String getPosisiTujuan() {return posisiTujuan;}
        public String getTanggalPemesanan() {return tanggalPemesanan;}
        public String getWaktuPemesanan() {return waktuPemesanan;}
        public String getStatus() {return status;}
        public Object getKeterangan() {return keterangan;}
        public String getCreatedAt() {return createdAt;}
        public String getUpdatedAt() {return updatedAt;}

        /**
         * Created by Asus on 13/11/2017.
         */

        public static class Driver {
            @SerializedName("id_user")
            @Expose
            public Integer idUser;
            @SerializedName("nama_driver")
            @Expose
            public String namaDriver;
            @SerializedName("user")
            @Expose
            public UserDriver user;

            public Driver(Integer idUser, String namaDriver){
                this.idUser = idUser;
                this.namaDriver = namaDriver;
            }


            /**
             * Created by Asus on 13/11/2017.
             */

            static class UserDriver {
                @SerializedName("id")
                @Expose
                public Integer id;
                @SerializedName("nama")
                @Expose
                public String nama;
                @SerializedName("foto")
                @Expose
                public String foto;
                @SerializedName("telepon")
                @Expose
                public String telepon;

                public UserDriver(Integer id, String nama, String foto, String telepon){
                    this.id = id;
                    this.nama = nama;
                    this.foto = foto;
                    this.telepon = telepon;
                }

                public Integer getId() {
                    return id;
                }

                public String getNama() {
                    return nama;
                }

                public String getFoto() {
                    return foto;
                }

                public String getTelepon() {
                    return telepon;
                }
            }

            public Integer getIdUser() {
                return idUser;
            }

            public String getNamaDriver() {
                return namaDriver;
            }

        }

        /**
         * Created by Asus on 13/11/2017.
         */

        public static class User {
            @SerializedName("id")
            @Expose
            public Integer id;
            @SerializedName("nama")
            @Expose
            public String nama;
            @SerializedName("foto")
            @Expose
            public String foto;
            @SerializedName("telepon")
            @Expose
            public String telepon;

            public User(Integer id, String nama, String foto, String telepon){

                this.id = id;
                this.nama = nama;
                this.foto = foto;
                this.telepon = telepon;
            }

            public Integer getId() {
                return id;
            }

            public String getNama() {
                return nama;
            }

            public String getFoto() {
                return foto;
            }

            public String getTelepon() {
                return telepon;
            }
        }

        /**
         * Created by Asus on 13/11/2017.
         */

        public static class MobilDrive {
            @SerializedName("id")
            @Expose
            public Integer id;
            @SerializedName("nama_mobil")
            @Expose
            public String namaMobil;

            public MobilDrive(Integer id, String namaMobil){
                this.id = id;
                this.namaMobil = namaMobil;
            }

            public Integer getId() {
                return id;
            }

            public String getNamaMobil() {
                return namaMobil;
            }

        }
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public List<Datum> getData() {
        return data;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public Object getNextPageUrl() {
        return nextPageUrl;
    }

    public String getPath() {
        return path;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public Object getPrevPageUrl() {
        return prevPageUrl;
    }

    public Integer getTo() {
        return to;
    }

    public Integer getTotal() {
        return total;
    }


}
