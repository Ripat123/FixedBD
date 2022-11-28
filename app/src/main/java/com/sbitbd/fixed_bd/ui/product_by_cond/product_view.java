package com.sbitbd.fixed_bd.ui.product_by_cond;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.fixed_bd.Adapter.pro_model;
import com.sbitbd.fixed_bd.Adapter.product_adapter;
import com.sbitbd.fixed_bd.Config.DoConfig;
import com.sbitbd.fixed_bd.R;
import com.sbitbd.fixed_bd.ui.home.HomeViewModel;

import java.util.HashMap;
import java.util.Map;

public class product_view extends AppCompatActivity {

    private pro_model pro_model;
    private product_adapter product_adapter;
    private RecyclerView prorec;
    private DoConfig config = new DoConfig();
    private HomeViewModel homeViewModel = new HomeViewModel();
    private ProgressDialog loading;
    String cat_id,offer,sm_shop,buy,feni,sql,item_id,seller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Products");
        prorec = findViewById(R.id.probycond_rec);
        seller = getIntent().getStringExtra("seller");
        cat_id = getIntent().getStringExtra("id");
        item_id = getIntent().getStringExtra("item_id");
        offer = getIntent().getStringExtra("menu");
        if(cat_id != null && !cat_id.equals("")) {
            sql = "SELECT `product_productinfo`.`id`,`product_productinfo`.`product_name`," +
                    "`product_productinfo`.`sale_price`,`product_productinfo`.`discount_price`," +
                    "`product_productinfo`.`current_price`,`product_productinfo`.`image` " +
                    "FROM `product_productinfo` WHERE product_productinfo.category_id = '"+cat_id+"' AND `status` = '1'";
            initpro(sql);
        }
        if(seller != null && !seller.equals("")) {
            sql = "SELECT `product_productinfo`.`id`,`product_productinfo`.`product_name`," +
                    "`product_productinfo`.`sale_price`,`product_productinfo`.`discount_price`," +
                    "`product_productinfo`.`current_price`,`product_productinfo`.`image` " +
                    "FROM `product_productinfo` WHERE product_productinfo.seller_id = '"+seller+"' AND `status` = '1'";
            initpro(sql);
        }
        else if(item_id != null && !item_id.equals("")) {
            sql = "SELECT `product_productinfo`.`id`,`product_productinfo`.`product_name`," +
                    "`product_productinfo`.`sale_price`,`product_productinfo`.`discount_price`," +
                    "`product_productinfo`.`current_price`,`product_productinfo`.`image` " +
                    "FROM `product_productinfo` WHERE product_productinfo.item_id = '"+item_id+"' AND `status` = '1'";
            initpro(sql);
        }
        else if(offer != null && offer.equals("Offer")) {
            sql = "SELECT offer_setups.product_id,`product_productinfo`.`id`,`product_productinfo`.`product_name`,`product_productinfo`.`sale_price`,`product_productinfo`.`discount_price`," +
                    "                    `product_productinfo`.`current_price`,`product_productinfo`.`image` FROM offer_setups INNER JOIN " +
                    "                    product_productinfo ON offer_setups.product_id = product_productinfo.id WHERE offer_setups.`status` = '1' AND " +
                    "                    offer_setups.`type` = '2' AND product_productinfo.`status` = '1'";
            initpro(sql);
        }
        else if(offer != null && offer.equals("SM Shop")) {
            sql = "SELECT `product_productinfo`.`id`,`product_productinfo`.`product_name`," +
                    "`product_productinfo`.`sale_price`,`product_productinfo`.`discount_price`," +
                    "`product_productinfo`.`current_price`,`product_productinfo`.`image` " +
                    "FROM `product_productinfo` WHERE `seller_id` = '45' AND `status` = '1'";
            initpro(sql);
        }
        else if(offer != null && offer.equals("Exclusive buy")) {
            sql = "SELECT offer_setups.product_id,`product_productinfo`.`id`,`product_productinfo`.`product_name`,`product_productinfo`.`sale_price`,`product_productinfo`.`discount_price`," +
                    "                   `product_productinfo`.`current_price`,`product_productinfo`.`image` FROM offer_setups INNER JOIN " +
                    "                    product_productinfo ON offer_setups.product_id = product_productinfo.id WHERE offer_setups.`status` = '1' AND " +
                    "                    offer_setups.`type` = '3' ";
            initpro(sql);
        }
        else if(offer != null && offer.equals("Feni Express")) {
            sql = "SELECT offer_setups.product_id,`product_productinfo`.`id`,`product_productinfo`.`product_name`,`product_productinfo`.`sale_price`,`product_productinfo`.`discount_price`," +
                    "                   `product_productinfo`.`current_price`,`product_productinfo`.`image` FROM offer_setups INNER JOIN " +
                    "                    product_productinfo ON offer_setups.product_id = product_productinfo.id WHERE offer_setups.`status` = '1' AND " +
                    "                    offer_setups.`type` = '5' AND product_productinfo.`status` = '1'";
            initpro(sql);
        }
        else if(offer != null && offer.equals("flash")) {
            sql = "SELECT offer_setups.product_id,`product_productinfo`.`id`,`product_productinfo`.`product_name`,`product_productinfo`.`sale_price`,`product_productinfo`.`discount_price`," +
                    "                    `product_productinfo`.`current_price`,`product_productinfo`.`image` FROM offer_setups INNER JOIN " +
                    "                    product_productinfo ON offer_setups.product_id = product_productinfo.id WHERE offer_setups.`status` = '1' AND " +
                    "                    offer_setups.`type` = '1' AND offer_setups.`start_date_time` <= NOW() " +
                    "                    AND offer_setups.`end_date_time` >= NOW() ";
            initpro(sql);
        }
        else if(offer != null && offer.equals("exclusive")) {
            sql = "SELECT offer_setups.product_id,`product_productinfo`.`id`,`product_productinfo`.`product_name`,`product_productinfo`.`sale_price`,`product_productinfo`.`discount_price`, " +
                    "                   `product_productinfo`.`current_price`,`product_productinfo`.`image` FROM offer_setups INNER JOIN " +
                    "                    product_productinfo ON offer_setups.product_id = product_productinfo.id WHERE offer_setups.`status` = '1' AND " +
                    "                    offer_setups.`type` = '3'";
            initpro(sql);
        }
        else if(offer != null && offer.equals("recent")) {
            sql = "SELECT `product_productinfo`.`id`,`product_productinfo`.`product_name`,`product_productinfo`.`sale_price`,`product_productinfo`.`discount_price`," +
                    "                    `product_productinfo`.`current_price`,`product_productinfo`.`image` " +
                    "                    FROM `product_productinfo` WHERE `status` = '1' ORDER BY product_productinfo.id DESC";
            initpro(sql);
        }
        else if(offer != null && offer.equals("life")) {
            sql = "SELECT `product_productinfo`.`id`,`product_productinfo`.`product_name`,`product_productinfo`.`sale_price`,`product_productinfo`.`discount_price`," +
                    "                    `product_productinfo`.`current_price`,`product_productinfo`.`image` " +
                    "                     FROM `product_productinfo` WHERE product_productinfo.item_id = '4' ";
            initpro(sql);
        }
        else if(offer != null && offer.equals("earn")) {
            sql = "SELECT `product_productinfo`.`id`,`product_productinfo`.`product_name`,`product_productinfo`.`sale_price`,`product_productinfo`.`discount_price`," +
                    "                    `product_productinfo`.`current_price`,`product_productinfo`.`image` " +
                    "                     FROM `product_productinfo` WHERE product_productinfo.buy_earn = '1' ";
            initpro(sql);
        }
    }
    private void initpro(String sql){
        loading = ProgressDialog.show(this,"","Loading...",false,false);
        GridLayoutManager manager = new GridLayoutManager(product_view.this, 2);
        prorec.setLayoutManager(manager);

        product_adapter = new product_adapter(product_view.this);

        try {


            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.PRO_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            if(!response.equals("1")){
                                homeViewModel.showProJSON(response,pro_model,product_adapter,product_view.this);
                            }
                            else {
                                Toast.makeText(product_view.this,"Not found",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.dismiss();
                    Toast.makeText(product_view.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(product_view.this);
            requestQueue.add(stringRequest);
        }catch (Exception e){

        }

        prorec.setAdapter(product_adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}