package fr.sio.ecp.federatedbirds.app.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.app.login.LoginTaskFragment;

import android.support.v4.app.Fragment;

/**
 * Created by bensoussan on 16/01/2016.
 */
public class CreateAccountFragment extends Fragment {

   /* private EditText mLoginText;
    private EditText mEmailText;
    private EditText mPassword;
    private EditText mConfimPassword;

    public void setArguments() {
        Bundle args = new Bundle();
        args.putString(LoginTaskFragment.ARG_LOGIN, login);
        args.putString(LoginTaskFragment.ARG_PASSWORD, password);
        setArguments(args);
    }

    */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_account_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*View v = getLayoutInflater(savedInstanceState).inflate(R.layout.create_account_fragment, null);
        mLoginText = (EditText) v.findViewById(R.id.message);
        mPassword = (EditText) v.findViewById(R.id.password);
        mConfimPassword = (EditText) v.findViewById(R.id.confirm_password);
        mEmailText = (EditText) v.findViewById(R.id.email);
        */

        view.findViewById(R.id.createMyAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });

    }

    public void create() {

        int i;
        EditText mLoginText = (EditText) getView().findViewById(R.id.username);
        EditText mPassword = (EditText) getView().findViewById(R.id.password);
        //EditText mConfimPassword = (EditText) getView().findViewById(R.id.confirm_password);
        EditText mEmailText = (EditText) getView().findViewById(R.id.email);

        String login = mLoginText.getText().toString();
        String pwd = mPassword.getText().toString();
        //String pwdC = mConfimPassword.getText().toString();
        String email = mEmailText.getText().toString();
        if (TextUtils.isEmpty(login)) {
            Toast.makeText(getContext(), R.string.empty_login_error, Toast.LENGTH_LONG).show();
            return;
        }
        /*if (TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(pwdC)) {
            Toast.makeText(getContext(), R.string.empty_pwd_error, Toast.LENGTH_LONG).show();
            return;
        }
        if (pwd != pwdC) {
            Toast.makeText(getContext(), R.string.empty_pwd_not_match_error, Toast.LENGTH_LONG).show();
            return;
        }
        */
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), R.string.empty_email_error, Toast.LENGTH_LONG).show();
            return;
        }

        LoginTaskFragment taskFragment = new CreateAccountTaskFragment();
        taskFragment.setArguments(login, pwd, email);
        taskFragment.show(getFragmentManager(), "login_task");


    }


}
