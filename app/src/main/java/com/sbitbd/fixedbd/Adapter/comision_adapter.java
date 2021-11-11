package com.sbitbd.fixedbd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sbitbd.fixedbd.R;

import java.util.ArrayList;
import java.util.List;

public class comision_adapter extends RecyclerView.Adapter<comision_adapter.viewholder>{

    List<four_model> four_models;
    Context context;

    public comision_adapter(Context context) {
        this.context = context;
        this.four_models = new ArrayList<>();
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.commision_balance,null);
        viewholder userH = new viewholder(inflate);
        return userH;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        four_model four_model = four_models.get(position);
        holder.bind(four_model);
    }

    @Override
    public int getItemCount() {
        return four_models.size();
    }

    public void ClearCategory(){
        four_models.clear();
        notifyDataSetChanged();
    }

    public void addUser(four_model user){
        try {
            four_models.add(user);
            //notifyDataSetChanged();
            int position = four_models.indexOf(user);
            notifyItemInserted(position);
        }catch (Exception e){
        }
    }

    class viewholder extends RecyclerView.ViewHolder{
        TextView date,invoice,amount;
        final Context context1;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            invoice = itemView.findViewById(R.id.inv);
            amount = itemView.findViewById(R.id.amount_t);
            context1 = itemView.getContext();
        }
        public void bind(four_model four_model){
            try {
                date.setText(four_model.getOne());
                invoice.setText(four_model.getTwo());
                amount.setText(four_model.getThree()+" TK");
            }catch (Exception e){
            }
        }
    }
}
