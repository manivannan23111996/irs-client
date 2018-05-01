package com.manichan.appname;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.manichan.appname.BuildConfig;
import com.manichan.appname.AppConstants;
import com.manichan.appname.AppController;
import com.manichan.appname.VolleyMultipartRequest;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

public class AddActivity extends AppCompatActivity {
    public static double lati=0,longi=0;
    public static float accu=0;
    Intent reportIntent;
    public static String latitude;
    public static String longitude;
    public static String username;
    public static String category;
    public static String description;
    public static String title;
    public static String fileName;
    public static String filePathCompressed;
    private Uri imageUri;
    File mediaFile;
    private Uri imageUriCompressed;
    private String filePath = null;
    long totalSize = 0;
    Button btnCamera,btnsend,btnCancel;
    EditText descriptionEdit,titleEdit;
    TextView gpsAccuracy;
    Spinner categorySpin;
    public ProgressDialog pd;
    SharedPreferences sharedPreferences;
    int CAMERA_PERMISSION=0;
    String fileNameCompressed;
    static final Integer CAMERA = 0x5;
    static final Integer FILE = 0x1;
    static final int CAMERA_CODE = 12;
    int GPS_ACCESS_FINE_LOCATION_PERMISSION=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setTitle("Report form");
        try{
            sharedPreferences = getSharedPreferences("login_details",0);
            AppConstants.username = sharedPreferences.getString("username","error saving username");
        }catch (Exception e){

        }
        //test
        //new UploadFileToServer().execute();
        gpsAccuracy = findViewById(R.id.gps_accuracy);
        descriptionEdit =(EditText) findViewById(R.id.problem_desc);
        titleEdit = (EditText) findViewById(R.id.problem_name);
        categorySpin = (Spinner) findViewById(R.id.spinner_category);
        btnCamera = (Button) findViewById(R.id.btn_capture_image);
        btnsend = (Button) findViewById(R.id.btn_send_report);
        btnCancel=(Button) findViewById(R.id.btn_report_cancel);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnsend.setVisibility(View.INVISIBLE);
        GPSTracker gpsTracker = new GPSTracker(AddActivity.this);
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = descriptionEdit.getText().toString();
                title = titleEdit.getText().toString();
                String categoryName = categorySpin.getSelectedItem().toString();
                category = getTableName(categoryName);
                username=AppConstants.username;
                if (categorySpin.getSelectedItemPosition()==0){
                    makeToast("Select a category");
                }else if(title.equals("")){
                    makeToast("Enter a name");
                }else if(imageUri==null){
                    makeToast("Take a photo");
                }else{
                    try {
                        saveToCSV();
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //makeToast("Username: "+username+" \n"+"Title: "+title+" \n"+"Description: "+description+" \n"+"Category: "+category+" \n"+"FileName: "+fileNameCompressed+" \n");
                }
            }
        });
        if (!isDeviceSupportCamera()){
            makeToast("Your device doesn't support camera");
            finish();
        }

    }
    public String getTableName(String name){
        String cate="";
        switch (name){
            case "Hospital":
                cate="hospital";
                break;
            case "School":
                cate="school";
                break;
            case "Shelter":
                cate="shelter";
                break;
            case "Others":
                cate="others";
                break;
        }
        return cate;
    }
    public void makeToast(String s) {
        Toast.makeText(AddActivity.this, s, Toast.LENGTH_LONG).show();
    }

    private boolean isDeviceSupportCamera(){
        if (AddActivity.this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        }else{
            return false;
        }
    }
    private void captureImage(){
        askForPermission(Manifest.permission.CAMERA,CAMERA);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", imageUri);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savesState){
        super.onRestoreInstanceState(savesState);
        imageUri = savesState.getParcelable("file_uri");
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if (requestCode==CAMERA_CODE){
            if (resultCode==RESULT_OK){
                Bitmap tempBitmap= BitmapFactory.decodeFile(mediaFile.getPath());
                try {
                    imageUriCompressed = FileProvider.getUriForFile(AddActivity.this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            new File(storeImage(tempBitmap)));
                    //makeToast("\n"+imageUriCompressed.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = getResizedBitmap(tempBitmap,150,150);
                ImageView i=(ImageView) findViewById(R.id.image_preview);
                i.setImageBitmap(bitmap);
            }else if (resultCode==RESULT_CANCELED){
                makeToast("Cancelled");
            }else{
                makeToast("Failed to capture");
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                case 1:
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    imageUri =getOutputMediaFile();
                    if (imageUri!=null){
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, CAMERA_CODE);
                    }
                    break;
                case 3:
                    break;
                case 4:
                    Intent imageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(imageIntent, 11);
                    break;
                case 5:
                    Intent intentu = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    imageUri =getOutputMediaFile();
                    if (imageUri!=null){
                        intentu.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intentu, CAMERA_CODE);
                    }
                    break;

            }

            //Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
    public String storeImage(Bitmap bitmap) throws IOException {
        File mediaStorageDir=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Mapmyenv");
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile=new File(mediaStorageDir.getPath()+File.separator+"IMG_"+timeStamp+"compressed.jpg");
        fileNameCompressed = "IMG_"+timeStamp+"compressed.jpg";
        filePathCompressed = mediaFile.getPath();
        OutputStream fOut = null;
        mediaFile.createNewFile();
        fOut = new FileOutputStream(mediaFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,fOut);
        fOut.flush();
        fOut.close();
        MediaStore.Images.Media.insertImage(AddActivity.this.getContentResolver(), mediaFile.getAbsolutePath(), mediaFile.getName(), mediaFile.getName());
        return  mediaFile.getAbsolutePath();
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
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageUri =getOutputMediaFile();
            if (imageUri!=null){
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, CAMERA_CODE);
            }
        }
    }
    private  Uri getOutputMediaFile(){
        File mediaStorageDir=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Mapmyenv");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,FILE);
            return null;
        }else{
            if (!mediaStorageDir.exists()){
                if (!mediaStorageDir.mkdirs()){
                    return null;
                }
            }
        }

        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        mediaFile=new File(mediaStorageDir.getPath()+File.separator+"IMG_"+timeStamp+".jpg");
        fileName = "IMG_"+timeStamp+".jpg";
        Toast.makeText(this, ""+mediaFile.getPath(), Toast.LENGTH_SHORT).show();
        return FileProvider.getUriForFile(AddActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",
                mediaFile);
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public static String parseToString(NetworkResponse response) { String parsed; try { parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers)); } catch (UnsupportedEncodingException e) { parsed = new String(response.data); } return parsed; }
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


    public void saveToCSV() throws IOException {
        {

            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Mapmyenv");

            boolean var = false;
            if (!folder.exists())
                var = folder.mkdir();

            System.out.println("" + var);


            final String filename = folder.toString() + "/" +category+".csv";

            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {




                }
            };

            new Thread() {
                public void run() {
                    try {
                        FileWriter fw = new FileWriter(filename,true);

                        fw.append(username);
                        fw.append(',');

                        fw.append(title);
                        fw.append(',');

                        fw.append(category);
                        fw.append(',');

                        fw.append(description);
                        fw.append(',');

                        fw.append(Double.toString(lati));
                        fw.append(',');

                        fw.append(Double.toString(longi));
                        fw.append(',');

                        fw.append(filePathCompressed);

                        fw.append('\n');

                        // fw.flush();
                        fw.close();

                    } catch (Exception e) {
                    }
                    handler.sendEmptyMessage(0);
                }
            }.start();

        }

    }





    public class GPSTracker implements LocationListener {
        LocationManager locationManager;
        boolean isGPSEnabled = false;
        boolean canGetLocation = false;
        private final Context mContext;
        Location location;
        double longitude;
        double latitude;

        public GPSTracker(Context mContext) {
            this.mContext = mContext;
            if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AddActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},GPS_ACCESS_FINE_LOCATION_PERMISSION);
                }else{
                    getLocation();
                    //Toast.makeText(mContext,latitude+" "+longitude+" Accuracy"+location.getAccuracy(),Toast.LENGTH_SHORT).show();

                    lati=latitude;
                    longi=longitude;
                    gpsAccuracy.setText("GPS Accuracy:"+location.getAccuracy());
                    accu = location.getAccuracy();
                    if (accu<10){
                        btnsend.setVisibility(View.VISIBLE);
                    }else{
                        btnsend.setVisibility(View.INVISIBLE);
                    }
                }
            }else{
                getLocation();
                //Toast.makeText(mContext,latitude+" "+longitude+" Accuracy"+location.getAccuracy(),Toast.LENGTH_SHORT).show();

                lati=latitude;
                longi=longitude;
                gpsAccuracy.setText("GPS Accuracy:"+location.getAccuracy());
                accu = location.getAccuracy();
                if (accu<10){
                    btnsend.setVisibility(View.VISIBLE);
                }else{
                    btnsend.setVisibility(View.INVISIBLE);
                }
            }
        }

        public Location getLocation() throws SecurityException {
            try {

                locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (!isGPSEnabled) {
                    showSettingsAlert();
                } else {
                    this.canGetLocation = true;
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 3, this);
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }else {

                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return location;
        }

        public void showSettingsAlert(){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("GPS is settings");
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });

            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();

        }

        @Override
        public void onLocationChanged(Location location) {
            if (location!=null){
                latitude=location.getLatitude();
                longitude=location.getLongitude();
                lati=latitude;
                longi=longitude;
                gpsAccuracy.setText("GPS Accuracy:"+location.getAccuracy());
                accu = location.getAccuracy();
                if (accu<10){
                    btnsend.setVisibility(View.VISIBLE);
                }else{
                    btnsend.setVisibility(View.INVISIBLE);
                }
            }
            //Toast.makeText(mContext,latitude+" "+longitude+" Accuracy"+location.getAccuracy(),Toast.LENGTH_SHORT).show();

            lati=latitude;
            longi=longitude;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
