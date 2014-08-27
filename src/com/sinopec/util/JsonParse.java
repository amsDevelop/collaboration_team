package com.sinopec.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public List<HashMap<String,Object>> parseItemsJson(JsonReader reader) throws IOException {
        List<HashMap<String,Object>> lists = new ArrayList<HashMap<String,Object>>();
        
        reader.beginArray();
        
        
        while (reader.hasNext()) {
//        	Log.v('mandy', 'list: ' + reader.nextName());
         	Log.v("mandy", "list: while....");
            lists.add(parseItemJson(reader));
         
        }
    	Log.v("mandy", "list: while....finish...");
        reader.endArray();
        return (List<HashMap<String, Object>>) lists;
    }

    /**
     * parse item each property, return value
     * 
     * @param reader
     * @return ItemProperty
     * @throws IOException
     */
    public HashMap<String,Object> parseItemJson(JsonReader reader) throws IOException {
    	HashMap<String,Object> item = new HashMap<String,Object>();
        reader.beginObject();

        while (reader.hasNext()) {
            try {
				String name  = 	reader.nextName();
				 if(name.equalsIgnoreCase("油气田储量产量")) {
					  reader.beginArray();
					  
					  List<Object> list = new ArrayList<Object>();
//        		  HashMap<String,Object> list = new HashMap<String,Object>(); 
					  while (reader.hasNext()) {
						  list.add(parseItemOtherJson(reader)); 
					  }
					   reader.endArray();
				 item.put(name, list);
				 } else {
					item.put(name, parseItemOtherJson(reader));
//        		 reader.skipValue();
				 }
			} catch (Exception e) {
				 reader.skipValue();
				e.printStackTrace();
			}
//        	
        }
        reader.endObject();
        return item;
    }
        
    public HashMap<String,Object> parseItemOtherJson(JsonReader reader) throws IOException {
    	HashMap<String,Object> item = new HashMap<String,Object>();
        reader.beginObject(); 

        while (reader.hasNext()) {
        	    try {
        	    String name = reader.nextName();
        	    if (name.equals("FXSJ") || name.equals("CCTCRQ")) {
        	    	  HashMap<String, String> hashMap = new HashMap<String, String>();   
        	    	  reader.beginObject(); 
        	    	  while (reader.hasNext()) {
        	    		  hashMap.put(reader.nextName(), reader.nextString());
        	    	  }
        	    	  reader.endObject();
        	    	  
        	    	item.put(name, hashMap);
        	    	 
        	    } else if (name.equals("GTQTYYCJTX") || name.equals("CCCJTX")){
        	    	
        	    	  item.put(name, "null");
        	    	
        	    } else {
        	    	String value = reader.nextString();
               	    item.put(name, value);
        	    }

				} catch (Exception e) {
					reader.skipValue();
				}
        	
        	
        	  
        }
        reader.endObject();
        return item;
    }
    
}
