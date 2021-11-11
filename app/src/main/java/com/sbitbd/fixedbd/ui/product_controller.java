package com.sbitbd.fixedbd.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sbitbd.fixedbd.Config.DoConfig;
import com.sbitbd.fixedbd.R;
import com.sbitbd.fixedbd.ui.checkout.checkout;
import com.sbitbd.fixedbd.ui.home.HomeViewModel;
import com.sbitbd.fixedbd.ui.seller_login.seller_login;
import com.sbitbd.fixedbd.ui.seller_reg.seller_reg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class product_controller {

    private ProgressDialog progressDialog;
    private DoConfig config = new DoConfig();
    private HomeViewModel homeViewModel = new HomeViewModel();
    private int otp = 0;
    ;

    protected void number_verify(Context context) {
        try {
            View view = LayoutInflater.from(context).inflate(R.layout.sign_lay, null);
            EditText phoneE = view.findViewById(R.id.phone_sign);
            EditText pass = view.findViewById(R.id.pass_sign);
            Button send = view.findViewById(R.id.send_otp);
            Button login = view.findViewById(R.id.login);
            Button otp_login = view.findViewById(R.id.login_otp);
            Button create = view.findViewById(R.id.create_btn);
            MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context, R.style.RoundShapeTheme);

            dialogBuilder.setView(view);
            AlertDialog alertDialog = dialogBuilder.create();
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (phoneE.getText().toString().equals("")) {
                        phoneE.setError("Empty Number");
                        Toast.makeText(context, "Empty Number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (phoneE.getText().toString().length() < 11) {
                        phoneE.setError("Phone Number must be 11 characters");
                        Toast.makeText(context, "Phone Number must be 11 characters", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (pass.getText().toString().equals("")) {
                        pass.setError("Empty Password");
                        Toast.makeText(context, "Empty Password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    progressDialog = ProgressDialog.show(context, "", "Loading", false, false);
                    if (otp == 0)
                        signin(context, phoneE.getText().toString().trim(), pass.getText().toString(), progressDialog);
                    else
                        otp_login(context, phoneE.getText().toString().trim(), pass.getText().toString().trim(), progressDialog);
                    alertDialog.dismiss();
                }
            });
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    send(context, phoneE.getText().toString().trim());
                    otp = 1;
                }
            });
//            otp_login.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (phoneE.getText().toString().equals("")) {
//                        phoneE.setError("Empty Number");
//                        Toast.makeText(context, "Empty Number", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    if (phoneE.getText().toString().length() < 11) {
//                        phoneE.setError("Phone Number must be 11 characters");
//                        Toast.makeText(context, "Phone Number must be 11 characters", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    if (pass.getText().toString().equals("")) {
//                        pass.setError("Empty OTP");
//                        Toast.makeText(context, "Empty OTP", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    otp_login(context, phoneE.getText().toString().trim(), pass.getText().toString().trim());
//                    alertDialog.dismiss();
//                }
//            });
            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    View view1 = LayoutInflater.from(context).inflate(R.layout.signup_lay, null);
                    EditText username_dis = view1.findViewById(R.id.username_dis);
                    EditText email_txt_dis = view1.findViewById(R.id.email_txt_dis);
                    EditText business = view1.findViewById(R.id.business);
                    EditText address_d = view1.findViewById(R.id.address_d);
                    EditText con_pass_sign = view1.findViewById(R.id.con_pass_sign);
                    EditText phoneE = view1.findViewById(R.id.phone_sign);
                    EditText pass = view1.findViewById(R.id.pass_sign);
                    EditText refer = view1.findViewById(R.id.refer);
                    Button reg = view1.findViewById(R.id.reg);
                    Button create = view1.findViewById(R.id.create_btn);
                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context, R.style.RoundShapeTheme);

                    dialogBuilder.setView(view1);
                    AlertDialog alertDialog1 = dialogBuilder.create();
                    reg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String refer_code = "";
                            if (phoneE.getText().toString().equals("")) {
                                phoneE.setError("Empty Number");
                                Toast.makeText(context, "Empty Number", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (phoneE.getText().toString().length() < 11) {
                                phoneE.setError("Phone Number must be 11 characters");
                                Toast.makeText(context, "Phone Number must be 11 characters", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (pass.getText().toString().equals("")) {
                                pass.setError("Empty Password");
                                Toast.makeText(context, "Empty Password", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (username_dis.getText().toString().equals("")) {
                                username_dis.setError("Empty Name");
                                Toast.makeText(context, "Empty Name", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (business.getText().toString().equals("")) {
                                business.setError("Empty Business Name");
                                Toast.makeText(context, "Empty Business Name", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (!con_pass_sign.getText().toString().equals(pass.getText().toString())) {
                                con_pass_sign.setError("Password did not matched");
                                Toast.makeText(context, "Password did not matched", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (refer.getText().toString().equals(""))
                                refer_code = "0";
                            else
                                refer_code = refer.getText().toString().trim();

                            check_phone(context, username_dis.getText().toString().trim(), business.getText().toString()
                                            .trim(), phoneE.getText().toString().trim(), email_txt_dis.getText().toString().trim()
                                    , pass.getText().toString(), address_d.getText().toString().trim(), refer_code);
                            alertDialog1.dismiss();
                        }
                    });
                    create.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    alertDialog1.show();
                }
            });
//            getCode(phoneE.getText().toString());

            alertDialog.show();
        } catch (Exception e) {
        }
    }

    private void insert_reg(Context context, String name, String business, String phone, String email,
                            String password, String address, String refer, ProgressDialog progressDialog) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.SELLER_REG,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("Could not Registered in online") && !response.equals("")) {
                                if (homeViewModel.getGuestID(context) == null || homeViewModel.getGuestID(context).equals(""))
                                    insertuserData(context, name, response.trim(), phone, email, password, address, progressDialog);
                                else
                                    guest_update(context,response.trim(), homeViewModel.getGuestID(context), progressDialog);
                                putPref("seller",response.trim(),context);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show();
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
                    params.put(config.FIRST_N, name);
                    params.put(config.LAST_N, refer);
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

    public void check_phone(Context context, String firstname, String business, String phone, String email,
                            String password, String address, String refer) {
        progressDialog = ProgressDialog.show(context, "", "Proccessing...", false, false);
        try {
            String sql = "SELECT id FROM sellers WHERE phone = '" + phone + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.trim().equals("")) {
                                Toast.makeText(context, "Phone already taken", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            } else {
                                insert_reg(context, firstname, business, phone, email, password,
                                        address, refer, progressDialog);
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

    public void insertuserData(Context context, String firstname, String seller_id, String phone, String email,
                               String password, String address, ProgressDialog progress) {
        try {
            homeViewModel = new HomeViewModel();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_GUEST_REG,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progress.dismiss();
                            if (!response.equals("Could not Registered in online") && !response.equals("")) {
                                homeViewModel.insertuser(context, firstname, phone, email, password, response);

                                Toast.makeText(context, "Registered Successful! Please click again", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Failed to guest Sign up", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.FIRST_N, firstname);
                    params.put(config.LAST_N, seller_id);
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

    private void signin(Context context, String username, String pass, ProgressDialog progressDialog) {
        try {


//            String sql = "SELECT id FROM `guest` WHERE (email = '"+username+"' OR phone = '"+username+"') AND password = '"+pass+"'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_LOGIN_INFO,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("1") && !response.equals("") && !response.equals("{\"result\":[]}")) {
                                show_user_data(response, context);

                            } else {
                                otp_login(context, username, pass, progressDialog);
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
                    params.put(config.QUERY, username);
                    params.put(config.PRO_SIZE, pass);
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

    public void show_user_data(String response, Context context) {
        try {
            String firstname = "", lastname = "", email = "", phone = "", gid = "", Address = "";

            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                gid = collegeData.getString(config.CAT_ID);
                firstname = collegeData.getString(config.PRO_NAME);
//                    lastname = collegeData.getString(config.CAT_NAME);
                email = collegeData.getString(config.EMAIL);
                phone = collegeData.getString(config.PHONE);
//                    Address = collegeData.getString(config.PRO_SALE_PRICE);

                homeViewModel.insertuser(context, firstname, phone, email, "", gid);
                progressDialog.dismiss();
                Toast.makeText(context, "Login Successful, Please click again", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
        }
    }

    private void send(Context context, String phone) {
        try {
            progressDialog = ProgressDialog.show(context, "", "Sending...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.OTP_SEND,
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

    private void otp_login(Context context, String username, String otp, ProgressDialog progressDialog) {
        try {

            String sql = "SELECT id FROM `guest` WHERE phone = '" + username + "' AND code = '" + otp + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (!response.trim().equals("")) {
                                homeViewModel.insertuser(context, "", username, "", "", response.trim());

                            } else {
                                Toast.makeText(context, "Failed, Please try again", Toast.LENGTH_LONG).show();
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

    private void guest_update(Context context, String seller, String guest, ProgressDialog progressDialog) {
        try {

            String sql = "UPDATE guest SET seller_id = '"+seller+"' WHERE id = '" + guest + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response.trim().equals("1")) {
                                Toast.makeText(context, "Registered Successful! Please click again", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(context, "Guest updating Failed", Toast.LENGTH_LONG).show();
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

    public void putPref(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getPref(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}
