package org.app.mboostertwv2.shopping_mall.Payment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import org.app.mboostertwv2.R;

public class PaymentMethod extends AppCompatActivity {

    private ExpandableListView pm_expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        pm_expandableListView = (ExpandableListView) findViewById(R.id.pm_expandableListView);


    }
}
