var fhmap=(function(module){
  module.setMapFile=setMapFile;
  module.getMapTile=getMapTile;
  module.openNativeMap=openNativeMap;

  var service="FHMapPlugin";
  var actions={
    "setMapFile":"setMapFile",
    "getMapTile":"getMapTile",
    "openNativeMap":"openNativeMap"
  }
  function setMapFile(param,cb){
    var fileName=param.fileName;
    var action=actions["setMapFile"];
    var params=_cordovaParam(action,cb,[fileName]);
    _cordova(params);
  }
  function openNativeMap(param,cb){
    var params=_cordovaParam(actions["openNativeMap"],cb,[]);
    _cordova(params);
  }
  function getMapTile(param,cb){
    var params=_cordovaParam(actions["getMapTile"],cb,[param.x,param.y,param.zoom]);
    _cordova(params);
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