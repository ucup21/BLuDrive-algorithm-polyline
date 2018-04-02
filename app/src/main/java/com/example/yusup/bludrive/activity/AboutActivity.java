package com.example.yusup.bludrive.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.yusup.bludrive.R;


public class AboutActivity extends AppCompatActivity {

    TextView androidVersion, Blu;
    String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().hide();

        Blu = (TextView) findViewById(R.id.txtBLu);
        androidVersion = (TextView)findViewById(R.id.versionAndroid);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        androidVersion.setText("Versi "+ version);

        getSupportActionBar().setTitle("BLuDrive");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
