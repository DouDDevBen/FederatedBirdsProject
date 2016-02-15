package fr.sio.ecp.federatedbirds.app.login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import fr.sio.ecp.federatedbirds.ApiClient;
import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.app.main.MainActivity;
import fr.sio.ecp.federatedbirds.auth.TokenManager;

/**
 * Created by Michaël on 30/11/2015.
 */

// Les classes SignInTaskFragment et CreateAcountTaskFragment étendent cette classe.
public abstract class LoginTaskFragment extends DialogFragment {

    protected static final String ARG_LOGIN = "login";
    protected static final String ARG_PASSWORD = "password";
    protected static final String ARG_EMAIL = "email";

    public abstract void setArguments(String login, String password, String email);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @NonNull
    @Override
    public abstract Dialog onCreateDialog(Bundle savedInstanceState) ;

}
