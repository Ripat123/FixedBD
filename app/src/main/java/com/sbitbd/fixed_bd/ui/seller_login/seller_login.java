package com.sbitbd.fixed_bd.ui.seller_login;

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
import com.sbitbd.fixed_bd.Config.DoConfig;
import com.sbitbd.fixed_bd.MainActivity;
import com.sbitbd.fixed_bd.R;
import com.sbitbd.fixed_bd.ui.home.HomeViewModel;
import com.sbitbd.fixed_bd.ui.seller_form.seller_nav;
import com.sbitbd.fixed_bd.ui.seller_reg.seller_reg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class seller_login extends AppCompatActivity {

    private Button sign_up, forget_btn, login_btn, otp_btn, otp_login;
    private EditText email_s, pass_s;
    private DoConfig config = new DoConfig();
    private ProgressDialog progressDialog;
    private HomeViewModel homeViewModel = new HomeViewModel();
    private MainActivity mainActivity = new MainActivity();
    private int otp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);
        initView();
    }

    private void initView() {
        sign_up = findViewById(R.id.sign_up);
        forget_btn = findViewById(R.id.forgot_btn);
        login_btn = findViewById(R.id.s_login_id);
        email_s = findViewById(R.id.email_s);
        pass_s = findViewById(R.id.pass_s);
        otp_btn = findViewById(R.id.otp_btn);
        otp_login = findViewById(R.id.otp_login);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(seller_login.this, seller_reg.class));
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(seller_login.this,seller_nav.class));
                check_phone_email(v);
            }
        });
        otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send(seller_login.this, email_s.getText().toString().trim());
                otp = 1;
            }
        });
//        otp_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                otp_login(seller_login.this,email_s.getText().toString().trim(),pass_s.getText().toString());
//            }
//        });
    }

    public void check_phone_email(View v) {

        try {
            String sql = "SELECT id FROM sellers WHERE (`email` = '" + email_s.getText().toString().trim() + "' OR `phone` = '" + email_s.getText().toString().trim() + "') AND `status` ='0'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (!response.equals("")) {
                                mainActivity.regularSnak("Please wait for Approve your Account", v);
//                                Toast.makeText(seller_login.this, "Please wait for approve your account", Toast.LENGTH_LONG).show();
                            } else {
                                if (otp == 0)
                                    sellerLogin();
                                else {
                                    progressDialog = ProgressDialog.show(seller_login.this,"","Loading...",false,false);
                                    otp_login(seller_login.this, email_s.getText().toString().trim(), pass_s.getText().toString(), progressDialog);
                                }
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(seller_login.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(seller_login.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private void send(Context context, String phone) {
        try {
            progressDialog = ProgressDialog.show(context, "", "Sending...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.SELLER_OTP,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response.trim().equals("1")) {
                                Toast.makeText(context, "OTP not sent", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(context, "OTP sent successful, Please wait for a moment.", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.PHONE, phone);
                    params.put(config.CAT_ID, "");
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

    private void sellerLogin() {
        try {
            String user, password;
            user = email_s.getText().toString().trim();
            password = pass_s.getText().toString().trim();
            progressDialog = ProgressDialog.show(seller_login.this, "", "verifying", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.SELLER_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null && !response.equals("") && !response.trim().equals("{\"result\":[]}")) {
                                showJson(response, user, password);
                                progressDialog.dismiss();
                            } else {
                                otp_login(seller_login.this, user, password, progressDialog);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(seller_login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, user);
                    params.put(config.PRO_SIZE, password);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(seller_login.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }

    }

    private void showJson(String response, String user, String pass) {
        String name = "", id = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                name = collegeData.getString(config.PRO_NAME);
                id = collegeData.getString(config.CAT_ID);
                startActivity(new Intent(seller_login.this, seller_nav.class));
                if (homeViewModel.checkSeller(seller_login.this)) {
                    homeViewModel.updateSeller(seller_login.this, id);
                } else {
                    homeViewModel.insertSeller(seller_login.this, name, "", user
                            , pass, id);
                }
            }
        } catch (Exception e) {
        }
    }

    private void otp_login(Context context, String username, String otp, ProgressDialog progressDialog) {
        try {
            String sql = "SELECT id FROM `sellers` WHERE phone = '" + username + "' AND code = '" + otp + "' AND status = '1'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (!response.trim().equals("")) {
                                if (homeViewModel.checkSeller(seller_login.this)) {
                                    homeViewModel.updateSeller(seller_login.this, response.trim());
                                } else {
                                    homeViewModel.insertSeller(seller_login.this, "", username, ""
                                            , "", response.trim());
                                }
                                startActivity(new Intent(seller_login.this, seller_nav.class));
                            } else {
                                Toast.makeText(context, "Please wait for Approve your Account", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
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

}