package com.example.cilentappilication;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Folders_Fragment extends Fragment {
    DatabaseReference databaseref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://cilentapp-b7159-default-rtdb.firebaseio.com/");
    TextView Name;
    ImageView logout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_folders_, container, false);
        Bundle b=getArguments();
        String userid=b.getString("userid");
        String referid=b.getString("referid");
        Name=view.findViewById(R.id.username);
        logout=view.findViewById(R.id.logout);
        databaseref.child("accounts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(referid)){
                    String url=snapshot.child(referid).child("link").getValue(String.class);
                    WebView web=view.findViewById(R.id.webview);
                    web.loadUrl(url);
                    web.setWebViewClient(new Client());
                    WebSettings ws = web.getSettings();

                    // Enabling javascript
                    ws.setJavaScriptEnabled(true);
                    web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                    web.clearCache(true);
                    web.clearHistory();

                    // download manager is a service that can be used to handle downloads
                    web.setDownloadListener(new DownloadListener() {
                        @Override
                        public void onDownloadStart(String url, String s1, String s2, String s3, long l) {
                            DownloadManager.Request req = new DownloadManager.Request(Uri.parse(url));
                            req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            DownloadManager dm = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                            dm.enqueue(req);
                            Toast.makeText(getActivity(), "Downloading....", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(getActivity(), "Refer id Not Exist", Toast.LENGTH_SHORT).show();
                }
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


        // Inflate the layout for this fragment

        return view;
    }
    private class Client extends WebViewClient {
        // on page started load start loading the url
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        // load the url of our drive
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
            // if stop loading
            try {
                webView.stopLoading();
            } catch (Exception e) {
            }

            if (webView.canGoBack()) {
                webView.goBack();
            }

            // if loaded blank then show error
            // to check internet connection using
            // alert dialog



            super.onReceivedError(webView, errorCode, description, failingUrl);
        }
    }

}