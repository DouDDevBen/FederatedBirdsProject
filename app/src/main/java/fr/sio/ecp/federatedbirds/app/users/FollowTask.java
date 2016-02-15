package fr.sio.ecp.federatedbirds.app.users;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import fr.sio.ecp.federatedbirds.ApiClient;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by bensoussan on 08/02/2016.
 */
public class FollowTask extends AsyncTask<Void, Void, User> {

    private Context context;
    private long userIdToFollow;
    private boolean isFollow;

    public FollowTask(Context context, long following_id, boolean follow){
        this.context = context;
        userIdToFollow = following_id;
        isFollow = follow;
    }

    @Override
    protected User doInBackground(Void... params) {
        try {
            return ApiClient.getInstance(context).follow(userIdToFollow, isFollow);
        } catch (IOException e) {
            Log.e(FollowTask.class.getSimpleName(), "follow task failed", e);
            return null;
        }
    }

}
