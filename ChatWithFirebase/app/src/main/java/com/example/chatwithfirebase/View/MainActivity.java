package com.example.chatwithfirebase.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.chatwithfirebase.Algorithm.Services.IServices;
import com.example.chatwithfirebase.Algorithm.Services.ServicesImpl;
import com.example.chatwithfirebase.Controller.InforUser;
import com.example.chatwithfirebase.Model.Chat;
import com.example.chatwithfirebase.View.UI.ChatsFragment;
import com.example.chatwithfirebase.View.UI.ProfileFragment;
import com.example.chatwithfirebase.View.UI.UsersFragment;
import com.example.chatwithfirebase.Model.TripleDesModel;
import com.example.chatwithfirebase.Model.User;
import com.example.chatwithfirebase.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private IServices iServices = new ServicesImpl();
    public static final String CHANEL_ID = "Ven";
    private static final String CHANEL_NAME = "Ven CODE";
    private static final String CHANEL_DESC = "NOTIFICATION CHATWITHFIREBASE";
    CircleImageView profile_image;
    TextView username;
    private String CHANNEL_ID = "Thong Bao Ne";
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name;
            NotificationChannel notificationChannel = new NotificationChannel(CHANEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(CHANEL_DESC);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        setSupportActionBar(toolbar);
        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        notificationChanel();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        InforUser.getInstance().getInforUser(username.getText().toString().trim(), firebaseUser.getUid(), new InforUser.IgetInforUser() {
            @Override
            public void userNull(String UserNull) {
                Intent intent = new Intent(MainActivity.this, StartActivity.class);
                startActivity(intent);
            }

            @Override
            public void onSuccess(String Success) {
                username.setText(Success);
            }

            @Override
            public void onFail(String Fail) {
                Toast.makeText(MainActivity.this, Fail, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void imageNull(String Image) {
                profile_image.setImageResource(R.mipmap.no_person);
            }

            @Override
            public void Image(String Image) {
                Glide.with(MainActivity.this).load(Image).into(profile_image);
            }
            @Override
            public void onReadListChat(List<Chat> chatList, String image) {

            }
        });


        final String deviceToken = FirebaseInstanceId.getInstance().getToken();
        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("User").
                child(firebaseUser.getUid());
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    databaseReference1.child("device_token").setValue(deviceToken);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
        viewPagerAdapter.addFragment(new UsersFragment(), "Users");
        viewPagerAdapter.addFragment(new ProfileFragment(), "Profile");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void notification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.googleg_standard_color_18)
                .setContentTitle("textTitle")
                .setContentText("textContent")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    private void notificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "VenNe";
            int importantNotification = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("VenNe", name, importantNotification);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch ((item.getItemId())) {
            case R.id.logout:
                final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("User")
                        .child(firebaseUser.getUid());
                databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            databaseReference1.child("device_token").setValue(null);
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(MainActivity.this, StartActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                return true;
        }
        return false;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }


}
