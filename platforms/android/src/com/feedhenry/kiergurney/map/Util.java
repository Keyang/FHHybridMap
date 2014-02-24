package com.feedhenry.kiergurney.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.mapsforge.map.reader.header.MapFileInfo;

/**
 * Created by kxiang on 21/02/2014.
 */
public class Util {
    public static Bitmap convertMapMeta(MapFileInfo mapFileInfo){
        Bitmap bmp=emptyBitmap(50,150);
        Canvas canvas=new Canvas(bmp);
        Paint paint=new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setTextSize(12f);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Version: "+mapFileInfo.fileVersion,0,5,paint);
        return null;
    }


    public static Bitmap emptyBitmap(int w,int h){
        Bitmap.Config config= Bitmap.Config.ARGB_8888;
        return Bitmap.createBitmap(w, h, config);
    }
}
