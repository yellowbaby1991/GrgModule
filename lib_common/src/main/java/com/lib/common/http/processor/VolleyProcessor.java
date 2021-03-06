package com.lib.common.http.processor;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lib.common.http.interfaces.ICallBack;
import com.lib.common.http.interfaces.IhttpProcessor;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class VolleyProcessor implements IhttpProcessor {

    public static final String TAG = "VolleyProcessor";

    private static RequestQueue mQueue = null;

    private RetryPolicy mRetryPolicy;

    public VolleyProcessor(Context context) {
        mQueue = Volley.newRequestQueue(context);
    }

    public void setRetryPolicy(RetryPolicy retryPolicy){
        mRetryPolicy = retryPolicy;
    }

    @Override
    public void get(String url, Map<String, Object> params, final ICallBack callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = dealString(response);
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFailed(volleyError.toString());
            }
        });
        stringRequest.setRetryPolicy(mRetryPolicy);
        mQueue.add(stringRequest);
    }

    @Override
    public void post(String url, Map<String, Object> params, final ICallBack callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = dealString(response);
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFailed(volleyError.toString());
            }
        });
        stringRequest.setRetryPolicy(mRetryPolicy);
        mQueue.add(stringRequest);
    }

    @Override
    public void postByJson(String url, Map<String, Object> params, final ICallBack callback) {

        JSONObject jsonObject = new JSONObject(params);

        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(dealString(response.toString()));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailed(error.toString());
                    }
                });

        jsonRequest.setRetryPolicy(mRetryPolicy);
        mQueue.add(jsonRequest);

    }

    @NonNull
    //处理中文乱码
    protected String dealString(String response) {
        /*try {
            response=new String(response.getBytes("iso-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        return response;
    }

}
