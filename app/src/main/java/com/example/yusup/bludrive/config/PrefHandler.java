package com.example.yusup.bludrive.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Asus on 19/09/2017.
 */

public class PrefHandler {
    public SharedPreferences.Editor editor;
    public SharedPreferences sharedPreferences;
    public Context context;

    int PRIVATE_MODE = 0;

    private final String SHAREDPREFNAME = "token";
    private final String IS_LOGIN = "isLoggin";

    private final String ID_OTORISASI = "IDOTORISASI";

    public final String TOKEN_KEY = "token";
    public final String NAME_KEY = "nama";
    public final String EMAIL_KEY = "email";
    public final String FACULTY_KEY = "faculty";
    public final String IMAGE_PROFILE_KEY = "imageprofile";
    public final String FROM_DATE_KEY = "fromDate";
    public final String TO_DATE_KEY = "toDate";


    public PrefHandler(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHAREDPREFNAME,PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public String getTOKEN_KEY(){
        return sharedPreferences.getString(TOKEN_KEY,"");
    }

    public boolean isToken_Key_Exist(){
        return sharedPreferences.contains(TOKEN_KEY);
    }

    public void setTOKEN_KEY(String token){
        editor.putString(TOKEN_KEY,token);
        editor.commit();
    }

    public boolean isName_User_Exist(){
        return sharedPreferences.contains(NAME_KEY);
    }

    public String getNAME_KEY(){
        return sharedPreferences.getString(NAME_KEY,"");
    }

    public void setNAME_KEY(String name){
        editor.putString(NAME_KEY,name);
        editor.commit();
    }

    public boolean isEmail_User_Exist(){
        return sharedPreferences.contains(EMAIL_KEY);
    }

    public String getEMAIL_KEY(){
        return sharedPreferences.getString(EMAIL_KEY,"");
    }

    public void setEMAIL_KEY(String email){
        editor.putString(EMAIL_KEY,email);
        editor.commit();
    }

    public String getFACULTY_KEY(){
        return sharedPreferences.getString(FACULTY_KEY,"");
    }

    public void setFACULTY_KEY(String faculty){
        editor.putString(FACULTY_KEY,faculty);
        editor.commit();
    }

    public String getFromDate(){
        return sharedPreferences.getString(FROM_DATE_KEY,"");
    }

    public void setFROM_DATE_KEY(String fromDate){
        editor.putString(FROM_DATE_KEY,fromDate);
        editor.commit();
    }

    public void clearFROM_DATE_KEY(){
        editor.remove(FROM_DATE_KEY);
        editor.commit();
    }


    public String getTO_DATE_KEY(){
        return sharedPreferences.getString(TO_DATE_KEY,"");
    }

    public void setTO_DATE_KEY(String toDate){
        editor.putString(TO_DATE_KEY,toDate);
        editor.commit();
    }

    public void clearTO_DATE_KEY(){
        editor.remove(TO_DATE_KEY);
        editor.commit();
    }

    public String getID_OTORISASI() {
        return sharedPreferences.getString(ID_OTORISASI,"");
    }

    public void setID_OTORISASI(String id_otorisasi){
        editor.putString(ID_OTORISASI,id_otorisasi);
        editor.commit();
    }

    public void setIMAGE_PROFILE_KEY(String URL_PROFIL_IMAGE){
        editor.putString(IMAGE_PROFILE_KEY,URL_PROFIL_IMAGE);
        editor.commit();
    }

    public String getIMAGE_PROFILE_KEY(){
        return sharedPreferences.getString(IMAGE_PROFILE_KEY,"");
    }

    public void setLogout(){
        editor.clear();
        editor.commit();
    }
}
