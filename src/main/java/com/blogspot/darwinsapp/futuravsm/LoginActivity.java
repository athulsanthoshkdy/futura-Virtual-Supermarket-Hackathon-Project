package com.blogspot.darwinsapp.trollkoduvally;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail, loginPass;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button loginBtn;
    private TextView notUser,Forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button)findViewById(R.id.loginBtn);
        loginEmail = (EditText)findViewById(R.id.login_email);
        loginPass = (EditText)findViewById(R.id.login_password);
        notUser=(TextView)findViewById(R.id.signUpTxtView);
        Forgot=(TextView)findViewById(R.id.Forgot);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        notUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
        Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                alertDialogBuilder.setTitle("Do yo want to reset your password");
                alertDialogBuilder
                        .setMessage("")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        loginPass.setVisibility(View.INVISIBLE);
                                        loginBtn.setText("Reset");
                                        loginBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                String s =loginEmail.getText().toString().trim();
                                                if (!TextUtils.isEmpty(s)) {
                                                    FirebaseAuth.getInstance().sendPasswordResetEmail(s).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Toast toast = Toast.makeText(getApplicationContext(),"Check your email", Toast.LENGTH_LONG);
                                                                View view = toast.getView();
                                                                view.setBackgroundResource(R.drawable.nice_button_enabled);
                                                                toast.show();
                                                                loginPass.setVisibility(View.VISIBLE);
                                                                loginBtn.setText("LOGIN");
                                                            }
                                                            else {
                                                                Toast toast = Toast.makeText(getApplicationContext(),"email not sent-email not valid", Toast.LENGTH_LONG);
                                                                View view = toast.getView();
                                                                view.setBackgroundResource(R.drawable.nice_button_enabled);
                                                                toast.show();
                                                              //  Toast.makeText(LoginActivity.this, "email not sent-email not valid", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });


                                    }


                                })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),"Processing", Toast.LENGTH_SHORT);
                View v = toast.getView();
                v.setBackgroundResource(R.drawable.nice_button_enabled);
                toast.show();
              //  Toast.makeText(LoginActivity.this, "PROCESSING....", Toast.LENGTH_LONG).show();
                String email = loginEmail.getText().toString().trim();
                String password = loginPass.getText().toString().trim();

                if (!TextUtils.isEmpty(email)&& !TextUtils.isEmpty(password)){

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                checkUserExistence();
                            }else {
                                Toast toast = Toast.makeText(getApplicationContext(),"User not found", Toast.LENGTH_SHORT);
                                View view = toast.getView();
                                view.setBackgroundResource(R.drawable.nice_button_enabled);
                                toast.show();

                            }
                        }
                    });
                }else {
                    Toast toas = Toast.makeText(getApplicationContext(),"Complete all fields", Toast.LENGTH_SHORT);
                    v = toast.getView();
                    v.setBackgroundResource(R.drawable.nice_button_enabled);
                    toas.show();
                  //  Toast.makeText(LoginActivity.this, "Complete all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkUserExistence(){

        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(user_id)){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }else {
                    Toast toast = Toast.makeText(getApplicationContext(),"User not registered", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.setBackgroundResource(R.drawable.nice_button_enabled);
                    toast.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
