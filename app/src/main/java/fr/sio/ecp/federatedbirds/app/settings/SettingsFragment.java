package fr.sio.ecp.federatedbirds.app.settings;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.app.main.MainActivity;
import fr.sio.ecp.federatedbirds.auth.TokenManager;

/**
 * Created by Michaël on 26/11/2015.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.settings);

        findPreference("logout").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                TokenManager.clear(getContext());
                startActivity(MainActivity.newIntent(getContext()));
                return true;
            }
        });

        findPreference("compte").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return true;
            }
        });

    }

}
