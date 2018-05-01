package com.manichan.appname;


import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by manichan on 19/7/17.
 */

public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static AppController mInstance;

    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;
    }
    public static  synchronized  AppController getInstance(){
        return  mInstance;
    }
    public RequestQueue getRequestQueue(){
        if (mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(this);
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> request,String tag){
        request.setTag(TextUtils.isEmpty(tag)?TAG:tag);
        getRequestQueue().add(request);
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
