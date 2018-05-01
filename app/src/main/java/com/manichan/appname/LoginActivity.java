package com.manichan.appname;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.manichan.appname.HomeActivity;
import com.manichan.appname.SessionManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button btnRegisterFromLogin,btnLogin;
    EditText editUsername,editPassword;
    ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private SQLiteHandler db;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    static final Integer SUBS = 0x1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLoggedIn()) {
            Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(homeIntent);
            finish();
        } else {
            sharedPreferences = getSharedPreferences("login_details", 0);
            editor = sharedPreferences.edit();
            editUsername = (EditText) findViewById(R.id.edit_username);
            editPassword = (EditText) findViewById(R.id.edit_password);
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            db = new SQLiteHandler(getApplicationContext());


            btnLogin = (Button) findViewById(R.id.btn_login);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = editUsername.getText().toString();
                    String password = editPassword.getText().toString();
                    if (username.equals("") || password.equals("")) {
                        makeToast("Please enter all the credentials!");
                    } else {
                        checkLogin(username, password);
                    }
                }
            });

            btnRegisterFromLogin = (Button) findViewById(R.id.btn_register_from_login);
            btnRegisterFromLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(registerIntent);
                    finish();
                }
            });
        }
    }
    public void checkLogin(final String username, final String password){
        String tag_string_request = "request_login";
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.LOGIN_URL,new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error){
                        sessionManager.setLogin(true);
                        String uid = jsonObject.getString("uid");
                        JSONObject user= jsonObject.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        db.addUser(name,email,uid);
                        Intent homeIntent = new Intent(getApplicationContext(),HomeActivity.class);
                        editor.putString("username",username);
                        editor.putString("name",name);
                        editor.commit();
                        startActivity(homeIntent);
                        finish();
                    }else{
                        String errorMessage = jsonObject.getString("error_message");
                        makeToast(errorMessage);
                    }
                }catch (Exception e){
                    makeToast("Internal error occured");
                }
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();

                makeToast("Check your network connection"+error.getMessage());
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put("lusername",username);
                params.put("lpassword",password);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest,tag_string_request);
    }
    public void makeToast(String mString){
        Toast.makeText(getApplicationContext(),""+mString,Toast.LENGTH_LONG).show();
    }
}
