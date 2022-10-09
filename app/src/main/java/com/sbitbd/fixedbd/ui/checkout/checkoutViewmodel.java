package com.sbitbd.fixedbd.ui.checkout;

import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.fixedbd.Config.DoConfig;
import com.sbitbd.fixedbd.R;
import com.sbitbd.fixedbd.ui.product_descripton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class checkoutViewmodel extends ViewModel {

    private MutableLiveData<payment> paymentMutableLiveData;

    protected LiveData<payment> getPaymentData(Context context){
        if (paymentMutableLiveData == null) {
            paymentMutableLiveData = new MutableLiveData<>();
            getData(context);
        }
        return paymentMutableLiveData;
    }

    private void getData(Context context) {
        try {
            String sql = "SELECT `settings`.`bkash` AS 'one',`settings`.`rocket` AS 'two'," +
                    "`settings`.`nagad` AS 'three',`settings`.`bank` AS 'four'" +
                    ",`settings`.`others` AS 'five' FROM `settings`";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.SIX_DMS,
                    response -> {
                try {
                    if (!response.trim().equals("")) {
                        JSONObject jsonObject = new JSONObject(response.trim());
                        JSONArray result = jsonObject.getJSONArray(DoConfig.RESULT);
                        JSONObject collegeData = result.getJSONObject(0);
                        paymentMutableLiveData.setValue(new payment(collegeData.getString(DoConfig.ONE),
                                collegeData.getString(DoConfig.TWO),collegeData.getString(DoConfig.THREE)
                        ,collegeData.getString(DoConfig.FOUR),collegeData.getString(DoConfig.FIVE)));
                    }else paymentMutableLiveData.setValue(null);
                }catch (Exception e){paymentMutableLiveData.setValue(null);
                }
                    }, error -> paymentMutableLiveData.setValue(null)) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(DoConfig.QUERY, sql);
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
}
