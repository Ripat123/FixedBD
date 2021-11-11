package com.sbitbd.fixedbd.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.sbitbd.fixedbd.Config.DoConfig;
import com.sbitbd.fixedbd.R;
import com.sbitbd.fixedbd.ui.category.all_category;
import com.sbitbd.fixedbd.ui.product_by_cond.product_view;

import java.util.ArrayList;
import java.util.List;

public class menu_adapter extends RecyclerView.Adapter<menu_adapter.viewHolder>{
    private Context context;
    private List<menu_model> userlist;
    private DoConfig config;

    public menu_adapter(Context context) {
        this.context = context;
        userlist = new ArrayList<>();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflat = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_menu,null);
        viewHolder viewHolder = new viewHolder(inflat);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        menu_model cart_model = userlist.get(position);
        holder.bind(cart_model);
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }
    public void adduser(menu_model cart_model){
        try {
            userlist.add(cart_model);
            int position = userlist.indexOf(cart_model);
            notifyItemInserted(position);
        }catch (Exception e){
        }
    }

    class viewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
//        TextView textView;
        MaterialCardView cardView;
        final View cartItem;
        private final Context context1;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            this.cartItem = itemView;
//            textView = itemView.findViewById(R.id.menu_title);
            imageView = itemView.findViewById(R.id.menu_img);
            cardView = itemView.findViewById(R.id.top_menu);
            context1 = itemView.getContext();
        }
        public void bind(menu_model cart_model){
            try {
                imageView.setImageResource(cart_model.getImage());
//                textView.setText(cart_model.getName());
//                Glide.with(cartItem)
//                        .load(config.PRO_IMG_URL+cart_model.getImage())
//                        .fitCenter()
//                        .into(imageView);

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!cart_model.getName().equals("All Category")) {
                            Intent intent = new Intent(context1, product_view.class);
                            intent.putExtra("menu",cart_model.getName());
                            context1.startActivity(intent);
                        }else {
                            context1.startActivity(new Intent(context1, all_category.class));
                        }
                    }
                });
            }catch (Exception e){
            }

        }
    }
}
