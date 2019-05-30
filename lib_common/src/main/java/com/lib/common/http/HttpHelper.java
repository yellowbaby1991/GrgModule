package com.lib.common.http;

import com.lib.common.http.interfaces.ICallBack;
import com.lib.common.http.interfaces.IhttpProcessor;

import java.util.HashMap;
import java.util.Map;

public class HttpHelper implements IhttpProcessor {

    private static IhttpProcessor mIhttpProcessor;
    private static HttpHelper _instance;
    private Map<String, Object> mParams;

    private HttpHelper() {
        mParams = new HashMap<>();
    }


    public static HttpHelper obtain() {
        synchronized (HttpHelper.class) {
            if (_instance == null) {
                _instance = new HttpHelper();
            }
        }
        return _instance;
    }

    public static void init(IhttpProcessor httpProcessor) {
        mIhttpProcessor = httpProcessor;

    }

    @Override
    public void get(String url, Map<String, Object> params, ICallBack callback) {
        mIhttpProcessor.get(url, params, callback);
    }

    @Override
    public void post(String url, Map<String, Object> params, ICallBack callback) {
        mIhttpProcessor.post(url, params, callback);
    }

    @Override
    public void postByJson(String url, Map<String, Object> params, ICallBack callback) {
        mIhttpProcessor.postByJson(url, params, callback);
    }

    //拼接url
    private String appendParams(String url, Map<String, Object> params) {
        return "";
    }
}
