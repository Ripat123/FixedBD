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

public class mainItem_adapter extends RecyclerView.Adapter<mainItem_adapter.userHolder>{
    private List<cat_model> userList;
    private Context context;
    private DoConfig config;

    public mainItem_adapter(Context context) {
        this.context = context;
        config = new DoConfig();
        userList = new ArrayList<>();
    }

    @NonNull
    @Override
    public userHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,null);
        userHolder userH = new userHolder(inflate);
        return userH;
    }

    @Override
    public void onBindViewHolder(@NonNull userHolder holder, int position) {
        cat_model user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    public void ClearItem(){
        userList.clear();
        notifyDataSetChanged();
    }

    public void addUser(cat_model user){
        try {
            userList.add(user);
            int position = userList.indexOf(user);
            notifyItemInserted(position);
        }catch (Exception e){
        }
    }

    class userHolder extends RecyclerView.ViewHolder{
        final View cat_view;
        private final Context context1;
        private ImageView imageView;
        private TextView textView;
        private MaterialCardView cardView;
        public userHolder(@NonNull View itemView) {
            super(itemView);
            this.cat_view = itemView;
            imageView = itemView.findViewById(R.id.item_img);
            textView = itemView.findViewById(R.id.item_txt);
            cardView = itemView.findViewById(R.id.item_p);
            context1 = itemView.getContext();
        }
        public void bind(cat_model user) {
            try {
//                Glide.with(cat_view)
//                        .load(config.ITEM_IMG_URL + user.getImage())
//                        .fitCenter().error(R.drawable.water)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .skipMemoryCache(true)
//                        .thumbnail(0.7f)
//                        .transition(DrawableTransitionOptions.withCrossFade())
//                        .into(imageView);

                Picasso.get().load(config.CAT_IMG_URL + user.getImage())
                        .fit().centerInside()
                        .placeholder(R.drawable.water)
                        .error(R.drawable.water)
                        .into(imageView);
                textView.setText(user.getName());
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context1, product_view.class);
                        intent.putExtra("id",user.getId());
                        context1.startActivity(intent);
                    }
                });
            } catch (Exception e) {
            }
        }
    }
}
