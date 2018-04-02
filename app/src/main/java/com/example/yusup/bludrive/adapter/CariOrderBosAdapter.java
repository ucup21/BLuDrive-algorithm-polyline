package com.example.yusup.bludrive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.yusup.bludrive.R;
import com.example.yusup.bludrive.response.GetCariOrderBos;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Kaisha on 15/11/2017.
 */

public class CariOrderBosAdapter extends RecyclerView.Adapter<CariOrderBosAdapter.ViewHolder>{

    private List<GetCariOrderBos.Datum> listCari;
    private Context context;
    int lastPosition = -1;
    public int value = 0;

    public CariOrderBosAdapter(List<GetCariOrderBos.Datum> listCari, Context context) {
        this.listCari = listCari;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_order,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GetCariOrderBos.Datum model = listCari.get(position);
        final String mobil = model.mobilDrive.namaMobil;
        final String posisi = model.posisi;
        final String posisiTujuan = model.posisiTujuan;
        final String tanggalPemesanan = model.tanggalPemesanan;
        final String waktuPemesanan = model.waktuPemesanan;
        final String namaDriver = model.driver.namaDriver;
        final String nama = model.user.nama;
        final String telepon = model.user.telepon;
        /*holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Toast.makeText(context, "#" + kategori + " - " + keterangan + " - " + foto + " - " + nama + " - " + ni + " - " + telepon + " - " + email + " - " + idstatus, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, DetilPesananActivity.class);
                intent.putExtra("nama_mobil", mobil);
                intent.putExtra("posisi", posisi);
                intent.putExtra("posisi_tujuan", posisiTujuan);
                intent.putExtra("tanggal_pemesanan", tanggalPemesanan);
                intent.putExtra("waktu_pemesanan", waktuPemesanan);
                intent.putExtra("nama_driver", namaDriver);
                intent.putExtra("nama", nama);
                intent.putExtra("telepon", telepon);
                context.startActivity(intent);
            }
        });*/
        try{
            SimpleDateFormat tm = new SimpleDateFormat("HH:mm");
            SimpleDateFormat tn = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat vd = new SimpleDateFormat("dd-MM-yyyy");

            String formmatedDate2 = vd.format(df.parse(tanggalPemesanan));
            String formattedTime = tm.format(tn.parse(waktuPemesanan));

            holder.txtNama.setText(nama);
            holder.txtTelepon.setText(telepon);
            holder.txtPosisi.setText(posisi);
            holder.txtPosisiTujuan.setText(posisiTujuan);
            holder.txtMobil.setText(mobil);
            holder.txtTanggal.setText(formmatedDate2);
            holder.txtWaktu.setText(formattedTime);
            holder.txtNamaDriver.setText("Berhasil Diantarkan Oleh "+namaDriver);
        }catch (Exception e){
            e.printStackTrace();
        }
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return listCari.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNama;
        private TextView txtTelepon;
        private TextView txtMobil;
        private TextView txtPosisi;
        private TextView txtPosisiTujuan;
        private TextView txtTanggal;
        private TextView txtWaktu;
        private TextView txtNamaDriver;

        public ViewHolder(View itemView) {
            super(itemView);

            txtNama = (TextView) itemView.findViewById(R.id.txtNama);
            txtTelepon = (TextView) itemView.findViewById(R.id.txtTelepon);
            txtPosisi = (TextView) itemView.findViewById(R.id.txtjemput);
            txtPosisiTujuan = (TextView) itemView.findViewById(R.id.txtTujuan);
            txtMobil = (TextView) itemView.findViewById(R.id.txtMobil);
            txtTanggal = (TextView) itemView.findViewById(R.id.txtTanggalJemput);
            txtWaktu = (TextView) itemView.findViewById(R.id.txtWaktu);
            txtNamaDriver = (TextView) itemView.findViewById(R.id.txtKeteranganDriver);
        }
    }

    public int getValue(){ return value; }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}