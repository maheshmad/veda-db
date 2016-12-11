package com.taksila.socket.services;

public interface SocketEventConsumer 
{
	/**
	 * 
	 * @param searchWebNewsRequest
	 * @return
	 */	
	public SocketEvent processSocketEvent(SocketEvent event);		
	boolean isEventSupported(String id);
	
}
