package com.sbitbd.fixed_bd.ui.edit_profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.sbitbd.fixed_bd.Config.DoConfig;
import com.sbitbd.fixed_bd.R;
import com.sbitbd.fixed_bd.ui.home.HomeViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class edit extends AppCompatActivity {

    private ImageView change_img,imagev;
    final int CODE_GALLERY_REQUEST = 9999;
    private EditText first_name,email,phone,address;
    private Button reg;
    String id;
    Bitmap bitmap;
    DoConfig config = new DoConfig();
    HomeViewModel homeViewModel = new HomeViewModel();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();

    }
    private void initView(){
        change_img = findViewById(R.id.edit_img);
        imagev = findViewById(R.id.profile_img);
        first_name = findViewById(R.id.first_n);
        phone = findViewById(R.id.phone_n);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        reg = findViewById(R.id.register);
        id = homeViewModel.getGuestID(edit.this);
        getData();
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        change_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        edit.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );
            }
        });
    }

    private void uploadBitmap(final Bitmap bitmap) {
        id = homeViewModel.getGuestID(edit.this);
        progressDialog = ProgressDialog.show(edit.this,"","Updating",false,false);
        String sql2 = "UPDATE `guest` SET `first_name` = '"+first_name.getText().toString()+"'" +
                ",`email` = '"+email.getText().toString()+"',`phone` = " +
                "'"+phone.getText().toString()+"',`address` = '"+address.getText().toString()+"'," +
                "`image` = '"+id+".jpeg' WHERE id = '"+id+"'";

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, config.UPDATE_GUEST,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                params.put("pic", new DataPart(id + ".jpeg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
    private void update(){
        id = homeViewModel.getGuestID(edit.this);
        try {
            progressDialog = ProgressDialog.show(edit.this,"","Updating",false,false);
            String sql2 = "UPDATE `guest` SET `first_name` = '"+first_name.getText().toString()+"'" +
                    ",`email` = '"+email.getText().toString()+"',`phone` = " +
                    "'"+phone.getText().toString()+"',`address` = '"+address.getText().toString()+"'," +
                    "`image` = '"+id+".jpeg' WHERE id = '"+id+"'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("1")){
                                progressDialog.dismiss();
                                Toast.makeText(edit.this,"Updated",Toast.LENGTH_LONG).show();
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(edit.this,response,Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(edit.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql2);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(edit.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }catch (Exception e){
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

//    private void showFileChooser() {
//        Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        pickImageIntent.setType("image/*");
//        pickImageIntent.putExtra("aspectX", 1);
//        pickImageIntent.putExtra("aspectY", 1);
//        pickImageIntent.putExtra("scale", true);
//        pickImageIntent.putExtra("outputFormat",
//                Bitmap.CompressFormat.JPEG.toString());
//        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
//    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CODE_GALLERY_REQUEST){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, CODE_GALLERY_REQUEST);
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(Intent.createChooser(intent, "Select Image"),CODE_GALLERY_REQUEST);

            }
            return;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null){
            Uri filepath = data.getData();
            try {
//                InputStream inputStream = getContentResolver().openInputStream(filepath);
//                bitmap = BitmapFactory.decodeStream(inputStream);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filepath);
                uploadBitmap(bitmap);
                imagev.setImageBitmap(bitmap);
            }catch(Exception e){
            }

        }

    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    private void getData(){
        try {
            progressDialog = ProgressDialog.show(edit.this,"","Fetching",false,false);
            String sql2 = "SELECT `first_name`,`last_name`,`email`,`phone`,`address`," +
                    "`image` FROM `guest` WHERE id = '"+id+"'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GUEST_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (!response.equals("1")){
                                showJson(response);
                            }
                            else {
                                Toast.makeText(edit.this,"Not found",Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(edit.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql2);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(edit.this);
            requestQueue.add(stringRequest);
        }catch (Exception e){
        }
    }
    private void showJson(String response){
//        String image = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                first_name.setText(collegeData.getString(config.PRO_NAME));
                phone.setText(collegeData.getString(config.PRO_DISCOUNT_PRICE));
                email.setText(collegeData.getString(config.EMAIL));
                address.setText(collegeData.getString(config.PRO_CURRENT_PRICE));
//                image = collegeData.getString(config.PRO_IMAGE);
//                Glide.with(edit.this)
//                        .load(config.GUEST_IMAGE+image)
//                        .fitCenter()
//                        .into(imagev);
            }
        } catch (Exception e) {
        }
    }
}