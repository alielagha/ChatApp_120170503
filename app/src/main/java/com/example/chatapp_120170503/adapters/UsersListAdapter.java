package com.example.chatapp_120170503.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp_120170503.R;
import com.example.chatapp_120170503.activities.ChatActivity;
import com.example.chatapp_120170503.models.User;

import java.util.List;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.UserViewHolder> {

    private Context context;
    private List<User> users;

    public UsersListAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(context).inflate(R.layout.user_list_item, viewGroup, false);
        return new UserViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i) {

        final User user = users.get(i);

        userViewHolder.usernameTextView.setText(user.getName());

        userViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("receiverId", user.getUid());
                intent.putExtra("receiverName", user.getName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView usernameTextView;

        UserViewHolder(@NonNull View itemView) {

            super(itemView);

            usernameTextView = itemView.findViewById(R.id.usernameTextView);

        }
    }
}
