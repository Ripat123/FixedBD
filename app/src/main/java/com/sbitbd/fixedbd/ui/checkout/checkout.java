package com.sbitbd.fixedbd.ui.checkout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.fixedbd.Adapter.cat_model;
import com.sbitbd.fixedbd.Adapter.checkout_pro_adapter;
import com.sbitbd.fixedbd.Adapter.checkout_pro_model;
import com.sbitbd.fixedbd.Config.DoConfig;
import com.sbitbd.fixedbd.R;
import com.sbitbd.fixedbd.ui.cart_operation.operation;
import com.sbitbd.fixedbd.ui.home.HomeViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class checkout extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private RecyclerView recyclerView;
    //private checkout_pro_model checkout_pro_model;
    private checkout_pro_adapter checkout_pro_adapter;
    private RadioButton cash_rd, bkash_rd, rocket_rd, nagad_rd, pro_bal_id, com_bal_id, fund_id;
    private DoConfig config;
    private HomeViewModel homeViewModel = new HomeViewModel();
    private operation operation = new operation();
    private TextView subT, disT, deliT, grnT;
    private AutoCompleteTextView country, district, thana;
    private Button submit, promo_btn;
    private EditText first_name, Phone, Email, fulladdress, promo_field;
    private static List<cat_model> districtList = new ArrayList<>();
    private static List<cat_model> thanaList = new ArrayList<>();
    private static List<String> model = new ArrayList<>();
    private static List<String> thanamodel = new ArrayList<>();
    private static List<String> countryList = new ArrayList<>();
    private static List<String> shipping_id = new ArrayList<>();
    private double del_ch = 0, Total = 0, gr_t = 0, pro_balance, commission_balance, fund_balance;
    private static String mobileNO = "", trnID = "", pay_type = "", disTK = "0", subTK, totalTK;
    private static String username, pass;
    private EditText firstname_ed, phone_ed, email_ed, address_ed, password_ed, con_password_ed;
    private View dialog_view;
    private View signin_view, singup_view;
    private EditText phone_n, trn_n;
    private String districtid = "", session = "", id = "", thana_id = "";
    private int coupon_check = 0, otp = 0;
    private ProgressDialog progressDialog, progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initview();
        initpro_check();
        showDistrict();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(checkout.this,
                R.layout.item_name, model);
        district.setAdapter(dataAdapter);

//        countryList.clear();
//        countryList.add("Bangladesh");
//        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(checkout.this,
//                R.layout.item_name,countryList);
//        country.setAdapter(dataAdapter1);
//        country.setText("Bangladesh");
    }

    private void initview() {
        cash_rd = findViewById(R.id.cash_id);
        bkash_rd = findViewById(R.id.bkash_id);
        rocket_rd = findViewById(R.id.rocket_id);
        nagad_rd = findViewById(R.id.nagad_id);
        subT = findViewById(R.id.total_id);
        disT = findViewById(R.id.dis_id);
        deliT = findViewById(R.id.delivery_id);
        grnT = findViewById(R.id.grand_id);
//        country = findViewById(R.id.ctry);
        district = findViewById(R.id.district);
        thana = findViewById(R.id.thana);
        submit = findViewById(R.id.submit_btn);
        promo_btn = findViewById(R.id.apply_prormo_btn);
        first_name = findViewById(R.id.first);
        Phone = findViewById(R.id.ph);
//        Email = findViewById(R.id.em);
        fulladdress = findViewById(R.id.addl);
        promo_field = findViewById(R.id.promo_text);
        pro_bal_id = findViewById(R.id.pro_bal_id);
        com_bal_id = findViewById(R.id.com_bal_id);
        fund_id = findViewById(R.id.fund_id);

        dialog_view = LayoutInflater.from(checkout.this).inflate(R.layout.payment_dialog, null);
        phone_n = dialog_view.findViewById(R.id.phone_di);
        trn_n = dialog_view.findViewById(R.id.trnid);
        district.setOnItemClickListener(this);

        thana.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int item;
                String inQuery = "";
                cat_model cat_model = thanaList.get(position);
                gr_t = Double.parseDouble(grnT.getText().toString());
//            Total =  gr_t - del_ch;

                item = checkout_pro_adapter.getItemCount();
                for (int i = 0; i < item; i++) {
                    if (inQuery.equals(""))
                        inQuery = "'" + shipping_id.get(i) + "'";
                    else
                        inQuery = inQuery + "," + "'" + shipping_id.get(i) + "'";
                }

                getCharge(cat_model.getId(), inQuery);
//            Total = del_ch + Total;
//            deliT.setText(cat_model.getId());
//            grnT.setText(String.valueOf(Total));
                thana_id = cat_model.getId();

                totalTK = grnT.getText().toString();
                subTK = subT.getText().toString();
            }
        });

        String gid = homeViewModel.getGuestID(checkout.this);
        if (gid != null && !gid.equals("")) {
            getGuestData(gid);
        } else {
            gid= "1";
            //sign_in(checkout.this, null, null, "1");
        }
        submit.setOnClickListener(v -> submitAction());
//        getBalance("SELECT sum(amount - withdraw) as 'id' from member_product_balance WHERE " +
//                "member_id = '"+homeViewModel.getSellerID(checkout.this)+"'",pro_bal_id,1);
//        getBalance("SELECT sum(commision_balance - withdraw) as 'id' from order_customer_commision WHERE " +
//                "member_id = '"+homeViewModel.getSellerID(checkout.this)+"'",com_bal_id,2);
//        getBalance("SELECT sum(amount - withdraw) as 'id' from addfunds WHERE " +
//                "member_id = '"+homeViewModel.getSellerID(checkout.this)+"'",fund_id,3);
        promo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coupon_check != 1) {
                    coupon_check = 1;
                    getdiscount();
                } else {
                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(checkout.this);
                    dialogBuilder.setTitle("Promotion");
                    dialogBuilder.setMessage("Already Applied");
                    dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                        dialog.cancel();
                    });
                    dialogBuilder.show();
                }
            }
        });
        nagad_rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog_view = LayoutInflater.from(checkout.this).inflate(R.layout.payment_dialog, null);
                    phone_n = dialog_view.findViewById(R.id.phone_di);
                    trn_n = dialog_view.findViewById(R.id.trnid);
                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(checkout.this);
                    dialogBuilder.setTitle("Nagad");
                    dialogBuilder.setMessage("+8801747666362");

                    dialogBuilder.setView(dialog_view);
                    if (pay_type != null && pay_type.equals("nagad")) {
                        phone_n.setText(mobileNO);
                        trn_n.setText(trnID);
                    }
                    dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.cancel();
                        mobileNO = "";
                        trnID = "";
                        nagad_rd.setSelected(false);
                    });
                    dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                        mobileNO = phone_n.getText().toString();
                        trnID = trn_n.getText().toString();
                        pay_type = "nagad";
                    });
                    dialogBuilder.show();
                } catch (Exception e) {
                }

            }
        });
        rocket_rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog_view = LayoutInflater.from(checkout.this).inflate(R.layout.payment_dialog, null);
                    phone_n = dialog_view.findViewById(R.id.phone_di);
                    trn_n = dialog_view.findViewById(R.id.trnid);
                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(checkout.this);
                    dialogBuilder.setTitle("Rocket");
                    dialogBuilder.setMessage("+8801747666362");

                    dialogBuilder.setView(dialog_view);
                    if (pay_type != null && pay_type.equals("rocket")) {
                        phone_n.setText(mobileNO);
                        trn_n.setText(trnID);
                    }
                    dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.cancel();
                        mobileNO = "";
                        trnID = "";
                        rocket_rd.setSelected(false);
                    });
                    dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                        mobileNO = phone_n.getText().toString();
                        trnID = trn_n.getText().toString();
                        pay_type = "rocket";
                    });
                    dialogBuilder.show();
                } catch (Exception e) {
                }

            }
        });
        bkash_rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog_view = LayoutInflater.from(checkout.this).inflate(R.layout.payment_dialog, null);
                    phone_n = dialog_view.findViewById(R.id.phone_di);
                    trn_n = dialog_view.findViewById(R.id.trnid);
                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(checkout.this);
                    dialogBuilder.setTitle("Bkash");
                    dialogBuilder.setMessage("+8801714654003");

                    dialogBuilder.setView(dialog_view);
                    if (pay_type != null && pay_type.equals("bkash")) {
                        phone_n.setText(mobileNO);
                        trn_n.setText(trnID);
                    }
                    dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.cancel();
                        mobileNO = "";
                        trnID = "";
                        bkash_rd.setSelected(false);
                    });
                    dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                        mobileNO = phone_n.getText().toString();
                        trnID = trn_n.getText().toString();
                        pay_type = "bkash";
                    });
                    dialogBuilder.show();
                } catch (Exception e) {
                }

            }
        });
        cash_rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_type = "cash";
                mobileNO = "";
                trnID = "";
            }
        });
        pro_bal_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_type = "Product Balance";
                mobileNO = "";
                trnID = "";
            }
        });
        com_bal_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_type = "Commision Balance";
                mobileNO = "";
                trnID = "";
            }
        });
        fund_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_type = "Add Funds Balance";
                mobileNO = "";
                trnID = "";
            }
        });
    }

    private void initpro_check() {
        recyclerView = findViewById(R.id.check_pro);
        checkout_pro_adapter = new checkout_pro_adapter(checkout.this, subT, grnT);
        GridLayoutManager manager = new GridLayoutManager(checkout.this, 1);
        recyclerView.setLayoutManager(manager);
        getOnlinedata();
        recyclerView.setAdapter(checkout_pro_adapter);
    }

    private void getdiscount() {
        try {
            progressDialog = ProgressDialog.show(checkout.this, "", "", false, false);
            String sql = "SELECT `id`,`discout_price` FROM `coupons` WHERE `coupon_name` = '" + promo_field.getText().toString() + "' " +
                    "AND `start_date` <= CURDATE() AND `end_date` >= CURDATE() AND `min_price` <= '" + subT.getText().toString() + "' AND `status` = '1'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.COUPON,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (!response.equals("1")) {
                                showDicountjson(response);
                            } else {
                                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(checkout.this);
                                dialogBuilder.setTitle("Promotion");
                                dialogBuilder.setMessage("Promotion not found");
                                dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                                    dialog.cancel();
                                });
                                dialogBuilder.show();
                                coupon_check = 0;
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(checkout.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(checkout.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private void showDicountjson(String response) {
        String discount = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                discount = collegeData.getString(config.PRO_DISCOUNT_PRICE);
                id = collegeData.getString(config.CAT_ID);
                disT.setText(discount);
                double subtotal = Double.parseDouble(grnT.getText().toString());
                double total = subtotal - Double.parseDouble(discount);
                grnT.setText(String.valueOf(total));
                disTK = disT.getText().toString();
                subTK = subT.getText().toString();
            }
        } catch (Exception e) {

        }
    }

    //    private void stockMaintain(){
//        homeViewModel = new HomeViewModel();
//        String session = homeViewModel.getSession(checkout.this);
//        sqliteDB sqlite_db = new sqliteDB(checkout.this);
//        try {
//            Cursor cursor = sqlite_db.getUerData("SELECT * FROM shopping_carts WHERE session_id " +
//                    "= '"+session+"'");
//            if (cursor.getCount() > 0){
//                if(cursor.moveToNext()){
//                    session = cursor.getString(cursor.getColumnIndex("session_data"));
//                }
//            }else {
//                Toast.makeText(checkout.this,"Session not found",Toast.LENGTH_LONG).show();
//            }
//        }catch (Exception e){
//        }
//        finally {
//            try {
//                sqlite_db.close();
//            }catch (Exception e){
//            }
//        }
//    }
    private void getOnlinedata() {
        try {
            homeViewModel = new HomeViewModel();
            String session = homeViewModel.getSession(checkout.this);
            String sql = "SELECT `shopping_carts`.`product_id`,`shopping_carts`.`quantity`,`product_productinfo`." +
                    "`product_name`,`product_productinfo`.`current_price`,`product_productinfo`.`shipping_id` FROM `shopping_carts` INNER JOIN " +
                    "`product_productinfo` ON `shopping_carts`.`product_id` = `product_productinfo`.id WHERE " +
                    "`shopping_carts`.`session_id` = '" + session + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.CHECK_PRO_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            shipping_id = operation.showCheck_data(response, checkout_pro_adapter);

                            getBalance("SELECT sum(amount - withdraw) as 'id' from member_product_balance WHERE " +
                                    "member_id = '" + homeViewModel.getSellerID(checkout.this) + "'", pro_bal_id, 1, getResources().getString(R.string.product_balance));
                            getBalance("SELECT sum(commision_balance - withdraw) as 'id' from order_customer_commision WHERE " +
                                    "member_id = '" + homeViewModel.getSellerID(checkout.this) + "'", com_bal_id, 2, getResources().getString(R.string.commission_balance));
                            getBalance("SELECT sum(amount - withdraw) as 'id' from addfunds WHERE " +
                                    "member_id = '" + homeViewModel.getSellerID(checkout.this) + "'", fund_id, 3, getResources().getString(R.string.funds_balance));
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(checkout.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(checkout.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {

        }
    }

    private void showDistrict() {
        try {
            String sql = "SELECT districts.district_name AS 'charge',`districts`.`id` FROM `districts`";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.DISTRICT_DELIVERY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            showDistrictList(response);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(checkout.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(checkout.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {

        }
    }

    private void showThana(String thID) {
        try {
            String sql = "SELECT thana_name AS 'charge',id FROM `thanas` WHERE `district_id` = '" + thID + "'";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.DISTRICT_DELIVERY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            showThanaList(response);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(checkout.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(checkout.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {

        }
    }

    private void showDistrictList(String response) {
        String id = "";
        String name = "";
        String quant = "";
        cat_model cat_models;
        try {
            districtList.clear();
            model.clear();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                name = collegeData.getString(config.PRO_NAME);
                quant = collegeData.getString(config.PRO_CURRENT_PRICE);
                id = collegeData.getString(config.CAT_ID);
                cat_models = new cat_model(quant, name, id);
                districtList.add(cat_models);
                model.add(quant);
            }

        } catch (Exception e) {
        }
    }

    private void showThanaList(String response) {
        String id = "";
        String name = "";
        String quant = "";
        cat_model cat_models;
        try {
            thanaList.clear();
            thanamodel.clear();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                name = collegeData.getString(config.PRO_NAME);
                quant = collegeData.getString(config.PRO_CURRENT_PRICE);
                id = collegeData.getString(config.CAT_ID);
                cat_models = new cat_model(quant, name, id);
                thanaList.add(cat_models);
                thanamodel.add(quant);
            }


        } catch (Exception e) {
        }
    }

    private void submitAction() {
        try {
//            if (first_name.getText().toString().trim().equals("")) {
//                first_name.setError("Empty First Name");
//                Toast.makeText(checkout.this, "Empty First Name", Toast.LENGTH_LONG).show();
//                return;
//            }
            if (Phone.getText().toString().trim().equals("")) {
                Phone.setError("Empty Phone Number");
                Toast.makeText(checkout.this, "Empty Phone Number", Toast.LENGTH_LONG).show();
                return;
            }
//            if(country.getText().toString().trim().equals("")){
//                country.setError("Select Country");
//                Toast.makeText(checkout.this,"Select Country",Toast.LENGTH_LONG).show();
//                return;
//            }
            if (district.getText().toString().trim().equals("") && districtid.equals("")) {
                district.setError("Select District");
                Toast.makeText(checkout.this, "Select District", Toast.LENGTH_LONG).show();
                return;
            }
//            if (fulladdress.getText().toString().trim().equals("")) {
//                fulladdress.setError("Empty Full Address");
//                Toast.makeText(checkout.this, "Empty Full Address", Toast.LENGTH_LONG).show();
//                return;
//            }
            if (!cash_rd.isSelected() && !pay_type.equals("cash") && !pay_type.equals("Product Balance")
                    && !pay_type.equals("Commision Balance") && !pay_type.equals("Add Funds Balance")) {
                if (mobileNO.equals("")) {
                    Toast.makeText(checkout.this, "Empty Mobile No", Toast.LENGTH_LONG).show();
                    Toast.makeText(checkout.this, "Please Select Payment Method", Toast.LENGTH_LONG).show();
                    return;
                }
                if (trnID.equals("")) {
                    Toast.makeText(checkout.this, "Empty Transaction ID", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            submit();
        } catch (Exception e) {
        }

    }

    private void submit() {
        progressDialog = ProgressDialog.show(checkout.this, "", "Loading...", false, false);
        try {
            String first_nameS = "", phoneS = "", emailS = "", fulladdressS = "";
            session = homeViewModel.getSession(checkout.this);
            first_nameS = first_name.getText().toString();
            phoneS = Phone.getText().toString();
//            emailS = Email.getText().toString();
            fulladdressS = fulladdress.getText().toString();
            String thisDate = operation.getCreateDate();

            String sql = "INSERT INTO `delivery_infos` (`first_name`,`last_name`,`email`,`address`,`phone`," +
                    "`country`,`district_id`,`session_id`,`created_at`) VALUES ('" + first_nameS + "',''," +
                    "'" + emailS + "','" + fulladdressS + "','" + phoneS + "','BD','" + districtid + "','" + session + "','" + thisDate + "')";

            deliveryAdd(checkout.this, sql, progressDialog);
        } catch (Exception e) {
        }
    }

    public void close() {
        finish();
    }

    public void invoice_query(String response, String gid, Context context, ProgressDialog progressDialog) {
        session = homeViewModel.getSession(checkout.this);
        String sql1 = "INSERT INTO `invoices` (`invoice_id`,`delivery_id`,`guest_id`," +
                "`coupon_id`,`delivery_charge`,`payment_type`,`mobile_acc`,`trans_id`," +
                "`discount`,`sub_total`,`grand_total`,`session_id`,`status`" +
                ") VALUES ('";
        String sql2 = "','" + response + "','" + gid + "','" + id + "','" + del_ch + "','" + pay_type + "'," +
                "'" + mobileNO + "','" + trnID + "','" + disTK + "'," +
                "'" + subTK + "','" + totalTK + "'," +
                "'" + session + "','0')";
        operation.invoice_proccess(context, sql1, sql2, progressDialog, id, subTK, disTK, String.valueOf(del_ch), grnT.getText().toString(), pay_type);

    }

    public void deliveryAdd(Context context, String sql, ProgressDialog progressDialog) {

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT_DELIVERY,
                    response -> {
                        if (!response.equals("failed") && !response.equals("problem")) {
                            String gid = homeViewModel.getGuestID(context);
                            if (gid == null || gid.equals("")) {
                                gid = "1";
                                //sign_in(context, response, progressDialog, "0");
//                                    online_gid(context,response);
                            }
                            //else
                                invoice_query(response, gid, context, progressDialog);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                        }
                    }, error -> {
                        progressDialog.dismiss();
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
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

    private void online_gid(Context context, String delID, String username, String pass, ProgressDialog progressDialog, String check) {
        try {
            if (check.equals("1")) {
                progress = ProgressDialog.show(checkout.this, "", "Loading", false, false);
            }
//            String sql = "SELECT id FROM `guest` WHERE (email = '"+username+"' OR phone = '"+username+"') AND password = '"+pass+"'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_LOGIN_INFO,
                    response -> {
                        if (check.equals("1"))
                            progress.dismiss();
                        if (!response.equals("1") && !response.equals("") && !response.equals("{\"result\":[]}")) {
                            show_user_data(response, check, context, delID, progressDialog);

                        } else {
                            Toast.makeText(context, "Failed, Please try again", Toast.LENGTH_LONG).show();
                            sign_in(context, delID, progressDialog, check);
                        }
                    }, error -> {
                        if (check.equals("1"))
                            progress.dismiss();
                        else
                            progressDialog.dismiss();
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                        sign_in(context, delID, progressDialog, check);
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
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    public void show_user_data(String response, String check, Context context, String delID, ProgressDialog progressDialog) {
        try {
            String firstname = "", lastname = "", email = "", phone = "", gid = "", Address = "";
            if (!check.equals("1")) {
                invoice_query(delID, response, context, progressDialog);
                homeViewModel.insertuser(context, firstname + " " + lastname, phone, email, pass, response);
            } else {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray result = jsonObject.getJSONArray(config.RESULT);
                for (int i = 0; i <= result.length(); i++) {
                    JSONObject collegeData = result.getJSONObject(i);
                    gid = collegeData.getString(config.CAT_ID);
                    firstname = collegeData.getString(config.PRO_NAME);
                    lastname = collegeData.getString(config.CAT_NAME);
                    email = collegeData.getString(config.EMAIL);
                    phone = collegeData.getString(config.PHONE);
                    Address = collegeData.getString(config.PRO_SALE_PRICE);

//                    invoice_query(delID, gid, context,progressDialog);
                    homeViewModel.insertuser(context, firstname + " " + lastname, phone, email, pass, gid);
                    first_name.setText(firstname);
                    Phone.setText(phone);
//                Email.setText(email);
                    fulladdress.setText(Address);

                }
            }
        } catch (Exception e) {
        }
    }

    private void sign_in(Context context, String response, ProgressDialog progressDialog, String check) {
        signin_view = LayoutInflater.from(checkout.this).inflate(R.layout.signin_dialog, null);
        phone_n = signin_view.findViewById(R.id.username_di);
        trn_n = signin_view.findViewById(R.id.password_di);
        Button otpb = signin_view.findViewById(R.id.send_otp);
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(checkout.this);
        AlertDialog alertDialog = dialogBuilder.create();
        dialogBuilder.setTitle("Sign In");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setView(signin_view);
        dialogBuilder.setNegativeButton("Sign Up", (dialog, which) -> {
            dialog.cancel();
            sign_up(context, response, progressDialog, check);
        });
        dialogBuilder.setPositiveButton("Sign In", (dialog, which) -> {
            username = phone_n.getText().toString();
            pass = trn_n.getText().toString();
//            if (otp == 0)
                online_gid(context, response, username, pass, progressDialog, check);
//            else {
//                ProgressDialog progressDialog1 = ProgressDialog.show(checkout.this,"","Loading...",false,false);
//                otp_login(checkout.this, username, pass, progressDialog1);
//            }
        });
        dialogBuilder.setNeutralButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
            finish();
            if (!check.equals("1"))
                progressDialog.dismiss();
        });
//        otpb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                send(context, phone_n.getText().toString());
//                otp = 1;
//            }
//        });
        alertDialog.show();
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
    private void otp_login(Context context, String username, String otp,String check,String delID, ProgressDialog progressDialog) {
        try {

            String sql = "SELECT id FROM `guest` WHERE phone = '" + username + "' AND code = '" + otp + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (!response.trim().equals("")) {
                                show_user_data(response, check, context, delID, progressDialog);

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

    public void sign_up(Context context, String response, ProgressDialog progressDialog, String check) {
        singup_view = LayoutInflater.from(checkout.this).inflate(R.layout.signup_dialog, null);
        firstname_ed = singup_view.findViewById(R.id.username_dis);
        phone_ed = singup_view.findViewById(R.id.phone_dis);
        email_ed = singup_view.findViewById(R.id.email_txt_dis);
        address_ed = singup_view.findViewById(R.id.address_ed);
        password_ed = singup_view.findViewById(R.id.vp_ed_dis);
        con_password_ed = singup_view.findViewById(R.id.vc_ed_dis);
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(checkout.this);
        dialogBuilder.setTitle("Sign Up");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setView(singup_view);
        dialogBuilder.setNegativeButton("Sign In", (dialog, which) -> {
            dialog.cancel();
            sign_in(context, response, progressDialog, check);
        });
        dialogBuilder.setPositiveButton("Sign Up", (dialog, which) -> {
            if (firstname_ed.getText().toString().trim().equals("")) {
                firstname_ed.setError("Empty First Name");
                return;
            }
            if (phone_ed.getText().toString().trim().equals("")) {
                phone_ed.setError("Empty Phone Number");
                return;
            }
//            if(address_ed.getText().toString().trim().equals("")){
//                address_ed.setError("Empty Email");
//                return;
//            }
            if (password_ed.getText().toString().equals("")) {
                password_ed.setError("Empty Password");
                return;
            }
            if (password_ed.getText().toString().length() < 5) {
                password_ed.setError("Password must be >= 5 character");
                return;
            }
            if (!password_ed.getText().toString().equals(con_password_ed.getText().toString())) {
                con_password_ed.setError("Password didn't matched");
                return;
            }
            if (check.equals("1")) {
                progress = ProgressDialog.show(checkout.this, "", "Loading", false, false);
            }
            check_phone_email(context, firstname_ed.getText().toString(), "", phone_ed.getText().toString()
                    , email_ed.getText().toString(), password_ed.getText().toString(), address_ed.getText().toString(), response, check, progressDialog, progress);
            showSignGuest(firstname_ed.getText().toString(), "", phone_ed
                    .getText().toString(), email_ed.getText().toString(), address_ed.getText().toString());
        });
        dialogBuilder.setNeutralButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
            finish();
            if (!check.equals("1"))
                progressDialog.dismiss();
        });
        dialogBuilder.show();
    }

    public void getGuestData(String sql) {
        try {

            String query = "SELECT * FROM guest WHERE id = '" + sql + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GUEST_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("1")) {
                                showGuest(response);
                            } else
                                Toast.makeText(checkout.this, response, Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(checkout.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, query);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(checkout.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private void showGuest(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                first_name.setText(collegeData.getString(config.PRO_NAME));
                Phone.setText(collegeData.getString(config.PRO_DISCOUNT_PRICE));
//                Email.setText(collegeData.getString(config.EMAIL));
                fulladdress.setText(collegeData.getString(config.PRO_CURRENT_PRICE));
            }
        } catch (Exception e) {
        }
    }

    public void showSignGuest(String firstname, String lastname, String phone, String email, String address) {
        try {
            first_name.setText(firstname);
            Phone.setText(phone);
//                Email.setText(email);
            fulladdress.setText(address);
        } catch (Exception e) {
        }
    }

    public void getCharge(String sql, String shipping) {
        try {
//            String query = "SELECT SUM(`charge`) AS 'id' FROM `delivery_charges` WHERE `district_id` = '"+sql+"' AND `shipping_id` IN ("+shipping+")";
            String query = "SELECT SUM(delivery_charges.`charge`) AS 'id'" +
                    " FROM delivery_charges " +
                    "INNER JOIN zone_districts ON zone_districts.`zone_id` = delivery_charges.`zone_id` " +
                    "WHERE delivery_charges.`shipping_id` IN (" + shipping + ") AND zone_districts.`thana_id` = '" + sql + "'";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null && !response.equals("1")) {
                                try {
                                    if (response.equals("")) {
                                        deliT.setText("0");
                                        gr_t = Double.parseDouble(grnT.getText().toString());
                                        Total = gr_t - del_ch;
                                        del_ch = 0;
                                        grnT.setText(String.valueOf(Total));
                                        totalTK = grnT.getText().toString();
                                        subTK = subT.getText().toString();
                                    } else {
                                        deliT.setText(response);
                                        gr_t = Double.parseDouble(grnT.getText().toString());
                                        Total = gr_t - del_ch;
                                        del_ch = Double.parseDouble(response);
                                        Total = del_ch + Total;
                                        grnT.setText(String.valueOf(Total));
                                        totalTK = grnT.getText().toString();
                                        subTK = subT.getText().toString();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                deliT.setText("0");
                                gr_t = Double.parseDouble(grnT.getText().toString());
                                Total = gr_t - del_ch;
                                del_ch = 0;
                                grnT.setText(String.valueOf(Total));
                                totalTK = grnT.getText().toString();
                                subTK = subT.getText().toString();
                            }
                            getBalance("SELECT sum(amount - withdraw) as 'id' from member_product_balance WHERE " +
                                    "member_id = '" + homeViewModel.getSellerID(checkout.this) + "'", pro_bal_id, 1, getResources().getString(R.string.product_balance));
                            getBalance("SELECT sum(commision_balance - withdraw) as 'id' from order_customer_commision WHERE " +
                                    "member_id = '" + homeViewModel.getSellerID(checkout.this) + "'", com_bal_id, 2, getResources().getString(R.string.commission_balance));
                            getBalance("SELECT sum(amount - withdraw) as 'id' from addfunds WHERE " +
                                    "member_id = '" + homeViewModel.getSellerID(checkout.this) + "'", fund_id, 3, getResources().getString(R.string.funds_balance));
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(checkout.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, query);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(checkout.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
//            int item;
//            String inQuery = "";
//            cat_model cat_model;
            int id_dis = getPosition(district.getText().toString());
            if (id_dis != -1) {
                districtid = String.valueOf(id_dis);
            } else
                districtid = "0";
//            gr_t = Double.parseDouble(grnT.getText().toString());
//            Total =  gr_t - del_ch;


//            Total = del_ch + Total;
//            deliT.setText(cat_model.getId());
//            grnT.setText(String.valueOf(Total));
//            districtid = cat_model.getId();
            thanaList.clear();
            thanamodel.clear();
            thana.setText("");
            showThana(districtid);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(checkout.this,
                    R.layout.item_name, thanamodel);
            thana.setAdapter(dataAdapter);
            totalTK = grnT.getText().toString();
            subTK = subT.getText().toString();
        } catch (Exception e) {
        }
    }

    private int getPosition(String pro_model) {
        try {
            for (cat_model x : districtList) {
                if (x.getImage().equals(pro_model)) {
                    return Integer.parseInt(x.getId().trim());
                }
            }
        } catch (Exception e) {
        }
        return -1;
    }

    public void insertuserData(Context context, String firstname, String lastname, String phone, String email,
                               String password, String address, String reponse, String check, ProgressDialog progressDialog, ProgressDialog progress) {
        try {
            homeViewModel = new HomeViewModel();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_GUEST_REG,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (check.equals("1"))
                                progress.dismiss();
                            if (!response.equals("Could not Registered in online") && !response.equals("")) {
                                if (check.equals("0")) {
                                    invoice_query(reponse, response, context, progressDialog);
                                }
//                                checkout.show_user_data(response,check,context,reponse,progressDialog);
                                homeViewModel.insertuser(context, firstname + " " + lastname, phone, email, password, response);
                            } else {
                                Toast.makeText(context, "Failed to Sign up", Toast.LENGTH_LONG).show();
                                sign_up(context, reponse, progressDialog, check);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (check.equals("1"))
                        progress.dismiss();
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    sign_up(context, reponse, progressDialog, check);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.FIRST_N, firstname + " " + lastname);
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

    public void check_phone_email(Context context, String firstname, String lastname, String phone, String email,
                                  String password, String address, String reponse, String check, ProgressDialog progressDialog, ProgressDialog progress) {
        try {
            String sql = "SELECT id FROM guest WHERE phone = '" + phone + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (!response.equals("1")) {
                                if (check.equals("0")) {
                                    Toast.makeText(context, "Phone already taken", Toast.LENGTH_LONG).show();
                                    sign_up(context, reponse, progressDialog, check);
                                } else {
                                    Toast.makeText(context, "Phone already taken", Toast.LENGTH_LONG).show();
                                    sign_up(context, reponse, progressDialog, check);
                                    progress.dismiss();
                                }
                            } else {
                                insertuserData(context, firstname, lastname, phone, email, password, address, reponse, check, progressDialog, progress);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (check.equals("1"))
                        progress.dismiss();
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    sign_up(context, reponse, progressDialog, check);
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

    public void getBalance(String sql, RadioButton radioButton, int check, String text) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.trim().equals("")) {
                                radioButton.setText(text + " " + response);
                                switch (check) {
                                    case 1:
                                        pro_balance = Double.parseDouble(response.trim());
                                        radioButton.setEnabled(Double.parseDouble(grnT.getText().toString()) <= pro_balance);
                                        break;
                                    case 2:
                                        commission_balance = Double.parseDouble(response.trim());
                                        radioButton.setEnabled(Double.parseDouble(grnT.getText().toString()) <= commission_balance);
                                        break;
                                    case 3:
                                        fund_balance = Double.parseDouble(response.trim());
                                        radioButton.setEnabled(Double.parseDouble(grnT.getText().toString()) <= fund_balance);
                                        break;
                                }
                            } else {
                                radioButton.setText(text + " 0");
                                radioButton.setEnabled(false);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    radioButton.setEnabled(false);
                    Toast.makeText(checkout.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(checkout.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }


}