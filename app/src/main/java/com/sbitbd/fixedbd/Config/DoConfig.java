package com.sbitbd.fixedbd.Config;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.fixedbd.Adapter.order_adapter;
import com.sbitbd.fixedbd.Adapter.order_model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DoConfig {

    public static final String FOUR_DMS = "https://fixedbd.com/java/four_dms.php";
    public static final String SIX_DMS = "https://fixedbd.com/java/six_dms.php";
    public static final String SELLER_REG = "https://fixedbd.com/java/seller_reg.php";
    public static final String OTP_SEND = "https://fixedbd.com/java/send_otp.php";
    public static final String SELLER_OTP = "https://fixedbd.com/java/seller_otp.php";
    public static final String CAT_DATA = "https://fixedbd.com/java/getCategory.php";
    public static final String FORGET = "https://fixedbd.com/java/forget_password.php";
    public static final String GUEST_REG = "https://fixedbd.com/java/guest_register.php";
    public static final String GET_GUEST_REG = "https://fixedbd.com/java/getData_register.php";
    //    public static final String UPDATE_GUEST = "http://salesman-bd.com/java/update_guest.php";
    public static final String UPDATE_GUEST = "https://fixedbd.com/java/fileUpload.php?apicall=uploadpic";
    public static final String PRO_IMAGE_UPLOAD = "https://fixedbd.com/java/product_image_upload.php?apicall=uploadpic";
//public static final String PRO_IMAGE_UPLOAD = "http://192.168.253.38/salesman-bd/product_image_upload.php?apicall=uploadpic";
//        public static final String UPDATE_GUEST = "http://192.168.59.38/salesman-bd/fileUpload.php?apicall=uploadpic";
    public static final String GUEST_DATA = "https://fixedbd.com/java/getGuestData.php";
    //    public static final String GUEST_DATA = "http://192.168.0.116/salesman-bd/getGuestData.php";
    public static final String ADD_PRODUCT = "https://fixedbd.com/java/product_add.php";
    public static final String ADD_ARRAY = "https://fixedbd.com/java/array_add.php";
    public static final String ORDER_DATA = "https://fixedbd.com/java/getOrderData.php";
    public static final String COUPON = "https://fixedbd.com/java/getCoupon.php";
    public static final String GET_ID = "https://fixedbd.com/java/getID.php";
    public static final String RESET_PASS = "https://fixedbd.com/java/reset_password.php";
    public static final String GET_LOGIN_INFO = "https://fixedbd.com/java/getLoginInfo.php";
    public static final String LOGIN = "https://fixedbd.com/java/getLoginUser.php";
    public static final String SELLER_LOGIN = "https://fixedbd.com/java/getLoginSeller.php";
    public static final String PROFILE = "https://fixedbd.com/java/getProfile.php";
    public static final String DISTRICT_DELIVERY = "https://fixedbd.com/java/getDistrict_delivery.php";
    public static final String CART_DATA = "https://fixedbd.com/java/getCart.php";
    public static final String INSERT = "https://fixedbd.com/java/insert.php";
    public static final String INSERT_DELIVERY = "https://fixedbd.com/java/insert_delivery.php";
    public static final String INSERT_INVOICE = "https://fixedbd.com/java/invoice_insert.php";
    //    public static final String INSERT_INVOICE = "http://192.168.0.108/salesman-bd/invoice_insert.php";
    public static final String PRO_DATA = "https://fixedbd.com/java/getProduct.php";
    //    public static final String PRO_DATA = "http://192.168.0.116/salesman-bd/getProduct.php";
    public static final String PRO_DES = "https://fixedbd.com/java/getProDescription.php";
    public static final String SLIDER_IMG = "https://fixedbd.com/java/getData.php";
    public static final String CHECK_PRO_DATA = "https://fixedbd.com/java/getCheckData.php";
    public static final String CAT_IMG_URL = "https://fixedbd.com/public/categoryImage/";
    public static final String SELLER_IMG_URL = "https://fixedbd.com/public/seller/";
    public static final String ITEM_IMG_URL = "https://fixedbd.com/public/itemImage/";
    public static final String PRO_IMG_URL = "https://fixedbd.com/public/productImage/";
    public static final String SLIDER_IMG_URL = "https://fixedbd.com/public/sliderImage/";
    public static final String GUEST_IMAGE = "https://fixedbd.com/public/guestImage/";
    public static final String BANNER = "https://www.fixedbd.com/public/Frontend/";
    public static final String CAT_IMG = "cat_img";
    public static final String CAT_NAME = "cat_name";
    public static final String SHIPPING = "shipping";
    public static final String CAT_ID = "id";
    public static final String RESULT = "result";
    public static final String QUERY = "query";
    public static final String PRO_NAME = "pro_name";
    public static final String PRO_SALE_PRICE = "sale_price";
    public static final String PRO_DISCOUNT_PRICE = "discount_price";
    public static final String PRO_CURRENT_PRICE = "current_price";
    public static final String PRO_IMAGE = "image";
    public static final String PRO_IMAGE_NAME = "image_name";
    public static final String PRO_SIZE = "size";
    public static final String EMAIL = "email";
    public static final String FIRST_N = "firstname";
    public static final String LAST_N = "lastname";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String PASS = "pass";
    public static final String BUSINESS = "business";
    public static final String ONE = "one";
    public static final String TWO = "two";
    public static final String THREE = "three";
    public static final String FOUR = "four";
    public static final String FIVE = "five";
    public static final String SIX = "six";
    private List<order_model> order_modelsList = new ArrayList<>();

//    public String encrypt(String value) {
//        try {
//            String key, initVector;
//            key = "Jar12345Jar12345";
//            initVector = "RandomInitVector";
//            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
//            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//            cipher.init(1, skeySpec, iv);
//            byte[] encrypted = cipher.doFinal(value.getBytes());
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                return Base64.getEncoder().encodeToString(encrypted);
//            } else
//                return new String(encrypted);
////            encodeBase64String(encrypted);getEncoder().encodeToString(encrypted);
//        } catch (Exception ex) {
//        }
//        return null;
//    }

    public void OrderData(Context context, String query, RecyclerView recyclerView, ConstraintLayout materialCardView) {
        try {
            String sql = query;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ORDER_DATA,
                    response -> {
                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                        if (!response.equals("1") && !response.equals("")) {
                            showOrderJson(response, recyclerView, context);
                        } else
                            materialCardView.setVisibility(View.VISIBLE);
                    }, error -> {
                        materialCardView.setVisibility(View.VISIBLE);
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    public void online_gid(Context context, String id, RecyclerView recyclerView, ConstraintLayout materialCardView) {
        try {
            String sql = "SELECT id FROM `guest` WHERE id = '1'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_ID,
                    response -> {
                        if (!response.equals("1")) {
                            String sql1 = "SELECT `invoices`.`invoice_id`,`shopping_carts`.`product_id`,`product_productinfo`.`product_name`," +
                                    "`invoices`.`created_at`,`invoices`.`status` FROM `invoices` INNER JOIN `shopping_carts` ON `invoices`.`session_id` = " +
                                    "`shopping_carts`.`session_id` INNER JOIN `product_productinfo` ON `shopping_carts`.`product_id` = " +
                                    "`product_productinfo`.`id` WHERE `invoices`.`guest_id` = '" + response + "' AND `invoices`.`session_id` LIKE '" + id + "%'";
                            OrderData(context, sql1, recyclerView, materialCardView);
                        } else
                            Toast.makeText(context, "Please Sign in", Toast.LENGTH_LONG).show();


                    }, error -> Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private void showOrderJson(String response, RecyclerView recyclerView, Context context) {
        String id = "";
        String name = "";
        String date = "";
        String status = "";
        order_model order_model;
        int position = -1;
        order_adapter order_adapter = new order_adapter(context);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                name = collegeData.getString(PRO_NAME);
                status = collegeData.getString(PRO_IMAGE);
                id = collegeData.getString(CAT_ID);
                date = collegeData.getString(PRO_SIZE);
                order_model = new order_model(id, name, date, status);

                position = order_adapter.getPosition(order_model);
                if (position == -1) {
                    order_adapter.adduser(order_model);
                    order_modelsList.add(order_model);
                } else {
                    order_model or_model = order_modelsList.get(position);
                    order_model = new order_model(id, name + "," + or_model.getPro_name(), date, status);
                    order_adapter.updateUser(order_model);
                    order_modelsList.set(position, order_model);
                }

                recyclerView.setAdapter(order_adapter);
            }
        } catch (Exception e) {
        }
    }

    public void Update(Context context, String query) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ORDER_DATA,
                    response -> {
                        if (!response.equals("1")) {
                        }
                    }, error -> Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(QUERY, query);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    public String getCreateDate() {
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        return thisDate;
    }
}
