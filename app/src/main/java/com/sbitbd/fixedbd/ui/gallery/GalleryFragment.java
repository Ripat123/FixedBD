package com.sbitbd.fixedbd.ui.gallery;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sbitbd.fixedbd.Adapter.order_adapter;
import com.sbitbd.fixedbd.Adapter.order_model;
import com.sbitbd.fixedbd.Config.DoConfig;
import com.sbitbd.fixedbd.Config.sqliteDB;
import com.sbitbd.fixedbd.R;
import com.sbitbd.fixedbd.ui.home.HomeViewModel;
import com.sbitbd.fixedbd.ui.login_page.ui.login.LoginActivity;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private RecyclerView order_rec;
    private order_model order_model;
    private order_adapter order_adapter;
    private HomeViewModel homeViewModel = new HomeViewModel();
    private DoConfig config = new DoConfig();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout notfound_id,order_sign;
    private Button sign;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        initorder(root);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }
    private void initorder(View root){
        order_rec = root.findViewById(R.id.order_rec);
        notfound_id = root.findViewById(R.id.notfound_id);
        order_sign = root.findViewById(R.id.order_sign_out_id);
        sign = root.findViewById(R.id.sing_in_btn_order);
        swipeRefreshLayout = root.findViewById(R.id.order_ref_id);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                order_view(root);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(root.getContext().getApplicationContext(), LoginActivity.class));

            }
        });
        order_view(root);

    }
    private void order_view(View root){
        try {
            order_adapter = new order_adapter(root.getContext().getApplicationContext());
            GridLayoutManager layoutManager = new GridLayoutManager(root.getContext().getApplicationContext(),1);
            order_rec.setLayoutManager(layoutManager);

            String email = "",pass = "",id = "",phone = "";
            sqliteDB sqliteDB = new sqliteDB(root.getContext().getApplicationContext());
            Cursor cursor = sqliteDB.getUerData("SELECT * FROM guest");
            if(cursor.getCount() > 0) {
                if (cursor.moveToNext()) {
                    email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                    phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                    id = cursor.getString(cursor.getColumnIndexOrThrow("guest_id"));
                }
            }
//            String gid = homeViewModel.getGuestID(root.getContext().getApplicationContext());
            if(id == null || id.equals("")){
                order_sign.setVisibility(View.GONE);
                String deviceUniqueIdentifier = Settings.Secure.getString(root.getContext().getApplicationContext()
                        .getContentResolver(), Settings.Secure.ANDROID_ID);
                String sql1 = "SELECT `invoices`.`invoice_id`,`shopping_carts`.`product_id`,`product_productinfo`.`product_name`," +
                        "`invoices`.`created_at`,`invoices`.`status` FROM `invoices` INNER JOIN `shopping_carts` ON `invoices`.`session_id` = " +
                        "`shopping_carts`.`session_id` INNER JOIN `product_productinfo` ON `shopping_carts`.`product_id` = " +
                        "`product_productinfo`.`id` WHERE `invoices`.`guest_id` = '1' AND `invoices`.`session_id` LIKE '" + deviceUniqueIdentifier + "%'";
                config.OrderData(root.getContext().getApplicationContext(), sql1, order_rec, notfound_id);
                //config.online_gid(root.getContext().getApplicationContext(),deviceUniqueIdentifier,order_rec,notfound_id);
            }else {
                order_sign.setVisibility(View.GONE);
                String sql = "SELECT `invoices`.`invoice_id`,`shopping_carts`.`product_id`,`product_productinfo`.`product_name`," +
                        "`invoices`.`created_at`,`invoices`.`status` FROM `invoices` INNER JOIN `shopping_carts` ON `invoices`.`session_id` = " +
                        "`shopping_carts`.`session_id` INNER JOIN `product_productinfo` ON `shopping_carts`.`product_id` = " +
                        "`product_productinfo`.`id` WHERE `invoices`.`guest_id` = '"+id+"' ORDER BY `invoices`.`id` DESC";
                config.OrderData(root.getContext().getApplicationContext(),sql,order_rec,notfound_id);
            }
        }catch (Exception e){

        }
    }
}