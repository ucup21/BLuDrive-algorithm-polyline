package com.example.yusup.bludrive.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.yusup.bludrive.activity.DetilRiwayatActivity;
import com.example.yusup.bludrive.R;
import com.example.yusup.bludrive.RestAPI.ItemClickListener;
import com.example.yusup.bludrive.response.GetRiwayat;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Kaisha on 15/11/2017.
 */

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder>{
    private List<GetRiwayat.Datum> listRiwayat;
    private Context context;

    ViewHolder viewHolder;
    int lastPosition = -1;

    public RiwayatAdapter(List<GetRiwayat.Datum> listRiwayat, Context context) {
        this.listRiwayat = listRiwayat;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_riwayat,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GetRiwayat.Datum model = listRiwayat.get(position);
        final String id = Integer.toString(model.id);
        final String mobil = model.mobilDrive.namaMobil;
        final String posisi = model.posisi;
        final String posisiTujuan = model.posisiTujuan;
        final String tanggalPemesanan = model.tanggalPemesanan;
        final String waktuPemesanan = model.waktuPemesanan;
        final String namaDriver = model.driver.namaDriver;
        final String nama = model.driver.user.nama;
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Toast.makeText(context, "#" + kategori + " - " + keterangan + " - " + foto + " - " + nama + " - " + ni + " - " + telepon + " - " + email + " - " + idstatus, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, DetilRiwayatActivity.class);
                intent.putExtra("nama_mobil", mobil);
                intent.putExtra("posisi", posisi);
                intent.putExtra("posisi_tujuan", posisiTujuan);
                intent.putExtra("tanggal_pemesanan", tanggalPemesanan);
                intent.putExtra("waktu_pemesanan", waktuPemesanan);
                intent.putExtra("nama_driver", namaDriver);
                intent.putExtra("nama", nama);
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });
        try{
            SimpleDateFormat tm = new SimpleDateFormat("HH:mm");
            SimpleDateFormat tn = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat vd = new SimpleDateFormat("dd-MM-yyyy");

            String formmatedDate2 = vd.format(df.parse(tanggalPemesanan));
            String formattedTime = tm.format(tn.parse(waktuPemesanan));

            holder.txtNama.setText(nama);
            holder.txtMobil.setText(mobil);
            holder.txtTanggal.setText(formmatedDate2);
            holder.txtWaktu.setText(formattedTime);
        }catch (Exception e){
            e.printStackTrace();
        }

        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return listRiwayat.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtMobil;
        private TextView txtNama;
        private TextView txtTanggal;
        private TextView txtWaktu;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
            txtNama = (TextView) itemView.findViewById(R.id.txtNama);
            txtMobil = (TextView) itemView.findViewById(R.id.txtMobil);
            txtTanggal = (TextView) itemView.findViewById(R.id.txtTanggalJemput);
            txtWaktu = (TextView) itemView.findViewById(R.id.txtWaktu);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition());
        }
    }

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
