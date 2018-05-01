package com.manichan.appname;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.manichan.appname.HomeActivity;

import org.osmdroid.api.IMapView;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

/**
 * Created by manichan on 4/9/17.
 */

public class MapItemizedOverlay extends ItemizedOverlay<OverlayItem> {
    private Context context;
    private ArrayList<OverlayItem> overlayItems = new ArrayList<OverlayItem>();

    public MapItemizedOverlay(Drawable pDefaultMarker, Context context) {
        super(pDefaultMarker);
        this.context = context;
    }

    public void addOverlay(OverlayItem overlayItem) {
        overlayItems.add(overlayItem);
        populate();
    }

    @Override
    protected OverlayItem createItem(int i) {
        return overlayItems.get(i);
    }

    @Override
    public int size() {
        return overlayItems.size();
    }

    @Override
    public boolean onSnapToItem(int x, int y, Point snapPoint, IMapView mapView) {
        return false;
    }

    public void removeAll() {
        overlayItems.clear();
    }

    protected boolean onTap(int index) {
        return true;
    }
}
