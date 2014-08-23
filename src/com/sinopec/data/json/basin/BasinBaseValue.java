package com.sinopec.data.json.basin;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
/**
 * ��ػ�����
 * @author liuzhaodong
 *
 */
public class BasinBaseValue extends Bean{
	@JsonFieldName("GZDYZWMC")
	private String name;
	
	@JsonFieldName("GZDYYWMC")
	private String name2;
	
	@JsonFieldName("GZDYJBBH")
	private String index;
	
	@JsonFieldName("GZDYLBMC")
	private String name3;
	
	@JsonFieldName("SJGZDYBM")
	private String name4;
	
	
	@JsonFieldName("PDLXBM")
	private String name5;
	
	@JsonFieldName("JDLXBM")
	private String name6;
	
	
	@JsonFieldName("CJLXBM")
	private String name7;
	
	
	@JsonFieldName("DCZYCX")
	private String info;
	
	@JsonFieldName("PDMJ")
	private String value;
	
	@JsonFieldName("PJCJHD")
	private String value1;
	
	@JsonFieldName("ZDCJHD")
	private String value2;
	@JsonFieldName("GZYDCS")
	private String value3;
	
	@JsonFieldName("GZYDCS")
	private String value4;
	
	@JsonFieldName("CJXHS")
	private String value5;
	
	@JsonFieldName("CJXHS")
	private String value6;
	@JsonFieldName("GDWTD")
	private String value7;
	@JsonFieldName("DBRL")
	private String value8;
	@JsonFieldName("BZ")
	private String value9;
	
}
