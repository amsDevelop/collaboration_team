package com.sinopec.data.json;

public interface Constant extends com.lenovo.nova.util.Constant{

	String 	baseURL = "http://202.204.193.201:8080/";
//	String 	baseURL = "http://10.225.14.204:8080/";
	/**
	 * 盆地
	 */
	String basinURL = baseURL + "peprisapi/basinAttribute.html?basinId=";
	/**
	 * 油气田
	 */
	String oilGas = baseURL + "peprisapi/oilGasFieldAttribute.html?dzdybm=";

	/**
	 * 全球海相碳酸盐岩含油气盆地分布
	 * 72057594037927935
	 */
	
	String distributeOilGas = baseURL + 
			"peprisapi/fixquery1.html?chenjitixi=";
	
	/**
	 * 2.全球含油气盆地碳酸盐岩层系油气储量占盆地油气总储量比例分布
	 */
	//type=72057594037927935&haixiang=72057594037927935&tansuanyanyan=72057594037927935 
	String distributeRate = baseURL + 
			"peprisapi/fixquery2.html?";
	
	/**
	 * 4.分层系全球海相碳酸盐岩烃源岩分布
	 */
	//type=72057594037927935&haixiang=72057594037927935&tansuanyanyan=72057594037927935 
	String distributeHydrocSource = baseURL + 
			"peprisapi/fixquery4.html?cengxi=";
	
	/**
	 * 盆地属性查询（对应接口文档一.1）
	 */
	//type=72057594037927935&haixiang=72057594037927935&tansuanyanyan=72057594037927935 
	String urlAttributeBasin = baseURL + 
			"peprisapi/basinAttribute.html?basinId=";
	
	/**
	 * 盆地简介（对应接口文档六.1）
	 */
	//type=72057594037927935&haixiang=72057594037927935&tansuanyanyan=72057594037927935 
	String urlIntroduceBasin = baseURL + 
			"peprisapi/docInfoQuery.html?type=1&dzdxbm=";
	
	/**
	 * 油气田属性查询（对应接口文档一.2）
	 */
	String urlAttributeOilGas = baseURL + 
			"peprisapi/oilGasFieldAttribute.html?dzdybm=";
	
	/**
	 * 油气田简介（对应接口文档六.2）
	 */
	String urlIntroduceOilGas = baseURL + 
			"peprisapi/docInfoOilQuery.html?type=1&dzdxbm=";
	
	
	
	
	
	
	
	
}
