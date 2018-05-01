package com.manichan.appname;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.manichan.appname.AppConstants;
import com.manichan.appname.AppController;
import com.manichan.appname.Config;
import com.manichan.appname.SQLiteHandler;
import com.manichan.appname.SessionManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    Button btnLoginFromRegister,btnRegister;
    EditText editName,editUsername,editEmail,editPassword,editRepassword;

    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        overridePendingTransition(R.anim.trans_left_in,R.anim.trans_left_out);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sessionManager = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        if (sessionManager.isLoggedIn()){
            Intent homeIntent = new Intent(RegisterActivity.this,HomeActivity.class);
            startActivity(homeIntent);
            finish();
        }

        editName = (EditText) findViewById(R.id.edit_register_name);
        editUsername = (EditText) findViewById(R.id.edit_register_username);
        editEmail = (EditText) findViewById(R.id.edit_register_email);
        editPassword = (EditText)  findViewById(R.id.edit_register_password);
        editRepassword = (EditText)  findViewById(R.id.edit_register_repassword);

        btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*editName.setText("Manivannan");
                editEmail.setText("manivannan23111996@gmail.com");
                editUsername.setText("manichan");
                editPassword.setText("manichan");
                editRepassword.setText("manichan");*/
                String mName,mUsername,mEmail,mPassword,mRepassword;
                mName = editName.getText().toString();
                mUsername = editUsername.getText().toString();
                mEmail = editEmail.getText().toString();
                mPassword = editPassword.getText().toString();
                mRepassword = editRepassword.getText().toString();
                if (mName.equals("")){
                    makeToast("Enter name");
                }else if (mUsername.equals("")){
                    makeToast("Enter Username");
                }else if (mEmail.equals("")){
                    makeToast("Enter Email-ID");
                }else if (mPassword.equals("")){
                    makeToast("Enter Password");
                }else if (mRepassword.equals("")){
                    makeToast("Retype the Password");
                }else if (!mPassword.equals(mRepassword)){
                    makeToast("Passwords don't match");
                }else{
                    registerUser(mName,mUsername,mEmail,mPassword);
                }
            }
        });

        btnLoginFromRegister = (Button) findViewById(R.id.btn_login_from_register);
        btnLoginFromRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    public void makeToast(String mString){
        Toast.makeText(getApplicationContext(),""+mString,Toast.LENGTH_LONG).show();
    }

    public void registerUser(final String mName,final String mUsername,final String mEmail,final String mPassword){
        String tag_string_request = "request_register";
        progressDialog.setMessage("Registering...");
        progressDialog.show();
        //final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.REGISTER_URL,new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error){
                        makeToast("Register Success. Login to continue");
                        Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(loginIntent);
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
                makeToast("Check your network connection");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put("rname",mName);
                params.put("rusername",mUsername);
                params.put("remail",mEmail);
                params.put("rpassword",mPassword);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest,tag_string_request);
    }
}
