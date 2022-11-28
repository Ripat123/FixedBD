package com.sbitbd.fixed_bd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.fixed_bd.Config.DoConfig;
import com.sbitbd.fixed_bd.R;
import com.sbitbd.fixed_bd.ui.cart_operation.operation;
import com.sbitbd.fixed_bd.ui.home.HomeViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class cart_adapter extends RecyclerView.Adapter<cart_adapter.viewHolder>{

    private Context context;
    private List<cart_model> userlist;
    private DoConfig config;
    private static double total;
    private TextView total_txt;
    private Button check_btn;
    ConstraintLayout empty_lay;
    private operation operation;
    private HomeViewModel homeViewModel;

    public cart_adapter(Context context,TextView total_txt,Button check_btn,ConstraintLayout empty_lay) {
        this.context = context;
        this.userlist = new ArrayList<>();
        config = new DoConfig();
        this.total_txt = total_txt;
        this.check_btn = check_btn;
        this.empty_lay = empty_lay;
        total = 0;
        this.operation = new operation();
        this.homeViewModel = new HomeViewModel();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflat = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,null);
        viewHolder viewHolder = new viewHolder(inflat);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        cart_model cart_model = userlist.get(position);
        holder.bind(cart_model);
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }
    public void adduser(cart_model cart_model){
        try {
            userlist.add(cart_model);
            int position = userlist.indexOf(cart_model);
            notifyItemInserted(position);
        }catch (Exception e){
        }
    }
    public void removeUser(cart_model cart_model){
        int position = getPosition(cart_model);

        if(position!=-1){
            removeUser(position);
        }
    }

    public void removeUser(int position){
        userlist.remove(position);
        notifyItemRemoved(position);
    }
    private int getPosition(cart_model cart_model){
        for (cart_model x:userlist){
            if(x.getProID().equals(cart_model.getProID())){
                return userlist.indexOf(x);
            }
        }
        return -1;
    }

    class viewHolder extends RecyclerView.ViewHolder{

        TextView pro_name,price_quant,total_price,quant;
        ImageView imageView,delete;
        Button incre,decre;
        final View cartItem;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            pro_name = itemView.findViewById(R.id.cart_pro_name);
            price_quant = itemView.findViewById(R.id.mul_quant_price);
            total_price = itemView.findViewById(R.id.cart_price);
            quant = itemView.findViewById(R.id.quant_num);
            imageView = itemView.findViewById(R.id.cart_image);
            incre = itemView.findViewById(R.id.pro_quant_incre);
            decre = itemView.findViewById(R.id.pro_quant_decre);
            delete = itemView.findViewById(R.id.del_img);
            this.cartItem = itemView;
        }
        public void bind(cart_model cart_model){
            pro_name.setText(cart_model.getPro_name());
            price_quant.setText(cart_model.getPrice_quant()+" X "+"1");
            total_price.setText(String.valueOf(cart_model.getTotal_price()));
            quant.setText(cart_model.getQuant());
            total = total + Double.parseDouble(total_price.getText().toString());
            total_txt.setText(String.valueOf(total));
//            imageView.setImageResource(cart_model.getImage());

//            Glide.with(cartItem)
//                    .load(config.PRO_IMG_URL+cart_model.getImage())
//                    .fitCenter()
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
//                    .into(imageView);
            Picasso.get().load(config.PRO_IMG_URL+cart_model.getImage())
                    .fit().centerInside()
                    .placeholder(R.drawable.water)
                    .error(R.drawable.water)
                    .into(imageView);

            incre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int oldquant = Integer.parseInt(quant.getText().toString());
                    oldquant++;
                    quant.setText(String.valueOf(oldquant));
                    double total = oldquant * Double.parseDouble(cart_model.getPrice_quant());
                    total_price.setText(String.valueOf(total));
                    double old =Double.parseDouble(total_txt.getText().toString());
                    double newt = old +  Double.parseDouble(cart_model.getPrice_quant());
                    total_txt.setText(String.valueOf(newt));
                    boolean check = operation.update(context,String.valueOf(oldquant),homeViewModel.getSession(context),
                            cart_model.getProID(),cart_model.getColor(),cart_model.getSize());
                    if (check){
                        operation.onlinUpdate(context,String.valueOf(oldquant),homeViewModel.getSession(context),
                                cart_model.getProID(),cart_model.getColor(),cart_model.getSize());
                        operation.stock("1",cart_model.getProID(),cart_model.getColor(),cart_model.getSize(),context);
                    }else {
                        Toast.makeText(context,"Update Failed",Toast.LENGTH_LONG).show();
                    }
                }
            });
            decre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Integer.parseInt(quant.getText().toString()) > Integer.parseInt(cart_model.getMin_quant())) {
                        int oldquant = Integer.parseInt(quant.getText().toString());
                        oldquant--;
                        quant.setText(String.valueOf(oldquant));
                        double total = oldquant * Double.parseDouble(cart_model.getPrice_quant());
                        total_price.setText(String.valueOf(total));
                        total_txt.setText(String.valueOf(Double.parseDouble(total_txt.getText().toString()) -
                                Double.parseDouble(cart_model.getPrice_quant())));
                        boolean check = operation.update(context,String.valueOf(oldquant),homeViewModel.getSession(context),
                                cart_model.getProID(),cart_model.getColor(),cart_model.getSize());
                        if (check){
                            operation.onlinUpdate(context,String.valueOf(oldquant),homeViewModel.getSession(context),
                                    cart_model.getProID(),cart_model.getColor(),cart_model.getSize());
                            stock_manage(cart_model.getProID(),cart_model.getColor(),cart_model.getSize(),"1");
                        }else {
                            Toast.makeText(context,"Update Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        operation.deleteCart(cart_model.getProID(),cart_model.getColor(),cart_model.getSize(),context);
                        stock_manage(cart_model.getProID(),cart_model.getColor(),cart_model.getSize(),quant.getText().toString());
                        double old =Double.parseDouble(total_txt.getText().toString());
                        int oldquant = Integer.parseInt(quant.getText().toString());
                        double delprice = Double.parseDouble(cart_model.getPrice_quant());
                        double tprice = oldquant  * delprice;
                        double nprice = old - tprice;
                        total_txt.setText(String.valueOf(nprice));
                        removeUser(cart_model);
                        if(getItemCount() == 0){
                            check_btn.setEnabled(false);
                            empty_lay.setVisibility(View.VISIBLE);
                        }
                    }catch (Exception e){
                    }

                }
            });
        }
        private void stock_manage(String id,String color,String size,String quantity){
            try {
                String sql = "UPDATE `productstocks` SET `quentity` = `quentity` + '"+quantity+"' WHERE `product_id` " +
                        "= '"+id+"' AND `size` = '"+size+"' AND `color` = '"+color+"'";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (!response.equals("1")) {
                                    Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(config.QUERY, sql);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            } catch (Exception e) {
            }
        }
    }
}
