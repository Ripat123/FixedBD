package com.sbitbd.fixed_bd.ui.seller_form.ui.notifications.view_tab;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.fixed_bd.Adapter.comision_adapter;
import com.sbitbd.fixed_bd.Adapter.four_model;
import com.sbitbd.fixed_bd.Config.DoConfig;
import com.sbitbd.fixed_bd.R;
import com.sbitbd.fixed_bd.databinding.FragmentCommisionWithdrawBinding;
import com.sbitbd.fixed_bd.databinding.FragmentMyCommisionBalanceBinding;
import com.sbitbd.fixed_bd.ui.home.HomeViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class commision_withdraw extends Fragment {


    private RecyclerView com_rec;
    private comision_adapter comision_adapter;
    private four_model four_model;
    private TextView total_t;
    private FragmentCommisionWithdrawBinding FragmentCommisionWithdrawBinding;
    private View root;
    private DoConfig config = new DoConfig();
    private HomeViewModel homeViewModel = new HomeViewModel();
    private double total=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentCommisionWithdrawBinding = FragmentCommisionWithdrawBinding.inflate(inflater, container, false);
        root = FragmentCommisionWithdrawBinding.getRoot();
        initView();
        return root;
    }

    private void initView() {
        try {
            com_rec = FragmentCommisionWithdrawBinding.comWithRec;
            total_t = FragmentCommisionWithdrawBinding.amountTt;
            GridLayoutManager manager = new GridLayoutManager(root.getContext().getApplicationContext(), 1);
            com_rec.setLayoutManager(manager);
            comision_adapter = new comision_adapter(root.getContext().getApplicationContext());
            initData(root, "select `date` as 'one',withdraw as 'three' from order_customer_commision " +
                    "where member_id = '"+homeViewModel.getSellerID(root.getContext().getApplicationContext())+"'");
//            four_model = new four_model("20-11-2021","54656546","5554","2");
//            comision_adapter.addUser(four_model);
//            four_model = new four_model("20-11-2021","54656546","5554","2");
//            comision_adapter.addUser(four_model);
//            four_model = new four_model("20-11-2021","54656546","5554","2");
//            comision_adapter.addUser(four_model);
//            four_model = new four_model("20-11-2021","54656546","5554","2");
//            comision_adapter.addUser(four_model);
//            com_rec.setAdapter(comision_adapter);
        } catch (Exception e) {
        }
    }

    private void initData(View root, String sql) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.FOUR_DMS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.trim().equals("")) {
                                showProJSON(response.trim());
                            }else
                                Toast.makeText(root.getContext().getApplicationContext(),"Empty",Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
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

        } catch (Exception e) {
        }

    }

    public void showProJSON(String response) {
        String proName,  id;
        try {
            total = 0;
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                try {
                    JSONObject collegeData = result.getJSONObject(i);
                    id = collegeData.getString(config.ONE);
                    proName = collegeData.getString(config.THREE);
                    four_model = new four_model(id, "", proName, "");
                    if (!proName.equals("") && !proName.equals("0") && !proName.equals("0.00"))
                        comision_adapter.addUser(four_model);
                    total += Double.parseDouble(proName.trim());
                } catch (Exception e) {
                }

            }
            com_rec.setAdapter(comision_adapter);
            total_t.setText(String.valueOf(total)+" ৳");
        } catch (Exception e) {
        }
    }
}