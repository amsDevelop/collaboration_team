package com.sinopec.util;

import java.util.HashMap;

public class RelativeUnicode {
    public static final String haixiang = "281473902968832";//海相 沉积相
    public static final String tansuanyanyan = "1095216660480";//储层岩性 碳酸盐岩
    public static final String qianhaiwuxi = "1023";//前寒武系
    public static final String zhiliuxi = "31457280";//志留系
    public static final String nipenxi = "1006632960";//泥盆系
    public static final String shitanxi = "7516192768"; //石炭系
    public static final String erdiexi = "128849018880"; //二叠系
    public static final String sandiexi = "4123168604160"; //三叠系
    public static final String zhuluoxi = "65970697666560"; //侏罗系
    public static final String baiyaxi = "492581209243648"; //白垩系
    public static final String gujinxi = "492581209243648"; //古近系
    public static final String xinjinxi = "558446353793941504"; //新近系
    public static final String aotaoxi = "1966080"; //奥陶系
    public static final String hanwuxi = "122880"; //寒武系
    public static final String youtian = "4092"; //油田
    public static final String qitian = "4096"; //气田
    public static final String youqitian = "4294967295"; //油气田
    public static final String niyangaiceng = "1"; //泥岩盖层
    public static final String gaoyanyangaiceng = "2"; //膏盐岩盖层
    public static final String tansuanyanyangaiceng = "4"; //碳酸盐岩盖层
    public static final String zhimiyangaiceng = "8"; //其它致密岩盖层
    public static final String teshugaiceng = "128"; //特殊盖层



    public static HashMap<String, String> mEnCode = new HashMap<String, String>();
    static {
        mEnCode.put("沉积体系", "cjtx");
        mEnCode.put("地层层系", "dccx");
        mEnCode.put("石油资源量规模最大值", "syzymax");
        mEnCode.put("石油资源量规模最小值", "syzymin");
        mEnCode.put("石油资源量规模（资源序列编码）", "syzybm");
        mEnCode.put("天然气资源量规模最大值", "trqzymax");
        mEnCode.put("天然气资源量规模最小值", "trqzymin");
        mEnCode.put("天然气资源量规模（资源序列编码）", "trqzybm");
        mEnCode.put("总资源量规模最大值", "zzymax");
        mEnCode.put("总资源量规模最小值", "zzymin");
        mEnCode.put("总资源量规模（资源序列编码）", "zzybm");
        mEnCode.put("石油储量规模最大值", "syclmax");
        mEnCode.put("石油储量规模最小值", "syclmin");
        mEnCode.put("石油储量规模（资源序列编码）", "syclbm");
        mEnCode.put("天然气储量规模最大值", "trqclmax");
        mEnCode.put("天然气储量规模最小值", "trqclmin");
        mEnCode.put("天然气储量规模（资源序列编码）", "trqclbm");
        mEnCode.put("总储量规模最大值", "zclmax");
        mEnCode.put("总储量规模最小值", "zclmin");
        mEnCode.put("总储量规模（资源序列编码）", "zclbm");
        mEnCode.put("面积大小最大值", "mjmax");
        mEnCode.put("面积大小最小值", "mjmin");
        mEnCode.put("油气田", "mblx=1");
        mEnCode.put("沉积相", "cjx");
        mEnCode.put("地层层系", "dccx");
        mEnCode.put("油气类型", "yqlx");
        mEnCode.put("石油储量规模(资源序列编码)", "syclbm");
        mEnCode.put("石油储量规模(最大值)", "syclmax");
        mEnCode.put("石油储量规模（最小值）", "syclmin");
        mEnCode.put("天然气储量规模（资源序列编码）", "trqclbm");
        mEnCode.put("天然气储量规模（最大值）", "trqclmax");
        mEnCode.put("天然气储量规模（最小值）", "trqclmin");
        mEnCode.put("总储量规模（资源序列编码）", "zclbm");
        mEnCode.put("总储量规模（最大值）", "zclmax");
        mEnCode.put("总储量规模（最小值）", "zclmin");
        mEnCode.put("面积大小(最大值)", "mjmax");
        mEnCode.put("面积大小(最小值)", "mjmin");
        mEnCode.put("烃源岩性", "jyyx");
        mEnCode.put("储层岩性", "ccyx");
        mEnCode.put("盖层岩性", "gcyx");
        mEnCode.put("圈闭类型", "qblx");
        mEnCode.put("构造部位", "gzbw");
        mEnCode.put("国家 （暂时没有）", "gj");
        mEnCode.put("油气藏", "mblx=2");
        mEnCode.put("沉积相", "cjx");
        mEnCode.put("地层层系", "dccx");
        mEnCode.put("油气类型", "yqlx");
        mEnCode.put("石油储量规模(资源序列编码)", "syclbm");
        mEnCode.put("石油储量规模(最大值)", "syclmax");
        mEnCode.put("石油储量规模（最小值）", "syclmin");
        mEnCode.put("天然气储量规模（资源序列编码）", "trqclbm");
        mEnCode.put("天然气储量规模（最大值）", "trqclmax");
        mEnCode.put("天然气储量规模（最小值）", "trqclmin");
        mEnCode.put("总储量规模（资源序列编码）", "zclbm");
        mEnCode.put("总储量规模（最大值）", "zclmax");
        mEnCode.put("总储量规模（最小值）", "zclmin");
        mEnCode.put("面积大小(最大值)", "mjmax");
        mEnCode.put("面积大小(最小值)", "mjmin");
        mEnCode.put("烃源岩性", "jyyx");
        mEnCode.put("储层岩性", "ccyx");
        mEnCode.put("盖层岩性", "gcyx");
        mEnCode.put("圈闭类型", "qblx");
        mEnCode.put("构造部位", "gzbw");
        mEnCode.put("国家 （暂时没有）", "gj");

        mEnCode.put("始太古代", "4");
        mEnCode.put("早太古代", "8");
        mEnCode.put("中太古代", "16");
        mEnCode.put("晚太古代", "32");
        mEnCode.put("早远古代", "128");
        mEnCode.put("中远古代", "256");
        mEnCode.put("晚远古代", "512");
        mEnCode.put("古生代", "137438949376");
        mEnCode.put("中生代", "562812514467840");
        mEnCode.put("近生代", "575897802350002176");



        ////////////////////////////油气田
        mEnCode.put("陆相", "10901");
        mEnCode.put("海相", "10902");
        mEnCode.put("碎屑岩沉积相", "1090201");
        mEnCode.put("碳酸盐岩沉积相", "1090202");
        mEnCode.put("碳酸盐岩潮坪相", "109020201");
        mEnCode.put("局限碳酸盐岩台地相", "109020202");
        mEnCode.put("开阔碳酸盐岩台地相", "109020203");
        mEnCode.put("碳酸盐岩海滩滩相", "109020204");
        mEnCode.put("生物礁相", "109020205");
        mEnCode.put("碳酸盐岩浅海陆棚相", "109020206");
        mEnCode.put("碳酸盐岩斜坡相", "109020207");
        mEnCode.put("碳酸盐岩盆地相", "109020208");
        mEnCode.put("过渡相", "10903");
        mEnCode.put("油田", "4092");
        mEnCode.put("气田", "4096");
        mEnCode.put("油气田", "4294967295");
        mEnCode.put("沉积岩", "10102");
        mEnCode.put("碎屑岩", "1010201");
        mEnCode.put("砾岩_角砾岩", "101020101");
        mEnCode.put("砂岩", "101020102");
        mEnCode.put("粉砂岩", "10102010204");
        mEnCode.put("页岩", "101020103");
        mEnCode.put("泥岩", "101020104");
        mEnCode.put("火山碎屑岩", "101020105");
        mEnCode.put("其它碎屑岩", "10102010503");
        mEnCode.put("碳酸盐岩", "1010202");
        mEnCode.put("石灰岩", "101020201");
        mEnCode.put("白云岩", "101020202");
        mEnCode.put("生物碎屑", "101020203");
        mEnCode.put("其它碳酸盐岩", "101020204");
        mEnCode.put("蒸发岩", "1010203");
        mEnCode.put("盐岩", "101020301");
        mEnCode.put("膏岩", "101020302");
        mEnCode.put("其它蒸发岩", "101020303");
        mEnCode.put("其它沉积岩", "1010204");
        mEnCode.put("煤", "101020401");
        mEnCode.put("沥青", "101020402");
        mEnCode.put("火成岩", "10101");
        mEnCode.put("沉积岩", "10102");
        mEnCode.put("碎屑岩", "1010201");
        mEnCode.put("砾岩_角砾岩", "101020101");
        mEnCode.put("砂岩", "101020102");
        mEnCode.put("粉砂岩", "10102010204");
        mEnCode.put("页岩", "101020103");
        mEnCode.put("泥岩", "101020104");
        mEnCode.put("火山碎屑岩", "101020105");
        mEnCode.put("其它碎屑岩", "101020106");
        mEnCode.put("碳酸盐岩", "1010202");
        mEnCode.put("石灰岩", "101020201");
        mEnCode.put("白云岩", "101020202");
        mEnCode.put("生物碎屑", "101020203");
        mEnCode.put("其它碳酸盐岩", "101020204");
        mEnCode.put("蒸发岩", "1010203");
        mEnCode.put("盐岩", "101020301");
        mEnCode.put("膏岩", "101020302");
        mEnCode.put("其它蒸发岩", "101020303");
        mEnCode.put("其它沉积岩", "1010204");
        mEnCode.put("其它沉积岩煤", "35184372088832");
        mEnCode.put("沥青", "101020402");
        mEnCode.put("变质岩", "10103");
        mEnCode.put("火成岩", "10101");
        mEnCode.put("沉积岩", "10102");
        mEnCode.put("碎屑岩", "1010201");
        mEnCode.put("砾岩_角砾岩", "101020101");
        mEnCode.put("砂岩", "101020102");
        mEnCode.put("粉砂岩", "10102010204");
        mEnCode.put("页岩", "101020103");
        mEnCode.put("泥岩", "101020104");
        mEnCode.put("火山碎屑岩", "101020105");
        mEnCode.put("其它碎屑岩", "101020106");
        mEnCode.put("碳酸盐岩", "1010202");
        mEnCode.put("石灰岩", "101020201");
        mEnCode.put("白云岩", "101020202");
        mEnCode.put("生物碎屑", "101020203");
        mEnCode.put("其它碳酸盐岩", "101020204");








        mEnCode.put("蒸发岩", "1010203");
        mEnCode.put("盐岩", "101020301");
        mEnCode.put("膏岩", "101020302");
        mEnCode.put("其它蒸发岩", "101020303");
        mEnCode.put("其它沉积岩", "1010204");
        mEnCode.put("煤", "101020401");
        mEnCode.put("沥青", "101020402");
        mEnCode.put("变质岩", "10103");





        mEnCode.put("构造圈闭", "10801");
        mEnCode.put("生物礁圈闭", "1080202");
        mEnCode.put("非生物礁圈闭", "2177548419072");
        mEnCode.put("水动力圈闭", "10803");
        mEnCode.put("复合圈闭", "10804");

        mEnCode.put("盆地边缘", "10701");
        mEnCode.put("断块带", "1070301");
        mEnCode.put("生长断层带", "1070302");
        mEnCode.put("逆掩断层带", "1070303");
        mEnCode.put("褶皱带", "1070304");
        mEnCode.put("背斜带", "1070305");
        mEnCode.put("单斜带", "1070306");
        mEnCode.put("地垒带", "1070307");
        mEnCode.put("盆地中部", "10702");
        mEnCode.put("断块带", "1070301");
        mEnCode.put("生长断层带", "1070302");
        mEnCode.put("逆掩断层带", "1070303");
        mEnCode.put("褶皱带", "1070304");
        mEnCode.put("背斜带", "1070305");
        mEnCode.put("单斜带", "1070306");
        mEnCode.put("地垒带", "1070307");
        mEnCode.put("盆间隆起", "10703");
        mEnCode.put("断块带", "1070301");
        mEnCode.put("生长断层带", "1070302");
        mEnCode.put("逆掩断层带", "1070303");
        mEnCode.put("褶皱带", "1070304");
        mEnCode.put("背斜带", "1070305");
        mEnCode.put("单斜带", "1070306");
        mEnCode.put("地垒带", "1070307");
        /////////////////////////////end 油气田


        ////////////////////////////
        mEnCode.put("海相碎屑岩", "281466386776064");
        mEnCode.put("海相碳酸盐岩", "287948901175001088");
        mEnCode.put("陆相碎屑岩", "65534");
        mEnCode.put("陆相碳酸盐岩", "67043328");
    }

}
