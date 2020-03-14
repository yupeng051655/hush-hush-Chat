package com.example.hushhushchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.io.File;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Button updateUserSettings;
    private EditText userName;
    private ImageView userImage;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    private static final int GalleryPick = 1;
    private StorageReference userImagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        myRef = FirebaseDatabase.getInstance().getReference();
        userImagesRef = FirebaseStorage.getInstance().getReference().child("Profile_Image");


        InitializeFields();




        updateUserSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSettings();
            }
        });

        retrieveUserInfo();

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/");
                startActivityForResult(galleryIntent, GalleryPick);

            }
        });


    }


    private void InitializeFields(){

        updateUserSettings = (Button) findViewById(R.id.settings_update_button);
        userName = (EditText) findViewById(R.id.settings_user_name);
        userImage = (ImageView) findViewById(R.id.profile_image);
        mToolbar = (Toolbar) findViewById(R.id.setting_bar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Settings");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data != null){

            Uri imageUri = data.getData();

            final StorageReference filepath = userImagesRef.child(currentUserID + ".jpg");
            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        filepath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {

                                if(task.isSuccessful()){
                                    String downloadURL = task.getResult().toString();
                                    myRef.child("User").child(currentUserID).child("image")
                                            .setValue(downloadURL);

                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void updateSettings(){
        String setUsername = userName.getText().toString();

        if(TextUtils.isEmpty(setUsername)){
            Toast.makeText(this, "please write your username", Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String, String> profileMap = new HashMap<>();
                profileMap.put("uid", currentUserID);
                profileMap.put("name", setUsername);

                myRef.child("User").child(currentUserID).setValue(profileMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    sendUserToMainActivity();
                                    Toast.makeText(SettingsActivity.this, "update successfully", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

            sendUserToMainActivity();

        }
    }

    private void sendUserToMainActivity() {

        Intent mainIntent = new Intent (SettingsActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void retrieveUserInfo(){

        myRef.child("User").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists() && dataSnapshot.hasChild("name") && dataSnapshot.hasChild("image")){
                            String settingUserName = dataSnapshot.child("name").getValue().toString();
                            String imageUrl = dataSnapshot.child("image").getValue().toString();
                            userName.setText(settingUserName);
                            Picasso.get().load(imageUrl).into(userImage);

                        }
                        else {
                            String settingUserName = dataSnapshot.child("name").getValue().toString();
                            userName.setText(settingUserName);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

}
