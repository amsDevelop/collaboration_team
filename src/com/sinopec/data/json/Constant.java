package com.sinopec.data.json;

public class Constant implements com.lenovo.nova.util.Constant{

	public static String baseIP = "202.204.193.201";
	
	public static String  baseURL = "http://"+baseIP+":8080/";

	/**
	 * 盆地 
	 */
	public static String basinURL = baseURL + "peprisapi/basinAttribute.html?basinId=";
	/**
	 * 油气田
	 */
	public static String oilGas = baseURL + "peprisapi/oilGasFieldAttribute.html?dzdybm=";

	/**
	 * 全球海相碳酸盐岩含油气盆地分布
	 * 72057594037927935
	 */
	
	public static String distributeOilGas = baseURL + 
			"peprisapi/fixquery1.html?chenjitixi=";
	
	/**
	 * 2.全球含油气盆地碳酸盐岩层系油气储量占盆地油气总储量比例分布
	 */
	//type=72057594037927935&haixiang=72057594037927935&tansuanyanyan=72057594037927935 
	public static String distributeRate = baseURL + 
			"peprisapi/fixquery2.html?";
	
	/**
	 * 4.分层系全球海相碳酸盐岩烃源岩分布
	 */
	//type=72057594037927935&haixiang=72057594037927935&tansuanyanyan=72057594037927935 
	public static String distributeHydrocSource = baseURL + 
			"peprisapi/fixquery4.html?cengxi=";
	
	/**
	 * 盆地属性查询（对应接口文档一.1）
	 */
	//type=72057594037927935&haixiang=72057594037927935&tansuanyanyan=72057594037927935 
	public static String urlAttributeBasin = baseURL + 
			"peprisapi/basinAttribute.html?basinId=";
	
	/**
	 * 盆地简介（对应接口文档六.1）
	 */
	//type=72057594037927935&haixiang=72057594037927935&tansuanyanyan=72057594037927935 
	public static String urlIntroduceBasin = baseURL + 
			"peprisapi/docInfoQuery.html?type=1&dzdxbm=";
	
	/**
	 * 油气田属性查询（对应接口文档一.2）
	 */
	public static String urlAttributeOilGas = baseURL + 
			"peprisapi/oilGasFieldAttribute.html?dzdybm=";
	
	/**
	 * 油气田简介（对应接口文档六.2）
	 */
	public static String urlIntroduceOilGas = baseURL + 
			"peprisapi/docInfoOilQuery.html?type=1&dzdxbm=";
	
	
	
	
	
	
	
	
}
