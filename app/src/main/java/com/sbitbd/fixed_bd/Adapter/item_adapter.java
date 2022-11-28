package com.sbitbd.fixed_bd.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.sbitbd.fixed_bd.Config.DoConfig;
import com.sbitbd.fixed_bd.R;
import com.sbitbd.fixed_bd.ui.home.HomeViewModel;
import com.sbitbd.fixed_bd.ui.product_by_cond.product_view;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class item_adapter extends RecyclerView.Adapter<item_adapter.userHolder>{
    private List<cat_model> userList;
    private Context context;
    private DoConfig config;
    private HomeViewModel homeViewModel = new HomeViewModel();

    public item_adapter(Context context) {
        this.context = context;
        this.userList = new ArrayList<>();
        config = new DoConfig();
    }

    @NonNull
    @Override
    public userHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card,null);
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

        private ImageView imageView;
        private RecyclerView recyclerView;
        private Button button;
        final View cat_view;
        private final Context context1;
        private pro_model pro_model;
        private product_adapter product_adapter;
        private Chip chip;


        public userHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_img);
            recyclerView = itemView.findViewById(R.id.item_p_recycler);
            button = itemView.findViewById(R.id.item_more_btn);
            chip = itemView.findViewById(R.id.item_chip);
            this.cat_view = itemView;
            context1 = itemView.getContext();
        }
        public void bind(cat_model user){
            try {

                    chip.setText(user.getName());
                    glideLoad(user.getImage());
                    String sql = "SELECT `product_productinfo`.`id`,`product_productinfo`.`product_name`,`product_productinfo`.`sale_price`,`product_productinfo`.`discount_price`," +
                            "`product_productinfo`.`current_price`,`product_productinfo`.`image` " +
                            "FROM `product_productinfo` WHERE product_productinfo.item_id = '" + user.getId() + "' LIMIT 4";
                    product_adapter = new product_adapter(context1);
                    GridLayoutManager manager = new GridLayoutManager(context1, 2);
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setNestedScrollingEnabled(false);
                    homeViewModel.initProduct(context1, sql, pro_model, product_adapter);
                    recyclerView.setAdapter(product_adapter);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Intent intent;
                            intent = new Intent(context1, product_view.class);
                            intent.putExtra("item_id", user.getId());
                            context1.startActivity(intent);

                        }
                    });

            }catch (Exception e){
            }
        }
        private void glideLoad(String img){
            try {
//                Glide.with(cat_view)
//                        .load(config.ITEM_IMG_URL+img)
//                        .fitCenter().error(R.drawable.water)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .skipMemoryCache(true)
//                        .thumbnail(0.8f)
//                        .transition(DrawableTransitionOptions.withCrossFade())
//                        .into(imageView);

                Picasso.get().load(config.ITEM_IMG_URL+img)
                        .fit().centerInside()
                        .placeholder(R.drawable.water)
                        .error(R.drawable.water)
                        .into(imageView);
            }catch (Exception e){
            }
        }
    }
}
