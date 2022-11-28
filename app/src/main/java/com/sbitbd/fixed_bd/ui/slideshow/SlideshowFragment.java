package com.sbitbd.fixed_bd.ui.slideshow;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.fixed_bd.Config.DoConfig;
import com.sbitbd.fixed_bd.Config.sqliteDB;
import com.sbitbd.fixed_bd.R;
import com.sbitbd.fixed_bd.ui.edit_profile.edit;
import com.sbitbd.fixed_bd.ui.login_page.ui.login.LoginActivity;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private TextView edit_personal,ch_pass,address,profile_name;
    private ImageView edit_p_img,ch_pass_img,add_img,profile_img;
    private DoConfig config = new DoConfig();
    private ConstraintLayout signIn,singOut;
    private Button signbtn;
    private MaterialCardView image_card;
    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        initView(root);
        getData(root);
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }
    private void initView(View root){
        edit_personal = root.findViewById(R.id.textView3);
        edit_p_img = root.findViewById(R.id.edit_txt);
        ch_pass_img = root.findViewById(R.id.ch_pass_img);
        ch_pass = root.findViewById(R.id.textView4);
        address = root.findViewById(R.id.textView5);
        add_img = root.findViewById(R.id.add_img);
        profile_name = root.findViewById(R.id.profile_name);
        signIn = root.findViewById(R.id.signin_id);
        singOut = root.findViewById(R.id.sign_out_id);
        signbtn = root.findViewById(R.id.sing_in_btn);
        profile_img = root.findViewById(R.id.profile_img_id);
        image_card = root.findViewById(R.id.profile_img);
        swipeRefreshLayout = root.findViewById(R.id.dash_ref_id);
        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(root.getContext().getApplicationContext(),LoginActivity.class));
            }
        });
        add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ch_pass_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ch_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        edit_p_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(root.getContext().getApplicationContext(), edit.class));
            }
        });
        edit_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(root.getContext().getApplicationContext(), edit.class));
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(root);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    private void getData(View root){
        sqliteDB sqliteDB = new sqliteDB(root.getContext().getApplicationContext());
        try {
            String email = "",id = "";
            Cursor cursor = sqliteDB.getUerData("SELECT * FROM guest");
            if(cursor.getCount() > 0){
                if(cursor.moveToNext()){
                    email = cursor.getString(cursor.getColumnIndex("email"));
//                    phone = cursor.getString(cursor.getColumnIndex("phone"));
//                    pass = cursor.getString(cursor.getColumnIndex("password"));
                    id = cursor.getString(cursor.getColumnIndex("guest_id"));
                }
                singOut.setVisibility(View.GONE);
                String sql = "SELECT first_name AS 'category_name',last_name AS 'id',image AS 'image' FROM `guest` WHERE `email` = '"+email+"' OR id = '"+id+"'";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, config.CAT_DATA,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (!response.equals("1")){
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONArray result = jsonObject.getJSONArray(config.RESULT);
                                        JSONObject collegeData = result.getJSONObject(0);
                                        profile_name.setText(collegeData.getString(config.CAT_NAME)+" "+collegeData.getString(config.CAT_ID));
                                        String image = collegeData.getString(config.CAT_IMG);
//                                        Glide.with(root).load(config.GUEST_IMAGE+image).
//                                                error(R.drawable.profile)
//                                                .placeholder(R.drawable.profile)
//                                                .fitCenter()
//                                                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                                                .skipMemoryCache(true).into(profile_img);
                                        Picasso.get().load(config.GUEST_IMAGE+image)
                                                .fit().centerInside()
                                                .placeholder(R.drawable.profile)
                                                .error(R.drawable.profile)
                                                .into(profile_img);
                                    }catch (Exception e){

                                    }
//                                    String str[] = response.split("<");
//                                    String image = (str[1]);
//                                    String deletePart = response.substring(response.indexOf("<"));
//                                    String name = response.replace(deletePart,"");
//                                    profile_name.setText(name);
//                                    Glide.with(root)
//                                            .load(config.GUEST_IMAGE+image)
//                                            .fitCenter()
//                                            .into(profile_img);
//                                    Toast.makeText(root.getContext().getApplicationContext(),response,Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(root.getContext().getApplicationContext(),"not found",Toast.LENGTH_LONG).show();

                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(root.getContext().getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(config.QUERY, sql);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(root.getContext().getApplicationContext());
                requestQueue.add(stringRequest);
            }else {
                signIn.setVisibility(View.GONE);
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
}