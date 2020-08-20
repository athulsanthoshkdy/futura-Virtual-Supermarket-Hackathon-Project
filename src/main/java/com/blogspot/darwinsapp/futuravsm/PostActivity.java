package com.blogspot.darwinsapp.trollkoduvally;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PostActivity extends AppCompatActivity {
    private ImageButton imageBtn;
    private static final int GALLERY_REQUEST_CODE=2;
    private Uri uri=null;
    private FirebaseAuth mauth;
    private EditText textDesc;
    private Button postBtn;
    private StorageReference mStorageRef;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef,mDatabaseUsers;
    private FirebaseUser mCurrentUser;
    private int check=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        imageBtn=(ImageButton)findViewById(R.id.imageBtn);

        textDesc=(EditText)findViewById(R.id.textDesc);
        postBtn=(Button)findViewById(R.id.postBtn);
        String str="home";
        if (Common.position==101){
            str="trolls";
            Common.position=0;
          }
          if (Common.position==201){
            textDesc.setVisibility(View.INVISIBLE);
            str="dp";
          }

        mStorageRef = FirebaseStorage.getInstance().getReference();
        databaseRef=database.getInstance().getReference().child(str);
        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        mauth=FirebaseAuth.getInstance();
        mCurrentUser=mauth.getCurrentUser();

        if (str.equals("result")||str.equals("schedule")){
            imageBtn.setVisibility(View.INVISIBLE);
            check=1;
        }

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_REQUEST_CODE);
            }
        });
        if (str.equals("result")||str.equals("schedule"))
        {
            postBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   final String PostDec=textDesc.getText().toString().trim();
                     {

                                Toast.makeText(getApplicationContext(),"Succesfull upload",Toast.LENGTH_SHORT).show();
                                final DatabaseReference newPost = databaseRef.push();

                                mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                     //                   newPost.child("title").setValue(PostTitle);
                                        newPost.child("desc").setValue(PostDec);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                    }
            });
        }
        else {
            final String finalStr = str;
            final String finalStr1 = str;
            postBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   Calendar c = Calendar.getInstance();
                    SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
                    if (Common.position==201){
                        textDesc.setText("DP");
                    }
                    final String formattedDate2 = df2.format(c.getTime());
                    final String PostDec = textDesc.getText().toString().trim();
                    if (check == 0) {
                        Toast.makeText(PostActivity.this, "Image not selected for upload", Toast.LENGTH_SHORT).show();
                    }
                    if (TextUtils.isEmpty(PostDec)) {
                        Toast.makeText(PostActivity.this, "text field empty", Toast.LENGTH_SHORT).show();
                    }
                    if (TextUtils.isEmpty(formattedDate2)) {
                        Toast.makeText(PostActivity.this, "text field empty", Toast.LENGTH_SHORT).show();
                    }
                    if (!TextUtils.isEmpty(PostDec) && (!TextUtils.isEmpty(formattedDate2)) && check == 1) {
                       // Toast.makeText(PostActivity.this, "posting......", Toast.LENGTH_SHORT).show();
                        final ProgressDialog progressDialog = new ProgressDialog(PostActivity.this);
                        progressDialog.setMax(100); // Progress Dialog Max Value
                        progressDialog.setMessage("Please wait while uploading"); // Setting Message
                        progressDialog.setTitle("Uploading"); // Setting Title
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // Progress Dialog Style Horizontal
                        progressDialog.show(); // Display Progress Dialog
                        progressDialog.setCancelable(false);
                        String s = finalStr + "_post_images";
                        StorageReference filepath = mStorageRef.child(s).child(uri.getLastPathSegment());
                        filepath.putFile(uri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                int currentProgress = (int) progress;
                              //  progressBar.setProgress(currentProgress);
                                progressDialog.setProgress(currentProgress);
                            }
                        });
                        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //getting the post image download url 
                                @SuppressWarnings("VisibleForTests") final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                Toast.makeText(getApplicationContext(), "Succesfully uploaded", Toast.LENGTH_SHORT).show();
                                final DatabaseReference newPost = databaseRef.push();
                                if (downloadUrl == null) {
                                    Toast.makeText(getApplicationContext(), "Unsuccesfull image upload", Toast.LENGTH_SHORT).show();
                                }if (Common.position==201) {
                                    mDatabaseUsers.child(mCurrentUser.getUid()).child(finalStr1).setValue(downloadUrl.toString());
                                    Common.position=0;
                                }
                                mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        newPost.child("title").setValue(formattedDate2);
                                        newPost.child("desc").setValue(PostDec);

                                        //assert downloadUrl != null;
                                        newPost.child("imageUrl").setValue(downloadUrl.toString());
//                                        newPost.child("uid").setValue(mCurrentUser.getUid());
                                         {
                                            String uid = mCurrentUser.getUid();
                                            newPost.child("email").setValue(mCurrentUser.getEmail());
                                            newPost.child("username").setValue(dataSnapshot.child(uid).child("Username").getValue()).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                            newPost.child("username").setValue(dataSnapshot.child(uid).child("Username").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {

                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    finish();
                                                    //Intent intent =new Intent(PostActivity.this,MainActivity.class);
                                                    //   startActivity(intent);
                                                }
                                            });

                                        }
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        });
                    }
                }
            });
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //image from gallery result
        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            uri = data.getData();
            imageBtn.setImageURI(uri);
            check=1;
        }
    }

}
