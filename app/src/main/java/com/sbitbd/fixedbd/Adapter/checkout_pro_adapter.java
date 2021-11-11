package com.sbitbd.fixedbd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sbitbd.fixedbd.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class checkout_pro_adapter extends RecyclerView.Adapter<checkout_pro_adapter.userHolder>{

    private Context context;
    private List<checkout_pro_model> userlist;
    private TextView subtT,grandT;
    private static double total;

    public checkout_pro_adapter(Context context,TextView subtotal,TextView grtotal) {
        this.context = context;
        userlist = new ArrayList<>();
        this.subtT=subtotal;
        this.grandT = grtotal;
        total = 0;
    }

    @NonNull
    @Override
    public userHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_out_product,null);
        userHolder userH = new userHolder(inflate);
        return userH;
    }

    @Override
    public void onBindViewHolder(@NonNull userHolder holder, int position) {
        checkout_pro_model checkout_pro_model = userlist.get(position);
        holder.bind(checkout_pro_model);
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }
    public void adduser(checkout_pro_model checkout_pro_model){
        try {
            userlist.add(checkout_pro_model);
            int position = userlist.indexOf(checkout_pro_model);
            notifyItemInserted(position);
        }catch (Exception e){

        }
    }

    class userHolder extends RecyclerView.ViewHolder{
        TextView proname,subtotal;
        MaterialCardView cardView;

        public userHolder(@NonNull View itemView) {
            super(itemView);
            proname = itemView.findViewById(R.id.pro_name);
            subtotal = itemView.findViewById(R.id.subtotal);
            cardView = itemView.findViewById(R.id.check_pro_card);
        }
        public void bind(checkout_pro_model checkout_pro_model){
                proname.setText(checkout_pro_model.getProduct_name()+" X "+checkout_pro_model.getQuantity());
                subtotal.setText(checkout_pro_model.getSubtotal());
                total = total + Double.parseDouble(subtotal.getText().toString());
                subtT.setText(String.valueOf(total));
                grandT.setText(String.valueOf(total));
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        }
    }
}
