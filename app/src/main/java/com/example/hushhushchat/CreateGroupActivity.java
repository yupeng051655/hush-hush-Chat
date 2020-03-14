package com.example.hushhushchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public class CreateGroupActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Button updateGroupSettings;
    private EditText groupName;
    private ImageView groupImage;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    private static final int GalleryPick = 1;
    private StorageReference userImagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
    }


    public void InitializeFields() {

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Group");
    }
}
