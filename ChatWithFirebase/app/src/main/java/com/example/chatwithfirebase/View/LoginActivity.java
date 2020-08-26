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
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatwithfirebase.Algorithm.Services.IServices;
import com.example.chatwithfirebase.Algorithm.Services.ServicesImpl;
import com.example.chatwithfirebase.Controller.Login;
import com.example.chatwithfirebase.Model.TripleDesModel;
import com.example.chatwithfirebase.Model.User;
import com.example.chatwithfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rengwuxian.materialedittext.MaterialEditText;

//import static com.example.chatwithfirebase.Algorithm.DES.TrippleDes._decrypt;
//import static com.example.chatwithfirebase.Algorithm.DES.TrippleDes.key;

public class LoginActivity extends AppCompatActivity {
    private IServices iServices = new ServicesImpl();
    private MaterialEditText email, password;
    private Button buttonLogin;
    private TextView textViewForgetPass;
    FirebaseAuth firebaseAuth;
    private CheckBox checkBox;
    private FirebaseDatabase mDatabase;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.login);
        checkBox = findViewById(R.id.checkbox);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                final String txt_password = password.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(LoginActivity.this, "All Fildes are required", Toast.LENGTH_SHORT).show();

                } else {
//                    firebaseAuth.signInWithEmailAndPassword(txt_email, txt_password)
//                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if (task.isSuccessful()) {
//                                        final String uid = firebaseAuth.getCurrentUser().getUid();
//                                        mDatabase = FirebaseDatabase.getInstance();
//                                        userRef = mDatabase.getReference("User");
//                                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                if (dataSnapshot.child(uid).exists()) {
//                                                    if (!uid.isEmpty()) {
//                                                        User login = dataSnapshot.child(uid).getValue(User.class);
//                                                        try {
////                                                            String decryptPassword = _decrypt(login.getPassword(), key);
//                                                            TripleDesModel encryptLoginPass = new TripleDesModel();
//                                                            encryptLoginPass.setEncrypt(login.getPassword());
//                                                            String decryptPassword = iServices._decryptTripleDes(login.getPassword());
//                                                            if (decryptPassword.equals(txt_password)) {
//                                                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công! ^^", Toast.LENGTH_SHORT).show();
//
//
//                                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                                startActivity(intent);
//                                                                finish();
//                                                            } else {
//                                                                Toast.makeText(LoginActivity.this, "Sai rồi đó!", Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        } catch (Exception e) {
//                                                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    }
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                            }
//                                        });
//                                    } else {
//                                        Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
                    Login.getInstance().LoginUser(txt_email, txt_password, new Login.Ilogin() {
                        @Override
                        public void onSuccess(String Success) {
                            Toast.makeText(LoginActivity.this, Success, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFail(String Fail) {
                            Toast.makeText(LoginActivity.this, Fail, Toast.LENGTH_SHORT).show();
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
}
