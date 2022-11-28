package com.sbitbd.fixed_bd.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.sbitbd.fixed_bd.Config.DoConfig;
import com.sbitbd.fixed_bd.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class seller_pro_adapter extends RecyclerView.Adapter<seller_pro_adapter.viewHolder> {
    private Context context;
    private List<order_model> userlist;
    private DoConfig config;

    public seller_pro_adapter(Context context) {
        this.context = context;
        userlist = new ArrayList<>();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflat = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_product, null);
        viewHolder viewHolder = new viewHolder(inflat);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        order_model cart_model = userlist.get(position);
        holder.bind(cart_model);
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public void adduser(order_model cart_model) {
        try {
            userlist.add(cart_model);
            int position = userlist.indexOf(cart_model);
            notifyItemInserted(position);
        } catch (Exception e) {
        }
    }

    public void removeUser(order_model cart_model) {
        int position = getPosition(cart_model);

        if (position != -1) {
            removeUser(position);
        }
    }

    public void removeUser(int position) {
        userlist.remove(position);
        notifyItemRemoved(position);
    }

    private int getPosition(order_model cart_model) {
        for (order_model x : userlist) {
            if (x.getOrder_id().equals(cart_model.getOrder_id())) {
                return userlist.indexOf(x);
            }
        }
        return -1;
    }

    class viewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView seller_pro_name;
        final View cartItem;
        MaterialCardView delete;
        ProgressDialog loading;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            seller_pro_name = itemView.findViewById(R.id.seller_pro_name);
            imageView = itemView.findViewById(R.id.seller_pro_image);
            delete = itemView.findViewById(R.id.delete);
            this.cartItem = itemView;
        }

        public void bind(order_model cart_model) {
            seller_pro_name.setText(cart_model.getPro_name());
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
                    dialogBuilder.setTitle("Confirmation");
                    dialogBuilder.setMessage("Are you sure you want to Delete?");
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setNegativeButton("NO", (dialog, which) -> {
                        dialog.dismiss();
                    });
                    dialogBuilder.setPositiveButton("Yes", (dialog, which) -> {
                        loading = ProgressDialog.show(context, "", "Loading...", false, false);
                        delete_size(context,cartItem,cart_model,loading);
                    });
                    dialogBuilder.show();
                }
            });
            Picasso.get().load(config.PRO_IMG_URL + cart_model.getDate()).transform(new RoundedCornersTransformation(10, 0))
                    .fit().centerInside()
                    .placeholder(R.drawable.water)
                    .error(R.drawable.water)
                    .into(imageView);
        }

        private void delete(Context con,View root,order_model cat_model,ProgressDialog loading) {
            try {
                String sql = "DELETE FROM `product_productinfo` WHERE id = '"+cat_model.getOrder_id()+"'";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                loading.dismiss();
                                if (response.equals("1")) {
                                    removeUser(cat_model);
                                    Snackbar snackbar = Snackbar.make(root, "Delete Successful", Snackbar.LENGTH_LONG);
                                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                                    snackbar.show();
                                }else {
                                    Snackbar snackbar = Snackbar.make(root, "Delete Unsuccessful", Snackbar.LENGTH_LONG);
                                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                                    snackbar.show();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(con, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(config.QUERY, sql);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(con);
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            } catch (Exception e) {
            }
        }
        private void delete_size(Context con,View root,order_model cat_model,ProgressDialog loading) {
            try {
                String sql = "DELETE FROM `product_size` WHERE product_id = '"+cat_model.getOrder_id()+"'";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("1")) {
                                    delete_color(con,root,cat_model,loading);
                                }else {

                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(con, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(config.QUERY, sql);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(con);
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            } catch (Exception e) {
            }
        }
        private void delete_color(Context con,View root,order_model cat_model,ProgressDialog loading) {
            try {
                String sql = "DELETE FROM `product_color` WHERE product_id = '"+cat_model.getOrder_id()+"'";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("1")) {
                                    delete_image(con,root,cat_model,loading);
                                }else {

                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(con, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(config.QUERY, sql);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(con);
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            } catch (Exception e) {
            }
        }
        private void delete_image(Context con,View root,order_model cat_model,ProgressDialog loading) {
            try {
                String sql = "DELETE FROM `product_images` WHERE product_id = '"+cat_model.getOrder_id()+"'";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("1")) {
                                    delete(con,root,cat_model,loading);
                                }else {

                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(con, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(config.QUERY, sql);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(con);
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
