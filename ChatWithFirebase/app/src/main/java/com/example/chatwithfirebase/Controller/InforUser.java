package com.example.chatwithfirebase.Controller;

import androidx.annotation.NonNull;

import com.example.chatwithfirebase.Algorithm.Services.IServices;
import com.example.chatwithfirebase.Algorithm.Services.ServicesImpl;
import com.example.chatwithfirebase.Model.Chat;
import com.example.chatwithfirebase.Model.TripleDesModel;
import com.example.chatwithfirebase.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class InforUser {
    public static InforUser instance;
    private DatabaseReference databaseReference;
    private IServices iServices = new ServicesImpl();

    public static InforUser getInstance() {
        if (instance == null)
            instance = new InforUser();
        return instance;
    }

    public interface IgetInforUser {
        void userNull(String UserNull);

        void onSuccess(String Success);

        void onFail(String Fail);

        void imageNull(String Image);

        void Image(String Image);

        void onReadListChat(List<Chat> chatList, String image);
    }

    public void getInforUser(final String username, final String uid, final IgetInforUser igetInforUser) {
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (username == null) {
                    igetInforUser.userNull("User Null");
                } else {
                    if (username != null) {
                        try {
                            TripleDesModel encryptUserName = new TripleDesModel("", user.getUsername());
                            igetInforUser.onSuccess(iServices._decryptTripleDes(user.getUsername()));
                        } catch (Exception e) {
                            igetInforUser.onFail("User Fail!");
                        }
                        try {
                            TripleDesModel encryptImageURL = new TripleDesModel("", user.getImageURL());
                            if (iServices._decryptTripleDes(user.getImageURL()).equals("default")) {
                                igetInforUser.imageNull("Null");
                            } else {
                                igetInforUser.Image(iServices._decryptTripleDes(user.getImageURL()));
                            }
                        } catch (Exception e) {
                            igetInforUser.onFail("User Fail!");
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("User").child(username);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (username == null) {
                    igetInforUser.userNull("User Null");
                } else {
                    if (username != null) {
                        try {
                            TripleDesModel encryptUserName = new TripleDesModel("", user.getUsername());
                            igetInforUser.onSuccess(iServices._decryptTripleDes(user.getUsername()));
                        } catch (Exception e) {
                            igetInforUser.onFail("User Fail!");
                        }
                        try {
                            TripleDesModel encryptImageURL = new TripleDesModel("", user.getImageURL());
                            if (iServices._decryptTripleDes(user.getImageURL()).equals("default")) {
                                igetInforUser.imageNull("Null");
                            } else {
                                igetInforUser.Image(iServices._decryptTripleDes(user.getImageURL()));
                            }
                            String myid = uid;
                            String userid = username;
                            ReadMessage.getInstance().ReadMeasseUser(myid, userid, iServices._decryptTripleDes(user.getImageURL()), new ReadMessage.IreadMessage() {
                                @Override
                                public void onSuccess(List<Chat> chatList, String image) {
                                    igetInforUser.onReadListChat(chatList, image);
                                }
                            });

                        } catch (Exception e) {
                            igetInforUser.onFail("User Fail!");
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
