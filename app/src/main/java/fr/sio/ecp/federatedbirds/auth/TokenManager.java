package fr.sio.ecp.federatedbirds.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Michaël on 25/11/2015.
 */
public class TokenManager {

    private static final String AUTH_PREFERENCES = "auth";
    private static final String TOKEN_KEY = "token";
    private static final String LOGIN_KEY = "login";

    public static String getUserToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(TOKEN_KEY, null);
    }

    public static String getUserLogin(Context context){
        SharedPreferences sp = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(LOGIN_KEY, null);
    }

    public static void setUserToken(Context context, String token, String login) {
        SharedPreferences sp = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        sp.edit().putString(TOKEN_KEY, token).apply();
        sp.edit().putString(LOGIN_KEY, login).apply();
        Log.i(TokenManager.class.getSimpleName(), "User token saved: " + token);
    }

    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
        Log.i(TokenManager.class.getSimpleName(), "Auth preferences cleared");
    }

}
