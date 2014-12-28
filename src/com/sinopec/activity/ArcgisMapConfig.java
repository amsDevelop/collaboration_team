package com.sinopec.activity;

import android.content.Context;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;

/**
 * Created by scenic on 2014/12/28.
 */
public class ArcgisMapConfig {

    //<!-- 基础地理 -->
    public static String url_marine_geo =
            "http://10.225.14.204/arcgis/rest/services/marine_geo/MapServer";
    //<!-- 盆地-->
    public static String url_basin =
            "http://10.225.14.204/arcgis/rest/services/basin/MapServer";
    //<!-- 油田-->
    public static String url_oilfields =
            "http://10.225.14.204/arcgis/rest/services/Oil_field/MapServer";
    //<!-- 盖层 -->
    public static String url_cover =
            " http://10.225.14.204/arcgis/rest/services/cover/MapServer";
    //<!-- 烃源岩 -->
    public static String url_source_rock =
            " http://10.225.14.204/arcgis/rest/services/source_rock/MapServer";
    //<!-- 储集层 -->
    public static String url_reservoir =
            " http://10.225.14.204/arcgis/rest/services/reservoir/MapServer";
    //<!-- 卫星图 -->
    public static String url_marine_image =
            "http://10.225.14.204/arcgis/rest/services/marine_image/MapServer";


    //<!-- 盆地查询url-->
    public static String url_basin_4search =
            "http://10.225.14.204/arcgis/rest/services/marine_oil/MapServer/10";


    // <!-- 气田-->
    public static String url_gasfields =
            "http://10.225.14.204/arcgis/rest/services/Gas_field/MapServer";


    //<!-- 油田查询url-->
    public static String url_oilfields_4search =
            "http://10.225.14.204/arcgis/rest/services/marine_oil/MapServer/8";
    //<!-- 气田查询url-->
    public static String url_gasfields_4search =
            "http://10.225.14.204/arcgis/rest/services/marine_oil/MapServer/9";


    // <!-- 盖层 -->
    public static String url_cover_4search =
            " http://10.225.14.204/arcgis/rest/services/marine_oil/MapServer/4";
    // <!-- 烃源岩 -->
    public static String url_source_rock_4search =
            "http://10.225.14.204/arcgis/rest/services/marine_oil/MapServer/5";
    // <!-- 储集层 -->
    public static String url_reservoir_4search =
            "http://10.225.14.204/arcgis/rest/services/marine_oil/MapServer/6";


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
                url_basin,
                url_oilfields,
                url_cover,
                url_source_rock,
                url_reservoir,
                url_marine_image};
        for (int i = 0; i < urls.length; i++) {
            ArcGISTiledMapServiceLayer layer = new ArcGISTiledMapServiceLayer(urls[i]);
            arcgisMap.addLayer(layer);
        }
    }
}
