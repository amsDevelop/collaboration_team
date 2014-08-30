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
		mEnCode.put("陆相", "1073741823");
		mEnCode.put("海相", "281473902968832");
		mEnCode.put("碎屑岩沉积相", "66571993088");
		mEnCode.put("碳酸盐岩沉积相", "281406257233920");
		mEnCode.put("碳酸盐岩潮坪相", "137438953472");
		mEnCode.put("局限碳酸盐岩台地相", "274877906944");
		mEnCode.put("开阔碳酸盐岩台地相", "549755813888");
		mEnCode.put("碳酸盐岩海滩滩相", "1099511627776");
		mEnCode.put("生物礁相", "2199023255552");
		mEnCode.put("碳酸盐岩浅海陆棚相", "4398046511104");
		mEnCode.put("碳酸盐岩斜坡相", "8796093022208");
		mEnCode.put("碳酸盐岩盆地相", "17592186044416");
		mEnCode.put("过渡相", "4611404543450677248");
		mEnCode.put("油田", "4092");
		mEnCode.put("气田", "4096");
		mEnCode.put("油气田", "4294967295");
		mEnCode.put("沉积岩", "281474976645120");
		mEnCode.put("碎屑岩", "8589803520");
		mEnCode.put("砾岩_角砾岩", "262144");
		mEnCode.put("砂岩", "7864320");
		mEnCode.put("粉砂岩", "8388608");
		mEnCode.put("页岩", "16777216");
		mEnCode.put("泥岩", "33554432");
		mEnCode.put("火山碎屑岩", "1006632960");
		mEnCode.put("其它碎屑岩", "2147483648");
		mEnCode.put("碳酸盐岩", "1095216660480");
		mEnCode.put("石灰岩", "8589934592");
		mEnCode.put("白云岩", "17179869184");
		mEnCode.put("生物碎屑", "34359738368");
		mEnCode.put("其它碳酸盐岩", "549755813888");
		mEnCode.put("蒸发岩", "16492674416640");
		mEnCode.put("盐岩", "2199023255552");
		mEnCode.put("膏岩", "4398046511104");
		mEnCode.put("其它蒸发岩", "8796093022208");
		mEnCode.put("其它沉积岩", "263882790666240");
		mEnCode.put("煤", "35184372088832");
		mEnCode.put("沥青", "70368744177664");
		mEnCode.put("火成岩", "65535");
		mEnCode.put("沉积岩", "281474976645120");
		mEnCode.put("碎屑岩", "8589803520");
		mEnCode.put("砾岩_角砾岩", "262144");
		mEnCode.put("砂岩", "7864320");
		mEnCode.put("粉砂岩", "8388608");
		mEnCode.put("页岩", "16777216");
		mEnCode.put("泥岩", "33554432");
		mEnCode.put("火山碎屑岩", "1006632960");
		mEnCode.put("其它碎屑岩", "2147483648");
		mEnCode.put("碳酸盐岩", "1095216660480");
		mEnCode.put("石灰岩", "8589934592");
		mEnCode.put("白云岩", "17179869184");
		mEnCode.put("生物碎屑", "34359738368");
		mEnCode.put("其它碳酸盐岩", "549755813888");
		mEnCode.put("蒸发岩", "16492674416640");
		mEnCode.put("盐岩", "2199023255552");
		mEnCode.put("膏岩", "4398046511104");
		mEnCode.put("其它蒸发岩", "8796093022208");
		mEnCode.put("其它沉积岩", "263882790666240");
		mEnCode.put("其它沉积岩煤", "35184372088832");
		mEnCode.put("沥青", "70368744177664");
		mEnCode.put("变质岩", "18446462598732840960");
		mEnCode.put("火成岩", "65535");
		mEnCode.put("沉积岩", "281474976645120");
		mEnCode.put("碎屑岩", "8589803520");
		mEnCode.put("砾岩_角砾岩", "262144");
		mEnCode.put("砂岩", "7864320");
		mEnCode.put("粉砂岩", "8388608");
		mEnCode.put("页岩", "16777216");
		mEnCode.put("泥岩", "33554432");
		mEnCode.put("火山碎屑岩", "1006632960");
		mEnCode.put("其它碎屑岩", "2147483648");
		mEnCode.put("碳酸盐岩", "1095216660480");
		mEnCode.put("石灰岩", "8589934592");
		mEnCode.put("白云岩", "17179869184");
		mEnCode.put("生物碎屑", "34359738368");
		mEnCode.put("其它碳酸盐岩", "549755813888");








		mEnCode.put("蒸发岩", "16492674416640");
		mEnCode.put("盐岩", "2199023255552");
		mEnCode.put("膏岩", "4398046511104");
		mEnCode.put("其它蒸发岩", "8796093022208");
		mEnCode.put("其它沉积岩", "263882790666240");
		mEnCode.put("煤", "35184372088832");
		mEnCode.put("沥青", "70368744177664");
		mEnCode.put("变质岩", "18446462598732840960");





		mEnCode.put("构造圈闭", "4294967295");
		mEnCode.put("生物礁圈闭", "17179869184");
		mEnCode.put("非生物礁圈闭", "2177548419072");
		mEnCode.put("水动力圈闭", "65970697666560");
		mEnCode.put("符合圈闭", "34902897112121344");

		mEnCode.put("盆地边缘", "4111");
		mEnCode.put("断块带", "4097");
		mEnCode.put("生长断层带", "4098");
		mEnCode.put("逆掩断层带", "4099");
		mEnCode.put("褶皱带", "4100");
		mEnCode.put("背斜带", "4101");
		mEnCode.put("单斜带", "4102");
		mEnCode.put("地垒带", "4103");
		mEnCode.put("盆地中部", "8207");
		mEnCode.put("断块带", "8193");
		mEnCode.put("生长断层带", "8194");
		mEnCode.put("逆掩断层带", "8195");
		mEnCode.put("褶皱带", "8196");
		mEnCode.put("背斜带", "8197");
		mEnCode.put("单斜带", "8198");
		mEnCode.put("地垒带", "8199");
		mEnCode.put("盆间隆起", "12303");
		mEnCode.put("断块带", "12289");
		mEnCode.put("生长断层带", "12290");
		mEnCode.put("逆掩断层带", "12291");
		mEnCode.put("褶皱带", "12292");
		mEnCode.put("背斜带", "12293");
		mEnCode.put("单斜带", "12294");
		mEnCode.put("地垒带", "12295");
		/////////////////////////////end 油气田
		
		
		////////////////////////////
		mEnCode.put("海相碎屑岩", "281466386776064");
		mEnCode.put("海相碳酸盐岩", "287948901175001088");
		mEnCode.put("陆相碎屑岩", "65534");
		mEnCode.put("陆相碳酸盐岩", "67043328");
	}
	
}
