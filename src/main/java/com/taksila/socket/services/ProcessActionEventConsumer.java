package com.taksila.socket.services;

import com.taksila.veda.utils.CommonUtils;

public class ProcessActionEventConsumer implements SocketEventConsumer
{

	@Override
	public SocketEvent processSocketEvent(SocketEvent event) 
	{
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!  Processing event "+CommonUtils.toJson(event));
		return event;
	}

	@Override
	public boolean isEventSupported(String id) 
	{
		if ("ACTION".equalsIgnoreCase(id))
			return true;
		else
			return false;
	}
	
}
