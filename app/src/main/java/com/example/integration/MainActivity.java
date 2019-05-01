package com.example.integration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.integration.R;
import com.example.integration.models.Checksum;
import com.example.integration.models.Paytm;
import com.example.integration.utils.WebServiceCaller;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
     EditText editText;
     Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
//it initializes the buttons we created on the screen...
    public void init() {
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPaytm();
            }
        });
    }
//generates the unique string ...
    private String generateString() {
        String uuid= UUID.randomUUID().toString();
        return uuid.replaceAll("-","");
    }

    private void processPaytm(){
        String custid=generateString();
        String orderid=generateString();
        String callbackurl="https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=order1";
        final Paytm paytm=new Paytm("YAWUVE89081883980894",
                "WAP",
                editText.getText().toString().trim(),
                "WEBSTAGING",
                callbackurl,
                "education",
                orderid,custid);
         WebServiceCaller.getClient().getChecksum(paytm.getmId(),paytm.getOrderId(), paytm.getCustId()
                , paytm.getChannelId(), paytm.getTxnAmount(), paytm.getWebsite(), paytm.getCallBackUrl(), paytm.getIndustryTypeId()
                        ).enqueue(new Callback<Checksum>() {
        @Override
        public void onResponse(Call<Checksum> call, Response<Checksum> response) {
            if (response.isSuccessful()) {
                processToPay(response.body().getChecksumHash(),paytm);
            }
        }

        @Override
        public void onFailure(Call<Checksum> call, Throwable t) {

        }
    });
    }
    private void processToPay(String checksumHash, Paytm paytm) {
        PaytmPGService Service = PaytmPGService.getStagingService();

        HashMap<String, String> paramMap = new HashMap<String,String>();
        paramMap.put( "MID" , paytm.getmId());
// Key in your staging and production MID available in your dashboard
        paramMap.put( "ORDER_ID",paytm.getOrderId());
        paramMap.put( "CUST_ID",paytm.getCustId());
        paramMap.put( "CHANNEL_ID",paytm.getChannelId());
        paramMap.put( "TXN_AMOUNT",paytm.getTxnAmount());
        paramMap.put( "WEBSITE",paytm.getWebsite());
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "INDUSTRY_TYPE_ID",paytm.getIndustryTypeId());
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "CALLBACK_URL",paytm.getCallBackUrl());
        paramMap.put( "CHECKSUMHASH" , checksumHash);
        PaytmOrder Order = new PaytmOrder(paramMap);
        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            /*Call Backs*/
            public void someUIErrorOccurred(String inErrorMessage) {
                Toast.makeText(MainActivity.this,"Unexpected error occured",Toast.LENGTH_SHORT).show();
            }
            public void onTransactionResponse(Bundle inResponse) {
                Toast.makeText(MainActivity.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
            }
            public void networkNotAvailable() {
                Toast.makeText(MainActivity.this,"go to the place where internet is fast", Toast.LENGTH_LONG).show();
            }
            public void clientAuthenticationFailed(String inErrorMessage) {
                Toast.makeText(MainActivity.this,"Authentication failed",Toast.LENGTH_LONG).show();
            }
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {}
            public void onBackPressedCancelTransaction() {
                Toast.makeText(MainActivity.this,"Transaction will be cancelled",Toast.LENGTH_SHORT).show();
            }
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                Toast.makeText(MainActivity.this,"Transaction Cancelled",Toast.LENGTH_SHORT).show();
            }
        });
    }
}




