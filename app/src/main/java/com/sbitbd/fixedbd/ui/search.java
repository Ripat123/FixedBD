package com.sbitbd.fixedbd.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.sbitbd.fixedbd.Adapter.pro_model;
import com.sbitbd.fixedbd.Adapter.product_adapter;
import com.sbitbd.fixedbd.Config.DoConfig;
import com.sbitbd.fixedbd.R;
import com.sbitbd.fixedbd.ui.home.HomeViewModel;

import java.util.HashMap;
import java.util.Map;

public class search extends AppCompatActivity {

    private EditText search_field;
    private RecyclerView search_recycler;
    private DoConfig config = new DoConfig();
    private ProgressDialog progressDialog;
    private HomeViewModel homeViewModel = new HomeViewModel();
    private pro_model pro_model;
    private product_adapter product_adapter;
    private Button search_btn;
    private MaterialButton pro_des_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Search Product");
        search_field = findViewById(R.id.searchfield);
        search_recycler = findViewById(R.id.searchpro_rec);
        search_btn = findViewById(R.id.search_butn);
        pro_des_back = findViewById(R.id.pro_des_back);
        search_field.requestFocus();
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProduct();
            }
        });
        pro_des_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void getProduct(){
        GridLayoutManager manager = new GridLayoutManager(search.this, 2);
        search_recycler.setLayoutManager(manager);

        product_adapter = new product_adapter(search.this);
        product_adapter.ClearProduct();
        progressDialog = ProgressDialog.show(search.this,"","Fetching",false,false);
        try {
            String sql = "SELECT `product_productinfo`.`id`,`product_productinfo`.`product_name`," +
                    "`product_productinfo`.`sale_price`,`product_productinfo`.`discount_price`," +
                    "`product_productinfo`.`current_price`,`product_productinfo`.`image`,`product_size`.`size` " +
                    "FROM `product_productinfo` INNER JOIN `product_size` ON `product_productinfo`.id = " +
                    "`product_size`.`product_id` WHERE product_productinfo.product_name LIKE '%"+search_field.getText().toString().trim()+"%'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.PRO_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if(!response.equals("1")){
                                homeViewModel.showProJSON(response,pro_model,product_adapter, search.this);
                            }
                            else {
                                Toast.makeText(search.this,"Not found",Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(search.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(search.this);
            requestQueue.add(stringRequest);
            search_recycler.setAdapter(product_adapter);
        }catch (Exception e){
        }
    }
}