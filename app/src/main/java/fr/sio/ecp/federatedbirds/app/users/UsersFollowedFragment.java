package fr.sio.ecp.federatedbirds.app.users;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by Michaël on 30/11/2015.
 */
public class UsersFollowedFragment extends UsersFragment {

    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        return new UsersFollowedLoader(getContext(),null);
    }

}
