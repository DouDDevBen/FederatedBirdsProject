package fr.sio.ecp.federatedbirds.app.users;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by Michaël on 24/11/2015.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MessageViewHolder> {

    private List<User> mUsers;

    private static final String U_ID_KEY = "user_id";
    private static final String U_NAME_KEY = "user_name";
    private static final String U_AVATAR_KEY = "user_avatar";

    public void setUsers(List<User> users) {
        mUsers = users;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mUsers != null ? mUsers.size() : 0;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        final User user = mUsers.get(position);

        Picasso.with(holder.mAvatarView.getContext())
                .load(user.avatar)
                .into(holder.mAvatarView);

        holder.mUsernameView.setText(user.login);
        //holder.mAvatarView.setImageURI(Uri.parse(user.avatar));

        holder.mAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(U_ID_KEY, user.id);
                bundle.putCharSequence(U_NAME_KEY, user.login);
                bundle.putString(U_AVATAR_KEY, user.avatar);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

        // Suivre un  user
        holder.mButtonFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskCompat.executeParallel(
                        new FollowTask(v.getContext(), user.id, true)
                );
                Toast.makeText(v.getContext(), R.string.follow_succeed, Toast.LENGTH_SHORT).show();
            }
        });

        // Ne plus suivre un User
        holder.mButtonUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskCompat.executeParallel(
                        new FollowTask(v.getContext(), user.id, false)
                );
                Toast.makeText(v.getContext(), R.string.unfollowing_succeed, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mAvatarView;
        private TextView mUsernameView;
        private Button mButtonFollow;
        private Button mButtonUnfollow;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mAvatarView = (ImageView) itemView.findViewById(R.id.avatar);
            mUsernameView = (TextView) itemView.findViewById(R.id.username);
            mButtonFollow = (Button) itemView.findViewById(R.id.followButton);
            mButtonUnfollow = (Button) itemView.findViewById(R.id.unfollowButton);
        }

    }

}