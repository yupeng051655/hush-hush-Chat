package com.example.hushhushchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class contactChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton sendMessageButton;
    private EditText userMessage;
    private ScrollView mScrollView;
    private TextView displayMessage;
    private TextView displayBlank;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private DatabaseReference contactRefKey;
    private DatabaseReference contactRef;
    private String currentUserID, currentUserName;
    private String contactUserID, contactUserName;
    private String currentDate, currentTime;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private RecyclerView userMsgList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_chat);

        contactUserName = getIntent().getExtras().get("contactName").toString();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("User");

        sendMessageButton = (ImageButton) findViewById(R.id.contact_send_message_button);
        userMessage = (EditText) findViewById(R.id.input_contact_message);
        //displayMessage = (TextView) findViewById(R.id.contact_text_view_display);
        //displayBlank = (TextView) findViewById(R.id.contact_blank_view_display);
        //mScrollView = (ScrollView) findViewById(R.id.contact_srcoll_view);

        getContactInfo();
        getUserInfo();
        getContactSpace();


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMegToDatabase();

                userMessage.setText("");

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("Contact")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(currentUserID + "" + contactUserID)) {
                            // The child do exist
                            FirebaseDatabase.getInstance().getReference().child("Contact")
                                    .child(currentUserID + "" + contactUserID).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    if(dataSnapshot.exists()){
                                        displayMsg(dataSnapshot);

                                    }

                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    if(dataSnapshot.exists()){
                                        displayMsg(dataSnapshot);

                                    }


                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                        else if(dataSnapshot.hasChild(contactUserID + "" + currentUserID)){

                            FirebaseDatabase.getInstance().getReference().child("Contact")
                                    .child(contactUserID + "" + currentUserID).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    if(dataSnapshot.exists()){
                                        displayMsg(dataSnapshot);

                                    }

                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    if(dataSnapshot.exists()){
                                        displayMsg(dataSnapshot);

                                    }


                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }



    private void getUserInfo() {

        usersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    currentUserName = dataSnapshot.child("name").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void getContactSpace() {
        FirebaseDatabase.getInstance().getReference().child("Contact")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(currentUserID + "" + contactUserID)) {
                            // The child do exist
                            contactRef = FirebaseDatabase.getInstance().getReference().child("Contact")
                                    .child(currentUserID + "" + contactUserID);

                        }
                        else if(dataSnapshot.hasChild(contactUserID + "" + currentUserID)){
                            contactRef = FirebaseDatabase.getInstance().getReference().child("Contact")
                                    .child(contactUserID + "" + currentUserID);

                        }
                        else{
                            FirebaseDatabase.getInstance().getReference().child("Contact")
                                    .child(currentUserID + "" + contactUserID)
                                    .setValue("");

                            contactRef = FirebaseDatabase.getInstance().getReference().child("Contact")
                                    .child(currentUserID + "" + contactUserID);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


    private void InitializeField(){

        mToolbar = (Toolbar) findViewById(R.id.contact_chat_bar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(contactUserName);

        messageAdapter = new MessageAdapter(messagesList);
        userMsgList = (RecyclerView)findViewById(R.id.contact_msg_recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        userMsgList.setLayoutManager(linearLayoutManager);
        userMsgList.setAdapter(messageAdapter);


    }


    private void getContactInfo(){

        usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Iterator iterator = dataSnapshot.getChildren().iterator();
                while(iterator.hasNext()){
                    String tmpName = (String) ((DataSnapshot)iterator.next()).getValue();
                    String tmpId = (String) ((DataSnapshot)iterator.next()).getValue();
                    if(tmpName.equals(contactUserName)){
                        contactUserID = tmpId;
                    }
                }
                InitializeField();


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }

    private void sendMegToDatabase(){

        String message = userMessage.getText().toString();
        String msgKey = contactRef.push().getKey();

        if(TextUtils.isEmpty(message)){
            Toast.makeText(this, "message empty...", Toast.LENGTH_SHORT).show();
        }
        else{
            Calendar date = Calendar.getInstance();
            SimpleDateFormat currentDataFormat = new SimpleDateFormat("MMM dd, yyyy");
            currentDate = currentDataFormat.format(date.getTime());

            Calendar time = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm");
            currentTime = currentTimeFormat.format(time.getTime());

            HashMap<String, Object> contactMessageKey = new HashMap<>();

            contactRef.child(contactUserID).updateChildren(contactMessageKey);
            contactRefKey = contactRef.child(msgKey);

            HashMap<String, Object> messageInfoMap = new HashMap<>();

            messageInfoMap.put("name", currentUserName);
            messageInfoMap.put("message", message);
            messageInfoMap.put("date", currentDate);
            messageInfoMap.put("time", currentTime);

            contactRefKey.updateChildren(messageInfoMap);
        }

    }


    private void displayMsg(DataSnapshot dataSnapshot){

        Iterator iterator = dataSnapshot.getChildren().iterator();

        while(iterator.hasNext()){

            String chatDate = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatMsg = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot)iterator.next()).getValue();

            //Messages messages = dataSnapshot.getValue(Messages.class);
            Messages messages = new Messages(chatDate, chatMsg, chatName, chatTime);
            messagesList.add(messages);
            messageAdapter.notifyDataSetChanged();

            //displayMessage.append(chatName + ":\n" + chatMsg + "\n" + chatTime + " " + chatDate + "\n");

        }


    }








}
