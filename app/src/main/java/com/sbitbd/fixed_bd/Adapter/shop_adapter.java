package com.sbitbd.fixed_bd.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.sbitbd.fixed_bd.Config.DoConfig;
import com.sbitbd.fixed_bd.R;
import com.sbitbd.fixed_bd.ui.product_by_cond.product_view;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class shop_adapter extends RecyclerView.Adapter<shop_adapter.viewHolder>{
    private List<cat_model> userList;
    private Context context;
    private DoConfig config;

    public shop_adapter(Context context) {
        this.context = context;
        this.userList = new ArrayList<>();
        config = new DoConfig();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item,null);
        viewHolder userH = new viewHolder(inflate);
        return userH;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        cat_model user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    public void ClearCategory(){
        userList.clear();
        notifyDataSetChanged();
    }

    public void addUser(cat_model user){
        try {
            userList.add(user);
            //notifyDataSetChanged();
            int position = userList.indexOf(user);
            notifyItemInserted(position);
        }catch (Exception e){
        }
    }

    class viewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name;
        MaterialCardView cardView;
        final View cat_view;
        private final Context context1;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cat_image);
            name = itemView.findViewById(R.id.cat_text);
            cardView = itemView.findViewById(R.id.cat_card);
            this.cat_view = itemView;
            context1 = itemView.getContext();
        }
        public void bind(cat_model user){
            try {
                name.setText(user.getName());

//                    Glide.with(cat_view)
//                            .load(config.SELLER_IMG_URL + user.getImage())
//                            .fitCenter().error(R.drawable.water)
//                            .diskCacheStrategy(DiskCacheStrategy.NONE)
//                            .skipMemoryCache(true)
//                            .into(imageView);

                Picasso.get().load(config.SELLER_IMG_URL + user.getImage())
                        .fit().centerInside()
                        .placeholder(R.drawable.water)
                        .error(R.drawable.water)
                        .into(imageView);

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent intent;

                            intent = new Intent(context1, product_view.class);
                            intent.putExtra("seller", user.getId());
                            context1.startActivity(intent);

                    }
                });
            }catch (Exception e){
            }
        }
    }
}
