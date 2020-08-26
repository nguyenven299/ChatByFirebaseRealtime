package com.example.chatwithfirebase.View;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chatwithfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class FirebaseInstanceIdService extends com.google.firebase.iid.FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refeshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TokenDevices", "onTokenRefresh: " +refeshedToken);
        sendRegistrationToServer(refeshedToken);
    }
    private void sendRegistrationToServer(final String token) {
        Log.d("TokenDevices", "sendRegistrationToServer: sending token to server: " + token);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("User").
                child(firebaseUser.getUid());
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    databaseReference1.child("device_token").setValue(token);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
