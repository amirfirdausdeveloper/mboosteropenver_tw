package org.app.mboostertwv2.listAdapter_folder;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.app.mboostertwv2.R;
import org.app.mboostertwv2.model_folder.Address;

import java.util.ArrayList;

/**
 * Created by Mobkini on 9/2/2018.
 */


public class AddressListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Address> items;

    public AddressListAdapter(Context context, ArrayList<Address> items) {
        this.context = context;
        this.items = items;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.address_listitem, null);

        TextView name = (TextView) view.findViewById(R.id.name);
        TextView address = (TextView) view.findViewById(R.id.address);
        TextView edit = (TextView) view.findViewById(R.id.edit);
        TextView shipbill = (TextView) view.findViewById(R.id.shipbill);
        TextView defaultas = (TextView) view.findViewById(R.id.defaultas);
        TextView status = (TextView) view.findViewById(R.id.status);
        final Address item = items.get(i);

        name.setText(item.name + "\n" + item.contact + "\n" + item.email + "\n" + item.icno);
        address.setText(item.addr1 + "\n" + item.addr2 + "\n" + item.city + ", " + item.postcode + ", " + item.state + "\n" + item.country);
        if(item.addr2.equals("")){
            address.setText(item.addr1 + "\n"  + item.city + ", " + item.postcode + ", " + item.state + "\n" + item.country);
        }
        if (item.status.equals("1")) {
            status.setTextColor(Color.GREEN);
            status.setText(context.getString(R.string.verified));
        } else if (item.status.equals("2")) {
            status.setTextColor(Color.RED);
            status.setText(context.getString(R.string.pending_verify));
        }

        if (item.shipping.equals("1") && item.billing.equals("0")) {
           // shipbill.setText(Html.fromHtml("<font color='#F39C12'>Shipping</font>"));
            shipbill.setText(context.getResources().getString(R.string.shipping));
            shipbill.setTextColor(Color.parseColor("#F39C12"));
        } else if (item.shipping.equals("0") && item.billing.equals("1")) {
//            shipbill.setText(Html.fromHtml("<font color='blue'>"+ context.getResources().getString(R.string.billing)+"</font>"));
            shipbill.setText(context.getResources().getString(R.string.billing));
            shipbill.setTextColor(Color.BLUE);
        } else if (item.shipping.equals("1") && item.billing.equals("1")) {
            shipbill.setText(Html.fromHtml("<font color='#F39C12'>"+context.getResources().getString(R.string.shipping) +"</font> & <font color='blue'>"+ context.getResources().getString(R.string.billing)+"</font>"));
        } else if (item.shipping.equals("0") && item.billing.equals("0")) {
            shipbill.setText(" - ");
            defaultas.setVisibility(View.GONE);
            shipbill.setVisibility(View.GONE);
        }

//            edit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, EditAddress.class);
//                    intent.putExtra("address_id", item.id);
//                    startActivity(intent);
//                }
//            });

        return view;
    }

}

