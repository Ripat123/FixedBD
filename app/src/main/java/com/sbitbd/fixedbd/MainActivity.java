package com.sbitbd.fixedbd;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sbitbd.fixedbd.Config.converter;
import com.sbitbd.fixedbd.Config.service;
import com.sbitbd.fixedbd.Config.sqliteDB;
import com.sbitbd.fixedbd.ui.home.HomeViewModel;
import com.sbitbd.fixedbd.ui.login_page.ui.login.LoginActivity;
import com.sbitbd.fixedbd.ui.seller_form.seller_nav;
import com.sbitbd.fixedbd.ui.seller_login.seller_login;
import com.sbitbd.fixedbd.ui.sh_cart.sh_cart;
import com.sbitbd.fixedbd.ui.wish_list.wish;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private HomeViewModel homeViewModel;
    private boolean check = false;
    private MenuItem sign,seller_login;
    private FloatingActionButton fab;
    Toolbar toolbar;
    private static int cart_count=0;
    private DrawerLayout drawer;
    private static final String TAG = "JobService";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
        toolbar.setLogo(R.drawable.finallogo);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_product,R.id.nav_developer,R.id.nav_terms_cond)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        homeViewModel = new HomeViewModel();
        homeViewModel.resetSession(MainActivity.this);

        sign = navigationView.getMenu().findItem(R.id.nav_sign);
        seller_login = navigationView.getMenu().findItem(R.id.nav_slogin);
        user_check();
        seller_login.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                homeViewModel = new HomeViewModel();
                check = homeViewModel.checkSeller(MainActivity.this);
                if (check)
                    startActivity(new Intent(MainActivity.this, seller_nav.class));
                else
                    startActivity(new Intent(MainActivity.this, seller_login.class));
//                scheduleJob();
                return true;
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (drawer.isOpen())
            drawer.close();
        else
            super.onBackPressed();
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart);
//        cart_count = getcart();
        menuItem.setIcon(converter.convertLayoutToImage(MainActivity.this,cart_count,R.drawable.carts));

        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, sh_cart.class));
//                cancelScheduled();
                return true;
            }
        });
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, wish.class));
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        user_check();

    }

    public void regularSnak(String msg,View v){
        Snackbar snackbar = Snackbar.make(v,msg, Snackbar.LENGTH_LONG);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        snackbar.show();
    }
    public void regularcondSnak(String msg){
        Snackbar snackbar = Snackbar.make(fab,msg, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
    private void user_check(){
        try {
            homeViewModel = new HomeViewModel();
            check = homeViewModel.checkUser(MainActivity.this);
            if (check){
                sign.setTitle(R.string.sign_out);
                sign.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(MainActivity.this);
                        dialogBuilder.setTitle("Confirmation");
                        dialogBuilder.setMessage("Are you sure you want to Log out?");
                        dialogBuilder.setCancelable(false);
                        dialogBuilder.setNegativeButton("NO",(dialog, which) -> {
                            dialog.dismiss();
                        });
                        dialogBuilder.setPositiveButton("Yes",(dialog, which) -> {

                            homeViewModel.userDelete(MainActivity.this);
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();

                        });
                        dialogBuilder.show();

                        return true;
                    }
                });
            }else {

                sign.setOnMenuItemClickListener(item -> {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    return true;
                });
            }
        }catch (Exception e){
        }
    }

    private int getcart(){
        int id = 0;
        sqliteDB sqliteDB = new sqliteDB(this);
        String session = homeViewModel.getSession(this);
        try {
            Cursor cursor = sqliteDB.getUerData("SELECT COUNT(id) AS 'id' FROM shopping_carts WHERE session_id = '" + session + "'");
            if(cursor.getCount() > 0){
                if(cursor.moveToNext()){
                    id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("id")));
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
        return id;
    }

    private void scheduleJob(){
        try {
            ComponentName componentName = new ComponentName(this, service.class);
            JobInfo jobInfo = new JobInfo.Builder(123,componentName)
                    .setRequiresCharging(false)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .setPersisted(true)
                    .setPeriodic(15 * 60 * 1000)
                    .build();
            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            int result_code = jobScheduler.schedule(jobInfo);
            if (result_code == JobScheduler.RESULT_SUCCESS)
                Log.d(TAG,"scheduled");
            else
                Log.d(TAG,"scheduling failed");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void cancelScheduled(){
        try {
            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            jobScheduler.cancel(123);
            Log.d(TAG,"job cancelled");
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}