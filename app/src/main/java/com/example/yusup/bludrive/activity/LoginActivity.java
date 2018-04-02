package com.example.yusup.bludrive.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yusup.bludrive.R;
import com.example.yusup.bludrive.RestAPI.BLuDriveAPI;
import com.example.yusup.bludrive.RestAPI.CheckConnection;
import com.example.yusup.bludrive.RestAPI.ServiceGenerator;
import com.example.yusup.bludrive.config.PrefHandler;
import com.example.yusup.bludrive.request.LoginRequest;
import com.example.yusup.bludrive.response.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText txtUsername, txtPassword;
    TextView error, btnDaftar;

    CheckConnection checkConnection;
    ProgressDialog pDialog;

    //Shared Prefrences
    SharedPreferences sharedPreferences;
    String token_key = "token";
    String mypref = "MYPREFRENCES";

    PrefHandler prefHandler;

    boolean isConnected,valid,doubleBackToExitPressedOnce = false;
    private static final int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_new);

        //Inisialisasi
        txtUsername = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        error       = (TextView)findViewById(R.id.failed_login);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnDaftar=(TextView)findViewById(R.id.txtRegister);

        checkConnection = new CheckConnection(getApplicationContext());

        //Instansiasi Progress Dialog
        pDialog = new ProgressDialog(LoginActivity.this);

        //Instansisasi SharedPref class PrefHandler
        sharedPreferences = getSharedPreferences(token_key, Context.MODE_PRIVATE);
        prefHandler = new PrefHandler(this);


        //Mengecek apakah token tersimpan kedalam SharedPref
        if (prefHandler.isToken_Key_Exist()){
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            Log.d("TOKEN 1", "ADA");
        }else{
            Log.d("TOKEN 2","TIDAK ADA");
        }



        //Melakukan Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error.setText("");
                error.setVisibility(View.GONE);

                //Jika Valid maka tampilkan Progress Dialog dan lakukan proses Login, else TextView error mucul
                if (validation()) {
                    pDialog.setCancelable(false);
                    pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                    pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    pDialog.show();
                    doLogin();
                } else {
                    error.setVisibility(View.VISIBLE);
                    error.setText("Username dan Password Salah!");
                }
            }
        });

        //Melakukan Daftar
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error.setVisibility(View.VISIBLE);
                error.setText("Username : yusup21 dan password : password");
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                LoginActivity.this.startActivity(intent);
//                LoginActivity.this.finish();
            }
        });
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

    }


    //Proses Login dengan Retrofit (proses mengirim data ke Server beserta responsenya), Jika Data Valid maka akan didirect ke Trantition.class
    private void doLogin(){
        BLuDriveAPI client = ServiceGenerator.createService(BLuDriveAPI.class);

        LoginRequest loginRequest = new LoginRequest(txtUsername.getText().toString().trim(),txtPassword.getText().toString().trim(),"xxxxx");

        Call<LoginResponse> call = client.doLogin(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                pDialog.dismiss();

                try {
                    if (response.isSuccessful()){
                        Log.d("VALIDITY LOGIN 1", "BERHASIL");
                        if (response.body().getStatus().toString().equalsIgnoreCase("false")){
                            Log.d("VALIDITY LOGIN 3", "BERHASIL");
                            error.setVisibility(View.VISIBLE);
                            error.setText(response.body().getMessage());
                        }else{
                            if (sharedPreferences.contains(token_key)){
                                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                                homeIntent.addCategory( Intent.CATEGORY_HOME );
                                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(homeIntent);
                                error.setText(token_key);
                                Log.d("TOKEN 1", "ADA");
                            }else{

                                //Simapn Token ke SharedPref
                                prefHandler.setTOKEN_KEY(response.body().getToken());
                                String otorisasi = response.body().getId_otorisasi();
                                Log.d("OTORISASI",otorisasi);
                                if(otorisasi.equals("3")){
                                    Intent intent = new Intent(LoginActivity.this, MainDrawer.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    prefHandler.setID_OTORISASI("3");
                                    startActivity(intent);
                                    finish();
                                } else if (otorisasi.equals("6")) {
                                    Intent intent = new Intent(LoginActivity.this, MainDrawerBos.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    prefHandler.setID_OTORISASI("6");
                                    startActivity(intent);
                                    finish();
                                } else if (otorisasi.equals("7")) {
                                    Intent intent = new Intent(LoginActivity.this, MainDrawerDriver.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    prefHandler.setID_OTORISASI("7");
                                    startActivity(intent);
                                    finish();
                                }

                                //TODO::bikin statement IF untuk profiling USER berdasarkan ID_OTORISASI

                               Toast.makeText(getApplicationContext(), "Berhasil Login!", Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(LoginActivity.this, MainDrawer.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                                        Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                startActivity(intent);
//                                finish();
                                error.setText(response.body().getToken());
                                Log.d("VALIDITY LOGIN 4", "BERHASIL");
                                Log.d("TOKEN 2","TIDAK ADA");
                            }
                        }
                    }else{
                        Log.d("VALIDITY LOGIN 2", "GAGAL");
                        error.setVisibility(View.VISIBLE);
                        error.setText(response.body().getMessage());
                    }
                } catch (Exception e) {
                    error.setText("Password dan Username Salah!");
                    Log.d("DEBUG EXCEPTION : ", e.toString());
                }

            }

            //Jika Response Gagal
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                pDialog.dismiss();
                Log.d("FAILURE", t.toString());
                if (!checkConnection()){
                    Toast.makeText(getApplicationContext(), "Internet bermasalah!", Toast.LENGTH_LONG).show();
                }else{
                    error.setText("Password dan Username Salah!");
                }
            }
        });
    }

    //Mengecek Koneksi internet
    private boolean checkConnection(){
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnected();
        return isConnected;
    }


    //Lakukan Validasi pada EditText
    private boolean validation(){

        valid = true;

        if (txtUsername.getText().toString().trim().isEmpty() || txtPassword.getText().toString().trim().isEmpty()
                || txtUsername.getText().toString().trim().length() < 5 || txtPassword.getText().toString().trim().length() < 6){
            Log.d("VALIDITY 1", "Username dan Password Salah");
            error.setVisibility(View.VISIBLE);
            error.setText("Username dan Password Salah!");
            valid = false;
        }else{
            Log.d("VALIDITY 2","BERHASIL");
        }

        return valid;
    }


    //Ketika Back Button diClik
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            //super.onBackPressed();
            //finish();
            //moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tap lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


}
