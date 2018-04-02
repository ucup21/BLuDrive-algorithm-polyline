package com.example.yusup.bludrive.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yusup.bludrive.R;
import com.example.yusup.bludrive.RestAPI.BLuDriveAPI;
import com.example.yusup.bludrive.RestAPI.ServiceGeneratorAuth;
import com.example.yusup.bludrive.adapter.RiwayatAdapter;
import com.example.yusup.bludrive.config.PrefHandler;
import com.example.yusup.bludrive.response.GetRiwayat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.yusup.bludrive.R.layout.activity_list_riwayat;

public class ListRiwayatFragment extends Fragment {

    private RecyclerView recyclerViewPesanan;
    private TextView txtPage, txtNoPage, txtItem, txtJmlItem;
    private Button btnPrev, btnNext;


    private SwipeRefreshLayout swipeRefreshLayout;

    ProgressDialog progress;

    Integer page, limit, item;

    String token;

    PrefHandler prefHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //super.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(activity_list_riwayat, container, false);

        progress = new ProgressDialog(getActivity());
        progress.setMessage("LOADING . . . .");
        progress.setCancelable(false);

        btnPrev = (Button) layout.findViewById(R.id.btnPrev);
        btnPrev.setVisibility(View.GONE);
        btnNext = (Button) layout.findViewById(R.id.btnNext);
        txtPage = (TextView) layout.findViewById(R.id.txtPage);
        txtNoPage = (TextView) layout.findViewById(R.id.txtNoPage);
        txtItem = (TextView) layout.findViewById(R.id.txtItem);
        txtJmlItem = (TextView) layout.findViewById(R.id.txtJmlItem);

        swipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipe_refresh_layout);

        recyclerViewPesanan = (RecyclerView) layout.findViewById(R.id.recyclerviewPesanan);

        page = 1; item = 0;

        prefHandler =   new PrefHandler(getActivity());
        token       =   prefHandler.getTOKEN_KEY();

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


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadRiwayat();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        LoadRiwayat();
        return layout;
    }

    private void LoadRiwayat(){
        BLuDriveAPI client = ServiceGeneratorAuth.createService(BLuDriveAPI.class, token);
        Call<GetRiwayat> call = client.getRiwayat();
        call.enqueue(new Callback<GetRiwayat>(){
            @Override
            public void onResponse(Call<GetRiwayat> call, Response<GetRiwayat> response) {
                GetRiwayat getRiwayat = response.body();
                limit = getRiwayat.lastPage;
                List<GetRiwayat.Datum> listRiwayat = getRiwayat.getData();

                item = listRiwayat.size();
                txtJmlItem.setText(Integer.toString(item));
                RiwayatAdapter riwayatAdapter = new RiwayatAdapter(listRiwayat, getActivity());
                recyclerViewPesanan.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewPesanan.setItemAnimator(new DefaultItemAnimator());
                recyclerViewPesanan.setHasFixedSize(true);
                recyclerViewPesanan.setAdapter(riwayatAdapter);
                txtNoPage.setText(Integer.toString(page));
                progress.dismiss();
            }
            @Override
            public void onFailure(Call<GetRiwayat> call, Throwable t) {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "ADA KESALAHAN JARINGAN, APLIKASI AKAN DI RESTART", Toast.LENGTH_LONG);
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

        BLuDriveAPI client = ServiceGeneratorAuth.createService(BLuDriveAPI.class, token);
        Call<GetRiwayat> call = client.getRiwayatMore(page);
        call.enqueue(new Callback<GetRiwayat>(){
            @Override
            public void onResponse(Call<GetRiwayat> call, Response<GetRiwayat> response) {
                GetRiwayat getRiwayat = response.body();
                List<GetRiwayat.Datum> listRiwayat = getRiwayat.getData();
                item = listRiwayat.size();
                txtJmlItem.setText(Integer.toString(item));
                RiwayatAdapter riwayatAdapter = new RiwayatAdapter(listRiwayat, getActivity());
                recyclerViewPesanan.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewPesanan.setItemAnimator(new DefaultItemAnimator());
                recyclerViewPesanan.setAdapter(riwayatAdapter);
                recyclerViewPesanan.setVisibility(View.VISIBLE);
                txtNoPage.setText(Integer.toString(page));
                btnPrev.setVisibility(View.VISIBLE);
                progress.dismiss();
            }
            @Override
            public void onFailure(Call<GetRiwayat> call, Throwable t) {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "ADA KESALAHAN JARINGAN, APLIKASI AKAN DI RESTART", Toast.LENGTH_LONG);
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
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "ANDA BERADA DI HALAMAN TERAKHIR", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void PrevPage() {
        progress.show();
        recyclerViewPesanan.setVisibility(View.GONE);
        page = page - 1;

        BLuDriveAPI client = ServiceGeneratorAuth.createService(BLuDriveAPI.class, token);
        Call<GetRiwayat> call = client.getRiwayatMore(page);
        call.enqueue(new Callback<GetRiwayat>(){
            @Override
            public void onResponse(Call<GetRiwayat> call, Response<GetRiwayat> response) {
                GetRiwayat getRiwayat = response.body();
                List<GetRiwayat.Datum> listRiwayat = getRiwayat.getData();
                item = listRiwayat.size();
                txtJmlItem.setText(Integer.toString(item));
                RiwayatAdapter riwayatAdapter = new RiwayatAdapter(listRiwayat, getActivity());
                recyclerViewPesanan.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewPesanan.setItemAnimator(new DefaultItemAnimator());
                recyclerViewPesanan.setAdapter(riwayatAdapter);
                recyclerViewPesanan.setVisibility(View.VISIBLE);
                txtNoPage.setText(Integer.toString(page));
                btnNext.setVisibility(View.VISIBLE);
                progress.dismiss();
            }
            @Override
            public void onFailure(Call<GetRiwayat> call, Throwable t) {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "ADA KESALAHAN JARINGAN, APLIKASI AKAN DI RESTART", Toast.LENGTH_LONG);
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
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "ANDA BERADA DI HALAMAN PERTAMA", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void refreshActivity (){
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }
}
