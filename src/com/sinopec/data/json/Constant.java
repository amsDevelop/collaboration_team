package com.sinopec.data.json;

public interface Constant extends com.lenovo.nova.util.Constant{

	String 	baseURL = "http://202.204.193.201:8080/";
	/**
	 * ���
	 */
	String basinURL = baseURL + "peprisapi/basinAttribute.html?basinId=";
	/**
	 * ������
	 */
	String oilGas = baseURL + "peprisapi/oilGasFieldAttribute.html?dzdybm=";

	/**
	 * ȫ����̼�����Һ�������طֲ�
	 * 72057594037927935
	 */
	
	String distributeOilGas = baseURL + 
			"peprisapi/fixquery1.html?chenjitixi=";
	
	/**
	 * 2.ȫ���������̼�����Ҳ�ϵ������ռ��������ܴ�������ֲ�
	 */
	//type=72057594037927935&haixiang=72057594037927935&tansuanyanyan=72057594037927935 
	String distributeRate = baseURL + 
			"peprisapi/fixquery2.html?";
	
	
	
	
	
	
	
	
	
	
}
