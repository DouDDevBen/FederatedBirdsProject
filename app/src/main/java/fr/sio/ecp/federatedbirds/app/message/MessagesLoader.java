package fr.sio.ecp.federatedbirds.app.message;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import fr.sio.ecp.federatedbirds.ApiClient;
import fr.sio.ecp.federatedbirds.model.Message;

/**
 * Created by Michaël on 24/11/2015.
 */
public class MessagesLoader extends AsyncTaskLoader<List<Message>> {

    private Long mUserId;
    private List<Message> mResult;

    public MessagesLoader(Context context, Long userId) {
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
    public List<Message> loadInBackground() {
        try {
            return ApiClient.getInstance(getContext()).getMessages(mUserId);
        } catch (IOException e) {
            Log.e("MessagesLoader", "Failed to load messages", e);
            return null;
        }
    }

    @Override
    public void deliverResult(List<Message> data) {
        mResult = data;
        super.deliverResult(data);
    }
}