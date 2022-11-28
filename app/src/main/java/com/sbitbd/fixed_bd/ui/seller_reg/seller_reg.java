package com.sbitbd.fixed_bd.ui.seller_reg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sbitbd.fixed_bd.Config.DoConfig;
import com.sbitbd.fixed_bd.R;
import com.sbitbd.fixed_bd.ui.seller_login.seller_login;

import java.util.HashMap;
import java.util.Map;

public class seller_reg extends AppCompatActivity {

    private EditText first_n_s,last_n_s,busi_s,email_s,phone_s,address_s,pass_s,con_pass_s;
    private Button s_login_id,sign_in;
    private DoConfig config = new DoConfig();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_reg);
        initView();
    }

    private void initView(){
        try {
            first_n_s = findViewById(R.id.first_n_s);
            last_n_s = findViewById(R.id.last_n_s);
            busi_s = findViewById(R.id.busi_s);
            email_s = findViewById(R.id.email_s);
            phone_s = findViewById(R.id.phone_s);
            address_s = findViewById(R.id.address_s);
            pass_s = findViewById(R.id.pass_s);
            con_pass_s = findViewById(R.id.con_pass_s);
            s_login_id = findViewById(R.id.s_login_id);
            sign_in = findViewById(R.id.sign_in);

            s_login_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit();
                }
            });
            sign_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(seller_reg.this, seller_login.class));
                }
            });
        }catch (Exception e){
        }
    }

    private void submit(){
        try {
            if (first_n_s.getText().toString().trim().equals("")) {
                first_n_s.setError("Empty First Name");
                Toast.makeText(seller_reg.this, "Empty First Name", Toast.LENGTH_LONG).show();
                return;
            }
            if (last_n_s.getText().toString().trim().equals("")) {
                last_n_s.setError("Empty Last Name");
                Toast.makeText(seller_reg.this, "Empty Last Name", Toast.LENGTH_LONG).show();
                return;
            }
            if (busi_s.getText().toString().trim().equals("")) {
                busi_s.setError("Empty Business Name");
                Toast.makeText(seller_reg.this, "Empty Business Name", Toast.LENGTH_LONG).show();
                return;
            }
            if (email_s.getText().toString().trim().equals("")) {
                email_s.setError("Empty Email");
                Toast.makeText(seller_reg.this, "Empty Email", Toast.LENGTH_LONG).show();
                return;
            }
            if (phone_s.getText().toString().trim().equals("")) {
                phone_s.setError("Empty Phone");
                Toast.makeText(seller_reg.this, "Empty Phone", Toast.LENGTH_LONG).show();
                return;
            }
            if (address_s.getText().toString().trim().equals("")) {
                address_s.setError("Empty Address");
                Toast.makeText(seller_reg.this, "Empty Address", Toast.LENGTH_LONG).show();
                return;
            }
            if (pass_s.getText().toString().trim().equals("")) {
                pass_s.setError("Empty Password");
                Toast.makeText(seller_reg.this, "Empty Password", Toast.LENGTH_LONG).show();
                return;
            }
            if (pass_s.getText().toString().length() < 5) {
                pass_s.setError("Password must be >= 5 character");
                Toast.makeText(seller_reg.this, "Password must be >= 8 character", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!pass_s.getText().toString().equals(con_pass_s.getText().toString())) {
                con_pass_s.setError("Password didn't matched");
                Toast.makeText(seller_reg.this, "Password didn't matched", Toast.LENGTH_SHORT).show();
                return;
            }
            check_phone(seller_reg.this,first_n_s.getText().toString().trim(),last_n_s.getText().toString().trim(),
                    busi_s.getText().toString().trim(),phone_s.getText().toString().trim(),email_s.getText().toString().trim(),pass_s.getText().toString()
            ,address_s.getText().toString().trim());
        }catch (Exception e){
        }
    }

    public void check_phone(Context context, String firstname,String lastname,String business, String phone, String email,
                                  String password, String address) {
        progressDialog = ProgressDialog.show(seller_reg.this, "", "Proccessing...", false, false);
        try {
            String sql = "SELECT id FROM sellers WHERE phone = '" + phone + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (!response.equals("")) {
                                Toast.makeText(context, "Phone already taken", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            } else {
                                check_email(context, firstname,lastname,business, phone, email, password, address, progressDialog);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }
    public void check_email(Context context, String firstname,String lastname,String business, String phone, String email,
                                  String password, String address,ProgressDialog progressDialog) {

        try {
            String sql = "SELECT id FROM sellers WHERE email = '" + email + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (!response.equals("")) {
                                Toast.makeText(context, "Email already taken", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            } else {
                                insertuserData(context, firstname,lastname,business, phone, email, password, address, progressDialog);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    public void insertuserData(Context context, String firstname,String lastname,String business, String phone, String email,
                               String password, String address, ProgressDialog progressDialog) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.SELLER_REG,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
//                            Toast.makeText(seller_reg.this,response,Toast.LENGTH_LONG).show();
                            if (!response.equals("Could not Registered in online") && !response.equals("")) {
                                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(seller_reg.this);
                                dialogBuilder.setIcon(R.drawable.icons8_ok_48px);
                                dialogBuilder.setTitle("Registration Successful");
                                dialogBuilder.setMessage("Please wait for Approve.");
                                dialogBuilder.setCancelable(false);
                                dialogBuilder.setPositiveButton("OK",(dialog, which) -> {
                                    startActivity(new Intent(seller_reg.this, seller_login.class));
                                });
                                dialogBuilder.show();

                            } else {
                                Toast.makeText(context, "Registration Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.FIRST_N, firstname);
                    params.put(config.LAST_N, lastname);
                    params.put(config.BUSINESS, business);
                    params.put(config.EMAIL, email);
                    params.put(config.PHONE, phone);
                    params.put(config.ADDRESS, address);
                    params.put(config.PASS, password);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }
}