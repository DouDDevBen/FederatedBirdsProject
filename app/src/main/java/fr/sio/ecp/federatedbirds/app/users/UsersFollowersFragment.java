package fr.sio.ecp.federatedbirds.app.users;

import android.os.Bundle;
import android.support.v4.content.Loader;

import java.util.List;

import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by bensoussan on 05/02/2016.
 */
public class UsersFollowersFragment extends UsersFragment {

    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        return new UsersFollowedLoader(getContext(),null);
    }
}
