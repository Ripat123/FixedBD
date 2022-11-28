package com.sbitbd.fixed_bd.ui.cart_operation;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.fixed_bd.Adapter.cart_adapter;
import com.sbitbd.fixed_bd.Adapter.cart_model;
import com.sbitbd.fixed_bd.Adapter.checkout_pro_adapter;
import com.sbitbd.fixed_bd.Adapter.checkout_pro_model;
import com.sbitbd.fixed_bd.Adapter.pro_model;
import com.sbitbd.fixed_bd.Config.DoConfig;
import com.sbitbd.fixed_bd.Config.sqliteDB;
import com.sbitbd.fixed_bd.MainActivity;
import com.sbitbd.fixed_bd.ui.checkout.checkout;
import com.sbitbd.fixed_bd.ui.home.HomeViewModel;
import com.sbitbd.fixed_bd.ui.invoice;
import com.sbitbd.fixed_bd.ui.login_page.ui.login.LoginActivity;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class operation {
    private HomeViewModel homeViewModel = new HomeViewModel();
    private String session;
    private MainActivity mainActivity;
    private DoConfig config = new DoConfig();
    private checkout_pro_model cat_models;

    public operation() {
    }

    public void cartInsert(Context context, String proID, String quant,String min, View v,String color,String size) {
        sqliteDB sqlite_db = new sqliteDB(context);
        try {
            mainActivity = new MainActivity();
            ContentValues contentValues = new ContentValues();
            homeViewModel = new HomeViewModel();
            session = homeViewModel.getSession(context);

            SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date todayDate = new Date();
            String thisDate = currentDate.format(todayDate);

            Cursor cursor = sqlite_db.getUerData("SELECT * FROM shopping_carts WHERE product_id = " +
                    "'" + proID + "' AND session_id = '" + session + "' AND color = '" + color + "' AND size = '" + size + "'");
            if (cursor.getCount() > 0) {
                if (cursor.moveToNext()) {
                    int old_quant = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                    int total_quant = Integer.parseInt(quant) + old_quant;
                    String net_quant = String.valueOf(total_quant);
                    boolean check = update(context, net_quant, session, proID,color,size);
                    if (check) {
                        onlinUpdate(context, net_quant, session, proID,color,size);
                        stock(quant,proID,color,size,context);
                    } else {
                        Toast.makeText(context, "Update Failed", Toast.LENGTH_LONG).show();
                    }
                } else {
                    insertC(contentValues, sqlite_db, context, proID, quant,min, thisDate, session, v,color,size);
                }
            } else {
                insertC(contentValues, sqlite_db, context, proID, quant,min, thisDate, session, v,color,size);
            }
        } catch (Exception e) {
        }
        finally {
            try {
                sqlite_db.close();
            }catch (Exception e){
            }
        }
    }

    public void stock(String quant,String id,String color,String size,Context context){
        try {
            String sql = "UPDATE `productstocks` SET `quentity` = `quentity` - '"+quant+"' WHERE `product_id` " +
                    "= '"+id+"' AND `size` = '"+size+"' AND `color` = '"+color+"'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("1")) {
                                Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(DoConfig.QUERY, sql);
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

    private void insertC(ContentValues contentValues, sqliteDB sqlite_db, Context context,
                         String proID, String quant,String min, String thisDate, String session, View v,String color,String size) {
        try {
            contentValues.put("product_id", proID);
            contentValues.put("session_id", session);
            contentValues.put("status", "0");
            contentValues.put("quantity", quant);
            contentValues.put("min_quantity", min);
            contentValues.put("color", color);
            contentValues.put("size", size);
            contentValues.put("created_at", thisDate);
            Boolean ch = sqlite_db.DataOperation(contentValues, "insert", "shopping_carts", null);
            if (ch) {
                onlineInsert(context, proID, quant, session, thisDate, v,color,size);
                stock(quant,proID,color,size,context);
            } else
                Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
        }
        finally {
            try {
                sqlite_db.close();
            }catch (Exception e){
            }
        }
    }

    private void onlineInsert(Context context, String proID, String quant, String Session, String date, View v,String color,String size) {
        try {
            String sql = "INSERT INTO `shopping_carts` (`product_id`,`session_id`,`status`,`quantity`,color,size," +
                    "`created_at`) VALUES ('" + proID + "','" + Session + "','0','" + quant + "','" + color + "','" + size + "','" + date + "')";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("1")) {
                                Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                            } else {
                                regularSnak("Added", v);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(DoConfig.QUERY, sql);
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

    public void showCartJSON(String response, String quant, String min_quant, cart_adapter cart_adapter,
                             String id,String color,String size) {
        double price = 0;
        String name = "";
        String img = "";
        config = new DoConfig();
        cart_model cart_model;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(DoConfig.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                name = collegeData.getString(DoConfig.PRO_NAME);
                img = collegeData.getString(DoConfig.PRO_IMAGE);
                price = Double.parseDouble(collegeData.getString(DoConfig.PRO_CURRENT_PRICE));
                double d_quant = Double.parseDouble(quant);
                double total = price * d_quant;
//                cart_model = new cart_model("Inspiron 15","150 x 1","150","1", R.drawable.sbit);
                cart_model = new cart_model(name, String.valueOf(price), quant, total, img, min_quant, id,color,size);
                cart_adapter.adduser(cart_model);
            }

        } catch (Exception e) {
        }
    }

    public void onlineCart(Context context, String id, String quant, String min_quant, cart_adapter
            cart_adapter, Button check_out_btn, ConstraintLayout empty_cart,String color,String size) {
        try {
            String sql = "SELECT `product_productinfo`.`product_name`,`product_productinfo`.`current_price`," +
                    "`product_productinfo`.`image` FROM `product_productinfo` WHERE `product_productinfo`.id = '" + id + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.CART_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("1")) {
                                showCartJSON(response, quant, min_quant, cart_adapter, id,color,size);
                            } else {
                                check_out_btn.setEnabled(false);
                                empty_cart.setVisibility(View.VISIBLE);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    check_out_btn.setEnabled(false);
                    empty_cart.setVisibility(View.VISIBLE);
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(DoConfig.QUERY, sql);
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

    public pro_model showProDesJSON(String response) {
        String id = "", proName = "", size = "", price = "", dis_val = "", dis_price = "", image = "";

        config = new DoConfig();
        pro_model cat_models;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(DoConfig.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                proName = collegeData.getString(DoConfig.PRO_NAME);
                size = collegeData.getString(DoConfig.CAT_NAME);
                price = collegeData.getString(DoConfig.PRO_DISCOUNT_PRICE);
                dis_price = collegeData.getString(DoConfig.EMAIL);
                dis_val = collegeData.getString(DoConfig.PRO_CURRENT_PRICE);
                image = collegeData.getString(DoConfig.PRO_IMAGE);

                cat_models = new pro_model(image, proName, size, price, dis_val, id, dis_price);
                return cat_models;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public boolean update(Context context, String quantity, String Session, String proID,String color,String size) {
        boolean check = false;
        sqliteDB sqliteDB = new sqliteDB(context);
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("quantity", quantity);
            check = sqliteDB.DataOperation(contentValues, "update", "shopping_carts",
                    "session_id = '" + Session + "' AND product_id = '" + proID + "' AND " +
                            "color = '" + color + "' AND size = '" + size + "'");

        } catch (Exception e) {
        }
        finally {
            try {
                sqliteDB.close();
            }catch (Exception e){
            }
        }
        return check;
    }

    public void onlinUpdate(Context context, String quantity, String Session, String proID,String color,String size) {
        try {
            String sql = "UPDATE shopping_carts SET quantity = '" + quantity + "' WHERE session_id = " +
                    "'" + Session + "' AND product_id = '" + proID + "' AND color = '" + color + "' AND size = '" + size + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("1")) {
                                Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(DoConfig.QUERY, sql);
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

    public void deleteCart(String proID,String color,String size, Context context) {
        sqliteDB sqliteDB = new sqliteDB(context);
        try {
            homeViewModel = new HomeViewModel();
            String ses = homeViewModel.getSession(context);
            boolean check = sqliteDB.DataOperation(null, "delete", "shopping_carts",
                    "product_id = '" + proID + "' AND session_id = '" + ses + "' AND color = '"+color+"' AND size = '"+size+"'");
            if (check) {
                onlineDelete(proID, context, ses,color,size);
            } else {
                Toast.makeText(context, "failed", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
        }
        finally {
            try {
                sqliteDB.close();
            }catch (Exception e){
            }
        }
    }

    public void onlineDelete(String proID, Context context, String ses,String color,String size) {
        try {
            String sql = "DELETE FROM shopping_carts WHERE session_id = " +
                    "'" + ses + "' AND product_id = '" + proID + "' AND color = '"+color+"' AND size = '"+size+"'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("1")) {
                                Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(DoConfig.QUERY, sql);
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

    public void numberInsert(String proIDs, Context context,String phone) {
        try {
            String sql = "insert into buynownotification (user_id,phone,product_id,date) values('"+random()+"'" +
                    ",'"+phone+"','"+proIDs+"','"+getCreatedDate()+"')";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.INSERT,
                    response -> {
                        if (!response.equals("1")) {
                            Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                        }
                    }, error -> Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(DoConfig.QUERY, sql);
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

    public static int random(){
        int num=0;
        Random random = new Random();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            num = random.nextInt(9999);
        }
        return num;
    }

    public List<String> showCheck_data(String response, checkout_pro_adapter category_adapter) {
        String id = "";
        String name = "";
        String quant = "";
        String price = "";
        String shipping = "";
        List<String> shipID = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(DoConfig.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                name = collegeData.getString(DoConfig.PRO_NAME);
                quant = collegeData.getString(DoConfig.CAT_NAME);
                id = collegeData.getString(DoConfig.CAT_ID);
                price = collegeData.getString(DoConfig.PRO_CURRENT_PRICE);
                shipping = collegeData.getString(DoConfig.SHIPPING);
                shipID.add(shipping);
                double quantity = Double.parseDouble(quant);
                double cur_price = Double.parseDouble(price);
                double subtotal = quantity * cur_price;
                cat_models = new checkout_pro_model(name, quant, String.valueOf(subtotal), id);
                category_adapter.adduser(cat_models);
            }
        } catch (Exception e) {
        }
        return shipID;
    }

    public List<String> products_id(String response) {
        String id = "";
        List<String> shipID = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(DoConfig.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                id = collegeData.getString(DoConfig.CAT_ID)+"->"+collegeData.getString(DoConfig.PRO_NAME);
                shipID.add(id);
            }
        } catch (Exception e) {
        }
        return shipID;
    }

    public void insertuser(Context context, String firstname, String lastname, String phone, String email, String password, String address) {
        try {
            homeViewModel = new HomeViewModel();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.GUEST_REG,
                    response -> {
                        if (!response.equals("1")) {
                            Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show();
                            homeViewModel.insertuser(context, firstname + " " + lastname, phone, email, password, "");
                            context.startActivity(new Intent(context, LoginActivity.class));
                        }
                    }, error -> Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(DoConfig.FIRST_N, firstname+ " "+ lastname);
                    params.put(DoConfig.LAST_N, "");
                    params.put(DoConfig.EMAIL, email);
                    params.put(DoConfig.PHONE, phone);
                    params.put(DoConfig.ADDRESS, address);
                    params.put(DoConfig.PASS, password);
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

    public String getCreateDate() {
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        return thisDate;
    }
    public String getCreatedDate() {
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy / HH:mm:ss");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        return thisDate;
    }

    public void invoice_proccess(Context context, String sql, String sql1, ProgressDialog progressDialog,
                                 String couponid,String subT,String disT,String delT,String totalT,String pay_type) {
        try {
            String sql2 = "SELECT SUBSTR(MAX(invoice_id),5,2) AS 'invoice_id', SUBSTR(CURRENT_DATE(),9,2) AS 'product_name'," +
                    "DATE_FORMAT(CURDATE(), '%y%m%d') AS 'created_at',SUBSTR(MAX(invoice_id),7,5) AS 'status' FROM invoices";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.ORDER_DATA,
                    response -> {
                        try {
                            String id = createInvoiceID(response);
                            switch (pay_type){
                                case "Product Balance":
                                    update_pay(context,"INSERT INTO `member_product_balance`(`date`, `member_id`, `type`, " +
                                            "`amount`, `withdraw`, `number`, `details`) VALUES(" +
                                            "'" + config.getCreateDate() + "','" + homeViewModel.getSellerID(context) + "'," +
                                            "'','0.00','" + totalT + "','','')");
                                    break;
                                case "Commision Balance":
                                    update_pay(context,"INSERT INTO `order_customer_commision`(`date`, `member_id`, `type`, " +
                                            "`commision_balance`, `withdraw`,invoice_id, `details`) VALUES(" +
                                            "'" + config.getCreateDate() + "','" + homeViewModel.getSellerID(context) + "','','0.00" +
                                            "','" + totalT + "','"+id+"','')");
                                    break;
                                case "Add Funds Balance":
                                    update_pay(context,"INSERT INTO `addfunds`(`date`, `member_id`, `customer_id`, `type`, " +
                                            "`amount`, `withdraw`, `details`, `status`) VALUES(" +
                                            "'" + config.getCreateDate() + "','" + homeViewModel.getSellerID(context) + "','" + homeViewModel.getGuestID(context) + "','','0.00" +
                                            "','" + totalT + "','','')");

                                    break;
                            }
                            String query = sql + id + sql1;
                            addInvoice(context, query, progressDialog, couponid,subT,disT,delT,totalT);
                        }catch (Exception e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }, error -> {
                        progressDialog.dismiss();
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(DoConfig.QUERY, sql2);
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

    public void update_pay(Context context, String sql){
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.INSERT,
                    response -> {
                        if(!response.trim().equals("1")){
                            Toast.makeText(context, "Payment update failed! "+response, Toast.LENGTH_SHORT).show();
                        }
                    }, error -> Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(DoConfig.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }catch (Exception e){
        }
    }

    private void Invoice_json(String response, Context context, String cuoponID, String session,String subT,
                              String disT,String delT,String totalT) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(DoConfig.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                config.Update(context, "UPDATE `shopping_carts` SET `status` = '1' WHERE " +
                        "`session_id` = '" + session + "'");
                if (cuoponID != null && !cuoponID.equals("")) {
                    config.Update(context, "UPDATE coupons SET apply_coupon = apply_coupon + 1 WHERE id = '" + cuoponID + "'");
                }
//                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
//                dialogBuilder.setTitle("Success");
//                dialogBuilder.setMessage(collegeData.getString(config.PRO_SIZE) + "\nYour Order Successfully Submited!" +
//                        "\nDelivery Date: " + reduce_date(collegeData.getString(config.CAT_ID)) + " to " + reduce_date(collegeData.getString(config.PRO_NAME)));
//                dialogBuilder.setCancelable(false);
//                dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
//                    dialog.cancel();
//
//                });
//                dialogBuilder.show();
                checkout checkout = new checkout();
                Intent intent = new Intent(context, invoice.class);
                intent.putExtra("invoice",collegeData.getString(DoConfig.PRO_SIZE));
                intent.putExtra("delivery",reduce_date(collegeData.getString(DoConfig.CAT_ID)) + " to " + reduce_date(collegeData.getString(DoConfig.PRO_NAME)));
                intent.putExtra("sub",subT);
                intent.putExtra("dis",disT);
                intent.putExtra("del",delT);
                intent.putExtra("total",totalT);
                context.startActivity(intent);
                checkout.close();
            }
        } catch (Exception e) {
        }
    }

    public String reduce_date(String date) {
        String data = date.substring(0, date.indexOf(" "));
        return data;
    }

    private void addInvoice(Context context, String sql, ProgressDialog progressDialog, String cuoponID,
                            String subT,String disT,String delT,String totalT) {
        homeViewModel = new HomeViewModel();
        config = new DoConfig();
        String session = homeViewModel.getSession(context);
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.INSERT_INVOICE,
                    response -> {
                        progressDialog.dismiss();
                        if (!response.equals("Could not Registered in online") && !response.equals("problem")) {
                            Invoice_json(response, context, cuoponID, session,subT,disT,delT,totalT);
                        } else {
                            Toast.makeText(context, "in failed", Toast.LENGTH_LONG).show();
                        }
                    }, error -> {
                        progressDialog.dismiss();
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(DoConfig.QUERY, sql);
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

    private String createInvoiceID(String INid) {
        String prefix = "", thisDate, invoice_date;
        long id;
        try {
            JSONObject obj = new JSONObject(INid);
            JSONArray result = obj.getJSONArray(DoConfig.RESULT);
            JSONObject employee = result.getJSONObject(0);
            thisDate = employee.getString("pro_name");
            invoice_date = employee.getString("id");
            prefix = employee.getString("size");
            id = Long.parseLong(employee.getString("image"));
            if (invoice_date.equals(thisDate)) {
                id++;
                if (id <= 9) {
                    return (prefix + "000" + "" + id);

                } else if (id <= 99) {
                    return (prefix + "00" + "" + id);
                } else if (id <= 999) {
                    return (prefix + "0" + "" + id);
                } else if (id <= 9999) {
                    return (prefix + "" + "" + id);
                }
            } else {
                return (prefix + "000" + "" + "1");
            }
        } catch (Exception e) {
            return (prefix + "000" + "" + "1");
        }
        return null;
    }

    public void regularSnak(String msg, View v) {
        Snackbar snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        snackbar.show();
    }

}
