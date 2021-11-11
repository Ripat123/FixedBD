package com.sbitbd.fixedbd.ui.verify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sbitbd.fixedbd.Config.DoConfig;
import com.sbitbd.fixedbd.R;
import com.sbitbd.fixedbd.ui.cart_operation.operation;

public class verfication extends AppCompatActivity {

//    GoogleApiClient mGoogleApiClient;
//    SmsBroadcastReceiver smsBroadcastReceiver;
    private int RESOLVE = 1;
    private Button set_pass;
    private EditText email,address,password,confirm_pass;
    private DoConfig config;
    String firstname="",lastname="",phone="";
    operation operation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfication);

        try {
            email = findViewById(R.id.email_txt);
            address = findViewById(R.id.add_txt);
            password = findViewById(R.id.vp_ed);
            confirm_pass = findViewById(R.id.vc_ed);
            set_pass = findViewById(R.id.pass_btn);

            config = new DoConfig();

            set_pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registration(firstname,lastname,phone);
                }
            });
        }catch (Exception e){
        }

    }
//    private void google_verify(){
//        smsBroadcastReceiver = new SmsBroadcastReceiver();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.CREDENTIALS_API)
//                .build();
//
//        smsBroadcastReceiver.setOnOtpListeners(this);
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
//        getApplicationContext().registerReceiver(smsBroadcastReceiver, intentFilter);
//        requestHint();
//    }
//    private void startmsg(){
//        SmsRetrieverClient retrieverClient = SmsRetriever.getClient(this);
//        Task<Void> task = retrieverClient.startSmsUserConsent("01825909842");
//        task.addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(verfication.this,"start",Toast.LENGTH_LONG).show();
//            }
//        });
//        task.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(verfication.this,"Error",Toast.LENGTH_LONG).show();
//            }
//        });
//    }


//    private void requestHint()  {
//        HintRequest hintRequest =
//                new HintRequest.Builder()
//                        .setPhoneNumberIdentifierSupported(true)
//                        .build();
//        PendingIntent mIntent = Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest);
//        try {
//            startIntentSenderForResult(mIntent.getIntentSender(), RESOLVE, null, 0, 0, 0);
//        } catch (IntentSender.SendIntentException e) {
//        }
//
//    }
    private void registration(String firstname,String lastname,String phone){
        try {
            if(email.getText().toString().trim().equals("")){
                email.setError("Empty Email");
                return;
            }
            if(address.getText().toString().trim().equals("")){
                address.setError("Empty Address");
                return;
            }
            if(password.getText().toString().equals("")){
                password.setError("Empty Password");
                return;
            }
            if(password.getText().toString().length() < 8){
                password.setError("Password must be >= 8 character");
                return;
            }
            if(!password.getText().toString().equals(confirm_pass.getText().toString())){
                confirm_pass.setError("Password didn't matched");
                return;
            }
            operation = new operation();
            operation.insertuser(verfication.this,firstname, lastname,phone,
                    email.getText().toString(),password.getText().toString(),address.getText().toString().trim());

        }catch (Exception e){
        }
    }

}