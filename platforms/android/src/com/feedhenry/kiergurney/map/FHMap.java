package com.feedhenry.kiergurney.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.model.TileOverlayOptions;

import org.mapsforge.android.maps.DebugSettings;
import org.mapsforge.android.maps.mapgenerator.JobParameters;
import org.mapsforge.android.maps.mapgenerator.MapGeneratorJob;
import org.mapsforge.android.maps.mapgenerator.databaserenderer.DatabaseRenderer;


import org.mapsforge.core.model.Tile;
import org.mapsforge.map.reader.MapDatabase;
import org.mapsforge.map.reader.header.FileOpenResult;
import org.mapsforge.map.reader.header.MapFileInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
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

    private DatabaseRenderer databaseRender;

    public FHMap(Context ctx) {
        this.ctx=ctx;
//        this.gMap=option.getMapFragment().getMap();
//        this.gMap.setMyLocationEnabled(true);
//        this.gMap.setMapType(GoogleMap.MAP_TYPE_NONE);

        this.mDatabase=new MapDatabase();

//        if (this.isServiceAvailable()){
//
//        }else{
//            throw new ExceptionGoogleServiceNotAvailable();
//        }
    }

    public void setMapFile(String filePath) throws ExceptionMapFileNotValid{
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

    public Bitmap getTileImg(int x ,int y , int zoom) throws ExceptionMapFileNotValid{
        if (this.mapFile==null || !this.mapFile.exists()){
            throw new ExceptionMapFileNotValid("Cannot find map file");
        }
        org.mapsforge.core.model.Tile mapforgeTile=new Tile(x,y,(byte)zoom);
        MapGeneratorJob rendererJob = new MapGeneratorJob(mapforgeTile,
                this.mapFile,
                new JobParameters(RenderTheme.INSTANCE,1),
                new DebugSettings(false,false,false));

        Bitmap tileBitmap =Bitmap.createBitmap((int)(org.mapsforge.core.model.Tile.TILE_SIZE), (int)(org.mapsforge.core.model.Tile.TILE_SIZE), Bitmap.Config.ARGB_8888);
        DatabaseRenderer mapGenerator = new DatabaseRenderer(this.mDatabase);
        mapGenerator.executeJob(rendererJob,tileBitmap);
        return tileBitmap;
    }
    public String getTileBase64ImgUrl(int x ,int y , int zoom) throws ExceptionMapFileNotValid{
        Bitmap tileImg=this.getTileImg(x,y,zoom);
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        tileImg.compress(Bitmap.CompressFormat.PNG,100,bos);
        byte[] bytArr=bos.toByteArray();
        String imgUrlHead="data:image/png;base64,";
        return imgUrlHead+Base64.encodeToString(bytArr,Base64.DEFAULT);
    }

}
