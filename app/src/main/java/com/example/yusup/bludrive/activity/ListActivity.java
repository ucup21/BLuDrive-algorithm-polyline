package com.example.yusup.bludrive.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yusup.bludrive.R;
import com.example.yusup.bludrive.RestAPI.BLuDriveAPI;
import com.example.yusup.bludrive.RestAPI.ServiceGeneratorAuth;
import com.example.yusup.bludrive.adapter.CariPesananAdapter;
import com.example.yusup.bludrive.adapter.PesananAdapter;
import com.example.yusup.bludrive.response.GetCariPesan;
import com.example.yusup.bludrive.response.GetPesan;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerViewPesanan;
    private TextView txtPage, txtNoPage, txtItem, txtJmlItem;
    private Button btnPrev, btnNext, btnCari;

    private String cariTanggal;

    private SwipeRefreshLayout swipeRefreshLayout;

    private EditText etCariTanggal;
    private int year, month, day;
    private Calendar calendar;

    ProgressDialog progress;

    Integer page, limit, item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pesanan);

        progress = new ProgressDialog(this);
        progress.setMessage("LOADING . . . .");
        progress.setCancelable(false);

        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnPrev.setVisibility(View.GONE);
        btnNext = (Button) findViewById(R.id.btnNext);
        txtPage = (TextView) findViewById(R.id.txtPage);
        txtNoPage = (TextView) findViewById(R.id.txtNoPage);
        txtItem = (TextView) findViewById(R.id.txtItem);
        txtJmlItem = (TextView) findViewById(R.id.txtJmlItem);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        btnCari = (Button) findViewById(R.id.search_btn);

        etCariTanggal = (EditText) findViewById(R.id.txtCariTanggal);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        etCariTanggal.setOnClickListener(this);

        recyclerViewPesanan = (RecyclerView) findViewById(R.id.recyclerviewPesanan);

        page = 1; item = 0;

        progress.show();
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

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrevPage();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NextPage();
            }
        });

        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CariTanggal();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadPesan();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        LoadPesan();
    }

    private void LoadPesan(){
        final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIsImlzcyI6Imh0dHBzOi8vYmx1ZHJpdmUuYW5nZ2l0cHJheW9nby5jb20vYXBpL2F1dGgvc2lnbmluIiwiaWF0IjoxNTEwNjczMTQ2LCJleHAiOjE1MjEwNDExNDYsIm5iZiI6MTUxMDY3MzE0NiwianRpIjoibEYwOFlpVlZEMExwdnNKcSJ9.KHL0_nBOqpNGl4UddSLrHS2ZTGhrZKGLzx05zt2W9ko";
        BLuDriveAPI client = ServiceGeneratorAuth.createService(BLuDriveAPI.class, "drive/bos/pesan");
        Call<GetPesan> call = client.getPesan();
        call.enqueue(new Callback<GetPesan>(){
            @Override
            public void onResponse(Call<GetPesan> call, Response<GetPesan> response) {
                GetPesan getPesan = response.body();
                limit = getPesan.lastPage;
                List<GetPesan.Datum> listPesan = getPesan.getData();
                item = listPesan.size();
                txtJmlItem.setText(Integer.toString(item));
                PesananAdapter pesananAdapter = new PesananAdapter(listPesan, ListActivity.this);
                recyclerViewPesanan.setLayoutManager(new LinearLayoutManager(ListActivity.this));
                recyclerViewPesanan.setItemAnimator(new DefaultItemAnimator());
                recyclerViewPesanan.setHasFixedSize(true);
                recyclerViewPesanan.setAdapter(pesananAdapter);
                txtNoPage.setText(Integer.toString(page));
                progress.dismiss();
            }
            @Override
            public void onFailure(Call<GetPesan> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), "ADA KESALAHAN JARINGAN, APLIKASI AKAN DI RESTART", Toast.LENGTH_LONG);
                toast.show();
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3500); //THREAD BUAT TOAST JALAN DULU
                            refreshActivity();
                            //startActivity(new Intent(ListActivity.this, NewMainActivity.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        });
    }

    private void NextPage() {
        progress.show();
        recyclerViewPesanan.setVisibility(View.GONE);
        page = page + 1;

        final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIsImlzcyI6Imh0dHBzOi8vYmx1ZHJpdmUuYW5nZ2l0cHJheW9nby5jb20vYXBpL2F1dGgvc2lnbmluIiwiaWF0IjoxNTEwNjczMTQ2LCJleHAiOjE1MjEwNDExNDYsIm5iZiI6MTUxMDY3MzE0NiwianRpIjoibEYwOFlpVlZEMExwdnNKcSJ9.KHL0_nBOqpNGl4UddSLrHS2ZTGhrZKGLzx05zt2W9ko";
        BLuDriveAPI client = ServiceGeneratorAuth.createService(BLuDriveAPI.class, token);
        Call<GetPesan> call = client.getPesanMore(page);
        call.enqueue(new Callback<GetPesan>(){
            @Override
            public void onResponse(Call<GetPesan> call, Response<GetPesan> response) {
                GetPesan getPesan = response.body();
                List<GetPesan.Datum> listPesan = getPesan.getData();
                item = listPesan.size();
                txtJmlItem.setText(Integer.toString(item));
                PesananAdapter pesananAdapter = new PesananAdapter(listPesan, ListActivity.this);
                recyclerViewPesanan.setLayoutManager(new LinearLayoutManager(ListActivity.this));
                recyclerViewPesanan.setItemAnimator(new DefaultItemAnimator());
                recyclerViewPesanan.setAdapter(pesananAdapter);
                recyclerViewPesanan.setVisibility(View.VISIBLE);
                txtNoPage.setText(Integer.toString(page));
                btnPrev.setVisibility(View.VISIBLE);
                progress.dismiss();
            }
            @Override
            public void onFailure(Call<GetPesan> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), "ADA KESALAHAN JARINGAN, APLIKASI AKAN DI RESTART", Toast.LENGTH_LONG);
                toast.show();
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3500); //THREAD BUAT TOAST JALAN DULU
                            refreshActivity();
                            //startActivity(new Intent(ListActivity.this, NewMainActivity.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        });
        if (page == limit){
            btnNext.setVisibility(View.GONE);
            Toast toast = Toast.makeText(getApplicationContext(), "ANDA BERADA DI HALAMAN TERAKHIR", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void PrevPage() {
        progress.show();
        recyclerViewPesanan.setVisibility(View.GONE);
        page = page - 1;

        final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIsImlzcyI6Imh0dHBzOi8vYmx1ZHJpdmUuYW5nZ2l0cHJheW9nby5jb20vYXBpL2F1dGgvc2lnbmluIiwiaWF0IjoxNTEwNjczMTQ2LCJleHAiOjE1MjEwNDExNDYsIm5iZiI6MTUxMDY3MzE0NiwianRpIjoibEYwOFlpVlZEMExwdnNKcSJ9.KHL0_nBOqpNGl4UddSLrHS2ZTGhrZKGLzx05zt2W9ko";
        BLuDriveAPI client = ServiceGeneratorAuth.createService(BLuDriveAPI.class, token);
        Call<GetPesan> call = client.getPesanMore(page);
        call.enqueue(new Callback<GetPesan>(){
            @Override
            public void onResponse(Call<GetPesan> call, Response<GetPesan> response) {
                GetPesan getPesan = response.body();
                List<GetPesan.Datum> listPesan = getPesan.getData();
                item = listPesan.size();
                txtJmlItem.setText(Integer.toString(item));
                PesananAdapter pesananAdapter = new PesananAdapter(listPesan, ListActivity.this);
                recyclerViewPesanan.setLayoutManager(new LinearLayoutManager(ListActivity.this));
                recyclerViewPesanan.setItemAnimator(new DefaultItemAnimator());
                recyclerViewPesanan.setAdapter(pesananAdapter);
                recyclerViewPesanan.setVisibility(View.VISIBLE);
                txtNoPage.setText(Integer.toString(page));
                btnNext.setVisibility(View.VISIBLE);
                progress.dismiss();
            }
            @Override
            public void onFailure(Call<GetPesan> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), "ADA KESALAHAN JARINGAN, APLIKASI AKAN DI RESTART", Toast.LENGTH_LONG);
                toast.show();
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3500); //THREAD BUAT TOAST JALAN DULU
                            refreshActivity();
                            //startActivity(new Intent(ListActivity.this, NewMainActivity.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        });
        if (page == 1){
            btnPrev.setVisibility(View.GONE);
            Toast toast = Toast.makeText(getApplicationContext(), "ANDA BERADA DI HALAMAN PERTAMA", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void refreshActivity (){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        etCariTanggal.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        cariTanggal = String.format((year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));

                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void CariTanggal() {
        progress.show();
        recyclerViewPesanan.setVisibility(View.GONE);

        final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIsImlzcyI6Imh0dHBzOi8vYmx1ZHJpdmUuYW5nZ2l0cHJheW9nby5jb20vYXBpL2F1dGgvc2lnbmluIiwiaWF0IjoxNTEwNjczMTQ2LCJleHAiOjE1MjEwNDExNDYsIm5iZiI6MTUxMDY3MzE0NiwianRpIjoibEYwOFlpVlZEMExwdnNKcSJ9.KHL0_nBOqpNGl4UddSLrHS2ZTGhrZKGLzx05zt2W9ko";
        BLuDriveAPI client = ServiceGeneratorAuth.createService(BLuDriveAPI.class, token);
        Call<GetCariPesan> call = client.getCariPesan(cariTanggal);
        call.enqueue(new Callback<GetCariPesan>() {
            @Override
            public void onResponse(Call<GetCariPesan> call, Response<GetCariPesan> response) {
                GetCariPesan getCariPesan = response.body();
                limit = getCariPesan.lastPage;
                List<GetCariPesan.Datum> listCari = getCariPesan.getData();
                item = listCari.size();
                txtJmlItem.setText(Integer.toString(item));
                CariPesananAdapter cariPesananAdapter = new CariPesananAdapter(listCari, ListActivity.this);
                recyclerViewPesanan.setLayoutManager(new LinearLayoutManager(ListActivity.this));
                recyclerViewPesanan.setItemAnimator(new DefaultItemAnimator());
                recyclerViewPesanan.setAdapter(cariPesananAdapter);
                recyclerViewPesanan.setVisibility(View.VISIBLE);
                txtNoPage.setText(Integer.toString(page));
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<GetCariPesan> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), "ADA KESALAHAN JARINGAN, APLIKASI AKAN DI RESTART", Toast.LENGTH_LONG);
                toast.show();
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3500); //THREAD BUAT TOAST JALAN DULU
                            refreshActivity();
                            //startActivity(new Intent(ListActivity.this, NewMainActivity.class));
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
