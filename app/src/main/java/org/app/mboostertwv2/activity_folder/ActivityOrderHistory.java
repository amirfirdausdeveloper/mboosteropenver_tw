package org.app.mboostertwv2.activity_folder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.app.mboostertwv2.Helper.Helper;
import org.app.mboostertwv2.R;
import org.app.mboostertwv2.listAdapter_folder.PHExpandableListViewAdapter;
import org.app.mboostertwv2.model_folder.EndlessScrollListener;
import org.app.mboostertwv2.model_folder.PHModels;
import org.app.mboostertwv2.model_folder.SavePreferences;
import org.app.mboostertwv2.model_folder.urlLink;
import org.app.mboostertwv2.shopping_mall.OrderSummary;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityOrderHistory extends BaseActivity {

    private Toolbar toolbar;
    private String userId;
    private ListView listview;
    private ArrayList<OrderItems> items = new ArrayList<>();
    private ArrayList<PHModels.PurchaseOrder> pitems = new ArrayList<>();
    private purchaseAdapter adapter;
    private TextView empty;
    private SwipeRefreshLayout refresh;

    private ExpandableListView exlist;

    private RelativeLayout relativeLayout;
    private ProgressBar progressBar;

    private TextView login;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        final Drawable back_arrow = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_black_24dp, null);
        back_arrow.setColorFilter(getResources().getColor(R.color.colorbutton), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userId = SavePreferences.getUserID(this);
        exlist = (ExpandableListView) findViewById(R.id.exlist);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        listview = (ListView) findViewById(R.id.listview);
        empty = (TextView) findViewById(R.id.empty);
//        login = (TextView) view.findViewById(R.id.login);
        empty.setVisibility(View.GONE);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ActivityOrderHistory.this, OrderSummary.class);
                i.putExtra("pi_id", items.get(position).getPi_id());
                i.putExtra("from", "1");
                startActivity(i);
            }
        });

        listview.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                if (page != 0) {
                    page = page * 20;
                }
                // transcationlog(userId,page);
                purchasehistory(page);

                System.out.println("page number = " + page);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
//        adapter = new purchaseAdapter(getContext(), items);
//        listview.setAdapter(adapter);

        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                purchasehistory(0);
            }
        });

        //login.setText(Html.fromHtml("<font color='blue'><u>Login</u></font>"));
        if(SavePreferences.getUserID(this).equals("0")){
            empty.setVisibility(View.VISIBLE);
        }else {
            purchasehistory(0);

        }
        Helper.CheckMaintenance(this);

    }

    @Override
    public void noNetwork() {

    }

    public void purchasehistory(final int page) {

        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (page == 0) {
                    pitems.clear();

                    progressBar.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);

                    refresh.setRefreshing(false);
                }
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                progressBar.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
                try {
                    if (jsonObject.getString("success").equals("1")) {
                        JSONArray orderHistory = jsonObject.getJSONArray("orderHistory");
                        for (int i = 0; i < orderHistory.length(); i++) {
                            JSONObject json = orderHistory.getJSONObject(i);
                            JSONArray vendors = json.getJSONArray("vendors");

//                            LogHelper.debug("[vendorrrrr] = " + vendors.toString());

                            ArrayList<PHModels.Vendor> vendor_list = new ArrayList<>();

                            for (int j = 0; j < vendors.length(); j++) {
                                JSONObject js = vendors.getJSONObject(j);
                                JSONArray products = js.getJSONArray("productArray");

                                ArrayList<PHModels.PurchaseItem> items = new ArrayList<>();
                                for (int k = 0; k < products.length(); k++) {
                                    JSONObject jobject = products.getJSONObject(k);
                                    items.add(new PHModels.PurchaseItem(jobject.getString("productid"), jobject.getString("cart_id"), jobject.getString("product_name"), jobject.getString("product_status"), jobject.getString("product_img")));
                                }
                                vendor_list.add(new PHModels.Vendor(js.getString("pi_id"), js.getString("supplier_name"), js.getString("point"), items));
                            }
                            pitems.add(new PHModels.PurchaseOrder(json.getString("invoiceid"), json.getString("date"),json.getString("total_paid"),json.getString("voucher"),json.getString("total_amount"),json.getString("voucher_qualify"),json.getString("transactioniid"),json.getString("total_paid_mat"), json.getString("total_outstanding_amount"),json.getString("paymtIncompl"),json.getString("payment_link"),json.getString("total_voucher_value"), vendor_list));

                        }


                        PHExpandableListViewAdapter adapter = new PHExpandableListViewAdapter(ActivityOrderHistory.this, pitems);

                        exlist.setAdapter(adapter);
                        exlist.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                            @Override
                            public boolean onGroupClick(ExpandableListView parent, View v,
                                                        int groupPosition, long id) {
                                return true; // This way the expander cannot be collapsed
                            }
                        });
                        for (int i = 0; i < pitems.size(); i++) {
                            exlist.expandGroup(i);
                        }
                        if(pitems.size() == 0){
                            empty.setVisibility(View.VISIBLE);
                        }else{
                            empty.setVisibility(View.GONE);
                        }
                    }else{
                        empty.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
//                } finally {
//                    adapter.notifyDataSetChanged();
//
//                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();
                return url.purchase_history(userId, String.valueOf(page));
            }
        }
        new getinfo().execute();

    }


    class purchaseAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<OrderItems> items;

        public purchaseAdapter(Context context, ArrayList<OrderItems> items) {
            this.context = context;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(context, R.layout.purchase_history_item, null);

            TextView orderid = (TextView) convertView.findViewById(R.id.piid);
            TextView status = (TextView) convertView.findViewById(R.id.status);
            TextView date = (TextView) convertView.findViewById(R.id.date);
            TextView pts = (TextView) convertView.findViewById(R.id.pts);

            OrderItems item = items.get(position);

            orderid.setText("#" + item.getInvoiceid());
            if (item.getPi_status().equals("1")) {//pending
                status.setText(R.string.order_processing);
                status.setTextColor(Color.parseColor("#eea236"));
            } else if (item.getPi_status().equals("2")) {//order shipped
                status.setText(R.string.order_shipped);
                status.setTextColor(Color.parseColor("#428bca"));
            } else if (item.getPi_status().equals("999")) {//incomplete payment
                status.setText(" Incomplete Payment ");
                status.setTextColor(Color.parseColor("#428bca"));
            } else {
                status.setText(R.string.order_completed);
                status.setTextColor(Color.parseColor("#d9534f"));
            }


            date.setText(item.getUpdate_datetime());

            pts.setText(item.getTotalamount_point());

            return convertView;
        }
    }

    class OrderItems {
        String pi_id, pi_status, totalamount_point, update_datetime, invoiceid;

        public OrderItems(String pi_id, String pi_status, String totalamount_point, String update_datetime, String invoiceid) {
            this.pi_id = pi_id;
            this.pi_status = pi_status;
            this.totalamount_point = totalamount_point;
            this.update_datetime = update_datetime;
            this.invoiceid = invoiceid;
        }

        public String getPi_id() {
            return pi_id;
        }

        public String getPi_status() {
            return pi_status;
        }

        public String getTotalamount_point() {
            return totalamount_point;
        }

        public String getUpdate_datetime() {
            return update_datetime;
        }

        public String getInvoiceid() {
            return invoiceid;
        }
    }


}
