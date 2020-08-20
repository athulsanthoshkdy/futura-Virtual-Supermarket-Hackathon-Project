package com.blogspot.darwinsapp.trollkoduvally;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.blogspot.darwinsapp.trollkoduvally.Interface.ItemClickListener;
import com.blogspot.darwinsapp.trollkoduvally.ViewHolder.CategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Semaphore;

public class PlaceActivity extends AppCompatActivity {
    RecyclerView ListCtegory;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category,CategoryViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference categories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        Common.IsClickable=true;
        database=FirebaseDatabase.getInstance();
        categories=database.getReference("Places");

        ListCtegory=(RecyclerView)findViewById(R.id.category_list);
        ListCtegory.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        ListCtegory.setLayoutManager(layoutManager);
        loadCategories();
    }
    private void loadCategories() {
        if (Common.flag){
            Common.counter=true;
            categories=database.getReference("Subplaces").child(Common.categoryId);
            Common.flag=false;
        }
        adapter=new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(Category.class,R.layout.category_layout,CategoryViewHolder.class,categories) {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, Category model, int position) {
                Picasso.with(PlaceActivity.this).load(model.getImage()).into(viewHolder.CategoryImage);
                viewHolder.CategoryText.setText(model.getName());
                //common.position=position;
                Common.im.add(model.getImage());
                Common.prevCategory=Common.categoryId;
                if(!model.getName().equals(" ")) {
                    viewHolder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void OnClick(View view, int position, boolean isLongClick) {
                            Common.categoryId = adapter.getRef(position).getKey();
                            Common.flag = true;
                            loadCategories();
                        }
                    });
                }
                else{
                    Common.IsClickable=false;
                }
            }
        };
        adapter.notifyDataSetChanged();
        ListCtegory.setAdapter(adapter);
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
