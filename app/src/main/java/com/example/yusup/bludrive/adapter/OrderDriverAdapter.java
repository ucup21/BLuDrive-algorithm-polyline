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

import com.example.yusup.bludrive.R;
import com.example.yusup.bludrive.RestAPI.ItemClickListener;
import com.example.yusup.bludrive.activity.MapsActivity_New;
import com.example.yusup.bludrive.response.GetOrderDriver;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Kaisha on 15/11/2017.
 */

public class OrderDriverAdapter extends RecyclerView.Adapter<OrderDriverAdapter.ViewHolder>{

    private List<GetOrderDriver.Datum> listOrder;
    private Context context;
    int lastPosition = -1;

    public OrderDriverAdapter(List<GetOrderDriver.Datum> listOrder, Context context) {
        this.listOrder = listOrder;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_order_driver,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GetOrderDriver.Datum model = listOrder.get(position);
        final String id = Integer.toString(model.id);
        final String mobil = model.mobilDrive.namaMobil;
        final String posisi = model.posisi;
        final String posisiTujuan = model.posisiTujuan;
        final String tanggalPemesanan = model.tanggalPemesanan;
        final String waktuPemesanan = model.waktuPemesanan;
        final String namaDriver = model.driver.namaDriver;
        final String nama = model.user.nama;
        final String telepon = model.user.telepon;
        final String keterangan = model.keteranganUser;
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Toast.makeText(context, "#" + kategori + " - " + keterangan + " - " + foto + " - " + nama + " - " + ni + " - " + telepon + " - " + email + " - " + idstatus, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MapsActivity_New.class);
                intent.putExtra("posisi", posisi);
                intent.putExtra("posisi_tujuan", posisiTujuan);
                intent.putExtra("nama_driver", namaDriver);
                intent.putExtra("nama", nama);
                intent.putExtra("telepon", telepon);
                intent.putExtra("id", id);
                intent.putExtra("waktu_pemesanan", waktuPemesanan);
                intent.putExtra("tanggal_pemesanan", tanggalPemesanan);
                intent.putExtra("keterangan_user", keterangan);
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
            holder.txtTelepon.setText(telepon);
            /*holder.txtPosisi.setText(posisi);
            holder.txtPosisiTujuan.setText(posisiTujuan);*/
            holder.txtMobil.setText(mobil);
            holder.txtTanggal.setText(formmatedDate2);
            holder.txtWaktu.setText(formattedTime);
            holder.txtKeterangan.setText(keterangan);
        }catch (Exception e){
            e.printStackTrace();
        }
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txtNama;
        private TextView txtTelepon;
        private TextView txtMobil;
        /*private TextView txtPosisi;
        private TextView txtPosisiTujuan;*/
        private TextView txtTanggal;
        private TextView txtWaktu;
        private TextView txtKeterangan;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);

            txtNama = (TextView) itemView.findViewById(R.id.txtNama);
            txtTelepon = (TextView) itemView.findViewById(R.id.txtTelepon);
            /*txtPosisi = (TextView) itemView.findViewById(R.id.txtjemput);
            txtPosisiTujuan = (TextView) itemView.findViewById(R.id.txtTujuan);*/
            txtMobil = (TextView) itemView.findViewById(R.id.txtMobil);
            txtTanggal = (TextView) itemView.findViewById(R.id.txtTanggalJemput);
            txtWaktu = (TextView) itemView.findViewById(R.id.txtWaktu);
            txtKeterangan = (TextView) itemView.findViewById(R.id.txtKeterangan);
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
