package com.example.chatwithfirebase.Controller;

import androidx.annotation.NonNull;

import com.example.chatwithfirebase.Algorithm.Services.IServices;
import com.example.chatwithfirebase.Algorithm.Services.ServicesImpl;
import com.example.chatwithfirebase.Model.Chat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SendMessage {
    public static SendMessage instance;
    private IServices iServices = new ServicesImpl();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    public static SendMessage getInstance()
    {
        if(instance == null)
            instance = new SendMessage();
        return instance;
    }
    public interface IsendMessage
    {
        void onSuccess (String Success);
    }
    public void SendMessageUser (final String sender, final String receiver, String message, IsendMessage isendMessage)
    {
        Chat chat = new Chat(sender, receiver, message);
        String encodeChat = null;
        try {
            encodeChat = iServices._encryptJWTChat(chat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> users = new HashMap<>();
//        users.put("sender", sender);
//        users.put("receiver", receiver);
//        users.put("message", messageEncrypt);
        users.put("JWT", encodeChat);
        databaseReference.child("Chat").push().setValue(users);


        final DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> notification = new HashMap<>();
        notification.put("from", sender);
        notification.put("type", "default");
        databaseReference3.child("Notification").child(receiver).push()
                .setValue(notification);

        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(sender).child(receiver);
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    databaseReference1.child("id").setValue(receiver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        final DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("ChatListReceiver")
                .child(receiver).child(sender);
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    databaseReference2.child("id").setValue(sender);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
