package com.sbitbd.fixedbd.ui.home;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
import com.sbitbd.fixedbd.Adapter.cat_model;
import com.sbitbd.fixedbd.Adapter.category_adapter;
import com.sbitbd.fixedbd.Adapter.item_adapter;
import com.sbitbd.fixedbd.Adapter.mainItem_adapter;
import com.sbitbd.fixedbd.Adapter.pro_model;
import com.sbitbd.fixedbd.Adapter.product_adapter;
import com.sbitbd.fixedbd.Adapter.shop_adapter;
import com.sbitbd.fixedbd.Config.DoConfig;
import com.sbitbd.fixedbd.Config.sqliteDB;
import com.sbitbd.fixedbd.ImageSlider.sliderModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    DoConfig
            config = new DoConfig();
    sliderModel sliderModel;
    private List<pro_model> proModelList = new ArrayList<>();
    private static String session;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }
    public void showCatJSON(String response, cat_model cat_models, category_adapter category_adapter) {
        String id = "";
        String name = "";
        String img = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                name = collegeData.getString(config.CAT_NAME);
                img = collegeData.getString(config.CAT_IMG);
                id = collegeData.getString(config.CAT_ID);
                cat_models = new cat_model(img, name, id);
                category_adapter.addUser(cat_models);
            }
        } catch (Exception e) {
        }
    }
    public void showShopJSON(String response, cat_model cat_models, shop_adapter category_adapter) {
        String id = "";
        String name = "";
        String img = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                name = collegeData.getString(config.CAT_NAME);
                img = collegeData.getString(config.CAT_IMG);
                id = collegeData.getString(config.CAT_ID);
                cat_models = new cat_model(img, name, id);
                category_adapter.addUser(cat_models);
            }
        } catch (Exception e) {
        }
    }
    public void showMain_itemJSON(String response, cat_model cat_models, mainItem_adapter category_adapter) {
        String id = "";
        String name = "";
        String img = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                name = collegeData.getString(config.CAT_NAME);
                img = collegeData.getString(config.CAT_IMG);
                id = collegeData.getString(config.CAT_ID);
                cat_models = new cat_model(img, name, id);
                category_adapter.addUser(cat_models);
            }
        } catch (Exception e) {
        }
    }
    public void showitemJSON(String response, cat_model cat_models, item_adapter category_adapter) {
        String id = "",img = "",name = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                img = collegeData.getString(config.CAT_IMG);
                id = collegeData.getString(config.CAT_ID);
                name = collegeData.getString(config.CAT_NAME);
                cat_models = new cat_model(img,name,id);
                if (!cat_models.getId().equals("4")) {
                    category_adapter.addUser(cat_models);
                }
            }
        } catch (Exception e) {
        }
    }
    public void showProJSON(String response, pro_model pro_model, product_adapter product_adapter, Context context) {
        String proName,size,price,dis_val,image,id,dis_price;
        id = "";
        proName = "";
        size = "";
        price = "";
        dis_val = "";
        dis_price = "";
        image = "";
        int position = -1;
        proModelList.clear();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                try {
                    JSONObject collegeData = result.getJSONObject(i);
                    id = collegeData.getString(config.CAT_ID);
                    proName = collegeData.getString(config.PRO_NAME);
                    size = collegeData.getString(config.PRO_SIZE);
                    price = collegeData.getString(config.PRO_CURRENT_PRICE);
                    dis_price = collegeData.getString(config.PRO_SALE_PRICE);
                    dis_val = collegeData.getString(config.PRO_DISCOUNT_PRICE);
                    image = collegeData.getString(config.PRO_IMAGE);
                    pro_model = new pro_model(image,proName,size,price,dis_val,id,dis_price);
                    position = product_adapter.getPosition(pro_model);

                    product_adapter.adduser(pro_model);
                    proModelList.add(pro_model);
//                    if (position == -1){
//
//                    }
//                    else {
//                        pro_model p_model = proModelList.get(position);
//                        pro_model = new pro_model(image,proName,size+","+p_model.getSize(),price,dis_val,id,dis_price);
//                        product_adapter.updateUser(pro_model);
//                        proModelList.set(position,pro_model);
//                    }

                }catch (Exception e){
                }

            }
        } catch (Exception e) {
        }
    }

    public void showshortProJSON(String response, pro_model pro_model, product_adapter product_adapter, Context context) {
        String proName,size,price,dis_val,image,id,dis_price;
        id = "";
        proName = "";
        size = "";
        price = "";
        dis_val = "";
        dis_price = "";
        image = "";
        int position = -1;
        proModelList.clear();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                try {
                    JSONObject collegeData = result.getJSONObject(i);
                    id = collegeData.getString(config.CAT_ID);
                    proName = collegeData.getString(config.PRO_NAME);
                    size = collegeData.getString(config.PRO_SIZE);
                    price = collegeData.getString(config.PRO_CURRENT_PRICE);
                    dis_price = collegeData.getString(config.PRO_SALE_PRICE);
                    dis_val = collegeData.getString(config.PRO_DISCOUNT_PRICE);
                    image = collegeData.getString(config.PRO_IMAGE);
                    pro_model = new pro_model(image,proName,size,price,dis_val,id,dis_price);
                    position = product_adapter.getPosition(pro_model);

                    product_adapter.adduser(pro_model);
                    proModelList.add(pro_model);
//                    if (position == -1){
//
//                    }
//                    else {
//                        pro_model p_model = proModelList.get(position);
//                        pro_model = new pro_model(image,proName,size+","+p_model.getSize(),price,dis_val,id,dis_price);
//                        product_adapter.updateUser(pro_model);
//                        proModelList.set(position,pro_model);
//                    }

                }catch (Exception e){
                }

            }
        } catch (Exception e) {
        }
    }
    public List<sliderModel> showSliderJSON(String response) {
        List<sliderModel> modelList = new ArrayList<>();
        String img = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                img = collegeData.getString(config.PRO_IMAGE);
                sliderModel = new sliderModel(config.SLIDER_IMG_URL+img);
                modelList.add(sliderModel);
            }
        } catch (Exception e) {
        }
        return modelList;
    }
    public Boolean checkUser(Context context){
        sqliteDB sqliteDB = new sqliteDB(context);
        try {
            Cursor cursor = sqliteDB.getUerData("SELECT * FROM guest");
            if(cursor.getCount() > 0){
                if(cursor.moveToNext()){
                    return true;
                }
            }
        }catch (Exception e){
        }
        finally {
            try {
                sqliteDB.close();
            }catch (Exception e){
            }
        }
        return false;
    }
    public Boolean checkSeller(Context context){
        sqliteDB sqliteDB = new sqliteDB(context);
        try {
            Cursor cursor = sqliteDB.getUerData("SELECT * FROM seller");
            if(cursor.getCount() > 0){
                if(cursor.moveToNext()){
                    return true;
                }
            }
        }catch (Exception e){
        }
        finally {
            try {
                sqliteDB.close();
            }catch (Exception e){
            }
        }
        return false;
    }
    public void updateUser(Context context,String guestID){
        sqliteDB sqliteDB = new sqliteDB(context);
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put("guest_id",guestID);
                boolean ch = sqliteDB.DataOperation(contentValues,"update","guest","id = '1'");
                if(ch){
                    Toast.makeText(context,"Updated",Toast.LENGTH_LONG).show();
                }else
                    Toast.makeText(context,"Update failed",Toast.LENGTH_LONG).show();
            }catch (Exception e){
            }
            finally {
                try {
                    sqliteDB.close();
                }catch (Exception e){
                }
            }
    }
    public void updateSeller(Context context,String sellerID){
        sqliteDB sqliteDB = new sqliteDB(context);
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("seller_id",sellerID);
            boolean ch = sqliteDB.DataOperation(contentValues,"update","seller","id = '1'");
            if(ch){
                Toast.makeText(context,"Updated",Toast.LENGTH_LONG).show();
            }else
                Toast.makeText(context,"Update failed",Toast.LENGTH_LONG).show();
        }catch (Exception e){
        }
        finally {
            try {
                sqliteDB.close();
            }catch (Exception e){
            }
        }
    }
    public String getGuestID(Context context){
        String id = "";
        sqliteDB sqliteDB = new sqliteDB(context);
        try {
            Cursor cursor = sqliteDB.getUerData("SELECT * FROM guest");
            if(cursor.getCount() > 0){
                if(cursor.moveToNext()){
                    id = cursor.getString(cursor.getColumnIndexOrThrow("guest_id"));
                    return id;
                }
            }
        }catch (Exception e){
        }
        finally {
            try {
                sqliteDB.close();
            }catch (Exception e){
            }
        }
        return null;
    }
    public String getSellerID(Context context){
        String id = "";
        sqliteDB sqliteDB = new sqliteDB(context);
        try {
            Cursor cursor = sqliteDB.getUerData("SELECT * FROM seller");
            if(cursor.getCount() > 0){
                if(cursor.moveToNext()){
                    id = cursor.getString(cursor.getColumnIndexOrThrow("seller_id"));
                    return id;
                }
            }
        }catch (Exception e){
        }
        finally {
            try {
                sqliteDB.close();
            }catch (Exception e){
            }
        }
        return null;
    }
    public void insertuser(Context context,String name,String phone,String email,String password,String guestID){
        sqliteDB sqlite_db = new sqliteDB(context);
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id","1");
            contentValues.put("guest_id",guestID);
            contentValues.put("name",name);
            contentValues.put("phone",phone);
            contentValues.put("email",email);
            contentValues.put("password",password);
            boolean ch = sqlite_db.DataOperation(contentValues,"insert","guest",null);
            if(ch){
            }else
                Toast.makeText(context,"Failed to create Cache",Toast.LENGTH_LONG).show();
        }catch (Exception e){
        }
        finally {
            try {
                sqlite_db.close();
            }catch (Exception e){
            }
        }
    }
    public void insertSeller(Context context,String name,String phone,String email,String password,String guestID){
        sqliteDB sqlite_db = new sqliteDB(context);
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id","1");
            contentValues.put("seller_id",guestID);
            contentValues.put("name",name);
            contentValues.put("phone",phone);
            contentValues.put("email",email);
            contentValues.put("password",password);
            boolean ch = sqlite_db.DataOperation(contentValues,"insert","seller",null);
            if(ch){
            }else
                Toast.makeText(context,"Failed to create Cache",Toast.LENGTH_LONG).show();
        }catch (Exception e){
        }
        finally {
            try {
                sqlite_db.close();
            }catch (Exception e){
            }
        }
    }
    public void userDelete(Context context){
        sqliteDB sqliteDB = new sqliteDB(context);
        try {
            boolean check = sqliteDB.DataOperation(null,"delete","guest","");
            if (check){

            }else {
                Toast.makeText(context,"Delete Failed",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
        }
        finally {
            try {
                sqliteDB.close();
            }catch (Exception e){
            }
        }
    }

    public void sellerDelete(Context context){
        sqliteDB sqliteDB = new sqliteDB(context);
        try {
            boolean check = sqliteDB.DataOperation(null,"delete","seller","");
            if (check){

            }else {
                Toast.makeText(context,"Delete Failed",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
        }
        finally {
            try {
                sqliteDB.close();
            }catch (Exception e){
            }
        }
    }

    public void session(Context context){
        try {
            String sql="SELECT DATE_FORMAT(NOW(), '%y%m%d%h%m%s') AS 'id'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    response -> {
                        if(!response.equals("1")) {
                            session(context,response);
                        }
                    }, error -> Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
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
        }catch (Exception e){
        }
    }

    public void session(Context context,String dbdate){
        sqliteDB sqlite_db = new sqliteDB(context);
        try {
            SimpleDateFormat currentDate = new SimpleDateFormat("yyyyMMdd");
            Date todayDate = new Date();
            String thisDate = currentDate.format(todayDate);
            session = dbdate + "-" +thisDate+ System.currentTimeMillis();
            ContentValues contentValues = new ContentValues();
            contentValues.put("id","1");
            contentValues.put("session_data",session);
            boolean ch = sqlite_db.DataOperation(contentValues,"insert","session",null);
            if(ch){
            }else
                Toast.makeText(context,"Failed to create Session",Toast.LENGTH_LONG).show();
        }catch (Exception e){
        }finally {
            try {
                sqlite_db.close();
            }catch (Exception e){
            }
        }
    }
    public String getSession(Context context){
        sqliteDB sqlite_db = new sqliteDB(context);
        try {
            Cursor cursor = sqlite_db.getUerData("SELECT * FROM session");
            if (cursor.getCount() > 0){
                if(cursor.moveToNext()){
                    session = cursor.getString(cursor.getColumnIndexOrThrow("session_data"));
                }
            }else {
                Toast.makeText(context,"Session not found",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
        }
        finally {
            try {
                sqlite_db.close();
            }catch (Exception e){
            }
        }
        return session;
    }
    public void resetSession(Context context){
        session = null;
        sqliteDB sqlite_db = new sqliteDB(context);
        try {
            boolean check = sqlite_db.DataOperation(null,"delete","session","id = '1'");
            if(check){
            }else
                Toast.makeText(context,"Session deleting failed",Toast.LENGTH_LONG).show();
            session(context);
        }catch (Exception e){
        }
        finally {
            try {
                sqlite_db.close();
            }catch (Exception e){
            }
        }
    }

    public void initProduct(Context context, String sql, pro_model pro_model, product_adapter product_adapter){
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.PRO_DATA,
                    response -> {
                        if(!response.equals("1")) {
                            showshortProJSON(response, pro_model, product_adapter, context);
                        }
                    }, error -> Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()) {
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
        }catch (Exception e){
        }
    }


    public LiveData<String> getText() {
        return mText;
    }
}