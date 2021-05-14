package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class parkAdapter extends RecyclerView.Adapter<parkAdapter.parkViewHolder> {
    ArrayList<parkModel> arrayList;
    Context context;
    Button down;
    public parkAdapter(ArrayList<parkModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public parkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.parkitem,parent,false);
        parkViewHolder holder = new parkViewHolder(view);
        return holder ;
    }


    @Override
    public void onBindViewHolder(@NonNull parkViewHolder holder, int position) {
        holder.park_location.setText(arrayList.get(position).getParklocation());
        holder.park_name.setText(arrayList.get(position).getParkname());
        holder.park_col.setText(arrayList.get(position).getParkcol());
    }


    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size():0);
    }

    public class parkViewHolder extends RecyclerView.ViewHolder {
        TextView park_location, park_name, park_col;
        public parkViewHolder(@NonNull View itemView) {
            super(itemView);
            this.park_name = itemView.findViewById(R.id.park_name);
            this.park_location = itemView.findViewById(R.id.park_location);
            this.park_col = itemView.findViewById(R.id.park_col);
        }
    }

}