package com.example.cilentappilication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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


public class login extends AppCompatActivity {
    DatabaseReference databaseref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://cilentapp-b7159-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText panno=findViewById(R.id.userid);
        final EditText pass=findViewById(R.id.pass);
        final Button loginbtn=findViewById(R.id.loginbtn);
        final TextView register=findViewById(R.id.registerbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final  String userid=panno.getText().toString();
                final  String password=pass.getText().toString();

                if(userid.isEmpty() || password.isEmpty())
                    Toast.makeText(login.this, "Both Fields are Mandatory", Toast.LENGTH_SHORT).show();
                else
                {

                    databaseref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(userid)){
                                final  String getpass=snapshot.child(userid).child("pass").getValue(String.class);
                                final  String getreferid=snapshot.child(userid).child("referid").getValue(String.class);
                                if(getpass.equals(password)){
                                    Intent i=new Intent(login.this,MainActivity.class);
                                    i.putExtra("userid",userid);
                                    i.putExtra("referid",getreferid);
                                    startActivity(i);
                                }else{
                                    Toast.makeText(login.this, "Enter Correct Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this,Register.class));
            }
        });
    }
}