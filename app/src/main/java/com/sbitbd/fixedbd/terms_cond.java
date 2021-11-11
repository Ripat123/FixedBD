package com.sbitbd.fixedbd;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.fixedbd.Config.DoConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class terms_cond extends Fragment {

    private WebView terms_web;
    private DoConfig config = new DoConfig();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_terms_cond, container, false);
        terms_web = root.findViewById(R.id.terms_webview);
        WebSettings webSettings = terms_web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        loadData(root);
        return root;
    }

    private void loadData(View root){
        try {

            String sql = "SELECT description AS 'id' FROM `terms_use`";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.COUPON,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(!response.equals("1")){
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray result = jsonObject.getJSONArray(config.RESULT);
                                    JSONObject collegeData = result.getJSONObject(0);
                                    String data = collegeData.getString(config.CAT_ID);
                                    terms_web.loadData("<html><body>"+data+"</body></html>",
                                            "text/html; charset=utf-8", "UTF-8");
                                }catch (Exception e){
                                }
                            }
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
            requestQueue.add(stringRequest);
        }catch (Exception e){
        }
    }
}