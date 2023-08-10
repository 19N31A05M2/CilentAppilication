package com.example.cilentappilication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.cilentappilication.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    NavigationBarView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent i=getIntent();
        String str=i.getStringExtra("userid");
        String referid=i.getStringExtra("referid");
        replaceFragment(new Folders_Fragment(),str,referid);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.report);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.consult:
                    replaceFragment(new Accounts_Fragement(),str,referid);
                    break;
                case R.id.report:
                    replaceFragment(new Folders_Fragment(),str,referid);
                    break;
                case R.id.yourca:
                    replaceFragment(new Payment_Fragment(),str,referid);
                    break;
            }
            return true;
        });



    }
    private void replaceFragment(Fragment fragment,String str,String refid){
        Bundle b=new Bundle();
        b.putString("userid",str);
        b.putString("referid",refid);
        fragment.setArguments(b);
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout,fragment);
        ft.commit();
    }
}