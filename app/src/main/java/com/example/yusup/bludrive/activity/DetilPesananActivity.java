package com.example.yusup.bludrive.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yusup.bludrive.R;
import com.example.yusup.bludrive.RestAPI.BLuDriveAPI;
import com.example.yusup.bludrive.RestAPI.ServiceGeneratorAuth;
import com.example.yusup.bludrive.request.TerimaRequest;
import com.example.yusup.bludrive.request.TolakRequest;
import com.example.yusup.bludrive.response.TerimaResponse;
import com.example.yusup.bludrive.response.TolakResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetilPesananActivity extends AppCompatActivity {

    private TextView txtNama, txtTelepon, txtPosisi, txtPosisiTujuan, txtMobil, txtTanggal, txtWaktu, txtKeterangan;
    private Button btnTolak, btnTerima;
    private ProgressDialog progress;
    private String id, id_driver;

    private int Hold;
    private Spinner spnDriver;
    String[] number = new String[]{
            "Samsul", "Danu"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detilpesanan);

        progress = new ProgressDialog(this);
        progress.setMessage("LOADING . . . .");
        progress.setCancelable(false);

        txtNama = (TextView) findViewById(R.id.txtNama);
        txtTelepon = (TextView) findViewById(R.id.txtTelepon);
        txtPosisi = (TextView) findViewById(R.id.txtjemput);
        txtPosisiTujuan = (TextView) findViewById(R.id.txtTujuan);
        txtMobil = (TextView) findViewById(R.id.txtMobil);
        txtTanggal = (TextView) findViewById(R.id.txtTanggalJemput);
        txtWaktu = (TextView) findViewById(R.id.txtWaktu);
        txtKeterangan = (TextView) findViewById(R.id.txtKeterangan);

        spnDriver = (Spinner) findViewById(R.id.spnDriver);

        btnTolak = (Button) findViewById(R.id.btnTolak);
        btnTerima = (Button) findViewById(R.id.btnTerima);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        id = b.getString("id");
        final String posisi = b.getString("posisi"); txtPosisi.setText(posisi);
        final String keterangan = b.getString("keterangan_user"); txtKeterangan.setText(keterangan);
        final String posisi_tujuan = b.getString("posisi_tujuan"); txtPosisiTujuan.setText(posisi_tujuan);
        final String nama = b.getString("nama"); txtNama.setText(nama);
        final String mobil = b.getString("nama_mobil"); txtMobil.setText(mobil);
        final String telepon = b.getString("telepon"); txtTelepon.setText(telepon);
        final String tanggal = b.getString("tanggal_pemesanan");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat vd = new SimpleDateFormat("dd-MM-yyyy");
        try {
            String formmatedDate2 = vd.format(df.parse(tanggal));
            txtTanggal.setText(formmatedDate2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final String waktu = b.getString("waktu_pemesanan");
        SimpleDateFormat tm = new SimpleDateFormat("HH:mm");
        SimpleDateFormat tn = new SimpleDateFormat("HH:mm:ss");
        try {
            String formattedTime = tm.format(tn.parse(waktu));
            txtWaktu.setText(formattedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d("id : ", id);
        Linkify.addLinks(txtTelepon, Linkify.PHONE_NUMBERS);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item_position,number );

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_position);

        spnDriver.setAdapter(spinnerArrayAdapter);

        spnDriver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                switch(position) {

                    case 0 : // for item 1
                        id_driver = "3";
                        break;

                    case 1 :
                        id_driver = "39";
                        break;

             /* you can have any number of case statements */
                    default :

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });


        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateStatusTolak();
            }
        });

        btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateStatusTerima();
            }
        });
    }

        /*Log.d("kategori : ", kategori);
        Log.d("keterangan : ", keterangan);
        Log.d("foto : ", foto);
        Log.d("nama : ", nama);
        Log.d("ni : ", nomorInduk);
        Log.d("telepon : ", telepon);
        Log.d("email : ", email);
        Log.d("status : ", idstatus);*/

    public void UpdateStatusTolak(){
        String status = "2";
        Integer pesan = Integer.parseInt(id);
        progress.show();
        final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIsImlzcyI6Imh0dHBzOi8vYmx1ZHJpdmUuYW5nZ2l0cHJheW9nby5jb20vYXBpL2F1dGgvc2lnbmluIiwiaWF0IjoxNTEwNjczMTQ2LCJleHAiOjE1MjEwNDExNDYsIm5iZiI6MTUxMDY3MzE0NiwianRpIjoibEYwOFlpVlZEMExwdnNKcSJ9.KHL0_nBOqpNGl4UddSLrHS2ZTGhrZKGLzx05zt2W9ko";
        BLuDriveAPI client = ServiceGeneratorAuth.createService(BLuDriveAPI.class, token);
        TolakRequest tolakRequest = new TolakRequest(status);
        Call<TolakResponse> call = client.tolakResponse(pesan, tolakRequest);
        call.enqueue(new Callback<TolakResponse>() {
            @Override
            public void onResponse(Call<TolakResponse> call, Response<TolakResponse> response) {
                String message = response.body().getMessage();
                String status1 = response.body().getStatus();
                Log.d("STATUS",status1);
                Log.d("MESSAGE",message);
                progress.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_LONG);
                toast.show();
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500); //THREAD BUAT TOAST JALAN DULU
                            startActivity(new Intent(DetilPesananActivity.this, MainDrawerBos.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
            @Override
            public void onFailure(Call<TolakResponse> call, Throwable t) {

                Toast toast1 = Toast.makeText(getApplicationContext(), "TERJADI KESALAHAN PADA JARINGAN, MOHON DIULANG", Toast.LENGTH_LONG);
                toast1.show();
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3500); //THREAD BUAT TOAST JALAN DULU
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        });
    }

    public void UpdateStatusTerima(){
        String status = "1";
        Integer pesan = Integer.parseInt(id);
        progress.show();
        final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIsImlzcyI6Imh0dHBzOi8vYmx1ZHJpdmUuYW5nZ2l0cHJheW9nby5jb20vYXBpL2F1dGgvc2lnbmluIiwiaWF0IjoxNTEwNjczMTQ2LCJleHAiOjE1MjEwNDExNDYsIm5iZiI6MTUxMDY3MzE0NiwianRpIjoibEYwOFlpVlZEMExwdnNKcSJ9.KHL0_nBOqpNGl4UddSLrHS2ZTGhrZKGLzx05zt2W9ko";
        BLuDriveAPI client = ServiceGeneratorAuth.createService(BLuDriveAPI.class, token);
        TerimaRequest terimaRequest = new TerimaRequest(status, id_driver);
        Call<TerimaResponse> call = client.terimaResponse(pesan, terimaRequest);
        call.enqueue(new Callback<TerimaResponse>() {
            @Override
            public void onResponse(Call<TerimaResponse> call, Response<TerimaResponse> response) {

                String message = response.body().getMessage();
                String status1 = response.body().getStatus();
                Log.d("STATUS",status1);
                Log.d("MESSAGE",message);
                progress.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_LONG);
                toast.show();
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500); //THREAD BUAT TOAST JALAN DULU
                            startActivity(new Intent(DetilPesananActivity.this, MainDrawerBos.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
            @Override
            public void onFailure(Call<TerimaResponse> call, Throwable t) {

                Toast toast1 = Toast.makeText(getApplicationContext(), "TERJADI KESALAHAN PADA JARINGAN, MOHON DIULANG", Toast.LENGTH_LONG);
                toast1.show();
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3500); //THREAD BUAT TOAST JALAN DULU
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        });
    }

}
