package com.example.cilentappilication;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Payment_Fragment extends Fragment {

    DatabaseReference databaseref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cilentapp-b7159-default-rtdb.firebaseio.com/");
    Button pay;
    ImageView logout;
    TextView Name,Amount,upiid,Username,Firm_name;
    Button reqbtn,sendmsgbtn;
    ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

//                    Amount.setText("No Due");
                    Toast.makeText(getActivity(),"On Actual Result",Toast.LENGTH_SHORT);
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_, container, false);
        Name = view.findViewById(R.id.name);
        Amount=view.findViewById(R.id.amount);
        pay=view.findViewById(R.id.paynow);
        upiid=view.findViewById(R.id.upiid);
        Username=view.findViewById(R.id.username);
        Firm_name=view.findViewById(R.id.firmname);
        logout=view.findViewById(R.id.logout);
        reqbtn=view.findViewById(R.id.reqbtn);
        sendmsgbtn=view.findViewById(R.id.sendmsgbtn);
        Integer payment = 0;
        Bundle b = getArguments();
        String userid = b.getString("userid");
        String referid = b.getString("referid");
        databaseref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userid)) {
                    final String getname = snapshot.child(referid).child("name").getValue(String.class);
                    Username.setText(getname);
//                    Toast.makeText(getActivity(), amount , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseref.child("accounts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userid)) {
                    final String firm = snapshot.child(userid).child("firm_name").getValue(String.class);

                    final String getname = snapshot.child(userid).child("name").getValue(String.class);
                    final String amount = snapshot.child(userid).child("amount").getValue(String.class);
                    final String upi_id = snapshot.child(userid).child("upiid").getValue(String.class);
                    Name.setText(getname);
                    Firm_name.setText(firm);
                    Amount.setText(amount);
                    upiid.setText(upi_id);
//                    Toast.makeText(getActivity(), amount , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String upi=upiid.getText().toString().trim();
                String Name_=Name.getText().toString().trim();
                String Amount_=Amount.getText().toString().trim();
                makePayment(Name_,Amount_,upi);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),login.class);
                startActivity(i);
            }
        });
        reqbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Appointment Request Sent", Toast.LENGTH_SHORT).show();
            }
        });
        sendmsgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCustomDialog dialog=new MyCustomDialog();
                dialog.show(getFragmentManager(),"myCustomDialog");
            }
        });
        return view;
    }
//
    private  void makePayment(String name,String Amount,String upi){
        Uri uri=Uri.parse("upi://pay").buildUpon().appendQueryParameter("pa",upi)
                .appendQueryParameter("pn",name)
                .appendQueryParameter("tn","No Thanks")
                .appendQueryParameter("am",Amount+".00")
                .appendQueryParameter("cu","INR").build();
        Intent upi_payment=new Intent(Intent.ACTION_VIEW);
        upi_payment.setData(uri);
        Intent chooser=Intent.createChooser(upi_payment,"Pay With");

        activityResultLauncher.launch(chooser);
    }
}