package com.blogspot.darwinsapp.trollkoduvally;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference mdatabase;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthlistener;
    private String current_user="";
    private String post_user;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);



        setTitle(getString(R.string.app_name));

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        // Toast.makeText(getApplicationContext(),post_user,Toast.LENGTH_SHORT).show();
        // Toast toast = Toast.makeText(getApplicationContext(),"Click around your image to DELETE", Toast.LENGTH_LONG);
        // View view = toast.getView();
        // view.setBackgroundResource(R.drawable.nice_button_enabled);
        // toast.show();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        mdatabase = database.getReference().child("User");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();


        if (mCurrentUser != null) {
           current_user= mCurrentUser.getEmail();
        }
        mAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(HomeActivity.this, RegisterActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };


        mdatabase = FirebaseDatabase.getInstance().getReference().child("home");


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isInternetConnection()){
            startActivity(new Intent(this,MultipActivity.class));
        }
        mAuth.addAuthStateListener(mAuthlistener);
        FirebaseRecyclerAdapter<Blog, HomeActivity.BlogzoneViewHolder> FBRA = new FirebaseRecyclerAdapter<Blog, HomeActivity.BlogzoneViewHolder>(
                Blog.class,
                R.layout.activity_corner_items1,

                HomeActivity.BlogzoneViewHolder.class,
                mdatabase
        ) {
            @Override
            protected void populateViewHolder(final HomeActivity.BlogzoneViewHolder viewHolder, final Blog model,final int position) {
                final String post_key = getRef(position).getKey();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                final String str=model.getDesc();
                //Config.videoId=model.getTitle();
                viewHolder.setImageUrl(HomeActivity.this, model.getImageUrl());
                viewHolder.setUserName(model.getUsername());
                viewHolder.imageView=(ScaleImageView)viewHolder.mView.findViewById(R.id.post_my_image);
                viewHolder.share=(Button)viewHolder.mView.findViewById(R.id.shr);
                viewHolder.delete=(Button)viewHolder.mView.findViewById(R.id.dlt);
                viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (str.equals("video")) {
                            //startActivity(new Intent(HomeActivity.this, VideoActivity.class));
                        }
                    }
                });
                post_user=model.getEmail();
                if (!post_user.equals(current_user)){
                    viewHolder.delete.setVisibility(View.INVISIBLE);
                }

                viewHolder.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DownloadImage().execute(model.getImageUrl());

                        File file            = getApplicationContext().getFileStreamPath("my_image.jpeg");
                        String imageFullPath = file.getAbsolutePath();
                        //Toast.makeText(CevCornerActivity.this,imageFullPath,Toast.LENGTH_SHORT).show();
                        Intent imageIntent = new Intent(Intent.ACTION_SEND);
                        Uri imageUri = Uri.parse(imageFullPath);
                        imageIntent.setType("image/jpeg");
                        imageIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                        startActivity(imageIntent);
                    }
                });
                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post_user=model.getEmail();

                        //  String user = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.post_user)).getText().toString();
                        // Toast.makeText(getApplicationContext(),post_user,Toast.LENGTH_SHORT).show();
                        if (post_user.equals(current_user)){
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);

                            // Setting Dialog Title
                            alertDialog.setTitle("DELETE");

                            // Setting Dialog Message
                            alertDialog.setMessage("Are you sure want to delete this post ?");

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.drawable.delete);

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int which) {
                                    mdatabase.child(post_key).removeValue();
                                    // Write your code here to invoke YES event
                                    Toast toast = Toast.makeText(getApplicationContext(),"Post deleted", Toast.LENGTH_SHORT);
                                    View view = toast.getView();
                                    view.setBackgroundResource(R.drawable.nice_button_enabled);
                                    toast.show();
                                }
                            });

                            // Setting Negative "NO" Button
                            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,	int which) {
                                    // Write your code here to invoke NO event
                                    //  Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();

                        }


                    }
                });


            }
        };
        recyclerView.setAdapter(FBRA);

    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    public void PostMyPhoto(View view)
    {

        Intent in = new Intent(HomeActivity.this, PostActivity.class);
        in.putExtra("name", "cevcorner");
        startActivity(in);
    }


    public static class BlogzoneViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Button share,delete;
        ScaleImageView imageView;
        public BlogzoneViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }


        public void setTitle(String title) {
            TextView post_title = mView.findViewById(R.id.event_name);
            post_title.setText(title);
            //post_title.setText();
        }

        public void setDesc(String desc) {
            TextView post_desc = mView.findViewById(R.id.result_date_time);
            post_desc.setText(desc);
        }

        public void setImageUrl(Context ctx, String imageUrl) {
            ImageView post_image = mView.findViewById(R.id.post_my_image);
            Glide.with(ctx).load(imageUrl).into(post_image);
        }

        public void setUserName(String userName) {
            TextView postUserName = mView.findViewById(R.id.post_user);
            postUserName.setText(userName);
        }
    }
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        private String TAG = "DownloadImage";
        private Bitmap downloadImageBitmap(String sUrl) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(sUrl).openStream();   // Download Image from URL
                bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
                inputStream.close();
            } catch (Exception e) {
                Log.d(TAG, "Exception 1, Something went wrong!");
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImageBitmap(params[0]);
        }

        protected void onPostExecute(Bitmap result) {
            //Toast.makeText(HomeActivity.this,"downloaded",Toast.LENGTH_SHORT).show();
            saveImage(getApplicationContext(), result, "my_image.jpeg");
        }
    }
    public void saveImage(Context context, Bitmap b, String imageName) {
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_WORLD_READABLE);
            b.compress(Bitmap.CompressFormat.JPEG, 100, foStream);
            foStream.close();
            //Toast.makeText(HomeActivity.this,"saved",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 2, Something went wrong!");
            e.printStackTrace();
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

