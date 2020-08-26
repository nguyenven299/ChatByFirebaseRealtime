package com.example.chatwithfirebase.Controller;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.chatwithfirebase.Algorithm.Services.IServices;
import com.example.chatwithfirebase.Algorithm.Services.ServicesImpl;
import com.example.chatwithfirebase.View.MainActivity;
import com.example.chatwithfirebase.View.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.regex.Pattern;

public class SignUp {
    public static SignUp instance;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private IServices iServices = new ServicesImpl();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])" +            //phái có ít nhất 1 số
                    "(?=.[a-z])" +              // phải có ít nhất 1 chữ thường
                    "(?=.*[A-Z])" +             // phải có ít nhất 1 chữ hoa
                    "(?=.*[@#$%^&+=])" +        // phải có ít nhất 1 ký tự
                    "(?=\\S+$)" +               // không có khoản trắng
                    ".{6,}" +                   // ít nhất 6 chữ số
                    "$");

    public static SignUp getInstance() {
        if (instance == null)
            instance = new SignUp();
        return instance;
    }

    public interface IsignUp {
        void onSuccess(String Success);

        void Fail(String Fail);

        void onMismatched(String Fail);
    }

    public void SignUpUser(final String username, String email, final String password, final IsignUp isignUp) {
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            isignUp.onMismatched("Password too weak!");
        } else if (PASSWORD_PATTERN.matcher(password).matches()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // mã hóa TrippleDes
                                try {
//                                String encryptUserName = _encrypt(username, key);
//                                String encryptImageURL = _encrypt("default", key);
//                                String encryptPassword = _encrypt(password, key);
                                    String encryptUserName = iServices._encryptTripleDes(username);
                                    String encryptImageURL = iServices._encryptTripleDes("default");
                                    String encryptPassword = iServices._encryptTripleDes(password);

                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    String userid = firebaseUser.getUid();
                                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                    databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userid);
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("id", userid);
                                    hashMap.put("username", encryptUserName);
                                    hashMap.put("imageURL", encryptImageURL);
                                    hashMap.put("password", encryptPassword);
                                    hashMap.put("device_token", deviceToken);
                                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
//                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                            startActivity(intent);
//                                            finish();
                                                isignUp.onSuccess("Success");

                                            }
                                        }
                                    });

                                } catch (Exception e) {
                                    Log.d("Fail", e.getMessage());
                                }
                            } else {
//                            Toast.makeText(RegisterActivity.this, "You can't register with this email and password ", Toast.LENGTH_SHORT).show();
                                isignUp.Fail("You can't register with this email and password");
                            }
                        }
                    });
        }
    }
}
