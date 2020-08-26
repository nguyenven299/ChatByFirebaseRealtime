package com.example.chatwithfirebase.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.chatwithfirebase.Algorithm.Services.IServices;
import com.example.chatwithfirebase.Algorithm.Services.ServicesImpl;
import com.example.chatwithfirebase.Controller.InforUser;
import com.example.chatwithfirebase.Controller.SendMessage;
import com.example.chatwithfirebase.Model.Chat;
import com.example.chatwithfirebase.Model.ChatJWT;
import com.example.chatwithfirebase.R;
import com.example.chatwithfirebase.View.Adapter.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//import static com.example.chatwithfirebase.Algorithm.DES.TrippleDes._encrypt;
//import static com.example.chatwithfirebase.Algorithm.DES.TrippleDes.key;

public class MessageActivity extends AppCompatActivity {
    private IServices iServices = new ServicesImpl();
    CircleImageView profile_image;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ImageButton imageButton_send;
    EditText editText_send;
    Intent intent;
    MessageAdapter messageAdapter;
    List<Chat> chats;
    ImageView imageView;
    RecyclerView recyclerView;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        imageButton_send = findViewById(R.id.buton_send);
        editText_send = findViewById(R.id.text_send);
        Toolbar toolbar = findViewById(R.id.toolbar);
        imageView = findViewById(R.id.profile_image);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        Context context;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        intent = getIntent();
        userid = intent.getStringExtra("userid");

        imageButton_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText_send.getText().toString();
                if (!msg.equals("")) {
                    try {
//                        sendMessage(firebaseUser.getUid(), userid, msg);
                        SendMessage.getInstance().SendMessageUser(firebaseUser.getUid(), userid, msg, new SendMessage.IsendMessage() {
                            @Override
                            public void onSuccess(String Success) {

                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(MessageActivity.this, ">>>>>>> Loi SendMessage", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                editText_send.setText("");
            }
        });
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        InforUser.getInstance().getInforUser(userid, firebaseUser.getUid(), new InforUser.IgetInforUser() {
            // đặt userid vòa username để tránh null
            @Override
            public void userNull(String UserNull) {

            }

            @Override
            public void onSuccess(String Success) {
                username.setText(Success);
            }

            @Override
            public void onFail(String Fail) {

            }

            @Override
            public void imageNull(String Image) {
                profile_image.setImageResource(R.mipmap.no_person);
            }

            @Override
            public void Image(String Image) {
                Glide.with(MessageActivity.this).load(Image).into(imageView);
            }
            @Override
            public void onReadListChat(List<Chat>chatList, String image) {
                messageAdapter = new MessageAdapter(MessageActivity.this, chatList, image);
                recyclerView.setAdapter(messageAdapter);
            }
        });


    }

    private void sendMessage(String sender, final String receiver, String message) throws Exception {
//        String messageEncrypt = _encrypt(message, key);
        Chat chat = new Chat(sender, receiver, message);
//        String encodeChat = JWT._encryptJWTChat(chat);
        String encodeChat = iServices._encryptJWTChat(chat);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> users = new HashMap<>();
//        users.put("sender", sender);
//        users.put("receiver", receiver);
//        users.put("message", messageEncrypt);
        users.put("JWT", encodeChat);
        databaseReference.child("Chat").push().setValue(users);

        String type = editText_send.getText().toString();
        String uid = firebaseUser.getUid();
        final DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> notification = new HashMap<>();
        notification.put("from", uid);
        notification.put("type", "default");
        databaseReference3.child("Notification").child(receiver).push()
                .setValue(notification);

        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(firebaseUser.getUid()).child(receiver);
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
                .child(receiver).child(firebaseUser.getUid());
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    databaseReference2.child("id").setValue(firebaseUser.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void readMessage(final String myid, final String userid, final String imageurl) {
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
                    messageAdapter = new MessageAdapter(MessageActivity.this, chats, imageurl);
                    recyclerView.setAdapter(messageAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
