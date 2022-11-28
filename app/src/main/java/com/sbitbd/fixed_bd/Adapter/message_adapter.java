package com.sbitbd.fixed_bd.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.sbitbd.fixed_bd.R;

import java.util.ArrayList;
import java.util.List;

public class message_adapter extends RecyclerView.Adapter<message_adapter.viewHolder>{
    private Context context;
    private List<cat_model> cat_models;

    public message_adapter(Context context) {
        this.context = context;
        cat_models = new ArrayList<>();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_box,null);
        viewHolder userH = new viewHolder(inflate);
        return userH;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        cat_model user = cat_models.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return cat_models.size();
    }
    public void addmessage(cat_model user){
        try {
            cat_models.add(user);
            //notifyDataSetChanged();
            int position = cat_models.indexOf(user);
            notifyItemInserted(position);
        }catch (Exception e){
        }
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView name;
        MaterialCardView cardView;
        final View cat_view;
        private final Context context1;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.mess_text);
            cardView = itemView.findViewById(R.id.mess_card);
            this.cat_view = itemView;
            context1 = itemView.getContext();
        }
        @SuppressLint("ResourceAsColor")
        public void bind(cat_model cat_model){
            try {
                if (cat_model.getId().equals("3"))
                    cardView.setCardBackgroundColor(R.color.main_color);
                name.setText(cat_model.getName());

            }catch (Exception e){
            }
        }
    }
}
