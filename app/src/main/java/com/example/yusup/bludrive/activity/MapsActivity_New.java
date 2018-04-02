package com.example.yusup.bludrive.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yusup.bludrive.R;
import com.example.yusup.bludrive.RestAPI.BLuDriveAPI;
import com.example.yusup.bludrive.RestAPI.ServiceGeneratorAuth;
import com.example.yusup.bludrive.config.PrefHandler;
import com.example.yusup.bludrive.modules.DirectionFinder;
import com.example.yusup.bludrive.modules.DirectionFinderListener;
import com.example.yusup.bludrive.modules.Route;
import com.example.yusup.bludrive.request.UpdateStatusRequest;
import com.example.yusup.bludrive.response.UpdateStatusResponse;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapsActivity_New extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mMap;
    private Button btnFindPath;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private TextView txtNama;
    private TextView txtTelepon;
    private TextView txtTanggal;
    private TextView txtWaktu;
    private TextView txtKeterangan;
    private Button btnKonfirmasi;
    private ProgressDialog progress;
    private String id, status = "1";

    private EditText txtJemput;
    private EditText txtTujuan;

    String token;

    PrefHandler prefHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        progress = new ProgressDialog(this);
        progress.setMessage("LOADING . . . .");
        progress.setCancelable(false);

        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        txtJemput = (EditText) findViewById(R.id.txtJemput);
        txtTujuan = (EditText) findViewById(R.id.txtTujuan);
        txtNama = (TextView) findViewById(R.id.txtNama);
        txtTelepon = (TextView) findViewById(R.id.txtTelepon);
        txtTanggal = (TextView) findViewById(R.id.txtTanggalJemput);
        txtWaktu = (TextView) findViewById(R.id.txtWaktu);
        txtKeterangan = (TextView) findViewById(R.id.txtKeterangan);

        txtTujuan.setClickable(false);
        txtJemput.setClickable(false);
        txtNama.setClickable(false);
        txtTanggal.setClickable(false);
        txtWaktu.setClickable(false);
        txtKeterangan.setClickable(false);
        txtTelepon.setClickable(false);

        txtJemput.setFocusable(false);
        txtTujuan.setFocusable(false);
        txtWaktu.setFocusable(false);
        txtTanggal.setFocusable(false);
        txtTelepon.setFocusable(false);
        txtNama.setFocusable(false);
        txtKeterangan.setFocusable(false);

        btnKonfirmasi = (Button) findViewById(R.id.btnTerima);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        id = b.getString("id");
        final String posisi = b.getString("posisi");
        txtJemput.setText(posisi);
        final String posisi_tujuan = b.getString("posisi_tujuan");
        txtTujuan.setText(posisi_tujuan);
        final String nama = b.getString("nama");
        txtNama.setText(nama);
        final String telepon = b.getString("telepon");
        txtTelepon.setText(telepon);
        final String tanggal_pemesanan = b.getString("tanggal_pemesanan");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat vd = new SimpleDateFormat("dd-MM-yyyy");
        try {
            String formmatedDate2 = vd.format(df.parse(tanggal_pemesanan));
            txtTanggal.setText(formmatedDate2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final String waktu_pemesanan = b.getString("waktu_pemesanan");
        SimpleDateFormat tm = new SimpleDateFormat("HH:mm");
        SimpleDateFormat tn = new SimpleDateFormat("HH:mm:ss");
        try {
            String formattedTime = tm.format(tn.parse(waktu_pemesanan));
            txtWaktu.setText(formattedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        prefHandler =   new PrefHandler(this);
        token       =   prefHandler.getTOKEN_KEY();

        final String keterangan_user = b.getString("keterangan_user");
        txtKeterangan.setText(keterangan_user);

        Linkify.addLinks(txtTelepon, Linkify.PHONE_NUMBERS);
        Log.d("id : ", id);
        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateStatus();
            }
        });

    }

    private void sendRequest() {
        String origin = txtJemput.getText().toString();
        String destination = txtTujuan.getText().toString();
        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng hcmus = new LatLng(-6.235500, 106.747327);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }


    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    public void UpdateStatus() {
        Integer order = Integer.parseInt(id);
        progress.show();
        BLuDriveAPI client = ServiceGeneratorAuth.createService(BLuDriveAPI.class, token);
        UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest(status);
        Call<UpdateStatusResponse> call = client.doupdatestatus(order, updateStatusRequest);
        call.enqueue(new Callback<UpdateStatusResponse>() {
            @Override
            public void onResponse(Call<UpdateStatusResponse> call, Response<UpdateStatusResponse> response) {
                String message = response.body().getMessage();
                String status1 = response.body().getStatus();
                Log.d("STATUS", status1);
                Log.d("MESSAGE", message);
                progress.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(), "Selamat, " + message, Toast.LENGTH_LONG);
                toast.show();
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500); //THREAD BUAT TOAST JALAN DULU
                            startActivity(new Intent(MapsActivity_New.this, MainDrawerDriver.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }

            @Override
            public void onFailure(Call<UpdateStatusResponse> call, Throwable t) {

                Toast toast1 = Toast.makeText(getApplicationContext(), "TERJADI KESALAHAN PADA JARINGAN, MOHON DIULANG", Toast.LENGTH_LONG);
                toast1.show();
                Thread thread = new Thread() {
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
