package com.example.cilentappilication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Accounts_Fragement extends Fragment {
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    DatabaseReference databaseref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cilentapp-b7159-default-rtdb.firebaseio.com/");
    TextView Name;
    ImageView logout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_accounts__fragement,container,false);
//        Toast.makeText(getActivity(), "Login Succesful", Toast.LENGTH_SHORT).show();

        Bundle b = getArguments();
        String userid = b.getString("userid");
        String referid = b.getString("referid");
        listView = view.findViewById(R.id.listview);
        list=new ArrayList<String>();
        Name=view.findViewById(R.id.username);
        logout=view.findViewById(R.id.logout);
        adapter=new ArrayAdapter<String>(getActivity(),R.layout.user_info,R.id.info,list);
        databaseref.child("caaccounts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    String name=ds.child("name").getValue().toString();

                    String firm=ds.child("firm_name").getValue().toString();

                    String fee=ds.child("fee").getValue().toString();

                    String designation=ds.child("designation").getValue().toString();

                    list.add( name + "\t-" +designation+"\n" +firm+"\n"+"RS:"+fee+"/-");


                }
                listView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userid)) {
                    final String getname = snapshot.child(referid).child("name").getValue(String.class);
                    Name.setText(getname);
//                    Toast.makeText(getActivity(), amount , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),login.class);
                startActivity(i);
            }
        });
        return view;
    }
}