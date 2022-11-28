package com.sbitbd.fixed_bd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sbitbd.fixed_bd.R;

import java.util.ArrayList;
import java.util.List;

public class wish_adapter extends RecyclerView.Adapter<wish_adapter.viewHolder> {

    private Context context;
    private List<wish_model> userlist;

    public wish_adapter(Context context) {
        this.context = context;
        this.userlist = new ArrayList<>();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_item,null);
        viewHolder holder = new viewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        wish_model wish_model = userlist.get(position);
        holder.bind(wish_model);
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }
    public void adduser(wish_model wish_model){
        userlist.add(wish_model);
        int position = userlist.indexOf(wish_model);
        notifyItemInserted(position);
    }

    class viewHolder extends RecyclerView.ViewHolder{

        TextView pro_name,price;
        ImageView pro_image;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            pro_name = itemView.findViewById(R.id.wish_pro_name);
            price = itemView.findViewById(R.id.wish_price);
            pro_image = itemView.findViewById(R.id.wish_image);
        }
        public void bind(wish_model wish_model){
            pro_name.setText(wish_model.getPro_name());
            price.setText(wish_model.getPrice());
            pro_image.setImageResource(wish_model.getImage());
        }
    }
}
