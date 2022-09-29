package com.sbitbd.fixedbd.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.fixedbd.Adapter.checkout_pro_adapter;
import com.sbitbd.fixedbd.Config.DoConfig;
import com.sbitbd.fixedbd.MainActivity;
import com.sbitbd.fixedbd.R;
import com.sbitbd.fixedbd.ui.cart_operation.operation;
import com.sbitbd.fixedbd.ui.home.HomeViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class invoice extends AppCompatActivity {

    private TextView invoice,order_date,payment,delivery_d,total,discount,delivery_ch,gr_total,
    paid,due;
    private Button hbtn;
    private HomeViewModel homeViewModel;
    private DoConfig config = new DoConfig();
    private operation operation = new operation();
    private checkout_pro_adapter checkout_pro_adapter;
    private RecyclerView recyclerView;
    private String invoice_s,delivery,sub,dis,del,total_s;
    private static final String CHANNEL_ID = "channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        initview();
    }
    private void initview(){
        invoice = findViewById(R.id.invoice_id_t);
        order_date = findViewById(R.id.order_date_t);
        payment = findViewById(R.id.payment_t);
        delivery_d = findViewById(R.id.del_t);
        total = findViewById(R.id.total_inid);
        discount = findViewById(R.id.dis_inid);
        delivery_ch = findViewById(R.id.delivery_inid);
        gr_total = findViewById(R.id.grand_inid);
        paid = findViewById(R.id.paid_inid);
        due = findViewById(R.id.due_inid);
        hbtn = findViewById(R.id.shop_btn);
        recyclerView = findViewById(R.id.inv_pro);
        initpro_check();
        setView();
        getdate();
        hbtn.setOnClickListener(v -> {
            startActivity(new Intent(invoice.this, MainActivity.class));
            finish();
        });


    }
    private void setView(){
        try {
            invoice_s = getIntent().getStringExtra("invoice");
            delivery = getIntent().getStringExtra("delivery");
            sub = getIntent().getStringExtra("sub");
            dis = getIntent().getStringExtra("dis");
            del = getIntent().getStringExtra("del");
            total_s = getIntent().getStringExtra("total");
            if (invoice_s != null && !invoice_s.equals("")){
                invoice.setText(invoice_s);
            }
            if (delivery != null && !delivery.equals("")){
                delivery_d.setText(delivery);
            }
            if (sub != null && !sub.equals("")){
                total.setText(sub);
            }
            if (dis != null && !dis.equals("")){
                discount.setText(dis);
            }
            if (del != null && !del.equals("")){
                delivery_ch.setText(del);
            }
            if (total_s != null && !total_s.equals("")){
                gr_total.setText(total_s);
            }

            createNotificationChannel();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.buynfeel)
                    .setContentTitle("Your order has been completed")
                .setContentText("Your order ID: "+invoice_s+" from FixedBD\nThanks for " +
                        "staying with us.")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Your order ID: "+invoice_s+" from FixedBD\nThanks for " +
                                    "staying with us."))
                    .setAutoCancel(false)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            notificationManager.notify(1, builder.build());

        }catch (Exception e){
        }
    }

    private void initpro_check(){
        checkout_pro_adapter = new checkout_pro_adapter(invoice.this,total,total);
        GridLayoutManager manager = new GridLayoutManager(invoice.this, 1);
        recyclerView.setLayoutManager(manager);
        getOnlinedata();
        recyclerView.setAdapter(checkout_pro_adapter);
    }

    private void getOnlinedata(){
        try {
            homeViewModel = new HomeViewModel();
            String session = homeViewModel.getSession(invoice.this);
            String sql = "SELECT `shopping_carts`.`product_id`,`shopping_carts`.`quantity`,`product_productinfo`." +
                    "`product_name`,`product_productinfo`.`current_price`,`product_productinfo`.`shipping_id` FROM `shopping_carts` INNER JOIN " +
                    "`product_productinfo` ON `shopping_carts`.`product_id` = `product_productinfo`.id WHERE " +
                    "`shopping_carts`.`session_id` = '"+session+"'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.CHECK_PRO_DATA,
                    response -> {
                        operation.showCheck_data(response,checkout_pro_adapter);
                        total.setText(sub);
//                            gr_total.setText(total_s);
                    }, error -> Toast.makeText(invoice.this, error.toString(), Toast.LENGTH_LONG).show()) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(invoice.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }catch (Exception e){
        }
    }
    private void getdate(){
        try {
            homeViewModel = new HomeViewModel();
//            String session = homeViewModel.getSession(invoice.this);
            String sql = "SELECT grand_total AS 'one',created_at AS 'two',payment_type AS 'three',amount AS 'four' FROM invoices WHERE invoice_id = '"+invoice_s+"'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.FOUR_DMS,
                    response -> {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray result = jsonObject.getJSONArray(config.RESULT);
                            JSONObject collegeData = result.getJSONObject(0);
                            payment.setText(collegeData.getString(config.THREE));
                            order_date.setText(collegeData.getString(config.TWO));
                            gr_total.setText(collegeData.getString(config.ONE));
                            paid.setText(collegeData.getString(config.FOUR));
                            double due_ = Double.parseDouble(collegeData.getString(config.ONE)) -
                                    Double.parseDouble(collegeData.getString(config.FOUR));
                            due.setText(String.valueOf(due_));
                        }catch (Exception e){
                        }
                    }, error -> Toast.makeText(invoice.this, error.toString(), Toast.LENGTH_LONG).show()) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(invoice.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }catch (Exception e){
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(invoice.this,MainActivity.class));
        finish();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

}