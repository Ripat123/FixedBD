package com.sbitbd.fixedbd.ui.seller_form.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sbitbd.fixedbd.Adapter.four_model;
import com.sbitbd.fixedbd.Config.DoConfig;
import com.sbitbd.fixedbd.R;
import com.sbitbd.fixedbd.databinding.FragmentDashboardBinding;
import com.sbitbd.fixedbd.ui.seller_login.seller_login;

import java.util.HashMap;
import java.util.Map;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Context> context;
    private MutableLiveData<four_model> four_modelData;
    private MutableLiveData<Boolean> data_insert;
    private MutableLiveData<FragmentDashboardBinding> binding;
    private DoConfig config = new DoConfig();


    public DashboardViewModel() {
        mText = new MutableLiveData<>();
    }

    protected MutableLiveData<Context> getcontext() {
        if (context == null)
            context = new MutableLiveData<Context>();
        return context;
    }

    protected MutableLiveData<FragmentDashboardBinding> getBinding() {
        if (binding == null)
            binding = new MutableLiveData<>();
        return binding;
    }

    protected LiveData<Boolean> getData_insert(String sql) {
//        if (data_insert == null){
            data_insert = new MutableLiveData<>();
            data_insertion(sql);
//        }
        return data_insert;
    }

    public LiveData<String> getText() {
        return mText;
    }

    private void data_insertion(String sql) {
        try {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("1"))
                                data_insert.setValue(true);
                            else{
                                data_insert.setValue(false);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context.getValue(), error.toString(), Toast.LENGTH_SHORT).show();
                    data_insert.setValue(false);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context.getValue());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

        } catch (Exception e) {
        }
    }
}