package com.manichan.appname;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    public static final Integer FILE = 0x1;

    LinearLayout lh,lsc,lsh,lo;
    Button bh,bsc,bsh,bo;
    TextView tvh,tvsc,tvsh,tvo;
    public static int count = 0;
    public ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        lh = findViewById(R.id.layoutHospi);
        lsc = findViewById(R.id.layoutSchool);
        lsh = findViewById(R.id.layoutShelter);
        lo = findViewById(R.id.layoutOthers);
        bh = findViewById(R.id.btnUploadHospital);
        bsc = findViewById(R.id.btnUploadSchool);
        bsh = findViewById(R.id.btnUploadShelter);
        bo = findViewById(R.id.btnUploadOthers);
        tvh = findViewById(R.id.textHospi);
        tvsc = findViewById(R.id.textSchool);
        tvsh = findViewById(R.id.textShelter);
        tvo = findViewById(R.id.textOthers);

        pd = new ProgressDialog(UploadActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Uploading data...");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,FILE);
        }else{
            lh.setVisibility((loadFiles("hospital"))? View.VISIBLE:View.INVISIBLE);
            lsc.setVisibility((loadFiles("school"))? View.VISIBLE:View.INVISIBLE);
            lsh.setVisibility((loadFiles("shelter"))? View.VISIBLE:View.INVISIBLE);
            lo.setVisibility((loadFiles("others"))? View.VISIBLE:View.INVISIBLE);
        }

        bh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                int linesc = 0;
                File mediaStorageDir=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Mapmyenv");
                File f = new File(mediaStorageDir+"/hospital.csv");
                try {
                    BufferedReader readert = new BufferedReader(new FileReader(f));
                    while (readert.readLine()!=null) linesc++;
                    readert.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    makeToast("Files are being uploaded ...");
                    BufferedReader reader = new BufferedReader(new FileReader(f));
                    String line= "";
                    for(int i=0;i<linesc;i++){
                        line = reader.readLine();
                        pd.show();
                        UploadFileToServer up = new UploadFileToServer();
                        String[] attr = line.split(",");
                        up.lines = linesc;
                        up.username = attr[0];
                        up.title = attr[1];
                        up.category = attr[2];
                        up.description = attr[3];
                        up.latitude = attr[4];
                        up.longitude = attr[5];
                        up.imageFile = new File(attr[6]);
                        up.execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        bsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                int linesc = 0;
                File mediaStorageDir=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Mapmyenv");
                File f = new File(mediaStorageDir+"/school.csv");
                try {
                    BufferedReader readert = new BufferedReader(new FileReader(f));
                    while (readert.readLine()!=null) linesc++;
                    readert.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    makeToast("Files are being uploaded ...");
                    BufferedReader reader = new BufferedReader(new FileReader(f));
                    String line= "";
                    for(int i=0;i<linesc;i++){
                        line = reader.readLine();
                        pd.show();
                        UploadFileToServer up = new UploadFileToServer();
                        String[] attr = line.split(",");
                        up.lines = linesc;
                        up.username = attr[0];
                        up.title = attr[1];
                        up.category = attr[2];
                        up.description = attr[3];
                        up.latitude = attr[4];
                        up.longitude = attr[5];
                        up.imageFile = new File(attr[6]);
                        up.execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        bsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                int linesc = 0;
                File mediaStorageDir=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Mapmyenv");
                File f = new File(mediaStorageDir+"/shelter.csv");
                try {
                    BufferedReader readert = new BufferedReader(new FileReader(f));
                    while (readert.readLine()!=null) linesc++;
                    readert.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    makeToast("Files are being uploaded ...");
                    BufferedReader reader = new BufferedReader(new FileReader(f));
                    String line= "";
                    for(int i=0;i<linesc;i++){
                        line = reader.readLine();
                        pd.show();
                        UploadFileToServer up = new UploadFileToServer();
                        String[] attr = line.split(",");
                        up.lines = linesc;
                        up.username = attr[0];
                        up.title = attr[1];
                        up.category = attr[2];
                        up.description = attr[3];
                        up.latitude = attr[4];
                        up.longitude = attr[5];
                        up.imageFile = new File(attr[6]);
                        up.execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        bo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                int linesc = 0;
                File mediaStorageDir=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Mapmyenv");
                File f = new File(mediaStorageDir+"/others.csv");
                try {
                    BufferedReader readert = new BufferedReader(new FileReader(f));
                    while (readert.readLine()!=null) linesc++;
                    readert.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    makeToast("Files are being uploaded ...");
                    BufferedReader reader = new BufferedReader(new FileReader(f));
                    String line= "";
                    for(int i=0;i<linesc;i++){
                        line = reader.readLine();
                        pd.show();
                        UploadFileToServer up = new UploadFileToServer();
                        String[] attr = line.split(",");
                        up.lines = linesc;
                        up.username = attr[0];
                        up.title = attr[1];
                        up.category = attr[2];
                        up.description = attr[3];
                        up.latitude = attr[4];
                        up.longitude = attr[5];
                        up.imageFile = new File(attr[6]);
                        up.execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public boolean loadFiles(String filename){
        File mediaStorageDir=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Mapmyenv");
        File f = new File(mediaStorageDir+"/"+filename+".csv");
        int lines = 0;
        if (f.exists()){
            try {
                BufferedReader reader = new BufferedReader(new FileReader(f));
                while (reader.readLine()!=null) lines++;
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (filename.equals("hospital")){
                tvh.setText("Number of Objects:"+lines);
            }else if (filename.equals("school")){
                tvsc.setText("Number of Objects:"+lines);
            }else if (filename.equals("shelter")){
                tvsh.setText("Number of Objects:"+lines);
            }else if (filename.equals("others")){
                tvo.setText("Number of Objects:"+lines);
            }
                return true;
        }
        return false;
    }

    private void askForPermission(String permission, Integer requestCode) {

        //Toast.makeText(this, permission, Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                case 1:

                    break;

            }

            //Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    public static String parseToString(NetworkResponse response) { String parsed; try { parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers)); } catch (UnsupportedEncodingException e) { parsed = new String(response.data); } return parsed; }
    public void makeToast(String s) {
        Toast.makeText(UploadActivity.this, s, Toast.LENGTH_LONG).show();
    }



    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        public String latitude;
        public String longitude;
        public String username;
        public String category;
        public String description;
        public String title;
        public String imageName;
        public Uri imageUri;
        public File imageFile;
        public int lines;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String s = uploadFile();
            return s;
        }

        private String uploadFile() {
            imageUri = Uri.fromFile(imageFile);
            imageName = imageFile.getName();
            final String responseString = "";
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, AppConstants.REPORT_URL, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = parseToString(response);
                    count=count+1;
                    makeToast("Successfully uploaded : "+count+" object(s)");
                    //pd.hide();
                    if (lines == count){
                        makeToast("Upload finished");
                        pd.hide();
                        File mediaStorageDir=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Mapmyenv");
                        File f = new File(mediaStorageDir+"/"+category+".csv");
                        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                        File tempFile = new File(mediaStorageDir+"/"+category+"_"+timeStamp+".csv");
                        f.renameTo(tempFile);
                        finish();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    makeToast("Uploading error"+error.getMessage());
                    pd.hide();
                    //finish();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("latitude", latitude);
                    params.put("longitude",longitude);
                    params.put("category", category);
                    params.put("description", description);
                    params.put("title", title);
                    return params;
                }

                @Override
                protected Map<String,DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("fileToUpload",new DataPart(imageName,convertImageToByte(imageUri)));
                    return params;
                }
            };
            multipartRequest.setRetryPolicy(new  DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(multipartRequest);
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

    }
    public byte[] convertImageToByte(Uri uri){
        byte[] data = null;
        try {
            ContentResolver cr = getBaseContext().getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }
}
