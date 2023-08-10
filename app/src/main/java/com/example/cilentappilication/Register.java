package com.example.cilentappilication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Register extends AppCompatActivity {
    DatabaseReference databaseref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://cilentapp-b7159-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText panno=findViewById(R.id.userid1);
        final EditText pass=findViewById(R.id.pass1);
        final EditText name=findViewById(R.id.name);
        final EditText caid=findViewById(R.id.caid);

        final Button registerbtn=findViewById(R.id.register);
        final TextView login=findViewById(R.id.loginnow);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final  String userid=panno.getText().toString();
                final  String password=pass.getText().toString();
                final String cilentname=name.getText().toString();
                final String referid=caid.getText().toString();

                if(userid.isEmpty() || password.isEmpty() || cilentname.isEmpty() || referid.isEmpty())
                    Toast.makeText(Register.this, "All Fields are Mandatory", Toast.LENGTH_SHORT).show();

                else{
                    databaseref.child("accounts").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(referid)){
                                databaseref.child("users").child(userid).child("name").setValue(cilentname);
                                databaseref.child("users").child(userid).child("pass").setValue(password);
                                databaseref.child("users").child(userid).child("referid").setValue(referid);
                                Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                finish();

                            }else{
                                Toast.makeText(Register.this, "Enter correct referid", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}