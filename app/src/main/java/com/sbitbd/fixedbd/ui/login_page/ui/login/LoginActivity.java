package com.sbitbd.fixedbd.ui.login_page.ui.login;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.sbitbd.fixedbd.Config.DoConfig;
import com.sbitbd.fixedbd.MainActivity;
import com.sbitbd.fixedbd.R;
import com.sbitbd.fixedbd.ui.create_admin.admin;
import com.sbitbd.fixedbd.ui.forget.forget_code;
import com.sbitbd.fixedbd.ui.home.HomeViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private TextView signup, timer;
    private DoConfig config = new DoConfig();
    private ProgressBar loadingProgressBar;
    private HomeViewModel homeViewModel = new HomeViewModel();
    private EditText usernameEditText, passwordEditText, phoneE;
    private Button forgot, otp_send, otp_login;
    private View view;
    private String forget_phone;
    private ProgressDialog progressDialog;
    private int otp = 0;
    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            otp_send.setEnabled(true);
            timer.setVisibility(View.GONE);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        timer = findViewById(R.id.timer);
        final Button loginButton = findViewById(R.id.login);
        forgot = findViewById(R.id.forgot_btn);
        loadingProgressBar = findViewById(R.id.loading);
        signup = findViewById(R.id.sign_up);
        otp_login = findViewById(R.id.otp_log);
        otp_send = findViewById(R.id.otp_btn);
        signup.setLinksClickable(true);

        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, 10)
                .build();

        MaterialShapeDrawable shapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);
        ViewCompat.setBackground(timer, shapeDrawable);
        shapeDrawable.setFillColor(ContextCompat.getColorStateList(this, R.color.main_color));
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.forgot_phone, null);
                phoneE = view.findViewById(R.id.phone_for);
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                dialogBuilder.setTitle("Forget Password");
                dialogBuilder.setMessage("Enter your Phone Number for a verification code");

                dialogBuilder.setView(view);
                dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
                    forget_phone = "";
                    dialog.cancel();
                });
                dialogBuilder.setPositiveButton("Reset", (dialog, which) -> {
                    if (phoneE.getText().toString().equals("")) {
                        phoneE.setError("Empty Number");
                        Toast.makeText(LoginActivity.this, "Empty Number", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (phoneE.getText().toString().length() < 11) {
                        phoneE.setError("Phone Number must be 11 characters");
                        Toast.makeText(LoginActivity.this, "Phone Number must be 11 characters", Toast.LENGTH_LONG).show();
                        return;
                    }
                    forget_phone = phoneE.getText().toString();
                    getCode(forget_phone);
                });
                dialogBuilder.show();
            }
        });
        signup.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, admin.class)));
        otp_send.setOnClickListener(v -> {
            send(LoginActivity.this, usernameEditText.getText().toString().trim());
            otp = 1;
        });

        otp_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp_login(LoginActivity.this, usernameEditText.getText().toString().trim(),
                        passwordEditText.getText().toString());
            }
        });

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }

                //Complete and destroy login activity once successful

            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(), LoginActivity.this);
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                if (otp == 0)
                    login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                else
                    otp_login(LoginActivity.this,usernameEditText.getText().toString().trim()
                            ,passwordEditText.getText().toString());
//                loginViewModel.login(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString(),LoginActivity.this);
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

    private void send(Context context, String phone) {
        try {
            progressDialog = ProgressDialog.show(context, "", "Sending...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.OTP_SEND,
                    response -> {
                        progressDialog.dismiss();
                        if (response.trim().equals("1")) {
                            Toast.makeText(context, "OTP not sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "OTP sent successful, Please wait for a moment.", Toast.LENGTH_SHORT).show();
                            timer.setVisibility(View.VISIBLE);
                            otp_send.setEnabled(false);
                            new CountDownTimer(60000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    timer.setText("Resend OTP: " + millisUntilFinished / 1000 + " seconds");
                                }

                                public void onFinish() {
                                    timer.setVisibility(View.GONE);
                                    otp_send.setEnabled(true);
                                }

                            }.start();
                        }
                    }, error -> {
                        progressDialog.dismiss();
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
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

    private void otp_login(Context context, String username, String otp) {
        try {
            progressDialog = ProgressDialog.show(context, "", "Loading", false, false);
            String sql = "SELECT id FROM `guest` WHERE phone = '" + username + "' AND code = '" + otp + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    response -> {
                        progressDialog.dismiss();
                        if (!response.trim().equals("")) {
                            homeViewModel.insertuser(context, "", username, "", "", response.trim());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            login(username,otp);
                        }
                    }, error -> {
                        progressDialog.dismiss();
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
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

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    public void login(String username, String password) {
        try {
//            String en_pass = config.encrypt(password);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.LOGIN,
                    response -> {
                        if (response != null && !response.trim().equals("1") && !response.trim().equals("{\"result\":[]}")) {
                            showJson(response);
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                        }
                        loadingProgressBar.setVisibility(View.GONE);
                    }, error -> {
                        Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        loadingProgressBar.setVisibility(View.GONE);
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, username);
                    params.put(config.PRO_SIZE, password);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private void showJson(String response) {
        String name = "", id = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                name = collegeData.getString(config.PRO_NAME);
                id = collegeData.getString(config.CAT_ID);
                updateUiWithUser(new LoggedInUserView(name));
                if (homeViewModel.checkUser(LoginActivity.this)) {
                    homeViewModel.updateUser(LoginActivity.this, id);
                } else {
                    homeViewModel.insertuser(LoginActivity.this, "", "", usernameEditText.getText().toString()
                            , passwordEditText.getText().toString(), id);
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    private void getCode(String phone) {
        try {
            progressDialog = ProgressDialog.show(LoginActivity.this, "", "Loading...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.FORGET,
                    response -> {
                        progressDialog.dismiss();
                        if (!response.equals("1")) {
                            Intent intent = new Intent(LoginActivity.this, forget_code.class);
                            intent.putExtra("phone", phone);
                            startActivity(intent);
                        } else
                            Toast.makeText(LoginActivity.this, "Invalid Phone", Toast.LENGTH_LONG).show();
                    }, error -> {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("phone", phone);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }
}