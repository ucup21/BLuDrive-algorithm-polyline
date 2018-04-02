package com.example.yusup.bludrive.Fragment;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yusup.bludrive.R;
import com.example.yusup.bludrive.RestAPI.BLuDriveAPI;
import com.example.yusup.bludrive.RestAPI.ServiceGeneratorAuth;
import com.example.yusup.bludrive.adapter.CariPesananAdapter;
import com.example.yusup.bludrive.adapter.PesananAdapter;
import com.example.yusup.bludrive.config.PrefHandler;
import com.example.yusup.bludrive.response.GetCariPesan;
import com.example.yusup.bludrive.response.GetPesan;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.yusup.bludrive.R.layout.activity_list_pesanan;

public class ListPesananFragment extends Fragment {

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
        View layout = inflater.inflate(activity_list_pesanan, container, false);

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

        btnCari = (Button) layout.findViewById(R.id.search_btn);

        recyclerViewPesanan = (RecyclerView) layout.findViewById(R.id.recyclerviewPesanan);

        etCariTanggal = (EditText) layout.findViewById(R.id.txtCariTanggal);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

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

        etCariTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                etCariTanggal.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                cariTanggal = String.format((year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));

                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        LoadPesan();
        return layout;
    }

    private void LoadPesan(){
        BLuDriveAPI client = ServiceGeneratorAuth.createService(BLuDriveAPI.class, token);
        Call<GetPesan> call = client.getPesan();
        call.enqueue(new Callback<GetPesan>(){
            @Override
            public void onResponse(Call<GetPesan> call, Response<GetPesan> response) {
                GetPesan getPesan = response.body();
                limit = getPesan.lastPage;
                List<GetPesan.Datum> listPesan = getPesan.getData();

                item = listPesan.size();
                txtJmlItem.setText(Integer.toString(item));
                PesananAdapter pesananAdapter = new PesananAdapter(listPesan, getActivity());
                recyclerViewPesanan.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewPesanan.setItemAnimator(new DefaultItemAnimator());
                recyclerViewPesanan.setHasFixedSize(true);
                recyclerViewPesanan.setAdapter(pesananAdapter);
                txtNoPage.setText(Integer.toString(page));
                etCariTanggal.setText("");
                progress.dismiss();
            }
            @Override
            public void onFailure(Call<GetPesan> call, Throwable t) {
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
        Call<GetPesan> call = client.getPesanMore(page);
        call.enqueue(new Callback<GetPesan>(){
            @Override
            public void onResponse(Call<GetPesan> call, Response<GetPesan> response) {
                GetPesan getPesan = response.body();
                List<GetPesan.Datum> listPesan = getPesan.getData();
                item = listPesan.size();
                txtJmlItem.setText(Integer.toString(item));
                PesananAdapter pesananAdapter = new PesananAdapter(listPesan, getActivity());
                recyclerViewPesanan.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewPesanan.setItemAnimator(new DefaultItemAnimator());
                recyclerViewPesanan.setAdapter(pesananAdapter);
                recyclerViewPesanan.setVisibility(View.VISIBLE);
                txtNoPage.setText(Integer.toString(page));
                btnPrev.setVisibility(View.VISIBLE);
                progress.dismiss();
            }
            @Override
            public void onFailure(Call<GetPesan> call, Throwable t) {
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
        Call<GetPesan> call = client.getPesanMore(page);
        call.enqueue(new Callback<GetPesan>(){
            @Override
            public void onResponse(Call<GetPesan> call, Response<GetPesan> response) {
                GetPesan getPesan = response.body();
                List<GetPesan.Datum> listPesan = getPesan.getData();
                item = listPesan.size();
                txtJmlItem.setText(Integer.toString(item));
                PesananAdapter pesananAdapter = new PesananAdapter(listPesan, getActivity());
                recyclerViewPesanan.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewPesanan.setItemAnimator(new DefaultItemAnimator());
                recyclerViewPesanan.setAdapter(pesananAdapter);
                recyclerViewPesanan.setVisibility(View.VISIBLE);
                txtNoPage.setText(Integer.toString(page));
                btnNext.setVisibility(View.VISIBLE);
                progress.dismiss();
            }
            @Override
            public void onFailure(Call<GetPesan> call, Throwable t) {
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


    private void CariTanggal() {
        progress.show();
        recyclerViewPesanan.setVisibility(View.GONE);

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
                CariPesananAdapter cariPesananAdapter = new CariPesananAdapter(listCari, getActivity());
                recyclerViewPesanan.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewPesanan.setItemAnimator(new DefaultItemAnimator());
                recyclerViewPesanan.setAdapter(cariPesananAdapter);
                recyclerViewPesanan.setVisibility(View.VISIBLE);
                txtNoPage.setText(Integer.toString(page));
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<GetCariPesan> call, Throwable t) {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "ADA KESALAHAN JARINGAN, APLIKASI AKAN DI RESTART", Toast.LENGTH_LONG);
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

//    private void checkItem(){
//        if (adapter.getItemCount() == 0){
//            Log.d("AHAY 1", String.valueOf(adapter.getItemCount()));
//           Toast toast = Toast.makeText(getActivity().getApplicationContext(), "ADA KESALAHAN JARINGAN, APLIKASI AKAN DI RESTART", Toast.LENGTH_LONG);
//                toast.show();
//        }else {
//            Log.d("AHAY 3", String.valueOf(adapter.getItemCount()));
//            cvEmpty.setVisibility(View.GONE);
//        }
//    }
}
