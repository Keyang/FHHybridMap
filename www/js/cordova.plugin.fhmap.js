var fhmap=(function(module){
  module.setMapFile=setMapFile;
  module.getMapTile=getMapTile;
  module.openNativeMap=openNativeMap;

  var service="FHMapPlugin";
  var actions={
    "setMapFile":"setMapFile",
    "getMapTile":"getMapTile",
    "openNativeMap":"openNativeMap",
    "config":"config",
    "downloadMap":"downloadMap",
    "mapExists":"mapExists",
    "mapInfo":"mapInfo"
  }
  function config(param,cb){
    _cordova(_cordovaParam(actions["config"],cb,[param.cacheSize]));
  }
  function downloadMap(param,cb){
    _cordova(_cordovaParam(actions["downloadMap"],cb,[param.mapUrl,param.fileName]));
  }
  function mapExists(param,cb){
    _cordova(_cordovaParam(actions["mapExists"],cb,[param.fileName]));
  }
  function mapInfo(param,cb){
    _cordova(_cordovaParam(actions["mapInfo"],cb,[param.fileName]));
  }
  function setMapFile(param,cb){
    _cordova(_cordovaParam(actions["setMapFile"],cb,[param.fileName]));
  }
  function openNativeMap(param,cb){
    _cordova(_cordovaParam(actions["openNativeMap"],cb));
  }
  function getMapTile(param,cb){
    _cordova(_cordovaParam(actions["getMapTile"],cb,[param.x,param.y,param.zoom]));
  }
  function _cordovaParam(action,cb,paramArr){
    return {
      "action":action,
      "cb":cb,
      "paramArr":paramArr || []
    }
  }
  function _cordova(params){
    if (typeof cordova =="undefined"){
      throw ("Cordova is not defined");
    }else{
      var action=params.action;
      var sucCb=function(res){params.cb(null,res)};
      var failCb=params.cb;
      var extraParamArr=params.paramArr;
      cordova.exec(sucCb,failCb,service,action,extraParamArr);
    }
  }

  return module;
})(fhmap || {});