package com.feedhenry.kiergurney;

import android.os.Environment;

import com.feedhenry.kiergurney.map.*;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kxiang on 24/02/2014.
 */
public class FHMapPlugin extends CordovaPlugin {
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("setMapFile")){
            String fileName=args.getString(0);
            String filePath= Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Environment.DIRECTORY_DOWNLOADS+ "/"+fileName;

            try {
                FHMap.FHMapInstance.setMapFile(filePath);
                callbackContext.success();
            } catch (ExceptionMapFileNotValid exceptionMapFileNotValid) {
                exceptionMapFileNotValid.printStackTrace();
                String msg=exceptionMapFileNotValid.getLocalizedMessage();
                callbackContext.error(msg);
            }
            return true;
        }else if (action.equals("getMapTile")){
           int x=args.getInt(0);
           int y=args.getInt(1);
           int zoom=args.getInt(2);
            try {
                String base64Img= FHMap.FHMapInstance.getTileBase64ImgUrl(x,y,zoom);
                JSONObject rtn=new JSONObject();
                rtn.put("base64Img",base64Img);
                callbackContext.success(rtn);
            } catch (ExceptionMapFileNotValid exceptionMapFileNotValid) {
                exceptionMapFileNotValid.printStackTrace();
                callbackContext.error(exceptionMapFileNotValid.getLocalizedMessage());
            }
            return true;
        }

        return false;
    }
}

