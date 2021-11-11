package com.sbitbd.fixedbd.ui.seller_form.ui.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.sbitbd.fixedbd.Config.DoConfig;
import com.sbitbd.fixedbd.MainActivity;
import com.sbitbd.fixedbd.R;
import com.sbitbd.fixedbd.databinding.FragmentDashboardBinding;
import com.sbitbd.fixedbd.ui.checkout.checkout;
import com.sbitbd.fixedbd.ui.home.HomeViewModel;
import com.sbitbd.fixedbd.ui.seller_form.ui.pro_view.product_view1;
import com.sbitbd.fixedbd.ui.seller_form.ui.product_add.product_form;
import com.sbitbd.fixedbd.ui.seller_login.seller_login;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private MaterialCardView add_card, back_card, all_pro, active_pro, inactive_pro, log_card, withdraw1,
            withdraw2, withdraw3, withdraw4, withdraw5, Withdraw6;
    private MaterialTextView seller_name, all_count, active_count, inacitve_count, seller_name2, seller_name1;
    private ImageView seller_pro_img;
    private List<String> stringList = new ArrayList<>();
    private DoConfig config = new DoConfig();
    private HomeViewModel homeViewModel = new HomeViewModel();
    private ProgressDialog progressDialog;
    private String seller_id;
    private View root1;
    private int datecheck = 0;
    private double accounts, amount, withdraw, sale_bal, commission_with, deposit_bal_with;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        root1 = binding.getRoot();
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        dashboardViewModel.getcontext().setValue(root1.getContext().getApplicationContext());
        dashboardViewModel.getBinding().setValue(binding);
        initview(root1);
        return root1;
    }

    private void initview(View root) {
        add_card = root.findViewById(R.id.add_p);
        back_card = root.findViewById(R.id.back_card);
        all_pro = root.findViewById(R.id.all_pro);
        inactive_pro = root.findViewById(R.id.inactive_pro);
        active_pro = root.findViewById(R.id.active_pro);
        seller_name = root.findViewById(R.id.seller_name);
        all_count = root.findViewById(R.id.all_count);
        active_count = root.findViewById(R.id.active_count);
        inacitve_count = root.findViewById(R.id.inacitve_count);
        seller_name2 = root.findViewById(R.id.seller_name2);
        seller_name1 = root.findViewById(R.id.seller_name1);
        seller_pro_img = root.findViewById(R.id.seller_pro_img);
        log_card = root.findViewById(R.id.log_card);
        withdraw2 = root.findViewById(R.id.withdraw2);
        withdraw1 = root.findViewById(R.id.withdraw1);
        withdraw3 = root.findViewById(R.id.withdraw3);
        withdraw4 = root.findViewById(R.id.withdraw4);
        withdraw5 = root.findViewById(R.id.withdraw5);
        Withdraw6 = binding.withdraw6;

        seller_id = homeViewModel.getSellerID(root.getContext().getApplicationContext());


        log_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeViewModel.sellerDelete(root.getContext().getApplicationContext());
                startActivity(new Intent(root.getContext().getApplicationContext(), seller_login.class));
                getActivity().finish();
            }
        });
        add_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(root.getContext().getApplicationContext(), product_form.class));
            }
        });
        back_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(root.getContext().getApplicationContext(), MainActivity.class));
                getActivity().finish();
            }
        });
        all_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(root.getContext().getApplicationContext(), product_view1.class);
                intent.putExtra("query", "SELECT `product_productinfo`.`id`,`product_productinfo`.`product_name`,`product_productinfo`.`image` " +
                        "                    FROM `product_productinfo` WHERE `seller_id` = '" + seller_id + "' LIMIT ");
                intent.putExtra("tag", "All Products");
                startActivity(intent);
            }
        });
        active_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(root.getContext().getApplicationContext(), product_view1.class);
                intent.putExtra("query", "SELECT `product_productinfo`.`id`,`product_productinfo`.`product_name`,`product_productinfo`.`image` " +
                        "                    FROM `product_productinfo` WHERE `seller_id` = '" + seller_id + "' AND `status` = '1' LIMIT ");
                intent.putExtra("tag", "Active Products");
                startActivity(intent);
            }
        });
        inactive_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(root.getContext().getApplicationContext(), product_view1.class);
                intent.putExtra("query", "SELECT `product_productinfo`.`id`,`product_productinfo`.`product_name`,`product_productinfo`.`image` " +
                        "                    FROM `product_productinfo` WHERE `seller_id` = '" + seller_id + "' AND `status` = '0' LIMIT ");
                intent.putExtra("tag", "Inactive Products");
                startActivity(intent);
            }
        });
        withdraw1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });
        withdraw2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fundWithdrawDialog();
            }
        });
        withdraw3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WithdrawSale_balDialog();
            }
        });
        withdraw4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Withdrawcom_balDialog();
            }
        });
        withdraw5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Withdrawdep_balDialog();
            }
        });
        Withdraw6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification_set();
            }
        });

    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(root1.getContext(), R.style.CustomBottomSheetDialog);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.setContentView(R.layout.add_found);
        Button submit = bottomSheetDialog.findViewById(R.id.add_fnd_btn);
        AutoCompleteTextView type = bottomSheetDialog.findViewById(R.id.type);
        EditText amount = bottomSheetDialog.findViewById(R.id.amount);
        EditText account = bottomSheetDialog.findViewById(R.id.account);
        EditText tran = bottomSheetDialog.findViewById(R.id.tran);
        EditText det = bottomSheetDialog.findViewById(R.id.det);
        stringList.clear();
        stringList.add("Bkash");
        stringList.add("Rocket");
        stringList.add("Nagad");
        stringList.add("Bank");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(root1.getContext().getApplicationContext(),
                R.layout.item_name, stringList);
        type.setAdapter(dataAdapter);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.getText().toString().equals("")) {
                    type.setError("Empty Type");
                    Toast.makeText(root1.getContext().getApplicationContext(), "Empty Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (account.getText().toString().equals("")) {
                    account.setError("Empty Account");
                    Toast.makeText(root1.getContext().getApplicationContext(), "Empty Account", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tran.getText().toString().equals("")) {
                    tran.setError("Empty Transaction ID");
                    Toast.makeText(root1.getContext().getApplicationContext(), "Empty Transaction ID", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (amount.getText().toString().equals("")) {
                    amount.setError("Empty Amount");
                    Toast.makeText(root1.getContext().getApplicationContext(), "Empty Amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = ProgressDialog.show(root1.getContext(), "", "Loading...", false, false);
                String sql = "INSERT INTO `addfunds`(`date`, `member_id`, `customer_id`, `type`, " +
                        "`amount`, `withdraw`, `number`, `transc_id`, `details`, `status`) VALUES(" +
                        "'" + config.getCreateDate() + "','" + homeViewModel.getSellerID(root1.getContext()
                        .getApplicationContext()) + "','" + homeViewModel.getGuestID(root1.getContext()
                        .getApplicationContext()) + "','" + type.getText().toString() + "','" + amount.getText()
                        .toString().trim() + "','0.00','" + account.getText().toString().trim() + "','" + tran
                        .getText().toString().trim() + "','" + det.getText().toString().trim() + "','0')";
                dashboardViewModel.getData_insert(sql).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        progressDialog.dismiss();
                        if (aBoolean) {
                            bottomSheetDialog.dismiss();
                            Toast.makeText(root1.getContext().getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(root1.getContext().getApplicationContext(), "Unsuccessful!", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        bottomSheetDialog.show();
    }

    private void fundWithdrawDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(root1.getContext(), R.style.CustomBottomSheetDialog);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.setContentView(R.layout.withdraw_fund);
        Button submit = bottomSheetDialog.findViewById(R.id.add_fnd_btn);
        AutoCompleteTextView type = bottomSheetDialog.findViewById(R.id.type);
        EditText amount = bottomSheetDialog.findViewById(R.id.amount);
        EditText account = bottomSheetDialog.findViewById(R.id.account);
        EditText det = bottomSheetDialog.findViewById(R.id.det);
        stringList.clear();
        stringList.add("Bkash");
        stringList.add("Rocket");
        stringList.add("Nagad");
        stringList.add("Bank");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(root1.getContext().getApplicationContext(),
                R.layout.item_name, stringList);
        type.setAdapter(dataAdapter);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.getText().toString().equals("")) {
                    type.setError("Empty Type");
                    Toast.makeText(root1.getContext().getApplicationContext(), "Empty Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (account.getText().toString().equals("")) {
                    account.setError("Empty Account");
                    Toast.makeText(root1.getContext().getApplicationContext(), "Empty Account", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!amount.getText().toString().trim().equals("")) {
                    if (Double.parseDouble(amount.getText().toString().trim()) > accounts) {
                        amount.setError("Your balance:" + accounts);
                        Toast.makeText(root1.getContext().getApplicationContext(), "Your balance:" + accounts, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else
                    return;
                progressDialog = ProgressDialog.show(root1.getContext(), "", "Loading...", false, false);
                String sql = "INSERT INTO `addfunds`(`date`, `member_id`, `customer_id`, `type`, " +
                        "`amount`, `withdraw`, `number`, `details`, `status`) VALUES(" +
                        "'" + config.getCreateDate() + "','" + homeViewModel.getSellerID(root1.getContext()
                        .getApplicationContext()) + "','" + homeViewModel.getGuestID(root1.getContext()
                        .getApplicationContext()) + "','" + type.getText().toString() + "','0.00" +
                        "','" + amount.getText().toString().trim() + "','" + account.getText().toString()
                        .trim() + "','" + det.getText().toString().trim() + "','0')";
                dashboardViewModel.getData_insert(sql).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        progressDialog.dismiss();
                        if (aBoolean) {
                            bottomSheetDialog.dismiss();
                            Toast.makeText(root1.getContext().getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(root1.getContext().getApplicationContext(), "Unsuccessful!", Toast.LENGTH_SHORT).show();
                        getCharge("select sum(amount) as 'id' from addfunds WHERE member_id " +
                                "= '" + homeViewModel.getSellerID(root1.getContext().getApplicationContext()) + "' " +
                                "and status = '1'", 0);
                        getCharge("select sum(withdraw) as 'id' from addfunds WHERE member_id " +
                                "= '" + homeViewModel.getSellerID(root1.getContext().getApplicationContext()) + "'", 1);
                    }
                });
            }
        });

        bottomSheetDialog.show();
    }

    private void WithdrawSale_balDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(root1.getContext(), R.style.CustomBottomSheetDialog);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.setContentView(R.layout.w_salebalance);
        Button submit = bottomSheetDialog.findViewById(R.id.add_fnd_btn);
        AutoCompleteTextView type = bottomSheetDialog.findViewById(R.id.type);
        EditText amount = bottomSheetDialog.findViewById(R.id.amount);
        EditText account = bottomSheetDialog.findViewById(R.id.account);
        EditText det = bottomSheetDialog.findViewById(R.id.det);
        stringList.clear();
        stringList.add("Bkash");
        stringList.add("Rocket");
        stringList.add("Nagad");
        stringList.add("Bank");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(root1.getContext().getApplicationContext(),
                R.layout.item_name, stringList);
        type.setAdapter(dataAdapter);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.getText().toString().equals("")) {
                    type.setError("Empty Type");
                    Toast.makeText(root1.getContext().getApplicationContext(), "Empty Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (account.getText().toString().equals("")) {
                    account.setError("Empty Account");
                    Toast.makeText(root1.getContext().getApplicationContext(), "Empty Account", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!amount.getText().toString().trim().equals("")) {
                    if (Double.parseDouble(amount.getText().toString().trim()) > sale_bal) {
                        amount.setError("Your balance:" + sale_bal);
                        Toast.makeText(root1.getContext().getApplicationContext(), "Your balance:" + sale_bal, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else
                    return;
                progressDialog = ProgressDialog.show(root1.getContext(), "", "Loading...", false, false);
                String sql = "INSERT INTO `member_product_balance`(`date`, `member_id`, `type`, " +
                        "`amount`, `withdraw`, `number`, `details`, `status`) VALUES(" +
                        "'" + config.getCreateDate() + "','" + homeViewModel.getSellerID(root1.getContext()
                        .getApplicationContext()) + "','" + type.getText().toString() + "','0.00" +
                        "','" + amount.getText().toString().trim() + "','" + account.getText().toString()
                        .trim() + "','" + det.getText().toString().trim() + "','0')";
                dashboardViewModel.getData_insert(sql).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        progressDialog.dismiss();
                        if (aBoolean) {
                            bottomSheetDialog.dismiss();
                            Toast.makeText(root1.getContext().getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(root1.getContext().getApplicationContext(), "Unsuccessful!", Toast.LENGTH_SHORT).show();
                        getCharge("select sum(amount - withdraw) as 'id' from member_product_balance where member_id " +
                                "= '" + homeViewModel.getSellerID(root1.getContext().getApplicationContext()) + "'", 2);
                    }
                });
            }
        });
        bottomSheetDialog.show();
    }

    private void Withdrawcom_balDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(root1.getContext(), R.style.CustomBottomSheetDialog);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.setContentView(R.layout.w_salebalance);
        TextView header = bottomSheetDialog.findViewById(R.id.title);
        header.setText("কমিশন ব্যালেন্স উত্তোলন");
        Button submit = bottomSheetDialog.findViewById(R.id.add_fnd_btn);
        AutoCompleteTextView type = bottomSheetDialog.findViewById(R.id.type);
        EditText amount = bottomSheetDialog.findViewById(R.id.amount);
        EditText account = bottomSheetDialog.findViewById(R.id.account);
        EditText det = bottomSheetDialog.findViewById(R.id.det);
        stringList.clear();
        stringList.add("Bkash");
        stringList.add("Rocket");
        stringList.add("Nagad");
        stringList.add("Bank");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(root1.getContext().getApplicationContext(),
                R.layout.item_name, stringList);
        type.setAdapter(dataAdapter);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.getText().toString().equals("")) {
                    type.setError("Empty Type");
                    Toast.makeText(root1.getContext().getApplicationContext(), "Empty Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (account.getText().toString().equals("")) {
                    account.setError("Empty Account");
                    Toast.makeText(root1.getContext().getApplicationContext(), "Empty Account", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!amount.getText().toString().trim().equals("")) {
                    if (Double.parseDouble(amount.getText().toString().trim()) > commission_with) {
                        amount.setError("Your balance:" + commission_with);
                        Toast.makeText(root1.getContext().getApplicationContext(), "Your balance:" + commission_with, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else
                    return;
                progressDialog = ProgressDialog.show(root1.getContext(), "", "Loading...", false, false);
                String sql = "INSERT INTO `order_customer_commision`(`date`, `member_id`, `type`, " +
                        "`commision_balance`, `withdraw`,invoice_id, `number`, `details`, `status`) VALUES(" +
                        "'" + config.getCreateDate() + "','" + homeViewModel.getSellerID(root1.getContext()
                        .getApplicationContext()) + "','" + type.getText().toString() + "','0.00" +
                        "','" + amount.getText().toString().trim() + "','','" + account.getText().toString()
                        .trim() + "','" + det.getText().toString().trim() + "','0')";
                dashboardViewModel.getData_insert(sql).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        progressDialog.dismiss();
                        if (aBoolean) {
                            bottomSheetDialog.dismiss();
                            Toast.makeText(root1.getContext().getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(root1.getContext().getApplicationContext(), "Unsuccessful!", Toast.LENGTH_SHORT).show();
                        getCharge("select sum(commision_balance - withdraw) as 'id' from order_customer_commision where member_id " +
                                "= '" + homeViewModel.getSellerID(root1.getContext().getApplicationContext()) + "'", 3);
                    }
                });
            }
        });

        bottomSheetDialog.show();
    }

    private void Withdrawdep_balDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(root1.getContext(), R.style.CustomBottomSheetDialog);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.setContentView(R.layout.w_salebalance);
        TextView header = bottomSheetDialog.findViewById(R.id.title);
        header.setText("ডিপোজিট ব্যালেন্স উত্তোলন");
        Button submit = bottomSheetDialog.findViewById(R.id.add_fnd_btn);
        AutoCompleteTextView type = bottomSheetDialog.findViewById(R.id.type);
        EditText amount = bottomSheetDialog.findViewById(R.id.amount);
        EditText account = bottomSheetDialog.findViewById(R.id.account);
        EditText det = bottomSheetDialog.findViewById(R.id.det);
        stringList.clear();
        stringList.add("Bkash");
        stringList.add("Rocket");
        stringList.add("Nagad");
        stringList.add("Bank");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(root1.getContext().getApplicationContext(),
                R.layout.item_name, stringList);
        type.setAdapter(dataAdapter);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.getText().toString().equals("")) {
                    type.setError("Empty Type");
                    Toast.makeText(root1.getContext().getApplicationContext(), "Empty Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (account.getText().toString().equals("")) {
                    account.setError("Empty Account");
                    Toast.makeText(root1.getContext().getApplicationContext(), "Empty Account", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!amount.getText().toString().trim().equals("")) {
                    if (Double.parseDouble(amount.getText().toString().trim()) > deposit_bal_with) {
                        amount.setError("Your balance:" + deposit_bal_with);
                        Toast.makeText(root1.getContext().getApplicationContext(), "Your balance:" + deposit_bal_with, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else
                    return;
                progressDialog = ProgressDialog.show(root1.getContext(), "", "Loading...", false, false);
                String sql = "INSERT INTO `Deposit_amount`(`date`, `member_id`, `type`, " +
                        "`deposit_amount`, `withdraw`, `number`, `details`, `status`) VALUES(" +
                        "'" + config.getCreateDate() + "','" + homeViewModel.getSellerID(root1.getContext()
                        .getApplicationContext()) + "','" + type.getText().toString() + "','0.00" +
                        "','" + amount.getText().toString().trim() + "','" + account.getText().toString()
                        .trim() + "','" + det.getText().toString().trim() + "','0')";
                dashboardViewModel.getData_insert(sql).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        progressDialog.dismiss();
                        if (aBoolean) {
                            bottomSheetDialog.dismiss();
                            Toast.makeText(root1.getContext().getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(root1.getContext().getApplicationContext(), "Unsuccessful!", Toast.LENGTH_SHORT).show();
                        getCharge("select sum(deposit_amount - withdraw) as 'id' from Deposit_amount where member_id " +
                                "= '" + homeViewModel.getSellerID(root1.getContext().getApplicationContext()) + "'", 4);
                    }
                });
            }
        });

        bottomSheetDialog.show();
    }

    private void seller_name_img(View root) {
        try {
            String sql = "SELECT business_name AS 'id',image FROM sellers WHERE id = '" + seller_id + "' AND status = '1'";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.CAT_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("1") && !response.equals(""))
                                showData(response);
                            else {
                                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(root.getContext());
                                dialogBuilder.setTitle("Warning");
                                dialogBuilder.setIcon(R.drawable.icons8_error_48px);
                                dialogBuilder.setMessage("Your Account has been deactivated!");
                                dialogBuilder.setCancelable(false);
                                dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                                    startActivity(new Intent(root.getContext().getApplicationContext(), seller_login.class));
                                    getActivity().finish();

                                });
                                dialogBuilder.show();
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

    private void seller_pro_count(View root) {
        try {
            String sql = "SELECT " +
                    "IF(`status` > 0,COUNT(*),0) AS 'id'," +
                    "IF(`status` < 1,COUNT(*),0) AS 'image'" +
                    " FROM product_productinfo WHERE seller_id = '" + seller_id + "' GROUP BY `status`";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.CAT_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("1") && !response.equals(""))
                                showcount(response, active_count, inacitve_count, all_count);
                            else {
                                active_count.setText("0");
                                inacitve_count.setText("0");
                                all_count.setText("0");
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

    private void showData(String response) {
        String name = "", image = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            final int len = result.length();
            for (int i = 0; i < len; i++) {
                JSONObject collegeData = result.getJSONObject(i);
                name = collegeData.getString(config.CAT_ID);
                image = collegeData.getString(config.CAT_IMG);
                seller_name.setText(name);
                seller_name1.setText(name);
                seller_name2.setText(name);
                Picasso.get().load(config.SELLER_IMG_URL + image).transform(new RoundedCornersTransformation(100, 0))
                        .fit().centerCrop()
                        .placeholder(R.drawable.user)
                        .error(R.drawable.user)
                        .into(seller_pro_img);
            }
        } catch (Exception e) {
        }
    }

    private void showcount(String response, MaterialTextView active_count, MaterialTextView inacitve_count, MaterialTextView all_count) {
        int active = 0, inactive = 0;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            final int len = result.length();
            for (int i = 0; i < len; i++) {
                JSONObject collegeData = result.getJSONObject(i);
                active = active + Integer.parseInt(collegeData.getString(config.CAT_ID));
                inactive = inactive + Integer.parseInt(collegeData.getString(config.CAT_IMG));
            }
            active_count.setText("" + active);
            inacitve_count.setText("" + inactive);
            all_count.setText(String.valueOf(active + inactive));
        } catch (Exception e) {
        }
    }

    private void getCharge(String query, int check) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.trim().equals("")) {
                                if (check == 0)
                                    amount = Double.parseDouble(response.trim());
                                else if (check == 1) {
                                    withdraw = Double.parseDouble(response.trim());
                                    accounts = amount - withdraw;
                                } else if (check == 2) {
                                    sale_bal = Double.parseDouble(response.trim());
                                } else if (check == 3) {
                                    commission_with = Double.parseDouble(response.trim());
                                } else if (check == 4) {
                                    deposit_bal_with = Double.parseDouble(response.trim());
                                }
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(root1.getContext().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, query);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(root1.getContext().getApplicationContext());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private void notification_set() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(root1.getContext(), R.style.CustomBottomSheetDialog);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.setContentView(R.layout.notification_lay);
        Button submit = bottomSheetDialog.findViewById(R.id.add_fnd_btn);
        EditText title = bottomSheetDialog.findViewById(R.id.amount);
        EditText description = bottomSheetDialog.findViewById(R.id.account);
        MaterialCardView start_card = bottomSheetDialog.findViewById(R.id.start_date);
        MaterialCardView end_card = bottomSheetDialog.findViewById(R.id.end_date1);
        TextView start_date = bottomSheetDialog.findViewById(R.id.date_t);
        TextView end_date = bottomSheetDialog.findViewById(R.id.date_t1);


        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        materialDateBuilder.setTheme(R.style.RoundShapeCalenderTheme);

        final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();

        start_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getChildFragmentManager(), "MATERIAL_DATE_PICKER");
                datecheck = 1;
            }
        });
        end_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getChildFragmentManager(), "MATERIAL_DATE_PICKER");
                datecheck = 2;
            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onPositiveButtonClick(Long selection) {
                TimeZone timeZoneUTC = TimeZone.getDefault();
                String date;
                // It will be negative, so that's the -1
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date date1 = new Date(selection + offsetFromUTC);
                date = simpleFormat.format(date1);
                if (datecheck == 1)
                    start_date.setText(date);
                else
                    end_date.setText(date);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().toString().trim().equals("")) {
                    Toast.makeText(root1.getContext(), "Empty Title!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (title.getText().toString().length() > 50) {
                    Toast.makeText(root1.getContext(), "Too long title!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (description.getText().toString().trim().equals("")) {
                    Toast.makeText(root1.getContext(), "Empty Description!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (start_date.getText().toString().trim().equals("Start Date")) {
                    Toast.makeText(root1.getContext(), "Select a date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (end_date.getText().toString().trim().equals("End Date")) {
                    Toast.makeText(root1.getContext(), "Select a date", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = ProgressDialog.show(root1.getContext(), "", "Loading...", false, false);
                String sql = "INSERT INTO `notification`(`title`, `description`, `start_date`,`end_date`) VALUES(" +
                        "'" + title.getText().toString().trim() + "','" + description.getText().toString().trim() + "'" +
                        ",'" + start_date.getText().toString() + "','" + end_date.getText().toString().trim() + "')";
                dashboardViewModel.getData_insert(sql).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        progressDialog.dismiss();
                        if (aBoolean) {
                            bottomSheetDialog.dismiss();
                            Toast.makeText(root1.getContext().getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(root1.getContext().getApplicationContext(), "Unsuccessful!", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        bottomSheetDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        seller_pro_count(root1);
        seller_name_img(root1);
        getCharge("select sum(amount) as 'id' from addfunds WHERE member_id " +
                "= '" + homeViewModel.getSellerID(root1.getContext().getApplicationContext()) + "' " +
                "and status = '1'", 0);
        getCharge("select sum(withdraw) as 'id' from addfunds WHERE member_id " +
                "= '" + homeViewModel.getSellerID(root1.getContext().getApplicationContext()) + "'", 1);
        getCharge("select sum(amount - withdraw) as 'id' from member_product_balance where member_id " +
                "= '" + homeViewModel.getSellerID(root1.getContext().getApplicationContext()) + "'", 2);
        getCharge("select sum(commision_balance - withdraw) as 'id' from order_customer_commision where member_id " +
                "= '" + homeViewModel.getSellerID(root1.getContext().getApplicationContext()) + "'", 3);
        getCharge("select sum(deposit_amount - withdraw) as 'id' from Deposit_amount where member_id " +
                "= '" + homeViewModel.getSellerID(root1.getContext().getApplicationContext()) + "'", 4);
    }
}