package com.example.yusup.bludrive.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yusup.bludrive.R;
import com.example.yusup.bludrive.RestAPI.BLuDriveAPI;
import com.example.yusup.bludrive.RestAPI.ServiceGeneratorAuth;
import com.example.yusup.bludrive.config.PrefHandler;
import com.example.yusup.bludrive.request.EditProfileRequest;
import com.example.yusup.bludrive.response.EditProfileResponse;
import com.example.yusup.bludrive.response.ProfileResponse;
import com.google.gson.Gson;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfil extends AppCompatActivity {
    EditText username, alamat, lahir, nama, email, txtTelp;
    TextView result,viewName;
    Button simpan;
    ImageView ivProfile,ivChange;
    NestedScrollView scrollView;

    String token;
    String extrasSlug;

    PrefHandler prefHandler;


    int year_x,month_x,day_x;
    static final int DIALOG_ID = 0;

    ProgressDialog pDialog;
    boolean isConnected,valid;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        username = (EditText)findViewById(R.id.txtUsername);
        alamat = (EditText)findViewById(R.id.txtAlamat);
        lahir = (EditText)findViewById(R.id.txtDate);
        nama = (EditText)findViewById(R.id.txtName);
        email = (EditText)findViewById(R.id.txtEmail);
        txtTelp = (EditText)findViewById(R.id.txtTelp);
        result = (TextView)findViewById(R.id.result);
//        viewName = (TextView)findViewById(R.id.viewName);
//        ivChange = (ImageView)findViewById(R.id.camera);
//        ivProfile = (ImageView)findViewById(R.id.ivProfile);
        simpan = (Button)findViewById(R.id.btnSimpan);

        scrollView = (NestedScrollView)findViewById(R.id.profileScrollView);

        username.setFocusable(false);
        username.setClickable(false);
        email.setFocusable(false);
        email.setClickable(false);

        prefHandler = new PrefHandler(this);

        token = prefHandler.getTOKEN_KEY();

        pDialog = new ProgressDialog(EditProfil.this);

        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        getProfile();

        final Calendar calendar = Calendar.getInstance();
        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        day_x = calendar.get(Calendar.DAY_OF_MONTH);

        showDialogOnClick();

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("");
                result.setVisibility(View.GONE);
                result.setBackgroundColor(Color.parseColor("#00ff00"));
                if (validation()){
                    doEditProfile();
                }
            }
        });


//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        setupViewPager(viewPager);
//
//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
        getSupportActionBar().setTitle("BLuDrive");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void showDialogOnClick(){
        lahir = (EditText)findViewById(R.id.txtDate);

        lahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID){
            return new DatePickerDialog(this, dpickerdialog, 1998,month_x,day_x);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerdialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month+1;
            day_x = dayOfMonth;
            lahir.setText(year_x+"-"+month_x+"-"+day_x);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        getProfile();
    }

    private void getProfile(){
        BLuDriveAPI client = ServiceGeneratorAuth.createService(BLuDriveAPI.class,token);
        Call<ProfileResponse> call = client.getProfile();
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                try{
                    pDialog.dismiss();
                    Log.d("RESPONSE : ", new Gson().toJson(response.body()));
                    username.setText(response.body().getUsername());
                    alamat.setText(response.body().getAlamat());
                    nama.setText(response.body().getNama());
                    lahir.setText(response.body().getTanggalLahir());
                    email.setText(response.body().getEmail());
                    txtTelp.setText(response.body().getTelepon());
                    viewName.setText(response.body().getNama());

                    prefHandler.setIMAGE_PROFILE_KEY(response.body().getFoto());

                }catch (Exception e){
                    Log.d("CATCH : ",e.toString());
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                pDialog.dismiss();
                Log.d("Failure", t.toString());
            }
        });
    }



    private void doEditProfile(){
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        BLuDriveAPI client = ServiceGeneratorAuth.createService(BLuDriveAPI.class, token);

        EditProfileRequest request = new EditProfileRequest(nama.getText().toString(),lahir.getText().toString(),alamat.getText().toString(),txtTelp.getText().toString());

        Call<EditProfileResponse> call = client.doEditProfile(request);

        call.enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response) {
                pDialog.dismiss();
                try{
                    if (response.isSuccessful()){
                        Log.d("DEBUG PROFILE 1",response.body().getMessage());
                        result.setVisibility(View.VISIBLE);
                        result.setBackgroundColor(Color.parseColor("#00ff00"));
                        result.setText(response.body().getMessage());

                        prefHandler.setNAME_KEY(nama.getText().toString());
                    }else{
                        Log.d("DEBUG PROFILE 2",response.body().getMessage());
                    }
                    scrollView.smoothScrollTo(0, 0);
                }catch (Exception e){
                    Log.d("DEBUG PROFILE 4",e.toString());
                }

            }

            @Override
            public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                pDialog.dismiss();
                Log.d("DEBUG PROFILE 3",t.toString());
                result.setVisibility(View.VISIBLE);
                if (!checkConnection()) {
                    Toast.makeText(getApplicationContext(), "Internet bermasalah!", Toast.LENGTH_LONG).show();
                } else {
                    result.setText("Semua Field Tidak Boleh Kosong");
                }
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private boolean validation(){

        valid = true;

        if (nama.getText().toString().trim().isEmpty() || lahir.getText().toString().trim().isEmpty() || alamat.getText().toString().trim().isEmpty()){
            Log.d("VALIDITY 1", "TIDAK BOLEH ADA FIELD KOSONG");
            result.setVisibility(View.VISIBLE);
            result.setText("Tidak ada Boleh Ada Field Yang Kosong");
            valid = false;
        }

        return valid;
    }

    private boolean checkConnection(){
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnected();
        return isConnected;
    }
}
