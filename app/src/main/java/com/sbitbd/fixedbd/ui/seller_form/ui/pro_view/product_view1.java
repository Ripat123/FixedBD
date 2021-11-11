package com.sbitbd.fixedbd.ui.seller_form.ui.pro_view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.sbitbd.fixedbd.Adapter.order_model;
import com.sbitbd.fixedbd.Adapter.seller_pro_adapter;
import com.sbitbd.fixedbd.Config.DoConfig;
import com.sbitbd.fixedbd.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class product_view1 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressDialog loading;
    private DoConfig config = new DoConfig();
    private seller_pro_adapter seller_pro_adapter;
    private order_model pro_model;
    private MaterialButton show_m_seller_btn;
    private int limit = 0;
    private ConstraintLayout constraintLayout;
    private String sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String name = getIntent().getStringExtra("tag");
        getSupportActionBar().setTitle(name);
        recyclerView = findViewById(R.id.pro_rec_seller);
        show_m_seller_btn = findViewById(R.id.show_m_seller_btn);
        constraintLayout = findViewById(R.id.notin_id);

        sql = getIntent().getStringExtra("query");

        GridLayoutManager manager = new GridLayoutManager(product_view1.this, 1);
        recyclerView.setLayoutManager(manager);
        seller_pro_adapter = new seller_pro_adapter(product_view1.this);
        show_product(sql, limit);
        show_m_seller_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limit += 30;
                show_product(sql, limit);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void show_product(String sql, int limit) {
        try {
            loading = ProgressDialog.show(product_view1.this, "", "Loading...", false, false);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.PRO_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            if (!response.equals("1")) {
                                show(response);
                            } else {
                                if (seller_pro_adapter.getItemCount() <= 0)
                                    constraintLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.dismiss();
                    Toast.makeText(product_view1.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql + "" + limit + ",30");
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(product_view1.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }

        recyclerView.setAdapter(seller_pro_adapter);
    }

    private void show(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            final int len = result.length();
            for (int i = 0; i < len; i++) {
                JSONObject collegeData = result.getJSONObject(i);

                pro_model = new order_model(collegeData.getString(config.CAT_ID), collegeData
                        .getString(config.PRO_NAME), collegeData.getString(config.PRO_IMAGE), "");
                seller_pro_adapter.adduser(pro_model);
            }
        } catch (Exception e) {
        }
    }
}