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
import com.example.yusup.bludrive.adapter.CariOrderDriverAdapter;
import com.example.yusup.bludrive.adapter.OrderDriverAdapter;
import com.example.yusup.bludrive.config.PrefHandler;
import com.example.yusup.bludrive.response.GetCariOrderDriver;
import com.example.yusup.bludrive.response.GetOrderDriver;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.yusup.bludrive.R.layout.activity_list_order_driver;

public class ListOrderDriverFragment extends Fragment{

    private RecyclerView recyclerViewLaporan;
    private TextView txtPage, txtNoPage, txtItem, txtJmlItem;
    private Button btnPrev, btnNext, btnCari;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String cariTanggal;

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
        View layout = inflater.inflate(activity_list_order_driver, container, false);

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

        recyclerViewLaporan = (RecyclerView) layout.findViewById(R.id.recyclerviewOrder);

        btnCari = (Button) layout.findViewById(R.id.search_btn);

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
                LoadOrderDriver();
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

        LoadOrderDriver();
        return layout;
    }

    private void LoadOrderDriver(){
        BLuDriveAPI client = ServiceGeneratorAuth.createService(BLuDriveAPI.class, token);
        Call<GetOrderDriver> call = client.getOrderDriver();
        call.enqueue(new Callback<GetOrderDriver>(){
            @Override
            public void onResponse(Call<GetOrderDriver> call, Response<GetOrderDriver> response) {
                GetOrderDriver getOrderDriver = response.body();
                limit = getOrderDriver.lastPage;
                List<GetOrderDriver.Datum> listOrder = getOrderDriver.getData();
                item = listOrder.size();
                txtJmlItem.setText(Integer.toString(item));
                OrderDriverAdapter orderDriverAdapter = new OrderDriverAdapter(listOrder, getActivity());
                recyclerViewLaporan.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewLaporan.setItemAnimator(new DefaultItemAnimator());
                recyclerViewLaporan.setAdapter(orderDriverAdapter);
                txtNoPage.setText(Integer.toString(page));
                etCariTanggal.setText("");
                progress.dismiss();
            }
            @Override
            public void onFailure(Call<GetOrderDriver> call, Throwable t) {
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
        recyclerViewLaporan.setVisibility(View.GONE);
        page = page + 1;

        BLuDriveAPI client = ServiceGeneratorAuth.createService(BLuDriveAPI.class, token);
        Call<GetOrderDriver> call = client.getOrderDriverMore(page);
        call.enqueue(new Callback<GetOrderDriver>(){
            @Override
            public void onResponse(Call<GetOrderDriver> call, Response<GetOrderDriver> response) {
                GetOrderDriver getOrderDriver = response.body();
                List<GetOrderDriver.Datum> listOrder = getOrderDriver.getData();
                item = listOrder.size();
                txtJmlItem.setText(Integer.toString(item));
                OrderDriverAdapter orderDriverAdapter = new OrderDriverAdapter(listOrder, getActivity());
                recyclerViewLaporan.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewLaporan.setItemAnimator(new DefaultItemAnimator());
                recyclerViewLaporan.setAdapter(orderDriverAdapter);
                recyclerViewLaporan.setVisibility(View.VISIBLE);
                txtNoPage.setText(Integer.toString(page));
                btnPrev.setVisibility(View.VISIBLE);
                progress.dismiss();
            }
            @Override
            public void onFailure(Call<GetOrderDriver> call, Throwable t) {
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
        recyclerViewLaporan.setVisibility(View.GONE);
        page = page - 1;

        BLuDriveAPI client = ServiceGeneratorAuth.createService(BLuDriveAPI.class, token);
        Call<GetOrderDriver> call = client.getOrderDriverMore(page);
        call.enqueue(new Callback<GetOrderDriver>(){
            @Override
            public void onResponse(Call<GetOrderDriver> call, Response<GetOrderDriver> response) {
                GetOrderDriver getOrderDriver = response.body();
                List<GetOrderDriver.Datum> listOrder = getOrderDriver.getData();
                item = listOrder.size();
                txtJmlItem.setText(Integer.toString(item));
                OrderDriverAdapter orderDriverAdapter = new OrderDriverAdapter(listOrder, getActivity());
                recyclerViewLaporan.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewLaporan.setItemAnimator(new DefaultItemAnimator());
                recyclerViewLaporan.setAdapter(orderDriverAdapter);
                recyclerViewLaporan.setVisibility(View.VISIBLE);
                txtNoPage.setText(Integer.toString(page));
                btnNext.setVisibility(View.VISIBLE);
                progress.dismiss();
            }
            @Override
            public void onFailure(Call<GetOrderDriver> call, Throwable t) {
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
        recyclerViewLaporan.setVisibility(View.GONE);

        BLuDriveAPI client = ServiceGeneratorAuth.createService(BLuDriveAPI.class, token);
        Call<GetCariOrderDriver> call = client.getCariOrderDriver(cariTanggal);
        call.enqueue(new Callback<GetCariOrderDriver>() {
            @Override
            public void onResponse(Call<GetCariOrderDriver> call, Response<GetCariOrderDriver> response) {
                GetCariOrderDriver getCariOrderDriver = response.body();
                limit = getCariOrderDriver.lastPage;
                List<GetCariOrderDriver.Datum> listCari = getCariOrderDriver.getData();
                item = listCari.size();
                txtJmlItem.setText(Integer.toString(item));
                CariOrderDriverAdapter cariOrderDriverAdapter = new CariOrderDriverAdapter(listCari, getActivity());
                recyclerViewLaporan.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewLaporan.setItemAnimator(new DefaultItemAnimator());
                recyclerViewLaporan.setAdapter(cariOrderDriverAdapter);
                recyclerViewLaporan.setVisibility(View.VISIBLE);
                txtNoPage.setText(Integer.toString(page));
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<GetCariOrderDriver> call, Throwable t) {
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

}
