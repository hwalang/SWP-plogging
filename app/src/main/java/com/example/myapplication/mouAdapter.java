package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.maps.MapsActivityCurrentPlace;

import java.util.ArrayList;

public class mouAdapter extends RecyclerView.Adapter<mouAdapter.mouAdapterViewHolder> {
    ArrayList<moModel> arrayList;
    Context context;


    public mouAdapter(ArrayList<moModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public mouAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mouitem,parent,false);
        mouAdapterViewHolder holder = new mouAdapterViewHolder(view);
        return holder ;
    }

    @Override
    public void onBindViewHolder(@NonNull mouAdapterViewHolder holder, int position) {

       Glide.with(holder.itemView).load(arrayList.get(position).getImage()).into(holder.mou_img);
        holder.mou_location.setText(arrayList.get(position).getLocation());
        holder.mou_name.setText(arrayList.get(position).getMouname());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("dd","dd");
                Intent intent = new Intent(view.getContext(),MapsActivityCurrentPlace.class);
                intent.putExtra("mouname",arrayList.get(position).getMouname());
                intent.putExtra("location",arrayList.get(position).getLocation());
                intent.putExtra("moulat",arrayList.get(position).getMoulat());
                intent.putExtra("moulong",arrayList.get(position).getMoulong());

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        //삼항 연산자
        return (arrayList != null ? arrayList.size():0);
    }

    public static class mouAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView mou_img;
        TextView mou_location, mou_name;
        CardView cardView;
        public mouAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mou_img = itemView.findViewById(R.id.mou_img);
            mou_location = itemView.findViewById(R.id.mou_location);
            mou_name = itemView.findViewById(R.id.mou_name);
            cardView = itemView.findViewById(R.id.mou_cardView);
        }
    }

}
