package com.example.hushhushchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {


    private Button updateUserSettings;
    private EditText userName;
    private ImageView userImage;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        myRef = FirebaseDatabase.getInstance().getReference();

        InitializeFields();

        updateUserSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSettings();
            }
        });

        retrieveUserInfo();
    }


    private void InitializeFields(){

        updateUserSettings = (Button) findViewById(R.id.settings_update_button);
        userName = (EditText) findViewById(R.id.settings_user_name);
        userImage = (ImageView) findViewById(R.id.settings_profile_image);

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

    }

}
