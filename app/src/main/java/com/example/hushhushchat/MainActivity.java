package com.example.hushhushchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.solver.widgets.ConstraintTableLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsAccessAdaptor myTabsAccessAdaptor;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference();

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Hush-Hush-Chat");

        myViewPager = (ViewPager) findViewById(R.id.main_tabs_pages);
        myTabsAccessAdaptor = new TabsAccessAdaptor(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessAdaptor);

        myTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);
    }


    @Override
    protected void onStart(){
        super.onStart();

        if(currentUser == null){
            sendUserToLoginActivity();
        }

    }

    private void sendUserToLoginActivity() {

        Intent loginIntent = new Intent (MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.log_out_option){
            mAuth.signOut();
            sendUserToLoginActivity();

        }
        if(item.getItemId() == R.id.setting_option){
            sendUserToSettingActivity();
        }
        if(item.getItemId() == R.id.create_group_option){
            //requestNewGroup();
            sendUserToGroupSettingActivity();
        }
        if(item.getItemId() == R.id.find_friends_option){
            sendUserToFindFriendActivity();
        }
        return true;

    }

    private void requestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
        builder.setTitle("Enter Group Name: ");

        final EditText groupNameField = new EditText(MainActivity.this);
        groupNameField.setHint("e.g COMP3004");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = groupNameField.getText().toString();

                if(TextUtils.isEmpty(groupName)){

                    Toast.makeText(MainActivity.this, "group name empty...", Toast.LENGTH_SHORT).show();

                }
                else{
                    createNewGroup(groupName);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void sendUserToSettingActivity() {

        Intent settingsIntent = new Intent (MainActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);

    }

    private void sendUserToGroupSettingActivity() {
        Intent groupSettingIntent = new Intent (MainActivity.this, CreateGroupActivity.class);
        startActivity(groupSettingIntent);
    }

    private void createNewGroup(String groupName){

        myRef.child("Group").child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "create successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void verifyUser(){
        String currentUserID = mAuth.getCurrentUser().getUid();


    }

    private void sendUserToFindFriendActivity(){
        Intent findFriendIntent = new Intent(MainActivity.this, findFriendActivity.class);
        startActivity(findFriendIntent);
    }
}
