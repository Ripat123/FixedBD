package com.sbitbd.fixed_bd.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.fixed_bd.Adapter.cat_model;
import com.sbitbd.fixed_bd.Adapter.category_adapter;
import com.sbitbd.fixed_bd.Adapter.item_adapter;
import com.sbitbd.fixed_bd.Adapter.item_model;
import com.sbitbd.fixed_bd.Adapter.mainItem_adapter;
import com.sbitbd.fixed_bd.Adapter.menu_adapter;
import com.sbitbd.fixed_bd.Adapter.menu_model;
import com.sbitbd.fixed_bd.Adapter.pro_model;
import com.sbitbd.fixed_bd.Adapter.product_adapter;
import com.sbitbd.fixed_bd.Adapter.shop_adapter;
import com.sbitbd.fixed_bd.Config.DoConfig;
import com.sbitbd.fixed_bd.ImageSlider.sliderAdapter;
import com.sbitbd.fixed_bd.ImageSlider.sliderModel;
import com.sbitbd.fixed_bd.R;
import com.sbitbd.fixed_bd.ui.category.all_category;
import com.sbitbd.fixed_bd.ui.product_by_cond.product_view;
import com.sbitbd.fixed_bd.ui.search;
import com.google.android.material.card.MaterialCardView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private sliderAdapter sliderAdapter;
    private SliderView sliderView;
    private pro_model pro_model;
    private product_adapter product_adapter, flash_adapter;
    List<sliderModel> modelList = new ArrayList<>();
    RecyclerView cat_rec, pro_rec, shop_rec;
    private MaterialCardView searchcard, flash_card,buy_earn;
    private NestedScrollView scrollView;
    DoConfig config;
    private sliderModel sliderModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private menu_model menu_model;
    private menu_adapter menu_adapter;
    private RecyclerView menu_rec, main_rec, flash_rec,pro_recycler_ex,life_rec;
    private item_model item_model;
    private item_adapter item_adapter;
    private cat_model cat_model;
    private category_adapter category_adapter;
    private mainItem_adapter mainItem_adapter;
    private Button flash_btn, shop_more,ex_morebtn,recent_morebtn,life_morebtn;
    private shop_adapter shop_adapter;
    private TextView sbit;
    private Handler handler = new Handler();
    private Runnable runnable;
    private TextView tv_days, tv_hour, tv_minute, tv_second;
    private View con;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        config = new DoConfig();
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        con = root;

        cat_rec = root.findViewById(R.id.cat_recycler);
        shop_rec = root.findViewById(R.id.shop_rec);
        searchcard = root.findViewById(R.id.searchID);
        scrollView = root.findViewById(R.id.main_scroll);
        main_rec = root.findViewById(R.id.item_rec);
        flash_rec = root.findViewById(R.id.flash_rec);
        flash_card = root.findViewById(R.id.flash_card);
        swipeRefreshLayout = root.findViewById(R.id.h_ref_id);
        flash_btn = root.findViewById(R.id.flash_more_btn);
        shop_more = root.findViewById(R.id.shop_more_btn);
        ex_morebtn = root.findViewById(R.id.ex_morebtn);
        recent_morebtn = root.findViewById(R.id.recent_morebtn);
        life_morebtn = root.findViewById(R.id.life_morebtn);
        sbit = root.findViewById(R.id.sbit);
        pro_recycler_ex = root.findViewById(R.id.pro_recycler_ex);
        life_rec = root.findViewById(R.id.life_rec);
        buy_earn = root.findViewById(R.id.buy_earn);
        cat_rec.setNestedScrollingEnabled(false);
        GridLayoutManager manager = new GridLayoutManager(root.getContext().getApplicationContext(), 1);
        cat_rec.setLayoutManager(manager);
        flash_adapter = new product_adapter(root.getContext().getApplicationContext());
        sliderView = root.findViewById(R.id.imageSlider);
        initSlider(root);
        initMenu(root);
        initflash(root);
        mainItem_adapter = new mainItem_adapter(root.getContext().getApplicationContext());
        GridLayoutManager main_manager = new GridLayoutManager(root.getContext().getApplicationContext(), 3);
        main_rec.setLayoutManager(main_manager);
        initCategory(root, 1,"SELECT id,`image`,category_name AS 'category_name' " +
                "                FROM `product_category` WHERE shop_by = '1' ORDER BY sl ASC");
        main_rec.setAdapter(mainItem_adapter);
        item_adapter = new item_adapter(root.getContext().getApplicationContext());
        initCategory(root, 0,"SELECT id,`banner` AS 'image',item_name AS " +
                "'category_name' FROM `product_item` WHERE shop_by = '1' ORDER BY id ASC");
        cat_rec.setAdapter(item_adapter);
        product_adapter = new product_adapter(root.getContext().getApplicationContext());
        initProduct(root, swipeRefreshLayout, 0);
        pro_rec.setAdapter(product_adapter);
        initShop(root);
        initExclusive(root);
        initLife_style(root);

        tv_days = root.findViewById(R.id.tb1);
        tv_hour = root.findViewById(R.id.tb2);
        tv_minute = root.findViewById(R.id.tb3);
        tv_second = root.findViewById(R.id.tb4);
        sbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.sbit.com.bd"));
                startActivity(browserIntent);
            }
        });
        searchcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(root.getContext().getApplicationContext(), search.class));
            }
        });
        ex_morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(root.getContext().getApplicationContext(), product_view.class);
                intent.putExtra("menu", "exclusive");
                startActivity(intent);
            }
        });
        recent_morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(root.getContext().getApplicationContext(), product_view.class);
                intent.putExtra("menu", "recent");
                startActivity(intent);
            }
        });
        buy_earn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext().getApplicationContext(),product_view.class);
                intent.putExtra("menu","earn");
                startActivity(intent);
            }
        });
        life_morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(root.getContext().getApplicationContext(), product_view.class);
                intent.putExtra("menu", "life");
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.main_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                product_adapter.ClearProduct();
//                sliderAdapter.ClearSlider();
                item_adapter.ClearItem();
                mainItem_adapter.ClearItem();
                initSlider(root);
                flash_card.setVisibility(View.VISIBLE);
                initflash(root);
                initCategory(root, 1,"SELECT id,`image`,category_name AS 'category_name' "  +
                        "               FROM `product_category` WHERE shop_by = '1' ORDER BY sl ASC");
                initCategory(root, 0,"SELECT id,`banner` AS 'image',item_name AS " +
                        "              'category_name' FROM `product_item` WHERE shop_by = '1' ORDER BY id ASC");
                initProduct(root, swipeRefreshLayout, 1);
            }
        });
        flash_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(root.getContext().getApplicationContext(), product_view.class);
                intent.putExtra("menu", "flash");
                startActivity(intent);
            }
        });
        shop_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(root.getContext().getApplicationContext(), all_category.class);
                intent.putExtra("shop", "sh");
                startActivity(intent);
            }
        });
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    System.exit(1);
                }
                return false;
            }
        });

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    private void countDownStart(String start_date,String end_date) {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date event_date = dateFormat.parse(end_date);
                    Date current_date = new Date();
                    if (!current_date.after(event_date)) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;

                        tv_days.setText(String.format("%02d", Days)+"d : ");
                        tv_hour.setText(String.format("%02d", Hours)+"h : ");
                        tv_minute.setText(String.format("%02d", Minutes)+"m : ");
                        tv_second.setText(String.format("%02d", Seconds)+"s");
                    } else {
                        flash_card.setVisibility(View.GONE);
                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onStart() {
        super.onStart();
        initflash(con);
    }

    private void initMenu(View root) {
        menu_rec = root.findViewById(R.id.top_card_rec);
        menu_rec.setNestedScrollingEnabled(false);
        menu_adapter = new menu_adapter(root.getContext().getApplicationContext());
        GridLayoutManager menuGrid = new GridLayoutManager(root.getContext().getApplicationContext(), 4, RecyclerView.VERTICAL, false);
        menu_rec.setLayoutManager(menuGrid);
//        menu_model = new menu_model(R.drawable.full_filled_by_salesman_bd, "SM Shop");
//        menu_adapter.adduser(menu_model);
        menu_model = new menu_model(R.drawable.special_offer, "Offer");
        menu_adapter.adduser(menu_model);
        menu_model = new menu_model(R.drawable.best_sale, "Exclusive buy");
        menu_adapter.adduser(menu_model);
        menu_model = new menu_model(R.drawable.express_service, "Feni Express");
        menu_adapter.adduser(menu_model);
        menu_model = new menu_model(R.drawable.allcategories, "All Category");
        menu_adapter.adduser(menu_model);
        menu_rec.setAdapter(menu_adapter);
    }

    private void initProduct(View root, SwipeRefreshLayout swipeRefreshLayout, int check) {
        pro_rec = root.findViewById(R.id.pro_recycler);
        pro_rec.setNestedScrollingEnabled(false);
        GridLayoutManager manager = new GridLayoutManager(root.getContext().getApplicationContext(), 2);
        pro_rec.setLayoutManager(manager);

        try {
            String sqld = "SELECT `product_productinfo`.`id`,`product_productinfo`.`product_name`,`product_productinfo`.`sale_price`,`product_productinfo`.`discount_price`," +
                    "                    `product_productinfo`.`current_price`,`product_productinfo`.`image` " +
                    "                    FROM `product_productinfo` WHERE `status` = '1' ORDER BY product_productinfo.id DESC LIMIT 10";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.PRO_DATA,
                    response -> {
//                            Toast.makeText(root.getContext().getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        if (check == 1)
                            swipeRefreshLayout.setRefreshing(false);
                        if (!response.equals("1")) {
                            homeViewModel.showProJSON(response, pro_model, product_adapter, root.getContext().getApplicationContext());
                        }
                    }, error -> {
                        if (check == 1)
                            swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(root.getContext().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sqld);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(root.getContext().getApplicationContext());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private void initExclusive(View root) {
        pro_recycler_ex.setNestedScrollingEnabled(false);
        product_adapter product_adapter1 = new product_adapter(root.getContext().getApplicationContext());
        GridLayoutManager manager = new GridLayoutManager(root.getContext().getApplicationContext(), 2);
        pro_recycler_ex.setLayoutManager(manager);

        try {
            String sqld = "SELECT offer_setups.product_id,`product_productinfo`.`id`,`product_productinfo`.`product_name`,`product_productinfo`.`sale_price`,`product_productinfo`.`discount_price`, " +
                    "                   `product_productinfo`.`current_price`,`product_productinfo`.`image` FROM offer_setups INNER JOIN " +
                    "                    product_productinfo ON offer_setups.product_id = product_productinfo.id WHERE offer_setups.`status` = '1' AND " +
                    "                    offer_setups.`type` = '3' LIMIT 0, 6";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.PRO_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("1")) {
                                homeViewModel.showProJSON(response, pro_model, product_adapter1, root.getContext().getApplicationContext());
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(root.getContext().getApplicationContext(), "Internet Error", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sqld);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(root.getContext().getApplicationContext());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
            pro_recycler_ex.setAdapter(product_adapter1);
        } catch (Exception e) {
        }
    }

    private void initLife_style(View root) {
        life_rec.setNestedScrollingEnabled(false);
        product_adapter product_adapter1 = new product_adapter(root.getContext().getApplicationContext());
        GridLayoutManager manager = new GridLayoutManager(root.getContext().getApplicationContext(), 2);
        life_rec.setLayoutManager(manager);

        try {
            String sqld = "SELECT `product_productinfo`.`id`,`product_productinfo`.`product_name`,`product_productinfo`.`sale_price`,`product_productinfo`.`discount_price`," +
                    "`product_productinfo`.`current_price`,`product_productinfo`.`image` " +
                    " FROM `product_productinfo` WHERE product_productinfo.item_id = '4' LIMIT 6";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.PRO_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("1")) {
                                homeViewModel.showProJSON(response, pro_model, product_adapter1, root.getContext().getApplicationContext());
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(root.getContext().getApplicationContext(), "Internet Error", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sqld);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(root.getContext().getApplicationContext());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
            life_rec.setAdapter(product_adapter1);
        } catch (Exception e) {
        }
    }

    private void initflash(View root) {
        flash_adapter.ClearProduct();
        flash_rec.setNestedScrollingEnabled(false);
        GridLayoutManager manager = new GridLayoutManager(root.getContext().getApplicationContext(), 2);
        flash_rec.setLayoutManager(manager);
        try {

            String sqld = "SELECT offer_setups.product_id,`product_productinfo`.`id`,`product_productinfo`.`product_name`,`product_productinfo`.`sale_price`,`product_productinfo`.`discount_price`," +
                    "                    `product_productinfo`.`current_price`,`product_productinfo`.`image` FROM offer_setups INNER JOIN " +
                    "                    product_productinfo ON offer_setups.product_id = product_productinfo.id WHERE offer_setups.`status` = '1' AND " +
                    "                    offer_setups.`type` = '1' AND offer_setups.`start_date_time` <= NOW() " +
                    "                    AND offer_setups.`end_date_time` >= NOW() LIMIT 0, 8";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.PRO_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("1")) {
                                getDate(root);
                                homeViewModel.showProJSON(response, pro_model, flash_adapter, root.getContext().getApplicationContext());
                            } else
                                flash_card.setVisibility(View.GONE);

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(root.getContext().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    flash_card.setVisibility(View.GONE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sqld);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(root.getContext().getApplicationContext());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
            flash_rec.setAdapter(flash_adapter);
        } catch (Exception e) {
        }
    }

    private void initCategory(View root, int check,String sql) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.CAT_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("1")) {
                                if (check == 0)
                                    homeViewModel.showitemJSON(response, cat_model, item_adapter);
                                else
                                    homeViewModel.showMain_itemJSON(response, cat_model, mainItem_adapter);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(root.getContext().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
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
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private void initShop(View root) {
        shop_adapter = new shop_adapter(root.getContext().getApplicationContext());
        GridLayoutManager manager = new GridLayoutManager(root.getContext().getApplicationContext(), 1, RecyclerView.HORIZONTAL, false);
        shop_rec.setLayoutManager(manager);
        try {
            String sql = "SELECT `sellers`.`id`,`sellers`.`business_name` AS 'category_name',`sellers`.`image` FROM `sellers` LIMIT 10";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.CAT_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            homeViewModel.showShopJSON(response, cat_model, shop_adapter);

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(root.getContext().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
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
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
            shop_rec.setAdapter(shop_adapter);
        } catch (Exception e) {
        }
    }

    private void initSlider(View root) {

        try {
            String sql = "SELECT image FROM `sliders`";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.SLIDER_IMG,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            modelList = homeViewModel.showSliderJSON(response);
                            sliderAdapter = new sliderAdapter(root.getContext().getApplicationContext(), modelList);
                            sliderView.setSliderAdapter(sliderAdapter);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(root.getContext().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
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
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINDEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
    }

    private void getDate(View root) {
        try {
            String sql = "SELECT end_date_time AS 'id',NOW() AS 'discout_price' FROM offer_setups  WHERE offer_setups.`status` = '1' AND " +
                    "                                        offer_setups.`type` = '1' AND offer_setups.`start_date_time` <= NOW() " +
                    "                                        AND offer_setups.`end_date_time` >= NOW() ORDER BY end_date_time DESC";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.COUPON,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            showtimeList(response);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(root.getContext().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
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
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
            shop_rec.setAdapter(shop_adapter);
        } catch (Exception e) {
        }
    }

    private void showtimeList(String response) {
        String start_date = "", end_date = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            JSONObject collegeData = result.getJSONObject(0);
            start_date = collegeData.getString(config.PRO_DISCOUNT_PRICE);
            end_date = collegeData.getString(config.CAT_ID);
            countDownStart(start_date,end_date);
        } catch (Exception e) {

        }
    }

}