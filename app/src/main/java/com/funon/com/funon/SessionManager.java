package com.funon.com.funon;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vidit on 17/6/17.
 */

public class SessionManager {


    SharedPreferences.Editor editor;
    Context _context;
    private static final String prefs = "zomeds";
    SharedPreferences preferences;
    public void setPrefs(Context _context, String key, String value)
    {


        editor = _context.getSharedPreferences(prefs, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();


    }
    public void setPrefs(Context _context, String key, int value)
    {


        editor = _context.getSharedPreferences(prefs, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();


    }
    public void setPrefs(Context _context, String key, Float value)
    {


        editor = _context.getSharedPreferences(prefs, Context.MODE_PRIVATE).edit();
        editor.putFloat(key, value);
        editor.commit();


    }
    public void setPrefs(Context _context, String key, boolean value)
    {
        editor = _context.getSharedPreferences(prefs, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.commit();

    }


    public String getPrefs(Context _context, String key) {


        preferences = _context.getSharedPreferences(prefs, Context.MODE_PRIVATE);//why ?
        String pos = preferences.getString(key," ");
        return pos;

    }
    public int getPrefs(Context _context, String key,int def) {


        preferences = _context.getSharedPreferences(prefs, Context.MODE_PRIVATE);//why ?
        int pos = preferences.getInt(key,def);
        return pos;

    }
    public boolean getPrefs(Context _context, String key,boolean def) {


        preferences = _context.getSharedPreferences(prefs, Context.MODE_PRIVATE);//why ?
        boolean pos = preferences.getBoolean(key,def);
        return pos;

    }
    public float getPrefs(Context _context, String key,float def) {


        preferences = _context.getSharedPreferences(prefs, Context.MODE_PRIVATE);//why ?
        float pos = preferences.getFloat(key,def);
        return pos;

    }
    public boolean logout(Context context){
        preferences = context.getSharedPreferences(prefs, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.clear();
        editor.commit();
        return true;
    }



}














