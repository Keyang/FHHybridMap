package com.feedhenry.kiergurney.map;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.model.TileOverlayOptions;


import com.feedhenry.kiergurney.NativeMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.mapsforge.android.maps.DebugSettings;

import org.mapsforge.android.maps.mapgenerator.InMemoryTileCache;
import org.mapsforge.android.maps.mapgenerator.JobParameters;
import org.mapsforge.android.maps.mapgenerator.MapGeneratorJob;
import org.mapsforge.android.maps.mapgenerator.TileCache;
import org.mapsforge.android.maps.mapgenerator.databaserenderer.DatabaseRenderer;

import org.mapsforge.core.Tile;
import org.mapsforge.map.reader.MapDatabase;
import org.mapsforge.map.reader.header.MapFileInfo;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;


/**
 * Created by kxiang on 21/02/2014.
 */
public class FHMap {
//    public GoogleMap getgMap() {
//        return gMap;
//    }
//
//    private GoogleMap gMap;
    private Context ctx;
    private File mapFile;
    private MapDatabase mDatabase;
    private TileCache memCache;
    private TileCache diskCache;
    private DatabaseRenderer databaseRender;
    private int DEFAULT_TILE_CACHE_SIZE_IN_MEMORY=30;
    private int DEFAULT_TILE_CACHE_SIZE_IN_DISK=3000; //each tile will take about 131KB
    private String mapPath=Environment.DIRECTORY_DOWNLOADS;
    public FHMap(Context ctx) {
        this.ctx=ctx;
//        this.gMap=option.getMapFragment().getMap();
//        this.gMap.setMyLocationEnabled(true);
//        this.gMap.setMapType(GoogleMap.MAP_TYPE_NONE);

        this.mDatabase=new MapDatabase();
        this.memCache=new InMemoryTileCache(DEFAULT_TILE_CACHE_SIZE_IN_MEMORY);
        this.diskCache=new FileSystemTileCache(DEFAULT_TILE_CACHE_SIZE_IN_DISK,9999);
        diskCache.setPersistent(true);

//        if (this.isServiceAvailable()){
//
//        }else{
//            throw new ExceptionGoogleServiceNotAvailable();
//        }
    }
    private String getFullDirPath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +mapPath+ "/";
    }
    public void setMapFile(String fileName) throws ExceptionMapFileNotValid{
        String filePath=getFullDirPath()+fileName;
        this.closeFile();
        this.mapFile=new File(filePath);
        if (mapFile.exists()){
            this.mDatabase.openFile(this.mapFile);
        }else{
            throw new ExceptionMapFileNotValid("Map file not found for:"+filePath);
        }


    }

//    public void render(File mapFile, FHMapOption option) throws ExceptionMapFileNotValid{
//        this.clearMap();
//        FileOpenResult fileOpenResult=mDatabase.openFile(mapFile);
//        if (fileOpenResult.isSuccess()){
//            //render map
//            this.getgMap().addTileOverlay(new TileOverlayOptions().tileProvider(new MapsForgeTileProvider(mDatabase,mapFile)));
//            if (option.isShowMapMetaInfo()){
//                this.showMapMetaInfo();
//            }
//        }else{
//            throw new ExceptionMapFileNotValid(fileOpenResult.getErrorMessage());
//        }
//    }
//    public void showMapMetaInfo(){
//        MapFileInfo info=mDatabase.getMapFileInfo();
//        String text="Map file Version: "+info.fileVersion;
//        text+="\nMap Date: "+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(info.mapDate));
//        text+="\nMap Size: "+info.fileSize;
//        text+="\nTile Pixel Size:"+info.tilePixelSize;
//        text+="\nMap Bound: "+info.boundingBox.toString();
////        this.option.getMetaInfoTextView().setText(text);
//
//
//    }
//    public void hideMapMetaInfo(){
//        if (this.option.getMetaInfoTextView()!=null){
//            this.option.getMetaInfoTextView().setText("");
//        }
//    }
//    private void clearMap(){
//        this.getgMap().clear();
//        this.mapFile=null;
//        if (mDatabase.hasOpenFile()){
//            mDatabase.closeFile();
//        }
//
//    }
    public void closeFile(){
        this.mapFile=null;
        if (mDatabase.hasOpenFile()){
            mDatabase.closeFile();
        }
    }


//    public boolean isServiceAvailable(){
//        int servicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.ctx);
//        if(servicesAvailable == ConnectionResult.SUCCESS) {
//            return true;
//        }
//        return false;
//    }

    private  Bitmap getTileImg(int x ,int y , int zoom) throws ExceptionMapFileNotValid{
        if (this.mapFile==null || !this.mapFile.exists()){
            throw new ExceptionMapFileNotValid("Cannot find map file");
        }
        Tile mapforgeTile=new Tile(x,y,(byte)zoom);
        Bitmap tileBitmap;
        MapGeneratorJob rendererJob = new MapGeneratorJob(mapforgeTile,
                this.mapFile,
                new JobParameters(RenderTheme.INSTANCE,1f),
                new DebugSettings(false,false,false));

       if (diskCache.containsKey(rendererJob)){
            tileBitmap=diskCache.get(rendererJob);

        }else{
            tileBitmap=Bitmap.createBitmap(Tile.TILE_SIZE,Tile.TILE_SIZE, Bitmap.Config.RGB_565);
            DatabaseRenderer mapGenerator = new DatabaseRenderer();
            mapGenerator.setMapDatabase(this.mDatabase);
            try{
                mapGenerator.executeJob(rendererJob,tileBitmap);
                Log.d("performance", "" + tileBitmap.getWidth()+" h:"+tileBitmap.getHeight());
                memCache.put(rendererJob, tileBitmap);
                diskCache.put(rendererJob,tileBitmap);
            }catch (Exception e){
                //Map generating corruptted. it will attempt to generate next time.
            }

        }



        return tileBitmap;
    }

    public String getTileBase64ImgUrl(int x ,int y , int zoom) throws ExceptionMapFileNotValid{

        Date d1=new Date();
        Bitmap tileImg=this.getTileImg(x,y,zoom);
        Log.d("performance", ((new Date()).getTime() - d1.getTime()) + "ms");
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        tileImg.compress(Bitmap.CompressFormat.PNG,0,bos);
        byte[] bytArr=bos.toByteArray();
//        byte[] bytArr=bb.array();
//        Log.d("performance",bytArr.length+" byte");
        String imgUrlHead="data:image/png;base64,";
        try {
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgUrlHead+Base64.encodeToString(bytArr,Base64.DEFAULT);
    }
    public void destroy(){
        this.diskCache.destroy();
        this.memCache.destroy();
        Log.d("performance","Destroyed");
    }

    public void openNativeMap(){
        Intent intent=new Intent(this.ctx,NativeMap.class);
        this.ctx.startActivity(intent);
    }
    public void downloadMap(String mapUrl, String fileName){
        DownloadManager.Request r = new DownloadManager.Request(Uri.parse(mapUrl));

        // This put the download in the same Download dir the browser uses
        r.setDestinationInExternalPublicDir(mapPath, fileName);

        // Notify user when download is completed
        // (Seems to be available since Honeycomb only)
        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // Start download
        DownloadManager dm = (DownloadManager) this.ctx.getSystemService(this.ctx.DOWNLOAD_SERVICE);
        dm.enqueue(r);
    }

    public void config(int diskCacheSize){
        this.diskCache.setCapacity(diskCacheSize);
    }
    public boolean mapExists(String fileName){
        File file=new File(getFullDirPath()+fileName);
        return file.exists();
    }
    public JSONObject mapInfo(String fileName) throws ExceptionMapFileNotValid,JSONException{
        if (!mapExists(fileName)){
            throw new ExceptionMapFileNotValid("Map does not exist:"+fileName);
        }else{
            File file=new File(getFullDirPath()+fileName);
            MapDatabase md=new MapDatabase();
            md.openFile(file);
            MapFileInfo info=md.getMapFileInfo();
            JSONObject res=new JSONObject();
            res.put("fileVersion", info.fileVersion);
            res.put("boundingBox",info.boundingBox.toString());
            res.put("fileSize",info.fileSize);
            res.put("mapCenter",info.mapCenter.toString());
            res.put("tilePixelSize",info.tilePixelSize);
            res.put("mapDate",info.mapDate);
            return res;
        }

    }

}
