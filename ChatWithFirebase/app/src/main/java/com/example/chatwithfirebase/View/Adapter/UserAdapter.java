package com.example.chatwithfirebase.View.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatwithfirebase.Algorithm.Services.IServices;
import com.example.chatwithfirebase.Algorithm.Services.ServicesImpl;
import com.example.chatwithfirebase.Model.TripleDesModel;
import com.example.chatwithfirebase.View.MessageActivity;
import com.example.chatwithfirebase.Model.User;
import com.example.chatwithfirebase.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private IServices iServices = new ServicesImpl();
    private Context context;
    private List<User> users;

    public UserAdapter(Context context, List<User> users) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = users.get(position);
        try {
            holder.username.setText(iServices._decryptTripleDes(user.getUsername()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (iServices._decryptTripleDes(user.getImageURL()).equals("default")) {
                holder.profile_image.setImageResource(R.mipmap.no_person);
            } else {
                Glide.with(context).load(iServices._decryptTripleDes(user.getImageURL())).into(holder.profile_image);
            }
        } catch (Exception e) {
            Toast.makeText(context, "Loi UserAdapter.java::: getImageURL decrypt!::: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profile_image;

        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }
}
