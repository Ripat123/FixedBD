package com.sbitbd.fixed_bd.ui.seller_form.ui.product_add;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.fixed_bd.Adapter.cat_model;
import com.sbitbd.fixed_bd.Adapter.image_model;
import com.sbitbd.fixed_bd.Adapter.size_model;
import com.sbitbd.fixed_bd.Config.DoConfig;
import com.sbitbd.fixed_bd.MainActivity;
import com.sbitbd.fixed_bd.R;
import com.sbitbd.fixed_bd.ui.home.HomeViewModel;
import com.sbitbd.fixed_bd.ui.seller_form.ui.dashboard.VolleyMultipart;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class product_form extends AppCompatActivity {


    private HomeViewModel homeViewModel = new HomeViewModel();
    private ListView listView, color_list;
    private List<String> size_list = new ArrayList<>();
    private List<size_model> size_list1 = new ArrayList<>();
    private List<String> colorlist = new ArrayList<>();
    private List<size_model> colorlist1 = new ArrayList<>();
    private Button submit, selectimg, clnbtn;
    private DoConfig config = new DoConfig();
    private List<cat_model> itemList = new ArrayList<>();
    private List<String> itemmodel = new ArrayList<>();
    private List<cat_model> categoryList = new ArrayList<>();
    private List<String> categorymodel = new ArrayList<>();
    private List<cat_model> subcategoryList = new ArrayList<>();
    private List<String> subcategorymodel = new ArrayList<>();
    private List<cat_model> brandList = new ArrayList<>();
    private List<String> brandmodel = new ArrayList<>();
    private List<cat_model> mesurementList = new ArrayList<>();
    private List<String> mesurementmodel = new ArrayList<>();
    private List<String> stocklist = new ArrayList<>();
    private List<String> publist = new ArrayList<>();
    private List<String> sipmodel = new ArrayList<>();
    private List<cat_model> siplist = new ArrayList<>();
    private AutoCompleteTextView item, category, subcategory, brand, mesurement, stock, ship;
    private List<image_model> bitmapList = new ArrayList<>();
    private List<String> size = new ArrayList<>();
    private List<String> color = new ArrayList<>();
    private int CODE_GALLERY_REQUEST = 55555, img_size = 0;
    private Bitmap bitmap;
    private TextView img_count;
    private View viewmain;
    private ProgressDialog progressDialog;
    private String itemID, categoryID, subcategoryID, brandID, sellerID, measurementID, stockAID, shippingID, first_image = "", proID;
    private double discount_percent = 0, sale_amount = 0;
    private EditText pro_code, pro_name, min_quant, purchase_price, sale_price, discount, current_price, pro_des, pro_details, pro_cond;
    private MainActivity mainActivity =new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        sellerID = homeViewModel.getSellerID(product_form.this);
    }

    private void initView() {
        try {
            submit = findViewById(R.id.submit_btn_seller);
            selectimg = findViewById(R.id.image_btn);
            clnbtn = findViewById(R.id.clean_seller);
            listView = findViewById(R.id.size_list);
            img_count = findViewById(R.id.imgcount);
            color_list = findViewById(R.id.color_list);
            item = findViewById(R.id.item);
            category = findViewById(R.id.category);
            subcategory = findViewById(R.id.subcategory);
            brand = findViewById(R.id.brand);
            mesurement = findViewById(R.id.mes_type);
            stock = findViewById(R.id.stock);
            ship = findViewById(R.id.ship);
            pro_code = findViewById(R.id.pro_code);
            pro_name = findViewById(R.id.pro_name_e);
            min_quant = findViewById(R.id.min_quant);
            purchase_price = findViewById(R.id.purchase_p);
            sale_price = findViewById(R.id.sale_p);
            discount = findViewById(R.id.discount);
            current_price = findViewById(R.id.current_p);
            pro_des = findViewById(R.id.short_des);
            pro_details = findViewById(R.id.pro_det);
            pro_cond = findViewById(R.id.pro_cond);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            color_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    CheckedTextView checkedTextView = (CheckedTextView) view;
//                    boolean currentChecked = checkedTextView.isChecked();
//                    String value = size_list.get(position);
//                }
//            });
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit(v);
//
                }
            });
            purchase_price.setText("0");
            sale_price.setText("0");
            discount.setText("0");
            current_price.setText("0");

            showItem(product_form.this, "SELECT product_item.item_name AS 'charge',`product_item`.`id` FROM product_item",
                    itemmodel, itemList, item);
            showItem(product_form.this, "SELECT product_category.category_name AS 'charge',`product_category`.`id` FROM product_category",
                    categorymodel, categoryList, category);
            showItem(product_form.this, "SELECT product_subcategory.subcategory_name AS 'charge',`product_subcategory`.`id` FROM product_subcategory",
                    subcategorymodel, subcategoryList, subcategory);
            showItem(product_form.this, "SELECT product_company.company_name AS 'charge',`product_company`.`id` FROM product_company",
                    brandmodel, brandList, brand);

            showItem(product_form.this, "SELECT product_measurement.measurement_type AS 'charge',`product_measurement`.`id` FROM product_measurement",
                    mesurementmodel, mesurementList, mesurement);
            showItem(product_form.this, "SELECT shipping_classes.shipping_name AS 'charge',`shipping_classes`.`id` FROM shipping_classes",
                    sipmodel, siplist, ship);
            sizeRender(product_form.this, listView, size_list, size_list1);
            colorRender( color_list, colorlist, colorlist1);

            stocklist.add("Stock Available");
            stocklist.add("Stock Out");
            setAuto(product_form.this, stocklist, stock);

            item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    itemID = itemList.get(position).getId();
                }
            });
            category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    categoryID = categoryList.get(position).getId();
                }
            });
            subcategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    subcategoryID = subcategoryList.get(position).getId();
                }
            });
            brand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    brandID = brandList.get(position).getId();
                }
            });
            mesurement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    measurementID = mesurementList.get(position).getId();
                }
            });
            ship.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    shippingID = siplist.get(position).getId();
                }
            });
            stock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0)
                        stockAID = "1";
                    else if (position == 1)
                        stockAID = "0";
                }
            });

            selectimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ActivityCompat.requestPermissions(
//                            getActivity(),
//                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                            CODE_GALLERY_REQUEST
//                    );
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, CODE_GALLERY_REQUEST);
                }
            });
        } catch (Exception e) {
        }

        TextWatcher textWatcher_sale = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    sale_amount = Double.parseDouble(sale_price.getText().toString().trim());
                    current_price.setText(String.valueOf(sale_amount - Double.parseDouble(discount.getText().toString().trim())));
                } catch (Exception e) {
                }
            }
        };
        TextWatcher textWatcher_discount = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    double taka;
                    if (discount.getText().toString() == null || discount.getText().toString().equals("")) {
                        taka = 0;
                    } else {
                        taka = Double.parseDouble(discount.getText().toString().trim());
                    }
                    double sum = sale_amount / 100;
                    discount_percent = taka / sum;
                    current_price.setText(String.valueOf(sale_amount - taka));
                } catch (Exception e) {
                }
            }
        };
        sale_price.addTextChangedListener(textWatcher_sale);
        discount.addTextChangedListener(textWatcher_discount);
    }

    private void sizeRender(Context root, ListView listView, List<String> stringList, List<size_model> size_models) {
        try {
            try {
                String sql = "SELECT product_size_infos.size_title AS 'image' FROM product_size_infos";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, config.SLIDER_IMG,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                showDistrictList(response, listView, stringList, size_models);
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(root, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(config.QUERY, sql);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(root);
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            } catch (Exception e) {

            }
        } catch (Exception e) {
        }
    }

    private void colorRender( ListView listView, List<String> stringList, List<size_model> size_models) {
        try {
            String sql = "SELECT product_color_infos.color_title AS 'image' FROM product_color_infos";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.SLIDER_IMG,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            showDistrictList(response, listView, stringList, size_models);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(product_form.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(product_form.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private void showDistrictList(String response, ListView listView, List<String> stringList, List<size_model> size_models) {
        String id = "";
        try {
            stringList.clear();
            size_models.clear();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            final int len = result.length();
            for (int i = 0; i < len; i++) {
                JSONObject collegeData = result.getJSONObject(i);
                id = collegeData.getString(config.PRO_IMAGE);
                stringList.add(id);
                size_models.add(new size_model(id));
            }
            ArrayAdapter<String> arrayAdapter
                    = new ArrayAdapter<String>(product_form.this, android.R.layout.simple_list_item_multiple_choice, stringList);

            listView.setAdapter(arrayAdapter);

            for (int i = 0; i < stringList.size(); i++) {
                listView.setItemChecked(i, size_models.get(i).isActive());
            }
        } catch (Exception e) {
        }
    }

    public List<String> printSelectedItems( ListView listView, List<size_model> models) {
        List<String> list = new ArrayList<>();
        try {
            SparseBooleanArray sp = listView.getCheckedItemPositions();
            int si = sp.size() - 1;
            for (int i = 0; i <= si; i++) {
                if (sp.valueAt(i)) {

                    size_model s = models.get(i);
                    list.add(s.getName());
                }
            }
        } catch (Exception e) {
        }
        return list;
    }

    private void showItem(Context context, String query, List<String> stringList, List<cat_model> cat_modellist, AutoCompleteTextView completeTextView) {
        try {
            String sql = query;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.DISTRICT_DELIVERY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            showDistrictList(response, context, stringList, cat_modellist, completeTextView);
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

    private void showDistrictList(String response, Context root, List<String> stringList, List<cat_model> cat_modellist, AutoCompleteTextView completeTextView) {
        String id = "";
        String name = "";
        String quant = "";
        cat_model cat_models;
        try {
            cat_modellist.clear();
            stringList.clear();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            final int len = result.length();
            for (int i = 0; i < len; i++) {
                JSONObject collegeData = result.getJSONObject(i);
                name = collegeData.getString(config.PRO_NAME);
                quant = collegeData.getString(config.PRO_CURRENT_PRICE);
                id = collegeData.getString(config.CAT_ID);
                cat_models = new cat_model(quant, name, id);
                cat_modellist.add(cat_models);
                stringList.add(quant);
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(root,
                    R.layout.item_name, stringList);
            completeTextView.setAdapter(dataAdapter);
        } catch (Exception e) {
        }
    }

    private void setAuto(Context root, List<String> stringList, AutoCompleteTextView completeTextView) {
        try {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(root,
                    R.layout.item_name, stringList);
            completeTextView.setAdapter(dataAdapter);
        } catch (Exception e) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(viewmain.getContext().getApplicationContext(), "yes", Toast.LENGTH_LONG).show();
        if (requestCode == CODE_GALLERY_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, CODE_GALLERY_REQUEST);
            }
            return;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                bitmapList.add(new image_model(bitmap, sellerID + System.currentTimeMillis() + ".jpeg"));
                first_image = bitmapList.get(0).getImage_name();
                img_size = bitmapList.size();
                img_count.setText(img_size + " Images Selected");
            } catch (Exception e) {
            }

        }
    }

    private void uploadBitmap(final Bitmap bitmap, String id, String image_name) {
        String sql2 = "INSERT INTO `product_images` (`product_id`,`image`) VALUES ('" + id + "','" + image_name + "')";

        //our custom volley request
        VolleyMultipart volleyMultipartRequest = new VolleyMultipart(Request.Method.POST, config.PRO_IMAGE_UPLOAD,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
//                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(product_form.this, "Uploaded", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(product_form.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tags", sql2);
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
//                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(image_name, getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(product_form.this).add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void submit(View v) {
        try {
            if (pro_code.getText().toString().trim().equals("")) {
                pro_code.setError("Empty Product Code");
                Toast.makeText(product_form.this, "Empty Product Code", Toast.LENGTH_LONG).show();
                return;
            }
            if (item.getText().toString().trim().equals("") || itemID.equals("")) {
                Toast.makeText(product_form.this, "Select Item", Toast.LENGTH_LONG).show();
                return;
            }
            if (category.getText().toString().trim().equals("") || categoryID.equals("")) {
                Toast.makeText(product_form.this, "Select Category", Toast.LENGTH_LONG).show();
                return;
            }
            if (subcategory.getText().toString().trim().equals("") || subcategoryID.equals("")) {
                Toast.makeText(product_form.this, "Select SubCategory", Toast.LENGTH_LONG).show();
                return;
            }
            if (brand.getText().toString().trim().equals("") || brandID.equals("")) {
                Toast.makeText(product_form.this, "Select Brand", Toast.LENGTH_LONG).show();
                return;
            }
            if (pro_name.getText().toString().trim().equals("")) {
                pro_name.setError("Empty Product Name");
                Toast.makeText(product_form.this, "Empty Product Name", Toast.LENGTH_LONG).show();
                return;
            }
            if (min_quant.getText().toString().trim().equals("")) {
                min_quant.setError("Empty Product Name");
                Toast.makeText(product_form.this, "Empty Min. Quantity", Toast.LENGTH_LONG).show();
                return;
            }
            if (mesurement.getText().toString().trim().equals("") || measurementID.equals("")) {
                Toast.makeText(product_form.this, "Select Measurement", Toast.LENGTH_LONG).show();
                return;
            }
            if (purchase_price.getText().toString().trim().equals("")) {
                purchase_price.setError("Empty Purchase Price");
                Toast.makeText(product_form.this, "Empty Purchase Price", Toast.LENGTH_LONG).show();
                return;
            }
            if (sale_price.getText().toString().trim().equals("")) {
                sale_price.setError("Empty Sale Price");
                Toast.makeText(product_form.this, "Empty Sale Price", Toast.LENGTH_LONG).show();
                return;
            }
            if (discount.getText().toString().trim().equals("")) {
                discount.setError("Empty Discount");
                Toast.makeText(product_form.this, "Empty Discount", Toast.LENGTH_LONG).show();
                return;
            }
            if (current_price.getText().toString().trim().equals("")) {
                current_price.setError("Empty Current Price");
                Toast.makeText(product_form.this, "Empty Current Price", Toast.LENGTH_LONG).show();
                return;
            }
            if (stock.getText().toString().trim().equals("") || stockAID.equals("")) {
                Toast.makeText(product_form.this, "Empty Stock Status", Toast.LENGTH_LONG).show();
                return;
            }
            if (ship.getText().toString().trim().equals("") || shippingID.equals("")) {
                Toast.makeText(product_form.this, "Select Shipping Class", Toast.LENGTH_LONG).show();
                return;
            }
            progressDialog = ProgressDialog.show(product_form.this, "", "Loading", false, false);
            AddProduct(progressDialog,v);

        } catch (Exception e) {
        }
    }

    private void AddProduct(ProgressDialog progressDialog, View v) {

        try {
            String sql = "INSERT  INTO `product_productinfo`(`product_id`,`item_id`,`category_id`," +
                    "`subcategory_id`,`brand_id`,`shipping_id`,`product_name`,`measurement_type`," +
                    "`purchase_price`,`sale_price`,`discount_price`,`discount_per`,`current_price`," +
                    "`min_qunt`,`product_us`,`product_details`,`condition`,`image`,`seller_id`,`status`," +
                    "`stock_status`,`created_at`,`updated_at`) VALUES ('" + pro_code.getText().toString().trim() + "'," +
                    "'" + itemID + "','" + categoryID + "','" + subcategoryID + "','" + brandID + "','" + shippingID + "'," +
                    "'" + pro_name.getText().toString().trim() + "','" + measurementID + "','" + purchase_price.getText().toString().trim() + "'," +
                    "'" + sale_price.getText().toString().trim() + "','" + discount.getText().toString().trim() + "','" + discount_percent + "'," +
                    "'" + current_price.getText().toString().trim() + "','" + min_quant.getText().toString().trim() + "'," +
                    "'" + pro_des.getText().toString().trim() + "','" + pro_details.getText().toString().trim() + "'," +
                    "'" + pro_cond.getText().toString().trim() + "','" + first_image + "','" + sellerID + "','0'," +
                    "'" + stockAID + "',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP())";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.ADD_PRODUCT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.equals("")) {
                                proID = response;
                                ArrayInsert( printSelectedItems( color_list, colorlist1), "INSERT INTO " +
                                        "`product_color` (`product_id`,status,`admin_id`,`color`) VALUES ('" + response + "','1','0','", 0,progressDialog,v);
                                ArrayInsert( printSelectedItems( listView, size_list1), "INSERT INTO " +
                                        "`product_size` (`product_id`,status,`admin_id`,`size`) VALUES ('" + response + "','1','','", 1,progressDialog,v);
                            }else
                                progressDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(product_form.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(product_form.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private void ArrayInsert( List<String> stringList, String id, int check,ProgressDialog progressDialog,View v) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.ADD_ARRAY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                                Toast.makeText(root.getContext().getApplicationContext(),response,Toast.LENGTH_LONG).show();

                            if (check == 1) {
                                try {
                                    for (int i = 0; i < bitmapList.size(); i++) {
                                        uploadBitmap(bitmapList.get(i).getImage(), proID, bitmapList.get(i).getImage_name());
                                    }
                                    progressDialog.dismiss();
                                    mainActivity.regularSnak("Successful",v);
                                } catch (Exception e) {
                                }
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(product_form.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        for (int i = 0; i < stringList.size(); i++) {
                            jsonObject.put("" + i, id + stringList.get(i) + "')");
                        }
                    } catch (Exception e) {
                    }
                    params.put(config.QUERY, jsonObject.toString());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(product_form.this);
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
}