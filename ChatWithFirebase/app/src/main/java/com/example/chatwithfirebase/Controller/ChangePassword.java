package com.example.chatwithfirebase.Controller;

import androidx.annotation.NonNull;

import com.example.chatwithfirebase.Algorithm.Services.IServices;
import com.example.chatwithfirebase.Algorithm.Services.ServicesImpl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class ChangePassword {
    public static ChangePassword instance;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])" +            //phái có ít nhất 1 số
                    "(?=.[a-z])" +              // phải có ít nhất 1 chữ thường
                    "(?=.*[A-Z])" +             // phải có ít nhất 1 chữ hoa
                    "(?=.*[@#$%^&+=])" +        // phải có ít nhất 1 ký tự
                    "(?=\\S+$)" +               // không có khoản trắng
                    ".{6,}" +                   // ít nhất 6 chữ số
                    "$");

    private IServices iServices = new ServicesImpl();

    public static ChangePassword getInstance() {
        if (instance == null)
            instance = new ChangePassword();
        return instance;
    }

    public interface IchangePassword {
        void onSuccess(String Success);

        void onFail(String Fail);

        void onMismatched(String Fail);
    }

    public void ChangePasswordUser(final String uid, final String rePassoword, final String Password, final IchangePassword ichangePassword) {

        if (rePassoword.equals(Password)) {
            if (!PASSWORD_PATTERN.matcher(rePassoword).matches()) {
                ichangePassword.onMismatched("Password too weak!");
            } else if(PASSWORD_PATTERN.matcher(rePassoword).matches() ) {
                try {
                    final String encryptPassword = iServices._encryptTripleDes(Password);
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
                    final DatabaseReference databaseReference1 = databaseReference.child(uid);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(Password)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("password", encryptPassword);
                                        databaseReference1.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                ichangePassword.onSuccess("Change Password Success");
                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        ichangePassword.onFail("Change Password Fail!");
                                                    }
                                                });
                                    }
                                }
                            });
                } catch (Exception e) {
                    ichangePassword.onFail(e.getMessage());
                }
            }

        } else {
            ichangePassword.onMismatched("Passwords Do Not Match");
        }

    }
}
