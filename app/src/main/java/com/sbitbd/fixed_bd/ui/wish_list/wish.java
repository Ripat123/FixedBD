package com.sbitbd.fixed_bd.ui.wish_list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.sbitbd.fixed_bd.Adapter.wish_adapter;
import com.sbitbd.fixed_bd.Adapter.wish_model;
import com.sbitbd.fixed_bd.R;

public class wish extends AppCompatActivity {

    RecyclerView recyclerView;
    wish_model wish_model;
    wish_adapter wish_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Wish List");
        initwish();
    }
    private void initwish(){
        recyclerView = findViewById(R.id.wish_rec);
        wish_adapter = new wish_adapter(wish.this);
        GridLayoutManager layoutManager = new GridLayoutManager(wish.this,1);
        recyclerView.setLayoutManager(layoutManager);
        wish_model = new wish_model("Inspiron 15","40000",R.drawable.sbit);
        wish_adapter.adduser(wish_model);
//        wish_model = new wish_model("Itell P11","5000",R.drawable.demo);
//        wish_adapter.adduser(wish_model);
//        wish_model = new wish_model("Samsung","20000",R.drawable.demo2);
//        wish_adapter.adduser(wish_model);
//        wish_model = new wish_model("HP 345","35000",R.drawable.demo1);
//        wish_adapter.adduser(wish_model);
        recyclerView.setAdapter(wish_adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}