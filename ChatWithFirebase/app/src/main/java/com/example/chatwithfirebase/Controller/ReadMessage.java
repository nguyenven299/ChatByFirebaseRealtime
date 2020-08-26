package com.example.chatwithfirebase.Controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chatwithfirebase.Algorithm.Services.IServices;
import com.example.chatwithfirebase.Algorithm.Services.ServicesImpl;
import com.example.chatwithfirebase.Model.Chat;
import com.example.chatwithfirebase.Model.ChatJWT;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReadMessage {
    public static ReadMessage instance;
    private List<Chat> chats;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private IServices iServices = new ServicesImpl();

    public static ReadMessage getInstance() {
        if (instance == null)
            instance = new ReadMessage();
        return instance;
    }

    public interface IreadMessage {
        void onSuccess(List<Chat>chatList, String image);
    }

    public void ReadMeasseUser(final String myid, final String userid, final String imageurl, final IreadMessage ireadMessage) {
        chats = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chat");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    Chat chat = dataSnapshot1.getValue(Chat.class);
                    ChatJWT jwtChat = dataSnapshot1.getValue(ChatJWT.class);
                    Chat chat = new Chat();
                    try {
//                        chat = JWT._decryptJWTChat(jwtChat.getJWT());
                        chat = iServices._decryptJWTChat(jwtChat.getJWT());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (chat.getReceiver().equals(myid)
                            && chat.getSender().equals(userid)
                            || chat.getReceiver().equals(userid)
                            && chat.getSender().equals(myid)) {
                        chats.add(chat);

                        Log.d("Thong bao", "onDataChange: thong bao ne");
                    }
                    ireadMessage.onSuccess(chats, imageurl);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
