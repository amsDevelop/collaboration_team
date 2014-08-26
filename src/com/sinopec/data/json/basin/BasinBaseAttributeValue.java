package com.sinopec.data.json.basin;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;

/*
 * 油气田基础属性
 */
public class BasinBaseAttributeValue extends Bean {
	@JsonFieldName("MBZWMC")
	private String name;
	
	@JsonFieldName("MBYWMC")
	private String name2;
	
	@JsonFieldName("MBLX")
	private int index;
	
	@JsonFieldName("MJ")
	private double index1;
	
	@JsonFieldName("BHGD")
	private int index2;
	
	
	@JsonFieldName("GDMS")
	private double GDMS;
	
	@JsonFieldName("DCCW")
	private String DCCW;
	
	
	@JsonFieldName("QBLX")
	private String QBLX;
	
	
	@JsonFieldName("XCSQ")
	private String XCSQ;
	
	@JsonFieldName("XCNL")
	private int XCNL;
	
	@JsonFieldName("DCSSX")
	private String DCSSX;
	
	@JsonFieldName("DCSKHD")
	private int DCSKHD;
	
	@JsonFieldName("KKCD")
	private int KKCD;
	
	@JsonFieldName("HBSS")
	private int HBSS;
	
	@JsonFieldName("DBTJ")
	private int DBTJ;
	
	@JsonFieldName("FXSJ")
	private String value6; //list object
	
	
	@JsonFieldName("YQLX")
	private String YQLX;
	@JsonFieldName("FXJLL")
	private int FXJLL;
	@JsonFieldName("CCTCRQ") //list object
	private String value9;
	
	@JsonFieldName("KTJS") 
	private int KTJS;
	@JsonFieldName("PJJS") 
	private int PJJS;
	@JsonFieldName("ZSJS") 
	private int ZSJS;
	@JsonFieldName("SCJS") 
	private int SCJS;
	@JsonFieldName("BZ") 
	private String BZ;
	@JsonFieldName("SSPDBM") 
	private long SSPDBM;
	@JsonFieldName("GZBW") 
	private String GZBW;

}
