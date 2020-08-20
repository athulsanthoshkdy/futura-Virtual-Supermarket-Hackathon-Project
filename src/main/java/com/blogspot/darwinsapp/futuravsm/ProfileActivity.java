package com.blogspot.darwinsapp.trollkoduvally;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        DatabaseReference mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        final FirebaseAuth mauth = FirebaseAuth.getInstance();
        FirebaseUser mCurrentUser = mauth.getCurrentUser();
        final TextView email = findViewById(R.id.Email);
        final TextView username = findViewById(R.id.UserName);
        final TextView phone = findViewById(R.id.Phone);
        final ImageView img = findViewById(R.id.profile);
        String uid = " ";
        if (mCurrentUser == null) {
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        } else
        {
                uid = mCurrentUser.getUid();
            TextView txt = (TextView) findViewById(R.id.logout);
            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mauth.signOut();
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                }
            });
            mDatabaseUsers.child(uid).addValueEventListener(new ValueEventListener() {
                Users user;

                @Override

                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(Users.class);
                    Common.u = user;
                    email.setText(Common.u.getEmail());
                    username.setText(Common.u.getUsername());
                    phone.setText(Common.u.getPhone());
                    Picasso.with(ProfileActivity.this).load(Common.u.getDp()).into(img);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if (Common.u!=null)
                    {
                        Common.position = 201;
                        startActivity(new Intent(ProfileActivity.this, PostActivity.class));
                    }
                }
            });

    }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isInternetConnection()){
            startActivity(new Intent(this,MultipActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isInternetConnection()){
            startActivity(new Intent(this,MultipActivity.class));
        }
    }

    public boolean isInternetConnection()
    {
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }
}

