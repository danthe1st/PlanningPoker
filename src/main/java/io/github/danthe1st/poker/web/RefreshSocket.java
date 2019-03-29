package io.github.danthe1st.poker.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/refresh")
public class RefreshSocket {
	private static List<Session> sessions=new ArrayList<>();
	public static void reloadAll() {
		synchronized(sessions) {
			for (Session session : sessions) {
				try {
					if(session.isOpen()) {
						session.getBasicRemote().sendText("Reload");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
    @OnOpen
    public synchronized void onOpen(Session session, EndpointConfig endpointConfig) {
    	synchronized(sessions) {
    		if (!sessions.contains(session)) {
    			sessions.add(session);
    		}
    	}
    	
    }
    @OnError
    public synchronized void onError(Session session, Throwable throwable) {
    }
    @OnClose
    public synchronized void onClose(Session session, CloseReason closeReason) {
    	synchronized(sessions) {
    		if (sessions.contains(session)) {
    			sessions.remove(session);
    		}
    	}
    	
    }
}
