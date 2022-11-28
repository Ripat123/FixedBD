package com.sbitbd.fixed_bd.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sbitbd.fixed_bd.R;
import com.google.android.material.card.MaterialCardView;
import com.sbitbd.fixed_bd.ui.track_order.order_info;

import java.util.ArrayList;
import java.util.List;

public class order_adapter extends RecyclerView.Adapter<order_adapter.viewHolder>{

    private Context context;
    private List<order_model> userlist;

    public order_adapter(Context context) {
        this.context = context;
        this.userlist = new ArrayList<>();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflat = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,null);
        viewHolder holder = new viewHolder(inflat);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        order_model order_model = userlist.get(position);
        holder.bind(order_model);
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }
    public void adduser(order_model order_model){
        try {
            userlist.add(order_model);
            int position = userlist.indexOf(order_model);
            notifyItemInserted(position);
        }catch (Exception e){

        }
    }
    public void updateUser(order_model order_model){
        try {
            int position = getPosition(order_model);
            if(position!=-1){
                userlist.set(position,order_model);
                notifyItemChanged(position);
            }
        }catch (Exception e){
        }
    }

    public int getPosition(order_model order_model){
        try {
            for (order_model x:userlist){
                if(x.getOrder_id().equals(order_model.getOrder_id())){
                    return userlist.indexOf(x);
                }
            }
        }catch (Exception e){
        }
        return -1;
    }

    class viewHolder extends RecyclerView.ViewHolder{
        private TextView orderID,pro_name,orderdate,orderst;
        MaterialCardView order_card;
        final View view;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            orderID = itemView.findViewById(R.id.order_id);
            pro_name = itemView.findViewById(R.id.order_pro_name);
            orderdate = itemView.findViewById(R.id.order_date);
            orderst = itemView.findViewById(R.id.order_status);
            order_card = itemView.findViewById(R.id.cart_card);
            view = itemView;
        }
        private void bind(order_model order_model){
            orderID.setText(order_model.getOrder_id());
            pro_name.setText(order_model.getPro_name());
            orderdate.setText(order_model.getDate());
            switch (order_model.getStatus()){
                case "0":
                    orderst.setText("Pending");
                    break;
                case "1":
                    orderst.setText("Process");
                    orderst.setTextColor(view.getResources().getColor(R.color.proccess));
                    break;
                case "2":
                    orderst.setText("On the way");
                    orderst.setTextColor(view.getResources().getColor(R.color.ontheway));
                    break;
                case "3":
                    orderst.setText("Success");
                    orderst.setTextColor(view.getResources().getColor(R.color.success));
                    break;
                case "4":
                    orderst.setText("Reject");
                    orderst.setTextColor(view.getResources().getColor(R.color.reject));
                    break;
            }

            order_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),order_info.class);
                    intent.putExtra("order_id",order_model.getOrder_id());
                    intent.putExtra("order_date",order_model.getDate());
                    intent.putExtra("order_state",order_model.getStatus());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
