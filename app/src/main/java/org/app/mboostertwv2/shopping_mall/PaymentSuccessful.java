package org.app.mboostertwv2.shopping_mall;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.app.mboostertwv2.R;
import org.app.mboostertwv2.activity_folder.MainActivity;
import org.app.mboostertwv2.model_folder.SavePreferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PaymentSuccessful extends AppCompatActivity {
    private TextView datetime, su;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_successful);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        datetime = (TextView) findViewById(R.id.datetime);
        su = (TextView) findViewById(R.id.su);
        btn = (Button) findViewById(R.id.btn);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());
        datetime.setText(date);

        Intent intent = getIntent();
        intent.getExtras();
        if(intent.hasExtra("message")){
            su.setText(intent.getStringExtra("message"));
        }

        //su.setText();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                setResult(RESULT_OK);
//                finish();

                //testing
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                SavePreferences.setMainActivitySelectTab(PaymentSuccessful.this,"2");
                startActivity(intent);
            }
        });



    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
