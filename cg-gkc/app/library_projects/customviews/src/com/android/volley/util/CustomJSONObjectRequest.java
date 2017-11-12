package com.android.volley.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

public class CustomJSONObjectRequest extends Request<JSONObject> {

	private Listener<JSONObject> listener;
	private Map<String, String> params;
	private Map<String, String> headers;

	public CustomJSONObjectRequest(String url, Map<String, String> params,
			Listener<JSONObject> reponseListener, ErrorListener errorListener) {
		super(Method.GET, url, errorListener);
		this.listener = reponseListener;
		this.params = params;
		this.headers=null;
	}

	public CustomJSONObjectRequest(int method, String url,
			Map<String, String> params, Listener<JSONObject> reponseListener,
			ErrorListener errorListener) {
		super(method, url, errorListener);
		this.listener = reponseListener;
		this.params = params;
		this.headers=null;
	}
	
	public CustomJSONObjectRequest(int method, String url,
			Map<String, String> params,Map<String, String> headers, Listener<JSONObject> reponseListener,
			ErrorListener errorListener) {
		super(method, url, errorListener);
		this.listener = reponseListener;
		this.params = params;
		this.headers=headers;
	}

	public Map<String, String> getParams()
			throws com.android.volley.AuthFailureError {
		return params;
	};

	public Map<String, String> getHeaders() {
        return headers;
    }
	
	@Override
	public Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONObject(jsonString),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}

	@Override
	public void deliverResponse(JSONObject response) {
		// TODO Auto-generated method stub
		listener.onResponse(response);
	}
}