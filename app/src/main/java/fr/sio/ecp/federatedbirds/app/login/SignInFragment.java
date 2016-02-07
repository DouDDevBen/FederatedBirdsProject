package fr.sio.ecp.federatedbirds.app.login;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.utils.ValidationUtils;

/**
 * Created by bensoussan on 27/01/2016.
 */
public class SignInFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.signin_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getView().findViewById(R.id.signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });
    }

    private void signin() {

        // Get form views
        EditText usernameText = (EditText) getView().findViewById(R.id.username);
        EditText passwordText = (EditText) getView().findViewById(R.id.password);

        String login = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        if (!ValidationUtils.validateLogin(login)) {
            usernameText.setError(getString(R.string.invalid_format));
            usernameText.requestFocus();
            return;
        }

        if (!ValidationUtils.validatePassword(password)) {
            passwordText.setError(getString(R.string.invalid_format));
            passwordText.requestFocus();
            return;
        }

        LoginTaskFragment taskFragment = new SignInTaskFragment();
        taskFragment.setArguments(login, password, null);
        taskFragment.show(getFragmentManager(), "login_task");

    }



}
