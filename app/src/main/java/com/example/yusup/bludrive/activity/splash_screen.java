package com.example.yusup.bludrive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.yusup.bludrive.R;
import com.example.yusup.bludrive.config.PrefHandler;

public class splash_screen extends AppCompatActivity {
    //Set waktu lama splashscreen
    private static int splashInterval = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_screen);

        //Instansiasi Class PrefHandler
        final PrefHandler prefHandler = new PrefHandler(this);


        //Memberi Delay pada Activity selama @splashInterval
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // TODO Auto-generated method stub


                String otorisasi = prefHandler.getID_OTORISASI();
                Log.d("OTORISASI",otorisasi);
                //Cek jika token tersimpan atau tidak di sharedpreference
                if (prefHandler.isToken_Key_Exist()&&otorisasi.equals("3")){
                        Log.d("OTORISASI 1",otorisasi);
                        Intent intent = new Intent(splash_screen.this, MainDrawer.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                } else if (prefHandler.isToken_Key_Exist()&&otorisasi.equals("6")) {
                        Log.d("OTORISASI 2",otorisasi);
                        Intent intent = new Intent(splash_screen.this, MainDrawerBos.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                } else if (prefHandler.isToken_Key_Exist()&&otorisasi.equals("7")) {
                        Log.d("OTORISASI 3",otorisasi);
                        Intent intent = new Intent(splash_screen.this, MainDrawerDriver.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                }else{
                    Intent intent = new Intent(splash_screen.this, LoginActivity.class);
                    startActivity(intent);
                }

                //jeda selesai Splashscreen
                this.finish();
            }

            private void finish() {
                // TODO Auto-generated method stub

            }
        }, splashInterval);

    };

}
