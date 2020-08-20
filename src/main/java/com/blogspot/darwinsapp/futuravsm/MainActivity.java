package com.blogspot.darwinsapp.trollkoduvally;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ViewFlipper mViewFlipper;
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Common.flag=false;
        Common.counter=false;
        int logos[]={R.drawable.home,R.drawable.logo,R.drawable.dp,R.drawable.places,R.drawable.share,R.drawable.settings};
        GridView simpleGrid;
        simpleGrid = findViewById(R.id.simpleGridView); // init GridView
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), logos);
        simpleGrid.setAdapter(customAdapter);
        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set an Intent to Another Activity
                //Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                //intent.putExtra("image", logos[position]); // put image data in Intent
                //startActivity(intent); // start Intent
                if (position==0){
                    startActivity(new Intent(MainActivity.this,HomeActivity.class));
                }if (position==2){
                    startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                }if (position==3){
                    startActivity(new Intent(MainActivity.this,PlaceActivity.class));
                }if (position==4){
                    Intent intent2 = new Intent(); intent2.setAction(Intent.ACTION_SEND);
                    intent2.setType("text/plain");
                    intent2.putExtra(Intent.EXTRA_TEXT, "Try our new app  \nhttps://play.google.com/store/apps/details?id=com.blogspot.darwinsapp.trollkoduvally" );
                    startActivity(Intent.createChooser(intent2, "Share via"));
                }if (position==5){
                    startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                }if (position==1){
                    startActivity(new Intent(MainActivity.this,TrollActivity.class));
                }
            }
        });
        final int count=0;
        imageView1=new ImageView(this);
        imageView2=new ImageView(this);
        imageView3=new ImageView(this);
        imageView4=new ImageView(this);
        imageView5=new ImageView(this);
        final TextView text=findViewById(R.id.message);
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        categories=database.getReference().child("flipper");
        categories.addValueEventListener(new ValueEventListener() {
            Flipper f;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                  f=dataSnapshot.getValue(Flipper.class);
                  Common.f=f;
                Picasso.with(MainActivity.this).load(Common.f.getUrl1()).into(imageView1);
                Picasso.with(MainActivity.this).load(Common.f.getUrl2()).into(imageView2);
                Picasso.with(MainActivity.this).load(Common.f.getUrl3()).into(imageView3);
                Picasso.with(MainActivity.this).load(Common.f.getUrl4()).into(imageView4);
                Picasso.with(MainActivity.this).load(Common.f.getUrl5()).into(imageView5);
                if (Common.f!=null) {
                    url1 = Common.f.getUrl1().toString();
                    url2 = Common.f.getUrl2().toString();
                    url3 = Common.f.getUrl3().toString();
                    url4 = Common.f.getUrl4().toString();
                    url5 = Common.f.getUrl5().toString();
                }
                if (Common.f.getUpdate().equals("no")){
                    //Toast.makeText(MainActivity.this,Common.f.getName(),Toast.LENGTH_SHORT).show();
                    text.setText(Common.f.getName());
                }
                else {
                    updateApp();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (Common.f!=null) {
            url1 = Common.f.getUrl1();
            url2 = Common.f.getUrl2();
            url3 = Common.f.getUrl3();
            url4 = Common.f.getUrl4();
            url5 = Common.f.getUrl5();
        }
        mViewFlipper=findViewById(R.id.view_flipper);

        //imageView1.setImageResource(R.mipmap.ic_launcher);
        //Picasso.with(MainActivity.this).load(url1).into(imageView1);
        //Picasso.with(this).load(url2).into(imageView2);
        //Picasso.with(this).load(url3).into(imageView3);
        //Picasso.with(this).load(url4).into(imageView4);
        //Picasso.with(this).load(url5).into(imageView5);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1500); //time in milliseconds

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1500);

        mViewFlipper.addView(imageView1);
        mViewFlipper.addView(imageView2);
        mViewFlipper.addView(imageView3);
        mViewFlipper.addView(imageView4);
        mViewFlipper.addView(imageView5);
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.out_from_right));
        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.in_from_left));
        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(4000);
        mViewFlipper.startFlipping();
    }
    ImageView imageView1,imageView2,imageView3,imageView4,imageView5;
    DatabaseReference categories;
    String url1="https://media1.tenor.com/images/2df94b2cbc32b465b9ec02451f6ffcd9/tenor.gif?itemid=8696160";
    String url2="https://media1.tenor.com/images/2df94b2cbc32b465b9ec02451f6ffcd9/tenor.gif?itemid=8696160";
    String url3="https://media1.tenor.com/images/2df94b2cbc32b465b9ec02451f6ffcd9/tenor.gif?itemid=8696160";
    String url4="https://media1.tenor.com/images/2df94b2cbc32b465b9ec02451f6ffcd9/tenor.gif?itemid=8696160";
    String url5="https://media1.tenor.com/images/2df94b2cbc32b465b9ec02451f6ffcd9/tenor.gif?itemid=8696160";
    public void updateApp(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Please update the app to latest version");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String url = "https://play.google.com/store/apps/details?id=com.blogspot.darwinsapp.trollkoduvally";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                alertDialog.show();
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = touchevent.getX();
                break;
            case MotionEvent.ACTION_UP:
                float finalX = touchevent.getX();
                if (initialX > finalX) {
                    if (mViewFlipper.getDisplayedChild() == 1)
                        break;

                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.in_from_right));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.out_from_left));

                    mViewFlipper.showNext();
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.out_from_left));
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.in_from_right));
                } else {
                    if (mViewFlipper.getDisplayedChild() == 0)
                        break;

                   mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.in_from_left));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.out_from_right));

                    mViewFlipper.showPrevious();
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.out_from_left));
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.in_from_right));
                }
                break;
        }
        return false;
    }
    private float initialX;
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
