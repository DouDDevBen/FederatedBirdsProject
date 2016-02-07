package fr.sio.ecp.federatedbirds.app.users;

import android.content.Context;

import java.io.IOException;
import java.util.List;

import fr.sio.ecp.federatedbirds.ApiClient;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by bensoussan on 05/02/2016.
 */
public class UsersFollowedLoader extends UsersLoader {

    public UsersFollowedLoader(Context context, Long userId) {
        super(context, userId);
    }

    @Override
    protected List<User> getUsers(Long userId) throws IOException {
        return ApiClient.getInstance(getContext()).getUserFollowed(userId);
    }
}
