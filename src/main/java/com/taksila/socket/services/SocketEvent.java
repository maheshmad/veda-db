package com.taksila.socket.services;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

/**
 * 
 * @author Mahesh.M
 *
 */
public class SocketEvent 
{    	  	 

	SocketEventType type;
	String msg;
	String from;
	List<String> to = new ArrayList<String>();
	
	
	public enum SocketEventType 
	{
		CONNECT_SUCCESS,
		ACTION,
		DATA,
	    DISCONNECTED,
	    ERROR;

	    public String value() {
	        return name();
	    }

	    public static SocketEventType fromValue(String v) {
	        return valueOf(v);
	    }

	}
	
	public SocketEventType getType() {
		return type;
	}



	public void setType(SocketEventType type) {
		this.type = type;
	}



	public String getMsg() {
		return msg;
	}



	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getFrom() {
		return from;
	}



	public void setFrom(String from) {
		this.from = from;
	}

	public List<String> getTo() {
		return to;
	}



	public void setTo(List<String> to) {
		this.to = to;
	}


	@Override
	public String toString() 
	{	
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}