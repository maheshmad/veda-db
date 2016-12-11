/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.taksila.socket.services;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taksila.socket.services.SocketEvent.SocketEventType;
import com.taksila.veda.utils.CommonUtils;

@ServerEndpoint(value = "/io/{authsessionid}")
public class SocketEventBrokerService implements Serializable
{
	static Logger logger = LogManager.getLogger(SocketEventBrokerService.class);
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private Session wsSession;
    private String sessionId = "";
    
    private static final List<SocketEventConsumer> eventConsumers = new ArrayList<SocketEventConsumer>();
    private static final Map<String, Session> connections = new HashMap<String, Session>();
    
    static 
    {
    	/**
    	 * registering event consumers
    	 */
    	CommonUtils.logEyeCatchingMessage("STARTING SOCKET BROKER SERVICE",false);
    	eventConsumers.add(new ProcessActionEventConsumer());
    }

    @OnOpen
    public void open(Session session, EndpointConfig config, @PathParam("authsessionid") String authsessionid)
    
    {                 	
    	this.wsSession = session;    	    		            
        sessionId = authsessionid ;
        connections.put(sessionId, session);
        System.out.println("Setting up connection for client session id = "+sessionId);
//        dataServicesConnections.put(session.getId(),this);
                
        /*
         * send CONNECT-SUCCESS message to the client
         */
        /* build an event for client */
        SocketEvent socketEvent = new SocketEvent();       
        socketEvent.setType(SocketEventType.CONNECT_SUCCESS);
        socketEvent.setFrom("Xedu");
//        socketEvent.getTo().add();
        socketEvent.setMsg("Successfully connected to the socket server ");
        pushEventToClient(socketEvent, session, sessionId);
        
      
        
    }
   

    
    @OnMessage
    public void incoming(String message) 
    {
       System.out.println("recieved socket message = "+ message);        
       /*
        * build the socket event from message
        */
       SocketEvent socketEvent = CommonUtils.fromJson(message, SocketEvent.class);
       pushEventToClient(socketEvent, this.wsSession, this.sessionId); /* echo back for debugging */
       
       /*
        * trigger the consumers based on the type of message
        */
       try 
       {
    	   for(SocketEventConsumer consumer: eventConsumers)
		   {
			   SocketEvent responseSocketEvent = consumer.processSocketEvent(socketEvent);  
			   
			   if (responseSocketEvent != null && responseSocketEvent.to != null)
			   {				   
				   for (String to: responseSocketEvent.to)
				   {    		     					   
					   if ("all".equalsIgnoreCase(to))    		   
				    	   pushEventToAllClient(responseSocketEvent);
					   else
						   pushEventToClient(responseSocketEvent,connections.get(to),to);
				   }
			   }
			   
		   }
       }
       catch (Exception e) 
       {		
    	   System.out.println("error occured during broadcast = "+ e.getMessage());        
    	   e.printStackTrace();
       }       
       
    
    }
    
    
    @OnError
    public void onError(Throwable t) throws Throwable 
    {                      
        try 
        {
        	System.out.println("Socket Error: Removing client session id  = "+this.sessionId+", because "+t.getMessage());     
        	connections.remove(this.sessionId);
		} 
        catch (Exception e) 
        {
        	e.printStackTrace();
		}
                
    }


    @OnClose
    public void end() 
    {
    	try 
    	{
    		System.out.println("connection closing for client = "+ this.sessionId);
    		connections.remove(this.sessionId);
//    		dataServicesConnections.remove(this.sessionId);
    		
		} 
    	catch (Exception e) 
    	{
    		System.out.println("Exception occured during close for client =  "+ this.sessionId+", reason = "+e.getMessage());
    		e.printStackTrace();
		}
    }
    
   
    /**
     * 
     * @param toClientUid
     * @param event
     */
    private void pushEventToAllClient(SocketEvent event)
    {
    	System.out.println("******** sending message to all "+connections.size()+" clients , msg= "+CommonUtils.toJson(event));
    	for (String sessionid: connections.keySet())
    	{
    		this.pushEventToClient(event, connections.get(sessionid),this.sessionId);
    	}
    	
    }
    
    
    /**
     * 
     * @param toClientUid
     * @param event
     */
    private void pushEventToClient(SocketEvent event, Session wss, String clientsessionid)
    {
//    	SocketEventBrokerService clientConnection = dataServicesConnections.get(toClientUid);
    	
//    	if (event != null || StringUtils.isBlank(toClientUid))
//    	{
//    		CommonUtils.logEyeCatchingMessage("unexpected push event on socket server for client id = "+toClientUid, true);
//    		return;
//    	}
    	
    	if (wss == null)
    	{
    		CommonUtils.logEyeCatchingMessage("socket push even cannot success as the client session is null  ", true);
    		return;
    	}
    	else;
    	
    	try 
        {            	            	            	        	
    		wss.getBasicRemote().sendText(CommonUtils.toJson(event));            
        } 
        catch (Exception e) 
        {        	        
        	e.printStackTrace();        	
        	System.out.println("Removing client session id  = "+clientsessionid+", because "+e.getMessage());        	
            try 
            {            	
            	connections.remove(clientsessionid);
            	wss.close();            	
            } 
            catch (Exception e1) 
            {
            	System.out.println("Failed to close the session for client session id = "+clientsessionid+", because "+e.getMessage());
            }
            
           
        }
    }
        
    
    

    
}