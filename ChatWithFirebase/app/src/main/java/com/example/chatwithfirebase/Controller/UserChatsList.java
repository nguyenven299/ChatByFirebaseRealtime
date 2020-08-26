package com.example.chatwithfirebase.Controller;

import androidx.annotation.NonNull;

import com.example.chatwithfirebase.Model.Chat;
import com.example.chatwithfirebase.Model.ChatList;
import com.example.chatwithfirebase.Model.User;
import com.example.chatwithfirebase.View.Adapter.UserAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class UserChatsList {
    public static  UserChatsList instance;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    public static UserChatsList getInstance()
    {
        if(instance == null)
            instance = new UserChatsList();
        return instance;
    }
    public  interface  IuserChatsList
    {
        void onSuccess(List<User> userList);
        void onFail(String Fail);
    }
    public void UserList(final List<ChatList> chatList, final IuserChatsList iuserChatsList)
    {
        final List<User> users;
        users = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    for (ChatList chatList : chatList) {
                        if (user.getId().equals(chatList.getId())) {
                            users.add(user);
                        }
                    }
                }
                iuserChatsList.onSuccess(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                iuserChatsList.onFail("Fail");
            }
        });
    }
}
