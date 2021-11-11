package com.sbitbd.fixedbd.ui.new_product;

import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.fixedbd.Adapter.pro_model;
import com.sbitbd.fixedbd.Adapter.product_adapter;
import com.sbitbd.fixedbd.Config.DoConfig;
import com.sbitbd.fixedbd.R;
import com.sbitbd.fixedbd.ui.home.HomeViewModel;

import java.util.HashMap;
import java.util.Map;

public class new_pro extends Fragment {

    private NewProViewModel mViewModel;
    private pro_model pro_model;
    private product_adapter product_adapter;
    private RecyclerView prorec;
    private DoConfig config = new DoConfig();
    private HomeViewModel homeViewModel = new HomeViewModel();
    private ProgressDialog loading;
    private Button morebtn;
    private int limit = 0;

    public static new_pro newInstance() {
        return new new_pro();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.new_pro_fragment, container, false);
        initView(root);
        return root;
    }
    private void initView(View root){
        prorec = root.findViewById(R.id.new_pro_rec);
        morebtn = root.findViewById(R.id.show_m_btn);
        prorec.setNestedScrollingEnabled(false);
        GridLayoutManager manager = new GridLayoutManager(root.getContext().getApplicationContext(), 2);
        prorec.setLayoutManager(manager);
        product_adapter = new product_adapter(root.getContext().getApplicationContext());
        show_product(root,limit);
        morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limit += 30;
                show_product(root,limit);
            }
        });
    }
    private void show_product(View root,int limit){

        try {
            loading = ProgressDialog.show(root.getContext(),"","Loading...",false,false);
            String sql = "SELECT `product_productinfo`.`id`,`product_productinfo`.`product_name`," +
                    "`product_productinfo`.`sale_price`,`product_productinfo`.`discount_price`," +
                    "`product_productinfo`.`current_price`,`product_productinfo`.`image` " +
                    "FROM `product_productinfo` WHERE `status` = '1' LIMIT "+limit+",30";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.PRO_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            if(!response.equals("1")){
                                homeViewModel.showProJSON(response,pro_model,product_adapter,root.getContext().getApplicationContext());
                            }
                            else {
                                Toast.makeText(root.getContext().getApplicationContext(),"Not found",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.dismiss();
                    Toast.makeText(root.getContext().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(root.getContext().getApplicationContext());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }catch (Exception e){
        }

        prorec.setAdapter(product_adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NewProViewModel.class);
        // TODO: Use the ViewModel
    }

}