package com.taksila.veda.utils;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

class FlattenDeserializer implements JsonDeserializer<Map<String, String>> 
{
	    @Override
	    public Map<String, String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException 
	    {
	        Map<String, String> map = new LinkedHashMap<>();

	        if (json.isJsonArray()) 
	        {
	            for (JsonElement e : json.getAsJsonArray()) 
	            {
	                map.putAll(deserialize(e, typeOfT, context));
	            }
	        } 
	        else if (json.isJsonObject()) 
	        {
	            for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) 
	            {
	                if (entry.getValue().isJsonPrimitive()) 
	                {
	                    map.put(entry.getKey(), entry.getValue().getAsString());
	                }
	                else if (entry.getValue().isJsonNull())
	                {
	                	map.put(entry.getKey(), "");
	                }
	                else 
	                {
	                    map.putAll(deserialize(entry.getValue(), typeOfT, context));
	                }
	            }
	        }
	        
	        return map;
	    }
	}