package com.sbitbd.fixed_bd.ui.contact;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.fixed_bd.Config.DoConfig;
import com.sbitbd.fixed_bd.R;
import com.sbitbd.fixed_bd.ui.cart_operation.operation;

import java.util.HashMap;
import java.util.Map;

public class contact extends Fragment {

    private ContactViewModel mViewModel;
    private EditText name,email,question;
    private Button send_btn;
    private WebView webView;
    private DoConfig config = new DoConfig();
    private operation operation = new operation();
    private ProgressDialog progressDialog;

    public static contact newInstance() {
        return new contact();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.contact_fragment, container, false);
        initview(root);
        return root;
    }
    private void initview(View root){
        name = root.findViewById(R.id.first);
        email = root.findViewById(R.id.email_t);
        question = root.findViewById(R.id.question);
        send_btn = root.findViewById(R.id.send_btn);
        webView = root.findViewById(R.id.con_webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        loadData(root);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlineInsert(root,v);
            }
        });
    }
    private void onlineInsert(View root,View v){
        String date = operation.getCreateDate();
        progressDialog = ProgressDialog.show(root.getContext(),"","Sending...",false,false);
        try {
            String sql = "INSERT INTO `customer_messages` (`name`,`email`,`description`,`created_at`) " +
                    "VALUES ('"+name.getText().toString()+"','"+email.getText().toString()+"'," +
                    "'"+question.getText().toString()+"','"+date+"')";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("1")){
                                progressDialog.dismiss();
                                operation.regularSnak("Successfully Send",v);

                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
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
    private void loadData(View root){
        try {

            String sql = "SELECT `contact_us`.`description` AS 'id' FROM `contact_us`";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response != null && !response.equals("") ){
                                webView.loadData("<html><body>"+response+"</body></html>",
                                        "text/html; charset=utf-8", "UTF-8");
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