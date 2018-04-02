package com.example.yusup.bludrive.RestAPI;

import com.example.yusup.bludrive.request.ChangePasswordRequest;
import com.example.yusup.bludrive.request.ChangeProfileDisplayRequest;
import com.example.yusup.bludrive.request.EditProfileRequest;
import com.example.yusup.bludrive.request.LoginRequest;
import com.example.yusup.bludrive.request.PesanRequest;
import com.example.yusup.bludrive.request.RegisterRequest;
import com.example.yusup.bludrive.request.TerimaRequest;
import com.example.yusup.bludrive.request.TolakRequest;
import com.example.yusup.bludrive.request.UpdateStatusRequest;
import com.example.yusup.bludrive.response.ChangeDisplayProfileResponse;
import com.example.yusup.bludrive.response.ChangePasswordResponse;
import com.example.yusup.bludrive.response.EditProfileResponse;
import com.example.yusup.bludrive.response.GetCariOrderBos;
import com.example.yusup.bludrive.response.GetCariOrderDriver;
import com.example.yusup.bludrive.response.GetCariPesan;
import com.example.yusup.bludrive.response.GetOrder;
import com.example.yusup.bludrive.response.GetOrderDriver;
import com.example.yusup.bludrive.response.GetPesan;
import com.example.yusup.bludrive.response.GetRiwayat;
import com.example.yusup.bludrive.response.LoginResponse;
import com.example.yusup.bludrive.response.PesanResponse;
import com.example.yusup.bludrive.response.ProfileResponse;
import com.example.yusup.bludrive.response.RegisterResponse;
import com.example.yusup.bludrive.response.TerimaResponse;
import com.example.yusup.bludrive.response.TolakResponse;
import com.example.yusup.bludrive.response.UpdateStatusResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Asus on 16/11/2017.
 */

public interface BLuDriveAPI {
    @POST("drive/user/pesan")
    Call<PesanResponse> doPemesanan (@Body PesanRequest pesanRequest);

    @GET("profile")
    Call<ProfileResponse> getProfile();

    @PATCH("profile")
    Call<EditProfileResponse> doEditProfile(@Body EditProfileRequest editProfileRequest);

    @PATCH("profile/password")
    Call<ChangePasswordResponse> doChangePassword(@Body ChangePasswordRequest changePasswordRequest);

    @PATCH("profile/foto")
    Call<ChangeDisplayProfileResponse> doChangeFoto(@Body ChangeProfileDisplayRequest changeProfileDisplayRequest);

    @POST("signin")
    Call<LoginResponse> doLogin(@Body LoginRequest loginRequest);

    @POST("signup")
    Call<RegisterResponse> doRegis(@Body RegisterRequest registerRequest);

    @Headers("Accept: application/json")
    @GET("drive/user/order/riwayat")
    Call<GetRiwayat> getRiwayat();

    @Headers("Accept: application/json")
    @GET("drive/user/order/riwayat/?page=")
    Call<GetRiwayat> getRiwayatMore(@Query("page") int page);

    @Headers("Accept: application/json")
    @GET("drive/bos/pesan")
    Call<GetPesan> getPesan();

    @Headers("Accept: application/json")
    @GET("drive/bos/pesan/?page=")
    Call<GetPesan> getPesanMore(@Query("page") int page);

    @Headers("Accept: application/json")
    @GET("drive/bos/order")
    Call<GetOrder> getOrder();

    @Headers("Accept: application/json")
    @GET("drive/bos/order/?page=")
    Call<GetOrder> getOrderMore(@Query("page") int page);

    @Headers("Accept: application/json")
    @PATCH("drive/bos/pesan/{pesan}")
    Call<TolakResponse> tolakResponse(@Path("pesan") int page, @Body TolakRequest tolakRequest);

    @Headers("Accept: application/json")
    @PATCH("drive/bos/pesan/{pesan}")
    Call<TerimaResponse> terimaResponse(@Path("pesan") int page, @Body TerimaRequest terimaRequest);

    @Headers("Accept: application/json")
    @GET("drive/bos/order/cari/?tanggal_pemesanan=")
    Call<GetCariOrderBos> getCariOrderBos(@Query("tanggal_pemesanan") String tanggal_pemesanan);

    @Headers("Accept: application/json")
    @GET("drive/pesan/cari/?tanggal_pemesanan=")
    Call<GetCariPesan> getCariPesan(@Query("tanggal_pemesanan") String tanggal_pemesanan);

    @Headers("Accept: application/json")
    @GET("drive/driver/order")
    Call<GetOrderDriver> getOrderDriver();

    @Headers("Accept: application/json")
    @GET("drive/driver/order/?page=")
    Call<GetOrderDriver> getOrderDriverMore(@Query("page") int page);

    @Headers("Accept: application/json")
    @GET("drive/driver/order/cari/?tanggal_pemesanan=")
    Call<GetCariOrderDriver> getCariOrderDriver(@Query("tanggal_pemesanan") String tanggal_pemesanan);

    @Headers("Accept: application/json")
    @PATCH("drive/driver/order/{order}")
    Call<UpdateStatusResponse> doupdatestatus(@Path("order") int page, @Body UpdateStatusRequest updateStatusRequest);

}
