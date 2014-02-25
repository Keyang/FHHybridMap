package com.feedhenry.kiergurney;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.feedhenry.kiergurney.map.*;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kxiang on 24/02/2014.
 */
public class FHMapPlugin extends CordovaPlugin {
    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("setMapFile")){
            String fileName=args.getString(0);


            try {
                FHMap.FHMapInstance.setMapFile(fileName);
                callbackContext.success();
            } catch (ExceptionMapFileNotValid exceptionMapFileNotValid) {
                exceptionMapFileNotValid.printStackTrace();
                String msg=exceptionMapFileNotValid.getLocalizedMessage();
                callbackContext.error(msg);
            }
            return true;
        }else if (action.equals("getMapTile")){
                AsyncTask asyncTask = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        try {
                        int x=args.getInt(0);
                        int y=args.getInt(1);
                        int zoom=args.getInt(2);
                        String base64Img= null;

                        base64Img = FHMap.FHMapInstance.getTileBase64ImgUrl(x,y,zoom);
                        JSONObject rtn=new JSONObject();
                        rtn.put("base64Img",base64Img);

                        callbackContext.success(rtn);
                        } catch (ExceptionMapFileNotValid exceptionMapFileNotValid) {
                            exceptionMapFileNotValid.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        return null;
                    }
                };

                asyncTask.executeOnExecutor(MapWorkerManager.getInstance().getThreadPool());

            return true;
        }else if (action.equals("openNativeMap")){
            FHMap.FHMapInstance.openNativeMap();
            return true;
        }else if (action.equals("config")){
            int diskCacheSize=args.getInt(0);
            FHMap.FHMapInstance.config(diskCacheSize);
            return true;
        }else if (action.equals("downloadMap")){
            String mapUrl=args.getString(0);
            String fileName=args.getString(1);
            FHMap.FHMapInstance.downloadMap(mapUrl,fileName);
            return true;
        }else if (action.equals("mapExists")){
            String mapName=args.getString(0);
            Boolean exists=FHMap.FHMapInstance.mapExists(mapName);
            JSONObject result=new JSONObject();
            if (exists){
                result.put("result",true);

            }else{
                result.put("result",false);
            }
            callbackContext.success(result);
            return true;
        }else if (action.equals("mapInfo")){
            String mapName=args.getString(0);
            try {
                JSONObject res=FHMap.FHMapInstance.mapInfo(mapName);
                callbackContext.success(res);
            } catch (ExceptionMapFileNotValid exceptionMapFileNotValid) {
                exceptionMapFileNotValid.printStackTrace();
                String msg=exceptionMapFileNotValid.getLocalizedMessage();
                callbackContext.error(msg);
            }
            return true;
        }

        return false;
    }
}

