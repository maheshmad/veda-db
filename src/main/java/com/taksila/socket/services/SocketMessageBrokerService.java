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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
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
import com.taksila.veda.model.api.security.v1_0.UserLoginResponse;
import com.taksila.veda.security.UserAuthComponent;
import com.taksila.veda.utils.CommonUtils;


@ServerEndpoint(value = "/io/{authsessionid}", configurator=HttpSessionConfigurator.class)
public class SocketMessageBrokerService implements Serializable
{
	static Logger logger = LogManager.getLogger(SocketMessageBrokerService.class);
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Map<String,SocketMessageBrokerService> connections = new HashMap<String,SocketMessageBrokerService>();       
    private Session wsSession;
    private HttpSession httpSession;
    private Map<String, List<String>> httpHeaders ;
    private String userid = "";

    @OnOpen
    public void open(@PathParam("authsessionid") String authsessionid,Session session, EndpointConfig config)  
    {             
    	logger.trace("socket message broker starting .....!!!! trying to read session param = "+HttpSession.class.getName());
    	this.wsSession = session;
    	this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
    	this.httpHeaders =  (Map<String, List<String>>) config.getUserProperties().get("headers");
    	
    	for (String key: this.httpHeaders.keySet())
    	{
    		List<String> vals = this.httpHeaders.get(key);
			for (String val: vals)
    			System.out.println(" ****** http header "+key+"   =   "+val);
        	
    	}
    	
    	String tenantId = CommonUtils.getSubDomain("");						
		UserAuthComponent userAuthComponent = new UserAuthComponent(tenantId);
		UserLoginResponse userInfoResp = userAuthComponent.getLoggedInUser(authsessionid);		
		
		if(userInfoResp == null) 
		{
			System.out.println("User session not found !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			return;
		}
        
		this.userid = userInfoResp.getUserInfo().getUserId();
        connections.put(this.userid,this);
                
        /*
         * send CONNECT-SUCCESS message to the client
         */
        /* build an event for client */
        SocketEvent socketEvent = new SocketEvent();       
        socketEvent.type = SocketEventType.CONNECT_SUCCESS;
        socketEvent.setFrom("Xedu");
        socketEvent.getTo().add(this.userid);
        socketEvent.setMsg("Successfully connected to the socket server ");
        pushEventToClient(userid, socketEvent);
        
        
    }
   

    @OnClose
    public void end() 
    {
    	try 
    	{
    		System.out.println("connection closing for client = "+ this.userid);
    		connections.remove(this.userid);
    		
		} 
    	catch (Exception e) 
    	{
    		System.out.println("Exception occured during close for client =  "+ this.userid+", reason = "+e.getMessage());
    		e.printStackTrace();
		}
    }
    
//    @OnMessage
//    public void incoming(String message) 
//    {
//       System.out.println("recieved message = "+ message);       
//       Gson gson = new Gson();
//       notificationEvent notificationEvent = gson.fromJson(message, notificationEvent.class);  
//       
//       if ("STATUS-UPDT".equals(notificationEvent.type))
//       {
//    	   this.userInfo.status = notificationEvent.userInfo.status;
//       }       
//       processMessage(notificationEvent);
//    }
       
   @OnMessage
   public void echo(String msg) throws IOException 
   {
       this.wsSession.getBasicRemote().sendText(msg);
   }


    @OnError
    public void onError(Throwable t) throws Throwable 
    {              
        /* build an event for client */
    	t.printStackTrace();
    	try 
        {
	    	SocketEvent notificationEvent = new SocketEvent();       
	        notificationEvent.type = SocketEventType.ERROR;
	        notificationEvent.setFrom("RA");
	        notificationEvent.getTo().add(this.userid);
	        notificationEvent.setMsg("@@@@@@@@ Error occured on SocketMessageBroker service ");
	               
        	System.out.println("onError: trying to send the error message back to client , if there is connection");
        	pushEventToClient(this.userid, notificationEvent); /* try sending a message back, may not reach the client if the connection is already lost */
        	connections.remove(this.userid);
		} 
        catch (Exception e) 
        {
        	e.printStackTrace();
		}
                
    }

    /**
     * 
     * @param notify
     */
    public static void pushEventToAllClients(SocketEvent socketEvent)
    {
    	System.out.println("Pushing event to client....");
    	for (String clientKey: connections.keySet())
    	{
    		SocketMessageBrokerService userSocketServer = connections.get(clientKey);
    		if (userSocketServer != null)
			{				    			
    			pushEventToClient(clientKey,socketEvent);
			}
			else
				CommonUtils.logEyeCatchingMessage("*** COULD NOT FIND SOCKET CONNECTION FOR USER = "+clientKey,false);
    	}
    }
    
    /**
     * 
     * @param notify
     */
//    private static void pushEvent(Notification notify)
//    {
//    	for (String userid: notify.getToList())
//    	{
//    		NotificationSocketServer userSocketServer = connections.get(userid);
//    		if (userSocketServer != null)
//    		{
//    			userSocketServer.sendMessage(notify);
//    		}
//    		else
//    			CommonUtils.logEyeCatchingMessage("*** COULD NOT FIND SOCKET CONNECTION FOR USER = "+userid,false);
//    	}
//    	
//    }
    
    /**
     * 
     * @param notify
     */
    public static void sendMessage(SocketEvent socketEvent)
    {
    	/*
         * check if its a broadcast message
         */    		    	
    	for (String clientUid : socketEvent.getTo()) 
        {    		
    		if ("all".equalsIgnoreCase(clientUid))
    			pushEventToAllClients(socketEvent);
    		else
    			pushEventToClient(clientUid,socketEvent);
        }   
    }
        
    
    /**
     * 
     * @param toClientUid
     * @param event
     */
    private static void pushEventToClient(String toClientUid, SocketEvent event)
    {
    	SocketMessageBrokerService clientConnection = connections.get(toClientUid);
    	
    	if (clientConnection == null)
    		return;
    	else;
    	
    	try 
        {            	            	            	
        	synchronized (clientConnection) 
            {
        		clientConnection.wsSession.getBasicRemote().sendText(event.toString());
            }
        } 
        catch (IOException e) 
        {
        	System.out.println("Chat Error: Failed to send message to client = toClientUid, because "+e.getMessage());
        	e.printStackTrace();
            try 
            {
            	connections.remove(toClientUid);
            	clientConnection.wsSession.close();
            } 
            catch (IOException e1) 
            {
            	System.out.println("Failed to close the session for client =  toClientUid, because "+e.getMessage());
            }
            
           
        }
    }
    
    

    
}