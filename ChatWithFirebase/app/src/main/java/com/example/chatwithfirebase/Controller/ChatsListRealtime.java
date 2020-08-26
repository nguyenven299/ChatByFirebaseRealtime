package com.example.chatwithfirebase.Controller;

import androidx.annotation.NonNull;

import com.example.chatwithfirebase.Model.Chat;
import com.example.chatwithfirebase.Model.ChatList;
import com.example.chatwithfirebase.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsListRealtime {
    public static ChatsListRealtime instance;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public static ChatsListRealtime getInstance() {
        if (instance == null)
            instance = new ChatsListRealtime();
        return instance;
    }

    public interface IchatList {
        void onSuccess(ChatList chatList);
        void onUserList(List<User> users);
        void onFail(String Fail);
    }

    public void ChatsListUser(String uid, final IchatList ichatList) {
        databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                userList.clear();
                List<ChatList> chatLists = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatList chatList = snapshot.getValue(ChatList.class);
                    chatLists.add(chatList);
                }
//                chatList();
                UserChatsList.getInstance().UserList(chatLists, new UserChatsList.IuserChatsList() {
                    @Override
                    public void onSuccess(List<User> userList) {
                        //xử lý chatlist  và trả về
                        ichatList.onUserList(userList);
                    }

                    @Override
                    public void onFail(String Fail) {
//                        ichatList.onFail(Fail);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                ichatList.onFail("Fail");
            }
        });
    }
}
