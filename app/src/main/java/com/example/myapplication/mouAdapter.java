package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class mouAdapter extends RecyclerView.Adapter<mouAdapter.mouAdapterViewHolder> {
    private ArrayList<mou> arrayList;
    private Context context;

    public mouAdapter(ArrayList<mou> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public mouAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mouitem,parent,false);
        mouAdapterViewHolder holder = new mouAdapterViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull mouAdapterViewHolder holder, int position) {
        Glide.with(holder.itemView).load(arrayList.get(position).getMou_img()).into(holder.mou_img);
        holder.mou_location.setText(arrayList.get(position).getMou_location());
        holder.mou_name.setText(arrayList.get(position).getMou_name());
    }

    @Override
    public int getItemCount() {
        //삼항 연산자
        return (arrayList != null ? arrayList.size():0);
    }

    public class mouAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView mou_img;
        TextView mou_location, mou_name;
        public mouAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mou_img = itemView.findViewById(R.id.mou_img);
            this.mou_location = itemView.findViewById(R.id.mou_location);
            this.mou_name = itemView.findViewById(R.id.mou_name);

        }
    }
}
