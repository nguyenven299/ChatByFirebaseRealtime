package com.example.chatwithfirebase.View.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.chatwithfirebase.Algorithm.Services.IServices;
import com.example.chatwithfirebase.Algorithm.Services.ServicesImpl;
import com.example.chatwithfirebase.Model.TripleDesModel;
import com.example.chatwithfirebase.Model.UploadImage;
import com.example.chatwithfirebase.Model.User;
import com.example.chatwithfirebase.R;
import com.example.chatwithfirebase.View.ResetPasswordActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
//import static com.example.chatwithfirebase.Algorithm.DES.TrippleDes._encrypt;
//import static com.example.chatwithfirebase.Algorithm.DES.TrippleDes.key;


public class ProfileFragment extends Fragment {
    private IServices iServices = new ServicesImpl();
    private Uri uri;
    private Bitmap selectBitmap;
    private Dialog dialog;
    private CircleImageView image_profile;
    private TextView username;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private StorageTask storageTask;
    private ImageView buttonChangePass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        image_profile = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.username);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        buttonChangePass = view.findViewById(R.id.buttonChangePass);
        buttonChangePass.setImageResource(R.mipmap.password_icon);
        buttonChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ResetPasswordActivity.class);
                getActivity().startActivity(intent);
            }
        });
        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChosse();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                try {
                    username.setText(iServices._decryptTripleDes(user.getUsername()));
//                    username.setText(user.getUsername());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (iServices._decryptTripleDes(user.getImageURL()).equals("default")) {
                        image_profile.setImageResource(R.mipmap.no_person);
                    } else {
                        Glide.with(getContext()).load(iServices._decryptTripleDes(user.getImageURL())).into(image_profile);
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Loi ProfileFragment.java getImageURL ::: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return view;
    }

    private void dialogChosse() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Chon Che Do Hinh");
        builder.setMessage("Ban Muon Dang Hinh Tu Thu Vien Hay May Anh?");
        builder.setCancelable(false);
        builder.setPositiveButton("Thu Vien", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                thuVien();
            }
        });
        builder.setNegativeButton("May Anh", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mayAnh();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void mayAnh() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
//            File f = new File(android.os.Environment.getExternalStorageDirectory(), image_profile.toString()+".jpg");
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            startActivityForResult(intent, 100);
        } else {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void thuVien() {
        Intent intent = new Intent();
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        String[] mimeType = {"image/jpeg", "image/png", "image/jpg"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType);
        startActivityForResult(intent, 200);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //xử lý hình ảnh chụp trực tiếp trên app
        if (requestCode == 100 && resultCode == RESULT_OK) {
//            File f = new File(Environment.getExternalStorageDirectory().toString());
            selectBitmap = (Bitmap) data.getExtras().get("data");
//            uri = data.getData();
            File f = new File(Environment.getExternalStorageDirectory().toString());
            uri = uri.fromFile(f);
            Glide.with(this).asBitmap().load(uri).into(new SimpleTarget<Bitmap>(200, 200) {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    storageReference = FirebaseStorage.getInstance().getReference("UploadsImage");
                    resource = Bitmap.createScaledBitmap(resource, (int) (resource.getWidth() * 0.8), (int) (resource.getHeight() * 0.8), true);
                    image_profile.setImageBitmap(resource);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                }
            });
        } else if (requestCode == 200 && resultCode == RESULT_OK) {
            uri = data.getData();
            Glide.with(this).asBitmap().load(uri).into(new SimpleTarget<Bitmap>(200, 200) {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    UploadImage();
                    resource = Bitmap.createScaledBitmap(resource, (int) (resource.getWidth() * 0.8), (int) (resource.getHeight() * 0.8), true);
                    image_profile.setImageBitmap(resource);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getFileExtention(Uri uri1) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri1));
    }

    private void UploadImage() {

        if (uri != null) {
            storageReference = FirebaseStorage.getInstance().getReference("UploadsImage");
            final StorageReference storageReference1 = storageReference.child(firebaseUser.getUid() + "." + getFileExtention(uri));
            storageTask = storageReference1.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(), "Change avatar image successfully!", Toast.LENGTH_SHORT).show();
                            UploadImage uploadImage = new UploadImage(uri.toString().trim(), taskSnapshot.getUploadSessionUri().toString());
//                            String uploadId = databaseReference.push().getKey();
//                            databaseReference.child(uploadId).setValue(uploadImage);
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Log.d("duongdan", "onSuccess: " + urlTask.getResult());
                            String urlImage = urlTask.getResult().toString();
                            Log.d("duongdan1", "onSuccess: " + urlImage);
                            try {
//                                String encryptUrlImage = _encrypt(urlImage, key);
                                String encryptUrlImage = iServices._encryptTripleDes(urlImage);
                                String uid = firebaseUser.getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("User");
                                databaseReference.child(uid).child("imageURL").setValue(encryptUrlImage);
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "Loi ProfileFragment.java : encrypt url image ::: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Loi cmnr", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "Khong file upload", Toast.LENGTH_SHORT).show();
        }
    }

}
