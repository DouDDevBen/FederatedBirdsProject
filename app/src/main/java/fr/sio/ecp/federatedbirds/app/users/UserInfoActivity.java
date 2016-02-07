package fr.sio.ecp.federatedbirds.app.users;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by bensoussan on 05/02/2016.
 */
public class UserInfoActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_activity);

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");

        TextView textView = (TextView) findViewById(R.id.username);
        ImageView imageView = (ImageView) findViewById(R.id.avatar);

        textView.setText(user.login);
        Picasso
                .with(this)
                .load(user.avatar)
                .into(imageView);
    }


}
