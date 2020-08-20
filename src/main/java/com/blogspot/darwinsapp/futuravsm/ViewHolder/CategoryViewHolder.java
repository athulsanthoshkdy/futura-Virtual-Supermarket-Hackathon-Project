package com.blogspot.darwinsapp.trollkoduvally.ViewHolder;

import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.darwinsapp.trollkoduvally.Interface.ItemClickListener;
import com.blogspot.darwinsapp.trollkoduvally.R;
import com.blogspot.darwinsapp.trollkoduvally.Common;
import com.blogspot.darwinsapp.trollkoduvally.Common;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView CategoryImage;
    public TextView CategoryText;

    private ItemClickListener itemClickListener;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        CategoryImage=(ImageView)itemView.findViewById(R.id.category_image);
        CategoryText=(TextView)itemView.findViewById(R.id.category_name);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        if (Common.IsClickable) {
            itemClickListener.OnClick(view, getAdapterPosition(), false);
            Common.position = getAdapterPosition();
        }
    }
}
