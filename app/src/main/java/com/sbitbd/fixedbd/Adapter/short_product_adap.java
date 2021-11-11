package com.sbitbd.fixedbd.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.sbitbd.fixedbd.Config.DoConfig;
import com.sbitbd.fixedbd.R;
import com.sbitbd.fixedbd.ui.product_descripton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class short_product_adap extends RecyclerView.Adapter<short_product_adap.viewHolder>{
    private List<pro_model> userList;
    private Context context;
    private DoConfig config = new DoConfig();

    public short_product_adap(Context context) {
        this.context = context;
        this.userList = new ArrayList<>();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.short_product_item,null);
        viewHolder userH = new viewHolder(inflate);
        return userH;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        pro_model model = userList.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void ClearProduct(){
        userList.clear();
        notifyDataSetChanged();
    }

    public void adduser(pro_model pro_model){
        try {
            userList.add(pro_model);
            int position = userList.indexOf(pro_model);
            notifyItemInserted(position);
        }catch (Exception e){
        }
    }

    public void updateUser(pro_model pro_model){
        try {
            int position = getPosition(pro_model);
            if(position!=-1){
                userList.set(position,pro_model);
                notifyItemChanged(position);
            }
        }catch (Exception e){
        }
    }

    public int getPosition(pro_model pro_model){
        try {
            for (pro_model x:userList){
                if(x.getId().equals(pro_model.getId()) && x.getProName().equals(pro_model.getProName())){
                    return userList.indexOf(x);
                }
            }
        }catch (Exception e){
        }
        return -1;
    }

    class viewHolder extends RecyclerView.ViewHolder{
        ImageView proImage;
        TextView proname,price,dis_sum;
        MaterialCardView cardView;
        String image_url = "";
        private final Context context1;
        final View pro_view;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            proImage = itemView.findViewById(R.id.pro_image);
            proname = itemView.findViewById(R.id.pro_text);
            price = itemView.findViewById(R.id.price_val);
            dis_sum = itemView.findViewById(R.id.dis_val);
            cardView = itemView.findViewById(R.id.pro_card);
            dis_sum.setPaintFlags( dis_sum.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            context1 = itemView.getContext();
            this.pro_view = itemView;
        }
        public void bind(pro_model pro_model){
            try {
//                Glide.with(pro_view)
//                        .load(config.PRO_IMG_URL+pro_model.getImage())
//                        .fitCenter().error(R.drawable.water)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .skipMemoryCache(true)
//                        .into(proImage);

                Picasso.get().load(config.PRO_IMG_URL+pro_model.getImage())
                        .fit().centerInside()
                        .placeholder(R.drawable.water)
                        .error(R.drawable.water)
                        .into(proImage);
                image_url = config.PRO_IMG_URL+pro_model.getImage();
                proname.setText(pro_model.getProName());
                price.setText(pro_model.getPrice());
                if(pro_model.getDis_price().equals("") || pro_model.getDis_price().equals("0.00") || pro_model.getDis_price().equals("0") ||
                        pro_model.getDis_val().equals("0.00") || pro_model.getDis_val().equals("0")){
                    dis_sum.setText("");
                }else {
                    dis_sum.setText(pro_model.getDis_price());
                }

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent intent;
                        intent = new Intent(context1, product_descripton.class);
                        intent.putExtra("name",pro_model.getProName());
                        intent.putExtra("price",pro_model.getPrice());
                        intent.putExtra("image",image_url);
                        intent.putExtra("id",pro_model.getId());
                        intent.putExtra("dis_price",pro_model.getDis_price());
                        context1.startActivity(intent);
                    }
                });
            }catch (Exception e){
            }
        }
    }
}
