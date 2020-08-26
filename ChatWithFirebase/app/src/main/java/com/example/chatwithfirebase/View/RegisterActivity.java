package com.example.chatwithfirebase.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;


import com.example.chatwithfirebase.Algorithm.Services.IServices;
import com.example.chatwithfirebase.Algorithm.Services.ServicesImpl;
import com.example.chatwithfirebase.Controller.SignUp;
import com.example.chatwithfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

//import static com.example.chatwithfirebase.Algorithm.DES.TrippleDes._encrypt;
//import static com.example.chatwithfirebase.Algorithm.DES.TrippleDes.key;

public class RegisterActivity extends AppCompatActivity {
    private IServices iServices = new ServicesImpl();
    MaterialEditText username, email, password;
    Button buttonregister;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        username = findViewById(R.id.username);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.password);
        checkBox = findViewById(R.id.checkbox);
        buttonregister = findViewById(R.id.register);
        firebaseAuth = FirebaseAuth.getInstance();
        buttonregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(RegisterActivity.this, "All fildes are required", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "password must be a least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
//                    register(txt_username, txt_email, txt_password);
                    SignUp.getInstance().SignUpUser(txt_username, txt_email, txt_password, new SignUp.IsignUp() {
                        @Override
                        public void onSuccess(String Success) {
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void Fail(String Fail) {
                            Toast.makeText(RegisterActivity.this, Fail, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onMismatched(String Fail) {
                            password.setError(Fail);
                        }
                    });
                }
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

//    private void register(final String username, String email, final String password) {
//        firebaseAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//
//                            // mã hóa TrippleDes
//                            try {
////                                String encryptUserName = _encrypt(username, key);
////                                String encryptImageURL = _encrypt("default", key);
////                                String encryptPassword = _encrypt(password, key);
//                                String encryptUserName = iServices._encryptTripleDes(username);
//                                String encryptImageURL = iServices._encryptTripleDes("default");
//                                String encryptPassword = iServices._encryptTripleDes(password);
//
//                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//                                String userid = firebaseUser.getUid();
//                                String deviceToken = FirebaseInstanceId.getInstance().getToken();
//                                databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userid);
//                                HashMap<String, String> hashMap = new HashMap<>();
//                                hashMap.put("id", userid);
//                                hashMap.put("username", encryptUserName);
//                                hashMap.put("imageURL", encryptImageURL);
//                                hashMap.put("password", encryptPassword);
//                                hashMap.put("device_token", deviceToken);
//                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                            startActivity(intent);
//                                            finish();
//
//                                        }
//                                    }
//                                });
//
//                            } catch (Exception e) {
//                                Toast.makeText(RegisterActivity.this,
//                                        e.getMessage(),
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(RegisterActivity.this, "You can't register with this email and password ", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
}
