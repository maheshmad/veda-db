package com.taksila.socket.services;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpSessionConfigurator extends ServerEndpointConfig.Configurator
{
	static Logger logger = LogManager.getLogger(HttpSessionConfigurator.class);

	@Override
    public void modifyHandshake(ServerEndpointConfig config, 
                                HandshakeRequest request, 
                                HandshakeResponse response)
    {
        HttpSession httpSession = (HttpSession)request.getHttpSession();
        logger.trace("http session is null = "+httpSession);
        Map<String, List<String>> headers = request.getHeaders();        
        if (config != null)
        {
        	logger.trace("modifying handshake.....!!!!!!!");
        	config.getUserProperties().put(HttpSession.class.getName(),httpSession);
        	config.getUserProperties().put("headers", headers);
        }
        
        
    }
}