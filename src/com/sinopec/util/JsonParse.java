package com.sinopec.util;

import java.io.IOException;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;


public class JsonParse {
   
	
	/**
     * parse item property,add to list
     * 
     * @param reader
     * @return List<ItemProperty>
     * @throws IOException
     */
    public List<HashMap<String,HashMap<String,String>>> parseItemsJson(JsonReader reader) throws IOException {
        List<HashMap<String,HashMap<String,String>>> lists = new ArrayList<HashMap<String,HashMap<String,String>>>();
        
        reader.beginArray();
        
        while (reader.hasNext()) {
//        	Log.v("mandy", "list: " + reader.nextName());
            lists.add(parseItemJson(reader));
        }
        reader.endArray();
        return lists;
    }

    /**
     * parse item each property, return value
     * 
     * @param reader
     * @return ItemProperty
     * @throws IOException
     */
    public HashMap<String,HashMap<String,String>> parseItemJson(JsonReader reader) throws IOException {
    	HashMap<String,HashMap<String,String>> item = new HashMap<String,HashMap<String,String>>();
        reader.beginObject();

        while (reader.hasNext()) {
            String name  = 	reader.nextName();
        	 if(name.equalsIgnoreCase("油气田基础属性")) {
        		 
        		item.put(name, parseItemOtherJson(reader)) ;
        	 } else {
        		 
        		 reader.skipValue();
        	 }
        	
        }
        reader.endObject();
        return item;
    }
    
    
    public HashMap<String,HashMap<String,String>> parseJson(String json) throws IOException {
    	Gson gson = new Gson();
//    	Type collectionType = new TypeToken<List<PersonBean>>(){}.getType();
//    	List<PersonBean> details = gson.fromJson(json, collectionType);
    	HashMap<String,HashMap<String,String>> item = new HashMap<String,HashMap<String,String>>();
    	return item;
    }
    
//    reader.beginObject();  
//	  while(reader.hasNext()){  
//		  HashMap<String,String> hashMap = new HashMap<String, String>();
//	      String tagName= reader.nextName();  
//	      hashMap.put(tagName, reader.nextString());
//	   }  
//	    reader.endObject();  
    private String tag = "json";
    public HashMap<String,String> parseItemOtherJson(JsonReader reader) throws IOException {
    	HashMap<String,String> item = new HashMap<String,String>();
        reader.beginObject(); 

        while (reader.hasNext()) {
        	 String value = "";
        	 String key = "";
        	    try {
        	    	
        	     key = reader.nextName();
        	     value = reader.nextString();
        	     Log.d(tag, "0000   键: " + key+" 值: "+value);
        	     if("FXSJ".equals(key) || "CCTCRQ".equals(key)){
        	    	  Log.d(tag, "键: " + key+" 值: "+value);
        	     }
        	     
        	     item.put(key,value);
				} catch (Exception e) {
					Log.e(tag, " 解析json 错误 "+e.toString());
					item.put(key,"");
					continue;
				}
        }
        reader.endObject();
        return item;
    }
    
}
