package com.example.hushhushchat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> userMessageList;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private String msgSendId;
    private String msgSendName;


    public MessageAdapter(List<Messages> userMessageList) {
        this.userMessageList = userMessageList;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView senderMsgText, receiverMsgText;
        public ImageView receiveMsgImage, senderMsgImage;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMsgImage = (ImageView) itemView.findViewById(R.id.sender_profile_image);
            receiveMsgImage = (ImageView) itemView.findViewById(R.id.receiver_profile_image);
            senderMsgText = (TextView) itemView.findViewById(R.id.sender_msg_text);
            receiverMsgText = (TextView) itemView.findViewById(R.id.receiver_msg_text);
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.msg_layout, parent, false);

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("User");

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        msgSendId = mAuth.getCurrentUser().getUid();
        Messages messages = userMessageList.get(position);

        getUserInfo();
        //String fromUserName = messages.getName();

        String fromUserName = messages.getName();


        if(fromUserName.equals(msgSendName)){
            holder.senderMsgImage.setVisibility(View.VISIBLE);
            holder.receiveMsgImage.setVisibility(View.INVISIBLE);
            holder.senderMsgText.setVisibility(View.VISIBLE);
            holder.receiverMsgText.setVisibility(View.INVISIBLE);
            holder.senderMsgText.setBackgroundResource(R.drawable.sender_msg_layout);
            holder.senderMsgText.setText(messages.getMsg());
        }
        else{
            holder.senderMsgImage.setVisibility(View.INVISIBLE);
            holder.receiveMsgImage.setVisibility(View.VISIBLE);
            holder.senderMsgText.setVisibility(View.INVISIBLE);
            holder.receiverMsgText.setVisibility(View.VISIBLE);
            holder.receiverMsgText.setBackgroundResource(R.drawable.receiver_msg_layout);
            holder.receiverMsgText.setText(messages.getMsg());
        }


    }

    private void getUserInfo(){
        userRef.child(msgSendId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    msgSendName = dataSnapshot.child("name").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return userMessageList.size();
    }

}
