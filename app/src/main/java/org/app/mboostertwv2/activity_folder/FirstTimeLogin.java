package org.app.mboostertwv2.activity_folder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.app.mboostertwv2.Google.AnalyticsApplication;
import org.app.mboostertwv2.Helper.Helper;
import org.app.mboostertwv2.R;
import org.app.mboostertwv2.model_folder.SavePreferences;
import org.app.mboostertwv2.model_folder.urlLink;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FirstTimeLogin extends AppCompatActivity {


    private EditText name, email, phonenumber, city;
    private EditText baddr1, baddr2, bcountry, bcity;

    private EditText shipname, shipcontact, saddr1, saddr2, scountry;

    private AutoCompleteTextView bpostcode, postcode;

    private TextView spstate, spbstate;
    //    private Spinner sppostcode, spbpostcode,spcity, spbcity;
    private RelativeLayout submit;
    private CheckBox cbox;
    private Toolbar toolbar;
    private Boolean copychk = false;


    private ArrayList<State> state_list = new ArrayList<>();
    private ArrayList<String> state_list_str = new ArrayList<>();
    private ArrayList<City> city_list = new ArrayList<>();
    private ArrayList<String> city_list_str = new ArrayList<>();
    private ArrayList<Postcode> postcode_list = new ArrayList<>();
    private ArrayList<String> postcode_list_str = new ArrayList<>();

    private ArrayList<State> bstate_list = new ArrayList<>();
    private ArrayList<String> bstate_list_str = new ArrayList<>();
    private ArrayList<City> bcity_list = new ArrayList<>();
    private ArrayList<String> bcity_list_str = new ArrayList<>();
    private ArrayList<Postcode> bpostcode_list = new ArrayList<>();
    private ArrayList<String> bpostcode_list_str = new ArrayList<>();

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    private Tracker mTracker;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_login);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        // firebase analytic
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ToobarSetting();
        ObjectSetup();

        pref = getSharedPreferences("MboosterMY", 0); // 0 - for private mode
        editor = pref.edit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FirstTimeLogin.this, signInActivity.class));
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void ObjectSetup() {

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        phonenumber = (EditText) findViewById(R.id.phnumber);

        baddr1 = (EditText) findViewById(R.id.baddr1);
        baddr2 = (EditText) findViewById(R.id.baddr2);
//        spbpostcode = (Spinner) findViewById(R.id.spbpostcode);
        bpostcode = (AutoCompleteTextView) findViewById(R.id.bpostcode);
        spbstate = findViewById(R.id.spbstate);
//        spbcity = (Spinner) findViewById(R.id.spbcity);
        bcity = (EditText) findViewById(R.id.bcity);
        bcountry = (EditText) findViewById(R.id.bcountry);
//        bcountry.setText("Malaysia");


        shipname = (EditText) findViewById(R.id.shipname);
        shipcontact = (EditText) findViewById(R.id.shipcontact);
        saddr1 = (EditText) findViewById(R.id.saddr1);
        saddr2 = (EditText) findViewById(R.id.saddr2);
//        sppostcode = (Spinner) findViewById(R.id.sppostcode);
        postcode = (AutoCompleteTextView) findViewById(R.id.postcode);
        spstate =  findViewById(R.id.spstate);
        postcode = (AutoCompleteTextView) findViewById(R.id.postcode);

        city = (EditText) findViewById(R.id.city);
//        spcity = (Spinner) findViewById(R.id.spcity);

        scountry = (EditText) findViewById(R.id.scountry);

        final TextWatcher watcher1 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                saddr1.setText(baddr1.getText().toString());
            }
        };
        final TextWatcher watcher2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                saddr2.setText(baddr2.getText().toString());
            }
        };

        final TextWatcher watcher3 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                city.setText(bcity.getText().toString());
            }
        };

        final TextWatcher watcher4 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                postcode.setText(bpostcode.getText().toString());
            }
        };


        cbox = (CheckBox) findViewById(R.id.cbox);
        cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    saddr1.setEnabled(false);
                    saddr2.setEnabled(false);
                    spstate.setEnabled(false);
//                    spcity.setEnabled(false);
                    city.setEnabled(false);
                    postcode.setEnabled(false);
//                    sppostcode.setEnabled(false);

                    saddr1.setText(baddr1.getText().toString());
                    saddr2.setText(baddr2.getText().toString());
                    city.setText(bcity.getText().toString());
                    postcode.setText(bpostcode.getText().toString());
                    //spstate.setSelection(spbstate.getSelectedItemPosition());

                    getState();

                    baddr1.addTextChangedListener(watcher1);
                    baddr2.addTextChangedListener(watcher2);
                    bcity.addTextChangedListener(watcher3);
                    bpostcode.addTextChangedListener(watcher4);
                } else {
                    saddr1.setEnabled(true);
                    saddr2.setEnabled(true);
                    spstate.setEnabled(true);
//                    spcity.setEnabled(true);
                    city.setEnabled(true);
                    postcode.setEnabled(true);
//                    sppostcode.setEnabled(true);


                    baddr1.removeTextChangedListener(watcher1);
                    baddr2.removeTextChangedListener(watcher2);
                    bcity.removeTextChangedListener(watcher3);
                    bpostcode.removeTextChangedListener(watcher4);

                }
            }
        });

        submit = (RelativeLayout) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.getText().toString().trim().length() == 0 ||
                        email.getText().toString().trim().length() == 0 ||
                        phonenumber.getText().toString().trim().length() == 0 ||
                        saddr1.getText().toString().trim().length() == 0 ||
                        baddr2.getText().toString().trim().length() == 0 ||
                        shipcontact.getText().toString().trim().length() == 0 ||
                        shipname.getText().toString().trim().length() == 0 ||
                        city.getText().toString().trim().length() == 0 ||
                        bcity.getText().toString().trim().length() == 0 ||
                        postcode.getText().toString().trim().length() == 0 ||
                        bpostcode.getText().toString().trim().length() == 0) {

                    Toast.makeText(FirstTimeLogin.this, "Please fill in all blanks", Toast.LENGTH_SHORT).show();

                } else {

                    if (Helper.isValidEmail(email.getText().toString())) {
                        updateInfo(name.getText().toString(), email.getText().toString(), phonenumber.getText().toString(),
                                baddr1.getText().toString(), baddr2.getText().toString(), spbstate.getText().toString(), bcity.getText().toString(),
                                bpostcode.getText().toString(), bcountry.getText().toString(), shipname.getText().toString(), shipcontact.getText().toString(), saddr1.getText().toString(),
                                saddr2.getText().toString(), spstate.getText().toString(), city.getText().toString(), postcode.getText().toString(), scountry.getText().toString());

                    } else {
                        if (SavePreferences.getApplanguage(FirstTimeLogin.this).equals("CN")) {
                            Toast.makeText(FirstTimeLogin.this, "电邮无效", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FirstTimeLogin.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        getState();
        getbState();

        getPostcodeList();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("First Time Login");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mFirebaseAnalytics.setCurrentScreen(this, "FirstTimeLogin Page", null /* class override */);

    }


    private void ToobarSetting() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
//        final Drawable back_arrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        final Drawable back_arrow = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_black_24dp, null);        back_arrow.setColorFilter(getResources().getColor(R.color.colorbutton), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstTimeLogin.this, signInActivity.class));
                finish();
            }
        });
    }


    private void getbState() {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                try {
                    JSONArray array = jsonObject.getJSONArray("state");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        bstate_list.add(new State(json.getString("ssid"), json.getString("state")));
                        bstate_list_str.add(json.getString("state"));
                    }

                    ArrayAdapter<String> stateList = new ArrayAdapter<>(FirstTimeLogin.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            bstate_list_str);
//                    spbstate.setAdapter(stateList);
//                    spbstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////                            spbpostcode.setAdapter(null);
////                            spbcity.setAdapter(null);
//                            bpostcode_list_str.clear();
//                            bpostcode_list.clear();
//                            bcity_list.clear();
//                            bcity_list_str.clear();
//                            String id = bstate_list.get(i).getId();
////                            getbCity(id);
//
//                            if (cbox.isChecked()) {
//                                spstate.setSelection(spbstate.getSelectedItemPosition());
//                            }
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> adapterView) {
//
//                        }
//                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().shipping_state();
            }
        }
        new getinfo().execute();
    }

//    private void getbCity(final String state_id) {
//        class getinfo extends AsyncTask<String, String, JSONObject> {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onPostExecute(JSONObject jsonObject) {
//                super.onPostExecute(jsonObject);
//
//                try {
//                    JSONArray array = jsonObject.getJSONArray("city");
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject json = array.getJSONObject(i);
//                        bcity_list.add(new City(json.getString("sciid"), json.getString("city")));
//                        bcity_list_str.add(json.getString("city"));
//                    }
//                    ArrayAdapter<String> cityList = new ArrayAdapter<>(FirstTimeLogin.this,
//                            android.R.layout.simple_spinner_item,
//                            bcity_list_str);
//                    spbcity.setAdapter(cityList);
//                    spbcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                            String id = bcity_list.get(i).getId();
//                            bpostcode_list.clear();
//                            bpostcode_list_str.clear();
//                            getbPostcode(id);
//
//                            if (cbox.isChecked()) {
//                                spcity.setSelection(spbcity.getSelectedItemPosition());
//                            }
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> adapterView) {
//
//                        }
//                    });
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected JSONObject doInBackground(String... strings) {
//                return new urlLink().shipping_city(state_id);
//            }
//        }
//        new getinfo().execute();
//    }

//    private void getbPostcode(final String city_id) {
//        class getinfo extends AsyncTask<String, String, JSONObject> {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onPostExecute(JSONObject jsonObject) {
//                super.onPostExecute(jsonObject);
//
//                try {
//                    JSONArray array = jsonObject.getJSONArray("postcode");
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject json = array.getJSONObject(i);
//                        bpostcode_list.add(new Postcode(json.getString("spid"), json.getString("postcode")));
//                        bpostcode_list_str.add(json.getString("postcode"));
//                    }
//                    ArrayAdapter<String> postcodeList = new ArrayAdapter<>(FirstTimeLogin.this,
//                            android.R.layout.simple_spinner_item,
//                            bpostcode_list_str);
////                    spbpostcode.setAdapter(postcodeList);
////
////
////                    spbpostcode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////                        @Override
////                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////                            if (cbox.isChecked()) {
////                                sppostcode.setSelection(spbpostcode.getSelectedItemPosition());
////                            }
////                        }
////
////                        @Override
////                        public void onNothingSelected(AdapterView<?> adapterView) {
////
////                        }
////                    });
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            protected JSONObject doInBackground(String... strings) {
//                return new urlLink().shipping_postcode(city_id);
//            }
//        }
//        new getinfo().execute();
//    }


    private void getState() {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                state_list.clear();
                state_list_str.clear();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                try {
                    JSONArray array = jsonObject.getJSONArray("state");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        state_list.add(new State(json.getString("ssid"), json.getString("state")));
                        state_list_str.add(json.getString("state"));
                    }

                    ArrayAdapter<String> stateList = new ArrayAdapter<>(FirstTimeLogin.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            state_list_str);
//                    spstate.setAdapter(stateList);
//                    spstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////                            sppostcode.setAdapter(null);
////                            spcity.setAdapter(null);
//                            //postcode_list_str.clear();
//                            //postcode_list.clear();
//                            city_list.clear();
//                            city_list_str.clear();
//                            String id = state_list.get(i).getId();
////                            getCity(id);
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> adapterView) {
//
//                        }
//                    });
                    if (cbox.isChecked()) {
//                        spstate.setSelection(spbstate.getSelectedItemPosition());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().shipping_state();
            }
        }
        new getinfo().execute();
    }

//    private void getCity(final String state_id) {
//        class getinfo extends AsyncTask<String, String, JSONObject> {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onPostExecute(JSONObject jsonObject) {
//                super.onPostExecute(jsonObject);
//
//                try {
//                    JSONArray array = jsonObject.getJSONArray("city");
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject json = array.getJSONObject(i);
//                        city_list.add(new City(json.getString("sciid"), json.getString("city")));
//                        city_list_str.add(json.getString("city"));
//                    }
//                    ArrayAdapter<String> cityList = new ArrayAdapter<>(FirstTimeLogin.this,
//                            android.R.layout.simple_spinner_item,
//                            city_list_str);
//                    spcity.setAdapter(cityList);
//                    spcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                            String id = city_list.get(i).getId();
//                            postcode_list.clear();
//                            postcode_list_str.clear();
//                            getPostcode(id);
//
//                            if (cbox.isChecked()) {
//                                spcity.setSelection(spbcity.getSelectedItemPosition());
//                            }
//                        }
//
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> adapterView) {
//
//                        }
//                    });
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected JSONObject doInBackground(String... strings) {
//                return new urlLink().shipping_city(state_id);
//            }
//        }
//        new getinfo().execute();
//    }

//    private void getPostcode(final String city_id) {
//        class getinfo extends AsyncTask<String, String, JSONObject> {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onPostExecute(JSONObject jsonObject) {
//                super.onPostExecute(jsonObject);
//
//                try {
//                    JSONArray array = jsonObject.getJSONArray("postcode");
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject json = array.getJSONObject(i);
//                        postcode_list.add(new Postcode(json.getString("spid"), json.getString("postcode")));
//                        postcode_list_str.add(json.getString("postcode"));
//                    }
//                    ArrayAdapter<String> postcodeList = new ArrayAdapter<>(FirstTimeLogin.this,
//                            android.R.layout.simple_spinner_item,
//                            postcode_list_str);
////                    sppostcode.setAdapter(postcodeList);
////
////                    if (cbox.isChecked()) {
////                        sppostcode.setSelection(spbpostcode.getSelectedItemPosition());
////                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            protected JSONObject doInBackground(String... strings) {
//                return new urlLink().shipping_postcode(city_id);
//            }
//        }
//        new getinfo().execute();
//    }


    private void getPostcodeList() {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                try {
                    JSONArray array = jsonObject.getJSONArray("postcode");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        postcode_list_str.add(json.getString("postcode"));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(FirstTimeLogin.this, android.R.layout.simple_spinner_dropdown_item, postcode_list_str);
                    postcode.setThreshold(1);
                    bpostcode.setThreshold(1);
                    postcode.setAdapter(adapter);
                    bpostcode.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().getPostcodeList();
            }
        }
        new getinfo().execute();
    }


    private void updateInfo(final String name, final String email, final String phnumber,
                            final String baddr1, final String baddr2, final String bstate, final String bcity, final String bpostcode, final String bcountry,
                            final String ship_name, final String ship_contact, final String saddr1, final String saddr2, final String sstate, final String scity, final String spostcode, final String scountry) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(FirstTimeLogin.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(getResources().getColor(R.color.colorbutton))
                        .fadeColor(Color.GRAY).build();
                if (!flowerDialog.isShowing()) {
                    flowerDialog.show();
                }
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                if (flowerDialog.isShowing()) {
                    flowerDialog.dismiss();
                }
                try {
                    if (jsonObject.getString("success").equals("1")) {
                        if (SavePreferences.getApplanguage(FirstTimeLogin.this).equals("CN")) {
                            Toast.makeText(FirstTimeLogin.this, "更新完毕", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FirstTimeLogin.this, "Update Successful", Toast.LENGTH_SHORT).show();
                        }
//                        startActivity(new Intent(FirstTimeLogin.this, MainActivity.class));
//                        finish();
//                        getLanguage(SavePreferences.getUserID(FirstTimeLogin.this));
                        Intent intent = new Intent(FirstTimeLogin.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                //return new urlLink().first_login(SavePreferences.getUserID(FirstTimeLogin.this), name, email, phonenumber, addr1, addr2, city, postcode, state, country);
                return new urlLink().first_login(SavePreferences.getUserID(FirstTimeLogin.this), name, email, phnumber, baddr1, baddr2, bstate, bcity, bpostcode, bcountry,
                        ship_name, ship_contact, saddr1, saddr2, sstate, scity, spostcode, scountry);
            }
        }
        new getinfo().execute();
    }

    public void getLanguage(final String userId) {

        class getinfo extends AsyncTask<String, String, JSONObject> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {

                super.onPostExecute(jsonObject);

                try {

                    if (jsonObject.getString("success").equals("1")) {
                        Log.i("Login langu", jsonObject.getString("langu"));

                        if (jsonObject.getString("langu").equals("SCN")) {
                            Helper.setAppLocale(FirstTimeLogin.this, "zh", "CN");
                        } else if (jsonObject.getString("langu").equals("TCN")) {
                            Helper.setAppLocale(FirstTimeLogin.this, "zh", "TW");
                        } else if (jsonObject.getString("langu").toUpperCase().contains("CN")) {
                            Helper.setAppLocale(FirstTimeLogin.this, "zh", "CN");
                        } else {
                            Helper.setAppLocale(FirstTimeLogin.this, "en", "");

                        }
                        Log.i("set language", getResources().getConfiguration().locale.toString());


                    } else {

                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {

                return new urlLink().getLanguage(userId);
            }
        }

        new getinfo().execute();
    }


    class Postcode {
        String id;
        String postcodestr;

        public Postcode(String id, String postcodestr) {
            this.id = id;
            this.postcodestr = postcodestr;
        }

        public String getId() {
            return id;
        }

        public String getPostcodestr() {
            return postcodestr;
        }
    }

    class City {
        String id;
        String name;

        public City(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    class State {
        String id;
        String name;

        public State(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

    }
}
