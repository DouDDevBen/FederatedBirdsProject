package fr.sio.ecp.federatedbirds.app.users;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by Michaël on 24/11/2015.
 */
public abstract class UsersLoader extends AsyncTaskLoader<List<User>> {

    private Long mUserId;
    private List<User> mResult;

    public UsersLoader(Context context, Long userId) {
        super(context);
        mUserId = userId;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mResult != null) {
            deliverResult(mResult);
        } else {
            forceLoad();
        }
    }

    @Override
    public List<User> loadInBackground() {
        try {
            return getUsers(mUserId);
        } catch (IOException e) {
            Log.e("MessagesLoader", "Failed to load messages", e);
            return null;
        }
    }

    protected abstract List<User> getUsers(Long userId) throws IOException;

    @Override
    public void deliverResult(List<User> data) {
        mResult = data;
        super.deliverResult(data);
    }
}