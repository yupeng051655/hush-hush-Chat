package com.example.hushhushchat;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class Contact extends Fragment {

    private View contactView;
    private ListView list_view;

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_contact = new ArrayList<>();

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    public Contact() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contactView =  inflater.inflate(R.layout.fragment_contact, container, false);
        myRef = FirebaseDatabase.getInstance().getReference().child("User");

        InitializeField();

        retrieveAndDisplayContact();



        return contactView;

    }

    private void InitializeField(){
        list_view = (ListView) contactView.findViewById(R.id.contact_list_view);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_of_contact);
        list_view.setAdapter(arrayAdapter);

    }

    private void retrieveAndDisplayContact(){

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while(iterator.hasNext()){
                    set.add(((DataSnapshot)iterator.next()).child("name").getValue().toString());

                }
                list_of_contact.clear();
                list_of_contact.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
