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
import com.example.yusup.bludrive.activity.DetilPesananActivity;
import com.example.yusup.bludrive.response.GetPesan;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Kaisha on 15/11/2017.
 */

public class PesananAdapter extends RecyclerView.Adapter<PesananAdapter.ViewHolder>{

    private List<GetPesan.Datum> listPesan;
    private Context context;
    int lastPosition = -1;

    public PesananAdapter(List<GetPesan.Datum> listPesan, Context context) {
        this.listPesan = listPesan;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_pesan,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GetPesan.Datum model = listPesan.get(position);
        final String id = Integer.toString(model.id);
        final String mobil = model.mobilDrive.namaMobil;
        final String posisi = model.posisi;
        final String posisiTujuan = model.posisiTujuan;
        final String tanggalPemesanan = model.tanggalPemesanan;
        final String waktuPemesanan = model.waktuPemesanan;
        final String keteranganUser = model.keteranganUser;
        final String nama = model.user.nama;
        final String telepon = model.user.telepon;
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Toast.makeText(context, "#" + kategori + " - " + keterangan + " - " + foto + " - " + nama + " - " + ni + " - " + telepon + " - " + email + " - " + idstatus, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, DetilPesananActivity.class);
                intent.putExtra("nama_mobil", mobil);
                intent.putExtra("posisi", posisi);
                intent.putExtra("posisi_tujuan", posisiTujuan);
                intent.putExtra("tanggal_pemesanan", tanggalPemesanan);
                intent.putExtra("waktu_pemesanan", waktuPemesanan);
                intent.putExtra("keterangan_user", keteranganUser);
                intent.putExtra("nama", nama);
                intent.putExtra("telepon", telepon);
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });
        try{
            SimpleDateFormat tm = new SimpleDateFormat("HH:mm");
            SimpleDateFormat tn = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat vd = new SimpleDateFormat("dd-MM-yyyy");

            String formattedTime = tm.format(tn.parse(waktuPemesanan));
            String formmatedDate2 = vd.format(df.parse(tanggalPemesanan));

            holder.txtNama.setText(nama);
            holder.txtTelepon.setText(telepon);
            holder.txtMobil.setText(mobil);
            holder.txtTanggal.setText(formmatedDate2);
            holder.txtWaktu.setText(formattedTime);
            holder.txtKeterangan.setText(keteranganUser);
        }catch (Exception e){
            e.printStackTrace();
        }


        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return listPesan.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtMobil;
        private TextView txtNama;
        private TextView txtTelepon;
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
