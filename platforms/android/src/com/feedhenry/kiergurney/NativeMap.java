package com.feedhenry.kiergurney;

import android.os.Bundle;

import com.feedhenry.kiergurney.*;
import com.feedhenry.kiergurney.FHMap;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.core.GeoPoint;

import java.io.File;

/**
 * Created by kxiang on 25/02/2014.
 */
public class NativeMap extends MapActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapView mapView = new MapView(this);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setMapFile(new File("/sdcard/Download/ireland.map"));

        setContentView(mapView);
        mapView.setCenter(new GeoPoint(53360387,-6262307));

    }
}
