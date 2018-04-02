package com.example.yusup.bludrive.Fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.yusup.bludrive.R;
import com.example.yusup.bludrive.RestAPI.BLuDriveAPI;
import com.example.yusup.bludrive.RestAPI.ServiceGeneratorAuth;
import com.example.yusup.bludrive.config.PrefHandler;
import com.example.yusup.bludrive.request.PesanRequest;
import com.example.yusup.bludrive.response.PesanResponse;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.example.yusup.bludrive.R.layout.activity_main;
import static com.example.yusup.bludrive.R.layout.spinner_item_position;

public class PesanFragment extends Fragment {

    PrefHandler prefHandler;

    private Button btnPesan;
    private Button btnTujuan;
    private EditText etJemput;
    private EditText etTujuan;
    private EditText ettglJemput;
    private EditText etWaktu;
    private EditText etKeterangan;
    private Spinner spnMobil;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day, jam, menit;
    private String tanggal;

    boolean doubleklikexit = false;
    // konstanta untuk mendeteksi hasil balikan dari place picker
    private int PLACE_PICKER_REQUEST = 1;
    private int PLACE_PICKER_REQUEST_TUJUAN = 2;

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    Geocoder geocoder;
    List<Address> addresses;

    private int Hold;
    String[] number = new String[]{
            "Avanza", "Ayla", "Innova","Xenia", "HI-Ace", "Bus"
    };
    ProgressDialog pDialog;

    String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //super.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(activity_main, container, false);

        etJemput = (EditText) layout.findViewById(R.id.txtJemput);
        etTujuan = (EditText) layout.findViewById(R.id.txtTujuan);
        etKeterangan = (EditText) layout.findViewById(R.id.txtKeterangan);

        ettglJemput = (EditText) layout.findViewById(R.id.txtDate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        spnMobil = (Spinner) layout.findViewById(R.id.spnMobil);
        btnPesan = (Button) layout.findViewById(R.id.btPesan);

        etWaktu = (EditText) layout.findViewById(R.id.txtTime);
        jam = calendar.get(Calendar.HOUR_OF_DAY);
        menit = calendar.get(Calendar.MINUTE);

        prefHandler =   new PrefHandler(getActivity());
        token       =   prefHandler.getTOKEN_KEY();

        etJemput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // membuat Intent untuk Place Picker
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Log.d("DEBUGGING", "DEBUGGING: 5");
                try {
                    //menjalankan place picker
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                    Log.d("DEBUGGING", "DEBUGGING: 6");

                    // check apabila <a title="Solusi Tidak Bisa Download Google Play Services di Android" href="http://www.twoh.co/2014/11/solusi-tidak-bisa-download-google-play-services-di-android/" target="_blank">Google Play Services tidak terinstall</a> di HP
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                    Log.d("DEBUGGING", "DEBUGGING: 7");
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                    Log.d("DEBUGGING", "DEBUGGING: 8");
                }
            }
        });

        etTujuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Log.d("DEBUGGING", "DEBUGGING: 9");
                try {
                    //menjalankan place picker
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST_TUJUAN);
                    Log.d("DEBUGGING", "DEBUGGING: 10");

                    // check apabila <a title="Solusi Tidak Bisa Download Google Play Services di Android" href="http://www.twoh.co/2014/11/solusi-tidak-bisa-download-google-play-services-di-android/" target="_blank">Google Play Services tidak terinstall</a> di HP
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                    Log.d("DEBUGGING", "DEBUGGING: 11");
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                    Log.d("DEBUGGING", "DEBUGGING: 12");
                }
            }
        });

        etWaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                jam = calendar.get(Calendar.HOUR_OF_DAY);
                menit = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if(hourOfDay == 0) {
                                    hourOfDay = 12;
                                }
                                String mEndTime = (String.format("%02d:%02d", hourOfDay, minute));
                                etWaktu.setText(mEndTime);
                            }
                        }, jam, menit, true);
                timePickerDialog.show();
            }
        });

        ettglJemput.setOnClickListener(new View.OnClickListener() {
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

                                ettglJemput.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                tanggal = String.format((year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));

                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPemesanan();
            }
        });


        pDialog = new ProgressDialog(getActivity());

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), spinner_item_position, number);

        spinnerArrayAdapter.setDropDownViewResource(spinner_item_position);

        spnMobil.setAdapter(spinnerArrayAdapter);

        spnMobil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                Hold = spnMobil.getSelectedItemPosition() + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
        return layout;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // menangkap hasil balikan dari Place Picker, dan menampilkannya pada TextView
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                String toastmsg = String.format (
                        "%s \n" +
                                "%s", place.getName(), place.getAddress());
                etJemput.setText(toastmsg);

            }
        }
        if (requestCode == PLACE_PICKER_REQUEST_TUJUAN) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                String toastmsg = String.format (
                        "%s \n" +
                                "%s", place.getName(), place.getAddress());
                etTujuan.setText(toastmsg);

            }
        }
    }

    private void doPemesanan(){
        if ("".equalsIgnoreCase(etJemput.getText().toString()) || "".equalsIgnoreCase(etTujuan.getText().toString()) || "".equalsIgnoreCase(ettglJemput.getText().toString()) ||
                "".equalsIgnoreCase(etWaktu.getText().toString()) || "".equalsIgnoreCase(etKeterangan.getText().toString())){

            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "ADA FORM YANG BELUM TERISI", Toast.LENGTH_LONG);
            toast.show();
        } else if (etKeterangan.getText().toString().length() < 8 ){
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "ISI CATATAN MINIMAL 6 KARAKTER", Toast.LENGTH_LONG);
            toast.show();
        } else{
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        BLuDriveAPI client = ServiceGeneratorAuth.createService(BLuDriveAPI.class,token);

        PesanRequest pesanRequest = new
                PesanRequest(
                spnMobil.getSelectedItemPosition()+1,
                etJemput.getText().toString(),
                etTujuan.getText().toString(),
                tanggal,
                etWaktu.getText().toString(),
                etKeterangan.getText().toString());

        Call<PesanResponse> call = client.doPemesanan(pesanRequest);

        call.enqueue(new Callback<PesanResponse>() {
            @Override
            public void onResponse(Call<PesanResponse> call, Response<PesanResponse> response) {
                try{
                    if(response.isSuccessful()){
                        pDialog.dismiss();
                        Log.d("VALIDITY Pesan 1", "BERHASIL");
                        spnMobil.setSelection(0);
                        etTujuan.setText("");
                        ettglJemput.setText("");
                        etWaktu.setText("");
                        etKeterangan.setText("");
                        getLocation();
                        if(response.body().getStatus().toString().equalsIgnoreCase("false")){
                            Toast.makeText(getActivity().getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity().getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        pDialog.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Log.d("DEBUG EXCEPTION :", e.toString());
                }
            }

            @Override
            public void onFailure(Call<PesanResponse> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }}

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);

                    String address = addresses.get(0).getAddressLine(0);
                    String area = addresses.get(0).getLocality();
                    String city = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalcode = addresses.get(0).getPostalCode();

                    String fullAddress = address + ", " + area + ", " + city + ", " + country + ", " + postalcode;

                    etJemput.setText(fullAddress);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}
