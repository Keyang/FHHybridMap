#FH Offline Map Usage

## How it works

It uses mapsforge as map engine which produces bitmap base on Map database (.map files)

The adapter lib FHMap exposes API for downloading map, loading map file, render file etc

The Cordova Plugin (FHMapPlugin) exposes APIs above to Hybrid app

The frontend uses leaflet.js to render map tiles to map. An adapter FHTile is developed to load tiles accordingly.

## APIs

1. use fhmap.setMapFile ({"fileName":"ireland.map"},cb); to setup the map needs to render
2. follow leaflet tutorial in http://leafletjs.com/examples/quick-start.html
3. instead of using L.titleLayer(url, options) use L.fhMap(options) for map initialising.
4. Now you can use all Leaflet apis.

## Todo List

* Map downloading API
* Map listing API
* Performance issue