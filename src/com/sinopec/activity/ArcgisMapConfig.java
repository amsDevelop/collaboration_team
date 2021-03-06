package com.sinopec.activity;

import android.content.Context;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;

/**
 * Created by scenic on 2014/12/28.
 */
public class ArcgisMapConfig {

//    public static String rootIp = "10.225.14.204";
//    http://202.204.193.201:8080/peprisapi/basinAttribute.html?basinId=200700000120

//    public static String commonUrl = "http://202.204.193.201:8080/arcgis/rest/";

    public static String STATISTICS_IP = "http://10.225.14.203:8080/";


    public static String commonUrl = "http://10.225.14.204/arcgis/rest/";

    //<!-- 基础地理 -->
    public static String url_marine_geo =
            commonUrl + "services/marine_geo/MapServer";
    //<!-- 盆地-->
    public static String url_basin =
            commonUrl + "services/basin/MapServer";
    //<!-- 油田-->
    public static String url_oilfields =
            commonUrl + "services/Oil_field/MapServer";

/*    //<!-- 盖层 -->
>>>>>>> Stashed changes
    public static String url_cover =
            commonUrl + "services/cover/MapServer";
    //<!-- 烃源岩 -->
    public static String url_source_rock =
            commonUrl + "services/source_rock/MapServer";
    //<!-- 储集层 -->
    public static String url_reservoir =
            commonUrl + "services/reservoir/MapServer";*/
    //<!-- 卫星图 -->
    public static String url_marine_image =
            commonUrl + "services/marine_image/MapServer";


    //<!-- 盆地查询url-->
    public static String url_basin_4search =
            commonUrl + "services/marine_oil/MapServer/9";


    // <!-- 气田-->
    public static String url_gasfields =
            commonUrl + "services/Gas_field/MapServer";


    //<!-- 油田查询url-->
    public static String url_oilfields_4search =
            commonUrl + "services/marine_oil/MapServer/7";
    //<!-- 气田查询url-->
    public static String url_gasfields_4search =
            commonUrl + "services/marine_oil/MapServer/6";


    // <!-- 盖层 -->
    public static String url_cover_4search =
            commonUrl + " http://10.225.14.204/arcgis/rest/services/marine_oil/MapServer/4";
    // <!-- 烃源岩 -->
    public static String url_source_rock_4search =
            commonUrl + "services/marine_oil/MapServer/5";
    // <!-- 储集层 -->
    public static String url_reservoir_4search =
            commonUrl + "services/marine_oil/MapServer/6";

    Context context;
    MapView arcgisMap;

    ArcgisMapConfig(Context context, MapView map) {
        this.context = context;
        arcgisMap = map;
    }

    public void onCreate() {
        // 加入6个专题图层
//        String[] urls = context.getResources().getStringArray(R.array.all_layer_urls);
        String[] urls = {
                url_marine_geo,
                url_marine_image,
                url_basin,
                url_oilfields,
                url_marine_image,
                url_gasfields
        };
        String defaultShow = url_marine_geo;
        for (int i = 0; i < urls.length; i++) {
            ArcGISDynamicMapServiceLayer layer = new ArcGISDynamicMapServiceLayer(urls[i]);
            arcgisMap.addLayer(layer);
            if (!urls[i].equals(defaultShow)) {
                layer.setVisible(false);
            }
        }
    }
}
