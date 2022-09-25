package com.sbitbd.fixedbd.ui.sh_cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sbitbd.fixedbd.Adapter.cart_adapter;
import com.sbitbd.fixedbd.Adapter.cart_model;
import com.sbitbd.fixedbd.Config.sqliteDB;
import com.sbitbd.fixedbd.R;
import com.sbitbd.fixedbd.ui.cart_operation.operation;
import com.sbitbd.fixedbd.ui.checkout.checkout;
import com.sbitbd.fixedbd.ui.home.HomeViewModel;

public class sh_cart extends AppCompatActivity {

    private Button check_out_btn,homebtn;
    private cart_adapter cart_adapter;
    private cart_model cart_model;
    RecyclerView recyclerView;
    private HomeViewModel homeViewModel;
    private operation operation;
    private TextView total_tex;
    private ConstraintLayout empty_cart;
    private int total_item = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sh_cart);
        check_out_btn = findViewById(R.id.check_btn);
        total_tex = findViewById(R.id.totalnum_id);
        empty_cart=findViewById(R.id.empty_cart_id);
        homebtn = findViewById(R.id.home_btn);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) check_out_btn.getLayoutParams();
        params.width = (getResources().getDisplayMetrics().widthPixels)/3;
//        params.height = ((getResources().getDisplayMetrics().heightPixels)/3)/3;
        check_out_btn.setLayoutParams(params);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Shopping Cart");
        inititem();
        homebtn.setOnClickListener(v -> finish());
        check_out_btn.setOnClickListener(v -> startActivity(new Intent(sh_cart.this, checkout.class)));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void inititem(){
        cart_adapter = new cart_adapter(sh_cart.this,total_tex,check_out_btn,empty_cart);
        recyclerView = findViewById(R.id.cart_rec);
        GridLayoutManager manager = new GridLayoutManager(sh_cart.this, 1);
        recyclerView.setLayoutManager(manager);
//        cart_model = new cart_model("Inspiron 15","150 x 1","150","1",R.drawable.sbit);

        getofflineData(cart_adapter);
        recyclerView.setAdapter(cart_adapter);

    }
    private void getofflineData(cart_adapter cart_adapter){
        String proID = "";
        String status = "";
        String quantity = "";
        String color = "";
        String size = "";
        String min_quant = "";
        homeViewModel = new HomeViewModel();
        operation = new operation();
        sqliteDB sqliteDB = new sqliteDB(sh_cart.this);
        try {
            Cursor cursor = sqliteDB.getUerData("SELECT * FROM shopping_carts WHERE session_id = '"+homeViewModel.getSession(sh_cart.this)+"'");
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    proID = cursor.getString(cursor.getColumnIndexOrThrow("product_id"));
                    status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                    quantity = cursor.getString(cursor.getColumnIndexOrThrow("quantity"));
                    color = cursor.getString(cursor.getColumnIndexOrThrow("color"));
                    size = cursor.getString(cursor.getColumnIndexOrThrow("size"));
                    min_quant = cursor.getString(cursor.getColumnIndexOrThrow("min_quantity"));
                        if(status.equals("0")){
                            operation.onlineCart(sh_cart.this,proID,quantity,min_quant,cart_adapter,
                                    check_out_btn,empty_cart,color,size);
                        }
                }
            }else {
                check_out_btn.setEnabled(false);
                empty_cart.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
        }
        finally {
            try {
                sqliteDB.close();
            }catch (Exception e){
            }
        }
    }
}