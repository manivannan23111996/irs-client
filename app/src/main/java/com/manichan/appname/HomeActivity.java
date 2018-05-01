package com.manichan.appname;

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
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SessionManager sessionManager;
    public static MapView map;
    public IMapController mapController;
    Button btnZoomIn, btnZoomOut;
    double lati=0,longi=0;
    int GPS_ACCESS_FINE_LOCATION_PERMISSION=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initializeActivity();
        try{
            setUpMap();
            GPSTracker gps = new GPSTracker(HomeActivity.this);
        }catch(Exception e){

        }
    }


    public void initializeActivity() {
        btnZoomIn = (Button) findViewById(R.id.btn_zoom_in);
        btnZoomOut = (Button) findViewById(R.id.btn_zoom_out);
        btnZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapController.zoomIn();
            }
        });
        btnZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapController.zoomOut();
            }
        });
        sessionManager = new SessionManager(getApplicationContext());
        if (!sessionManager.isLoggedIn()  &&  AppConstants.IS_GUEST==false) {
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLoggedIn()){
            String tag_string_request = "update_token";
            SharedPreferences sharedPreferences = getSharedPreferences("login_details",0);
            final String username = sharedPreferences.getString("username",null);
            SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
            final String token = pref.getString("regId", null);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.UPDATE_TOKEN_URL,new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {

                }
            },new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("firetoken",token);
                    params.put("username",username);
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(stringRequest,tag_string_request);
        }
    }

    public void setUpMap() throws ExecutionException, InterruptedException {
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(15);
        map.setMaxZoomLevel(20);
        map.setMinZoomLevel(11);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add) {
            Intent addIntent = new Intent(getApplicationContext(), AddActivity.class);
            startActivity(addIntent);
        } else if (id == R.id.nav_upload) {
            Intent uploadIntent = new Intent(getApplicationContext(), UploadActivity.class);
            startActivity(uploadIntent);
        }else if(id == R.id.nav_logout){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
            alertDialog.setTitle("Logout");
            alertDialog.setMessage("Are you sure to logout?");
            alertDialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    sessionManager.setLogin(false);
                    SharedPreferences preferences = getSharedPreferences("login_details",0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.commit();
                    Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        } else if (id == R.id.nav_exit) {
            System.exit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * GPS TRACKER CLASS
     **/

    public class GPSTracker implements LocationListener {
        LocationManager locationManager;
        boolean isGPSEnabled = false;
        boolean canGetLocation = false;
        private final Context mContext;
        Location location;
        double longitude;
        double latitude;
        MapItemizedOverlay gpsItemizedOverlay;
        OverlayItem gpsOverlayItem;

        public GPSTracker(Context mContext) {
            this.mContext = mContext;
            if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},GPS_ACCESS_FINE_LOCATION_PERMISSION);
                }else{
                    getLocation();
                    //Toast.makeText(mContext,latitude+" "+longitude+" Accuracy"+location.getAccuracy(),Toast.LENGTH_SHORT).show();
                    GeoPoint center = new GeoPoint(latitude, longitude);
                    mapController.setCenter(center);
                    lati=latitude;
                    longi=longitude;
                    gpsItemizedOverlay=new MapItemizedOverlay(resizeImage(R.drawable.gps_icon),HomeActivity.this);
                    gpsOverlayItem=new OverlayItem("","Your GPS Location","Latitude: "+latitude
                            +"\n\nLongitude: "+longitude,new GeoPoint(latitude,longitude));
                    gpsItemizedOverlay.addOverlay(gpsOverlayItem);
                    map.getOverlays().add(gpsItemizedOverlay);
                    map.invalidate();
                }
            }else{
                getLocation();
                //Toast.makeText(mContext,latitude+" "+longitude+" Accuracy"+location.getAccuracy(),Toast.LENGTH_SHORT).show();
                GeoPoint center = new GeoPoint(latitude, longitude);
                mapController.setCenter(center);
                lati=latitude;
                longi=longitude;
                gpsItemizedOverlay=new MapItemizedOverlay(resizeImage(R.drawable.gps_icon),HomeActivity.this);
                gpsOverlayItem=new OverlayItem("","Your GPS Location","Latitude: "+latitude
                        +"\n\nLongitude: "+longitude,new GeoPoint(latitude,longitude));
                gpsItemizedOverlay.addOverlay(gpsOverlayItem);
                map.getOverlays().add(gpsItemizedOverlay);
                map.invalidate();
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
            }
            //Toast.makeText(mContext,latitude+" "+longitude+" Accuracy"+location.getAccuracy(),Toast.LENGTH_SHORT).show();
            GeoPoint center = new GeoPoint(latitude, longitude);
            mapController.setCenter(center);
            gpsItemizedOverlay.removeAll();
            gpsOverlayItem=new OverlayItem("","Your GPS Location","Latitude: "+latitude
                    +"\n\nLongitude: "+longitude,new GeoPoint(latitude,longitude));
            gpsItemizedOverlay.addOverlay(gpsOverlayItem);
            map.invalidate();
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

    public Drawable resizeImage (int imageID) {
        Bitmap bMap = BitmapFactory.decodeResource(getResources(), imageID);
        Drawable drawable = new BitmapDrawable(this.getResources(),getResizedBitmap(bMap,65,65));
        return drawable;
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

}
