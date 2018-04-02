package com.example.yusup.bludrive.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.yusup.bludrive.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class DetilRiwayatActivity extends AppCompatActivity {

    private TextView txtNama, txtPosisi, txtPosisiTujuan, txtMobil, txtTanggal, txtWaktu;
    private ProgressDialog progress;
    private String id, id_driver;

    private int Hold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detil_riwayat);

        progress = new ProgressDialog(this);
        progress.setMessage("LOADING . . . .");
        progress.setCancelable(false);

        txtNama = (TextView) findViewById(R.id.txtNama);
        txtPosisi = (TextView) findViewById(R.id.txtjemput);
        txtPosisiTujuan = (TextView) findViewById(R.id.txtTujuan);
        txtMobil = (TextView) findViewById(R.id.txtMobil);
        txtTanggal = (TextView) findViewById(R.id.txtTanggalJemput);
        txtWaktu = (TextView) findViewById(R.id.txtWaktu);


        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        id = b.getString("id");
        final String posisi = b.getString("posisi"); txtPosisi.setText(posisi);
        final String posisi_tujuan = b.getString("posisi_tujuan"); txtPosisiTujuan.setText(posisi_tujuan);
        final String nama = b.getString("nama"); txtNama.setText(nama);
        final String mobil = b.getString("nama_mobil"); txtMobil.setText(mobil);
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

    }
}
