package com.sbitbd.fixedbd.ui.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.sbitbd.fixedbd.Adapter.cat_model;
import com.sbitbd.fixedbd.Adapter.category_adapter;
import com.sbitbd.fixedbd.Config.DoConfig;
import com.sbitbd.fixedbd.R;
import com.sbitbd.fixedbd.ui.home.HomeViewModel;

import java.util.HashMap;
import java.util.Map;

public class all_category extends AppCompatActivity {

    HomeViewModel homeViewModel = new HomeViewModel();
    DoConfig config = new DoConfig();
    cat_model cat_models;
    category_adapter category_adapter;
    RecyclerView all_cat_recycler;
    SwipeRefreshLayout swipeRefreshLayout;
    private TextView top_text;
    private MaterialButton top_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);
        all_cat_recycler = findViewById(R.id.all_cat_recycler);
        swipeRefreshLayout = findViewById(R.id.cat_refresh);
        top_btn = findViewById(R.id.pro_des_back);
        top_text  = findViewById(R.id.pro_des_text);

        GridLayoutManager manager = new GridLayoutManager(all_category.this, 3);
        all_cat_recycler.setLayoutManager(manager);
        String id = getIntent().getStringExtra("item");
        String shop = getIntent().getStringExtra("shop");
        if (shop != null && shop.equals("sh")) {
            top_text.setText("All Shop");
            category_adapter = new category_adapter(all_category.this, 1);
            initCategory("SELECT `sellers`.`id`,`sellers`.`business_name` AS 'category_name',`sellers`.`image` FROM `sellers`");
        } else if (id != null && !id.equals("")) {
            top_text.setText("Category");
            category_adapter = new category_adapter(all_category.this, 0);
            initCategory("SELECT id,`category_name`,`image` FROM `product_category` WHERE item_id = '" + id + "'");
        } else {
            top_text.setText("All Category");
            category_adapter = new category_adapter(all_category.this, 0);
            initCategory("SELECT id,`category_name`,`image` FROM `product_category` WHERE shop_by = '1'");
        }
        top_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                category_adapter.ClearCategory();
                if (id != null && !id.equals("")) {
                    initCategory("SELECT id,`category_name`,`image` FROM `product_category` WHERE item_id = '" + id + "'");
                } else {
                    initCategory("SELECT id,`category_name`,`image` FROM `product_category`");
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initCategory(String sql) {
        try {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.CAT_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            homeViewModel.showCatJSON(response, cat_models, category_adapter);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(all_category.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(all_category.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

            all_cat_recycler.setAdapter(category_adapter);
        } catch (Exception e) {
        }
    }


}