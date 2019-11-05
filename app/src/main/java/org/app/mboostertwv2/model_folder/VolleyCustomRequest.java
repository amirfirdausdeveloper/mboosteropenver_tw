package org.app.mboostertwv2.model_folder;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * Created by Roger Tiong on 5/3/2016.
 */

public class VolleyCustomRequest extends Request<JSONObject> {

    private int mMethod;
    private String mUrl;
    private Map<String, String> mParams;
    private Listener<JSONObject> mListener;

    public VolleyCustomRequest(int method, String url, Map<String, String> params, Listener<JSONObject> reponseListener, ErrorListener errorListener) {

        super(method, url, errorListener);
        this.mMethod = method;
        this.mUrl = url;
        this.mParams = params;
        this.mListener = reponseListener;

    }

    @Override
    public String getUrl() {

        if(mMethod == Method.GET) {

            StringBuilder stringBuilder = new StringBuilder(mUrl);
            Iterator<Map.Entry<String, String>> iterator = mParams.entrySet().iterator();
            int i = 1;

            while (iterator.hasNext()) {

                Map.Entry<String, String> entry = iterator.next();

                if(i == 1) {
                    stringBuilder.append("?" + entry.getKey() + "=" + entry.getValue());
                } else {
                    stringBuilder.append("&" + entry.getKey() + "=" + entry.getValue());
                }

                iterator.remove(); // avoids a ConcurrentModificationException
                i++;

            } mUrl = stringBuilder.toString();

        } return mUrl;

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError { return mParams; };

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

        try {

            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {

            return Response.error(new ParseError(e));

        } catch (JSONException je) { return Response.error(new ParseError(je)); }

    }

    @Override
    protected void deliverResponse(JSONObject response) {

        // TODO Auto-generated method stub
        mListener.onResponse(response);

    }

}
