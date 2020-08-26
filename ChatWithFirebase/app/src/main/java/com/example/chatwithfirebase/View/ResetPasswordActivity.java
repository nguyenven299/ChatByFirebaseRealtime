package com.example.chatwithfirebase.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chatwithfirebase.Controller.ChangePassword;
import com.example.chatwithfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ResetPasswordActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Button buttonResetPass, buttonBackHome;
    private MaterialEditText editTextEmail, editTextPassword, editTextPasswordReset;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private CheckBox checkBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        buttonResetPass = findViewById(R.id.buttonResetPass);
        buttonBackHome = findViewById(R.id.buttonComback);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordReset = findViewById(R.id.editTextPasswordReset);
        editTextEmail.setText(user.getEmail());
        checkBox = findViewById(R.id.checkbox);
        buttonResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangePassword.getInstance().ChangePasswordUser(user.getUid(), editTextPasswordReset.getText().toString().trim(), editTextPassword.getText().toString().trim(), new ChangePassword.IchangePassword() {
                    @Override
                    public void onSuccess(String Success) {
                        Toast.makeText(ResetPasswordActivity.this, Success, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(String Fail) {
                        Toast.makeText(ResetPasswordActivity.this, Fail, Toast.LENGTH_SHORT).show();
                    }

                    public void onMismatched(String Fail) {
                        editTextPassword.setError(Fail);
                    }
                });
            }
        });
        buttonBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    editTextPasswordReset.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editTextPasswordReset.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }
}
