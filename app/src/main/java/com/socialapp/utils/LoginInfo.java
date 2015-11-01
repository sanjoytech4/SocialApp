package com.socialapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginInfo
{
    private Context context;
    private String SHARED_PREF_NAME="social_app_pref";
    private String SHARED_ACCESS_TOKEN="token";
    private String SHARED_IS_LOGIN="isLogin";
    private String SHARED_USER_NAME="username";

    private String SHARED_LOCALITY="locality";
//    private String SHARED_LOCALITY_LAT="localityLat";
//    private String SHARED_LOCALITY_LNG="localityLng";

    private SharedPreferences pref;

    public LoginInfo(Context context)
    {
        this.context=context;
        pref= context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn()
    {
        return pref.getBoolean(SHARED_IS_LOGIN,false);
    }

    public String getAccessToken(){
        return pref.getString(SHARED_ACCESS_TOKEN, "");
    }

    public void setAccessToken(String token)
    {
        SharedPreferences.Editor editor=pref.edit();
        editor.putString(SHARED_ACCESS_TOKEN,token);
        editor.putBoolean(SHARED_IS_LOGIN,true);
        editor.commit();
    }

    public void updateLoginStatus(boolean flag)
    {
        SharedPreferences.Editor editor=pref.edit();
        editor.putBoolean(SHARED_IS_LOGIN,flag);
        editor.commit();
    }

    public void setLoggedInUser(String username)
    {
        SharedPreferences.Editor editor=pref.edit();
        editor.putString(SHARED_USER_NAME, username);
        editor.commit();
    }

    public String getLoggedInUser()
    {
        return pref.getString(SHARED_USER_NAME, "");
    }

    public void setLocalityInfo(String locality)
    {
        SharedPreferences.Editor editor=pref.edit();
        editor.putString(SHARED_LOCALITY, locality);
        editor.commit();
    }
    public String getLocality()
    {
        return pref.getString(SHARED_LOCALITY, "");
    }
}
