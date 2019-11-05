package org.app.mboostertwv2.shopping_mall.Payment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.app.mboostertwv2.Helper.JSonHelper;
import org.app.mboostertwv2.Helper.LogHelper;
import org.app.mboostertwv2.R;
import org.app.mboostertwv2.model_folder.urlLink;
import org.app.mboostertwv2.shopping_mall.CheckOut;
import org.app.mboostertwv2.shopping_mall.PaymentFailed;
import org.app.mboostertwv2.shopping_mall.PaymentSuccessful;
import org.json.JSONObject;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class PaymentWebView extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private WebView webView;


    String userId, getwayTID, temp_tid, paymentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web_view);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        //final Drawable back_arrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        final Drawable back_arrow = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_black_24dp, null);
        back_arrow.setColorFilter(getResources().getColor(R.color.colorbutton), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(back_arrow);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent i = getIntent();
        i.getExtras();
        String url = i.getStringExtra("url");

        userId = i.getStringExtra("userId");
        getwayTID = i.getStringExtra("getwayTID");
        temp_tid = i.getStringExtra("temp_tid");
        paymentDate = i.getStringExtra("paymentDate");

        webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setSupportZoom(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setBuiltInZoomControls(true);


        webView.setWebViewClient(new WebViewClient() {



            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                LogHelper.debug("request url load = "  + request.getUrl().toString());
                if (request.getUrl().toString().contains("ipay88_success.php")) {
                    startActivity(new Intent(PaymentWebView.this, PaymentSuccessful.class));
                    finish();
                } else if (request.getUrl().toString().contains("ipay88_failed.php")) {
                    startActivity(new Intent(PaymentWebView.this, PaymentFailed.class));
                    finish();
                } else if(request.getUrl().toString().contains("PaymentProcess_tw.php")){
//                    PaymentProcess();
                    catchURLInfo(request.getUrl().toString());
                } else {
                    view.loadUrl(request.getUrl().toString());
                }
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                LogHelper.debug("url load = "  + url);
//                if (url.startsWith("https://mbooster.my/app/shop/ipay88_success.php") || url.startsWith("https://www.mbooster.my/app/shop/ipay88_success.php")
//                        || url.startsWith("https://www.mbooster.my/mmspot/payment/ipay88_success.php") || url.startsWith("https://mbooster.my/mmspot/payment/ipay88_success.php") || url.startsWith("https://mbooster.my/stgmb/payment/ipay88_success.php")) {
                if (url.contains("ipay88_success.php")) {
                    // Parse further to extract function and do custom action
                    startActivity(new Intent(PaymentWebView.this, PaymentSuccessful.class));
                    finish();
//                } else if (url.startsWith("https://mbooster.my/app/shop/ipay88_failed.php") || url.startsWith("https://www.mbooster.my/app/shop/ipay88_failed.php")||
//                        url.startsWith("https://www.mbooster.my/mmspot/payment/ipay88_failed.php") ||url.startsWith("https://mbooster.my/mmspot/payment/ipay88_failed.php") || url.startsWith("https://mbooster.my/stgmb/payment/ipay88_failed.php")) {
                } else if (url.contains("ipay88_failed.php")) {
                    startActivity(new Intent(PaymentWebView.this, PaymentFailed.class));
                    finish();
                } else if(url.contains("PaymentProcess_tw.php")){
//                    PaymentProcess();
                    catchURLInfo(url);
                } else {
                    // Load the page via the webview
//                    Log.i("url redirected" , url);
                    view.loadUrl(url);
                }

                return false;
            }
        });
        webView.getSettings().setDomStorageEnabled(true);
//        webView.getSettings().setSaveFormData(false);

        webView.loadUrl(url);
    }

    private void catchURLInfo(String url) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(PaymentWebView.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(getResources().getColor(R.color.colorbutton))
                        .fadeColor(Color.GRAY).build();
                flowerDialog.setCancelable(false);
                try {
                    if (!flowerDialog.isShowing()) {
                        flowerDialog.show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                try {
                    if (flowerDialog.isShowing()) {
                        flowerDialog.dismiss();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    LogHelper.debug("[catchURLInfo]" + jsonObject.toString());
                    if (jsonObject.getString("success").equals("1")) {
                        JSONObject dataJSon = jsonObject.getJSONObject("data");
                        webView.loadUrl(JSonHelper.getObjString(dataJSon, "payment_link"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().loadUrlData(url);
            }
        }
        new getinfo().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(webView!=null){
            webView.stopLoading();
        }
    }
}
