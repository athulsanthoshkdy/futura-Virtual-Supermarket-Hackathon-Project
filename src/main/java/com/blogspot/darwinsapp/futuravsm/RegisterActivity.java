package com.blogspot.darwinsapp.trollkoduvally;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.google.android.gms.internal.zzbfq.NULL;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {

    private Button registerBtn;
    private EditText emailField, usernameField, passwordField;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView loginTxtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginTxtView = (TextView)findViewById(R.id.loginTxtView);
        registerBtn = (Button)findViewById(R.id.registerBtn);
        emailField = (EditText)findViewById(R.id.emailField);

        usernameField = (EditText)findViewById(R.id.usernameField);
        passwordField = (EditText)findViewById(R.id.passwordField);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        loginTxtView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
        registerBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username = usernameField.getText().toString().trim();
                final String email = emailField.getText().toString().trim();
                final String password = passwordField.getText().toString().trim();

                int size=password.length();
                if (size<8){
                    Toast.makeText(RegisterActivity.this, "Password : Minimum 8 characters", Toast.LENGTH_LONG).show();
                }
                int Flag=0;
                String pattern="[a-z0-9]+@[a-z]+\\.+[a-z]+";
                if (email.matches(pattern))
                {
                    Flag=0;
                }
                else {
                    Flag=1;
                    Toast.makeText(RegisterActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                }


                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)&&size>=8&&(Flag==0)){
                    Toast.makeText(RegisterActivity.this, "LOADING...", Toast.LENGTH_LONG).show();
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "user exists", Toast.LENGTH_LONG).show();
                                Intent regIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(regIntent);
                            }
                        }
                    });
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            String user_id = mAuth.getCurrentUser().getUid();

                            if (user_id.equals(NULL)){
                                Toast.makeText(RegisterActivity.this, "invalid email", Toast.LENGTH_SHORT).show();
                            }
                            else{
                            DatabaseReference current_user_db = mDatabase.child(user_id);
                            current_user_db.child("Username").setValue(username);
                            current_user_db.child("email").setValue(mAuth.getCurrentUser().getEmail());
                            current_user_db.child("dp").setValue("https://firebasestorage.googleapis.com/v0/b/notepad-28f5f.appspot.com/o/download.jpg?alt=media&token=b5095dfb-5078-49d7-9f42-1bbe9f1f2456");
                                current_user_db.child("phone").setValue("not set");

                                Toast.makeText(RegisterActivity.this, "Registeration Succesful", Toast.LENGTH_SHORT).show();
                            Intent regIntent = new Intent(RegisterActivity.this, HomeActivity.class);
                            regIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(regIntent);
                            finish();
                            }
                        }
                    });
                }else {

                    Toast.makeText(RegisterActivity.this, "Complete all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }
}

