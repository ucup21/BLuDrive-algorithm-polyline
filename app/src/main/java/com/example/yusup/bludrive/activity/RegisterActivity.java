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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yusup.bludrive.R;
import com.example.yusup.bludrive.RestAPI.BLuDriveAPI;
import com.example.yusup.bludrive.RestAPI.CheckConnection;
import com.example.yusup.bludrive.RestAPI.ServiceGenerator;
import com.example.yusup.bludrive.config.PrefHandler;
import com.example.yusup.bludrive.request.RegisterRequest;
import com.example.yusup.bludrive.response.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.yusup.bludrive.R.id.txtTelp;

public class RegisterActivity extends AppCompatActivity{
    Button btnDaftar;
    EditText txtUsername, txtPassword, txtNama, txtNoTelp, txtEmail;
    TextView  result;

    CheckConnection checkConnection;
    ProgressDialog pDialog;
    ScrollView scrollView;

    //Shared Prefrences
    SharedPreferences sharedPreferences;
    String token_key = "token";
    String mypref = "MYPREFRENCES";

    PrefHandler prefHandler;

    boolean isConnected,valid;
    private static final int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        //Inisialisasi
        txtUsername = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtNama = (EditText)findViewById(R.id.txtName);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtNoTelp = (EditText)findViewById(txtTelp);
        result       = (TextView)findViewById(R.id.result);
        btnDaftar = (Button)findViewById(R.id.btnDaftar);
        scrollView = (ScrollView)findViewById(R.id.scrollViewuser);

        checkConnection = new CheckConnection(getApplicationContext());



        //Instansiasi Progress Dialog
        pDialog = new ProgressDialog(RegisterActivity.this);


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

        //Menampilkan ShowPassword saat button Show diklik
//        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (!isChecked) {
//                    // show password
//                    txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                } else {
//                    // hide password
//                    txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                }
//            }
//        });


        //Melakukan Login
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("");
                result.setVisibility(View.GONE);
                result.setBackgroundColor(Color.parseColor("#00ff00"));

                //Jika Valid maka tampilkan Progress Dialog dan muncul halaman login, else TextView error mucul
                if (validation()) {
                    pDialog.setCancelable(false);
                    pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                    pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    pDialog.show();
                    doRegis();
                }
            }
        });


        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        getSupportActionBar().setTitle("BLuDrive");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void doRegis(){
        BLuDriveAPI client = ServiceGenerator.createService(BLuDriveAPI.class);

        RegisterRequest registerRequest = new RegisterRequest(txtUsername.getText().toString().trim(), txtEmail.getText().toString(),
                txtNama.getText().toString(), txtNoTelp.getText().toString(), txtPassword.getText().toString().trim(),"xxxxx");

        Call<RegisterResponse> call = client.doRegis(registerRequest);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Berhasil Mendaftar!",Toast.LENGTH_LONG).show();
                Log.d("RESPONSE ALL", response.raw().message());
                Log.d("RESPONSE EMAIL",response.body().getEmail());
                Log.d("RESPONSE ID", String.valueOf(response.body().getId()));
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("nama",response.body().getNama());
                intent.putExtra("email",response.body().getEmail());
                startActivity(intent);

            }

            //Jika Response Gagal
            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                pDialog.dismiss();
                Log.d("FAIL", t.toString());
                result.setVisibility(View.VISIBLE);
                if (!checkConnection()) {
                    Toast.makeText(getApplicationContext(), "Internet bermasalah!", Toast.LENGTH_LONG).show();
                } else {
                    result.setText("Username atau Email sudah digunakan");
                    scrollView.smoothScrollTo(0, 0);
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


    private boolean validation() {

        valid = true;

        if (txtUsername.getText().toString().trim().isEmpty() || txtNama.getText().toString().isEmpty() || txtEmail.getText().toString().trim().isEmpty() || txtPassword.getText().toString().trim().isEmpty() || txtNoTelp.getText().toString().trim().isEmpty()) {
            Log.d("VALIDITY 1", "TIDAK BOLEH ADA FIELD KOSONG");
            result.setVisibility(View.VISIBLE);
            result.setText("Tidak ada Boleh Ada Field Yang Kosong");
            scrollView.smoothScrollTo(0, 0);
            valid = false;
        } else {
            Log.d("VALIDITY 2", "ISI");
            if (txtUsername.getText().toString().trim().length() < 5 || txtPassword.getText().toString().trim().length() < 5) {
                Log.d("VALIDITY 5", "ISI");
                result.setVisibility(View.VISIBLE);
                result.setText("Username dan Password minamal 5 karakter");
                scrollView.smoothScrollTo(0, 0);
                valid = false;
            }
        }
        return valid;
    }

}
