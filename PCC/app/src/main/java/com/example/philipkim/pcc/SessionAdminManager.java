package com.example.philipkim.pcc;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.HashMap;

public class SessionAdminManager {
    static final String ID = "ID";
    static final String NAME = "NAME";
    static final String DATE = "DATE";
    private static final String STIME = "STIME";
    private static final String ETIME = "ETIME";
    private static final String TIMES = "TIMES";
    private static final String USER = "USERNAME";
    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public Context context;

    /*
     * Constructs SessionAdminManager with Context
     * @params Context context
     * @return SessionAdminManager
     * */
    public SessionAdminManager(Context context)
    {
        int PRIVATE_MODE = 0;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    /*
     * A void method to create a new session for admins when they log in.
     * @params String id
     * @params String name
     * @params String user
     * @return The admins info when they log into the admins page.
     * */
    public void createSession(String id, String name, String user)
    {
        editor.putBoolean(LOGIN, true);
        editor.putString(ID, id);
        editor.putString(NAME, name);
        editor.putString(USER, user);
        editor.apply();
    }

    /*
     * A boolean method to see if the admin is logged in or not.
     * @return false if admin is not logged in.
     * */
    public boolean isLoggedIn()
    {
        return sharedPreferences.getBoolean(LOGIN, false);
    }


    /*
     * A void method to check login
     * */
    public void checkLogin()
    {
        if(!this.isLoggedIn())
        {
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((AdminActivity) context).finish();
        }
    }


    /*
     * A hashMap method to check the id, name, and username with the database
     * */
    public HashMap<String, String> getAdminDetail()
    {
        HashMap<String, String> user = new HashMap<>();
        user.put(ID, sharedPreferences.getString(ID, null));
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(DATE, sharedPreferences.getString(DATE, null));
        user.put(STIME, sharedPreferences.getString(STIME, null));
        user.put(ETIME, sharedPreferences.getString(ETIME, null));
        user.put(TIMES, sharedPreferences.getString(TIMES, null));
        return user;
    }

    /*
     * A void method to clear all the components when the user logs out
     * */
    public void logout()
    {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        ((AdminActivity) context).finish();
    }
}