package com.example.yusup.bludrive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.yusup.bludrive.Fragment.ListOrderDriverFragment;
import com.example.yusup.bludrive.R;
import com.example.yusup.bludrive.RestAPI.BLuDriveAPI;
import com.example.yusup.bludrive.RestAPI.ServiceGeneratorAuth;
import com.example.yusup.bludrive.config.PrefHandler;
import com.example.yusup.bludrive.response.ProfileResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainDrawerDriver extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean doubleBackToExitPressedOnce = false;
    PrefHandler prefHandler;

    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_driver_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("BLuDrive");


        prefHandler = new PrefHandler(this);
        token = prefHandler.getTOKEN_KEY();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);


        Log.d("JAMBAN", "INI DRIVER");


        //TODO::Ini buat default yang mau ditampilin duluan pas pertama muncul
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, new ListOrderDriverFragment()).commit();

        getProfile();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
//            SharedPreferences preferences3 = getSharedPreferences(token_key, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor3 = preferences3.edit();
//            editor3.remove(fromDate_key);
//            editor3.remove(toDate_key);
//            editor3.commit();
            prefHandler.clearFROM_DATE_KEY();
            prefHandler.clearTO_DATE_KEY();
            moveTaskToBack(true);
            finish();

            //android.os.Process.killProcess(android.os.Process.myPid());
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tap lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_pesanan) {
            // Handle the camera action
            fragment = new ListOrderDriverFragment();
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainDrawerDriver.this,EditProfil.class);
            startActivity(intent);
        } else if (id == R.id.nav_password) {
            Intent intent = new Intent(MainDrawerDriver.this, ChangePassword.class);
            startActivity(intent);
        } else if (id == R.id.nav_contactus) {
            Intent intent = new Intent(MainDrawerDriver.this, ContactActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainDrawerDriver.this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            prefHandler.setLogout();

            Intent i=new Intent(MainDrawerDriver.this,splash_screen.class);
            i.setAction(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            try {startActivity(i);}
            catch (Exception ignore) {}
            System.exit(0);

            Toast.makeText(getApplicationContext(),"Anda Berhasil Logout",Toast.LENGTH_LONG).show();

        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getProfile(){
        BLuDriveAPI client = ServiceGeneratorAuth.createService(BLuDriveAPI.class,token);
        Call<ProfileResponse> call = client.getProfile();
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                try{
                    Log.d("RESPONSE : ", new Gson().toJson(response.body()));
                }catch (Exception e){
                    Log.d("CATCH : ",e.toString());
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
            }
        });
    }
}
