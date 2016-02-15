package fr.sio.ecp.federatedbirds.app.users;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.app.message.MessagesAdapter;
import fr.sio.ecp.federatedbirds.app.message.MessagesLoader;
import fr.sio.ecp.federatedbirds.auth.TokenManager;
import fr.sio.ecp.federatedbirds.model.Message;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by bensoussan on 05/02/2016.
 */
public class UserDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Message>> {

    private static final int MESSAGES = 0;
    private static final String U_ID_KEY = "user_id";
    private static final String U_NAME_KEY = "user_name";
    private static final String U_AVATAR_KEY = "user_avatar";

    private MessagesAdapter mMessagesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details_activity);

        Intent intent = getIntent();
        //User user = (User) intent.getSerializableExtra("user");

        RecyclerView listView = (RecyclerView) findViewById(R.id.messages_list);
        listView.setLayoutManager(new LinearLayoutManager(this));
        mMessagesAdapter = new MessagesAdapter();
        listView.setAdapter(mMessagesAdapter);

        TextView textView = (TextView) findViewById(R.id.username);
        textView.setText(getIntent().getExtras().getCharSequence(U_NAME_KEY));

        ImageView imageView = (ImageView) findViewById(R.id.avatar);

        Picasso.with(this)
                .load(getIntent().getExtras().getString(U_AVATAR_KEY))
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String authUserName = TokenManager.getUserLogin(v.getContext());
                if (authUserName.equals(getIntent().getExtras().getCharSequence(U_NAME_KEY))) {
                    uploadAvatar();
                } else {
                    Toast.makeText(v.getContext(), R.string.auth_not_autorize, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        getSupportLoaderManager().initLoader(
                MESSAGES,
                getIntent().getExtras(),
                this
        );
    }

    @Override
    public Loader<List<Message>> onCreateLoader(int id, Bundle args) {
        return new MessagesLoader(getApplicationContext(), null);

    }

    @Override
    public void onLoadFinished(Loader<List<Message>> loader, List<Message> messages) {
        mMessagesAdapter.setMessages(messages);
    }

    @Override
    public void onLoaderReset(Loader<List<Message>> loader) {

    }

    private void uploadAvatar(){
        startActivity(new Intent(this, AvatarUploadActivity.class));
        finish();
    }


}
