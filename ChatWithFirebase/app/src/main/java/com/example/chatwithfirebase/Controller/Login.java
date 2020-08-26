package com.example.chatwithfirebase.Controller;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.chatwithfirebase.Algorithm.Services.IServices;
import com.example.chatwithfirebase.Algorithm.Services.ServicesImpl;
import com.example.chatwithfirebase.Model.TripleDesModel;
import com.example.chatwithfirebase.Model.User;
import com.example.chatwithfirebase.View.LoginActivity;
import com.example.chatwithfirebase.View.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login {
    public  static  Login instance;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseDatabase mDatabase;
    private IServices iServices = new ServicesImpl();

    public static  Login getInstance()
    {
        if(instance ==null)
            instance = new Login();
        return instance;
    }
    public interface Ilogin
    {
        void onSuccess (String Success);
        void onFail(String Fail);
    }
    public void LoginUser (String Email, final String Password, final Ilogin ilogin)
    {
        firebaseAuth.signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final String uid = firebaseAuth.getCurrentUser().getUid();
                            mDatabase = FirebaseDatabase.getInstance();
                            userRef = mDatabase.getReference("User");
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(uid).exists()) {
                                        if (!uid.isEmpty()) {
                                            User login = dataSnapshot.child(uid).getValue(User.class);
                                            try {
//                                                            String decryptPassword = _decrypt(login.getPassword(), key);
                                                TripleDesModel encryptLoginPass = new TripleDesModel();
                                                encryptLoginPass.setEncrypt(login.getPassword());
                                                String decryptPassword = iServices._decryptTripleDes(login.getPassword());
                                                if (decryptPassword.equals(Password)) {
                                                    ilogin.onSuccess("WelCome to Chat Encode");
                                                } else {
                                                    ilogin.onFail("Login Fail");
                                                }
                                            } catch (Exception e) {
                                                ilogin.onFail("Login Fail");
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            ilogin.onFail("Login Fail");
                        }
                    }
                });
    }
}
