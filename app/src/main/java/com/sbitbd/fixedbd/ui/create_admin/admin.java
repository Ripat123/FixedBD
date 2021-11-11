package com.sbitbd.fixedbd.ui.create_admin;

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
import com.sbitbd.fixedbd.Config.DoConfig;
import com.sbitbd.fixedbd.MainActivity;
import com.sbitbd.fixedbd.R;
import com.sbitbd.fixedbd.ui.cart_operation.operation;
import com.sbitbd.fixedbd.ui.home.HomeViewModel;

import java.util.HashMap;
import java.util.Map;

public class admin extends AppCompatActivity {
    private EditText first_n, phone, email, address, password, confirm_pass;
    private Button reg;
    private operation operation;
    private DoConfig config = new DoConfig();
    private ProgressDialog progressDialog;
    private HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        reg = findViewById(R.id.register);
        first_n = findViewById(R.id.username);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email_txt);
        address = findViewById(R.id.add_txt);
        password = findViewById(R.id.vp_ed);
        confirm_pass = findViewById(R.id.vc_ed);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    registration();

                } catch (Exception e) {
                }

            }
        });
    }

    private void registration() {
        try {
            if (first_n.getText().toString().trim().equals("")) {
                first_n.setError("Empty First Name");
                Toast.makeText(admin.this, "Empty First Name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (phone.getText().toString().trim().equals("")) {
                phone.setError("Empty Phone Number");
                Toast.makeText(admin.this, "Empty Phone Number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (phone.getText().toString().length() < 11) {
                phone.setError("Phone number must be 11 character");
                Toast.makeText(admin.this, "Phone number must be 11 character", Toast.LENGTH_SHORT).show();
                return;
            }
            if (address.getText().toString().trim().equals("")) {
                address.setError("Empty Address");
                Toast.makeText(admin.this, "Empty Address", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.getText().toString().equals("")) {
                password.setError("Empty Password");
                Toast.makeText(admin.this, "Empty Password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.getText().toString().length() < 8) {
                password.setError("Password must be >= 8 character");
                Toast.makeText(admin.this, "Password must be >= 8 character", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.getText().toString().equals(confirm_pass.getText().toString())) {
                confirm_pass.setError("Password didn't matched");
                Toast.makeText(admin.this, "Password didn't matched", Toast.LENGTH_SHORT).show();
                return;
            }
            operation = new operation();
            check_phone_email(admin.this,first_n.getText().toString().trim(),phone.getText().toString().trim()
            ,email.getText().toString().trim(),password.getText().toString(),address.getText().toString().trim());

        } catch (Exception e) {
        }
    }

    public void check_phone_email(Context context, String firstname, String phone, String email,
                                  String password, String address) {
        progressDialog = ProgressDialog.show(admin.this, "", "Proccessing...", false, false);
        try {
            String sql = "SELECT id FROM guest WHERE phone = '" + phone + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (!response.equals("")) {
                                Toast.makeText(context, "Phone already taken", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            } else {
                                insertuserData(context, firstname, phone, email, password, address, progressDialog);
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

    public void insertuserData(Context context, String firstname, String phone, String email,
                               String password, String address, ProgressDialog progressDialog) {
        try {
            homeViewModel = new HomeViewModel();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_GUEST_REG,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (!response.equals("Could not Registered in online") && !response.equals("")) {
                                homeViewModel.insertuser(context, firstname, phone, email, password, response);
                                Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(admin.this, MainActivity.class));
                            } else {
                                Toast.makeText(context, "Failed to Sign up", Toast.LENGTH_LONG).show();
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
                    params.put(config.LAST_N, "");
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