package hello;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@ServerEndpoint("/websocket/{username}")
@Component
public class WebSocketServer {
	
	// Store all socket session and their corresponding username.
    private static Map<Session, String> sessionUsernameMap = new HashMap<>();
    private static Map<String, Session> usernameSessionMap = new HashMap<>();
    
    private static Map<String, List<String>> conversationHistory = new HashMap<>();
    
    
    @OnOpen
    public void onOpen(
    	      Session session, 
    	      @PathParam("username") String username) throws IOException 
    {
        System.out.println("Entered into Open\n");
        String[] parts = username.split("\\$");
        String uname = parts[0];
        String othername = parts[1];
        String key = "";
        if(uname.compareTo(othername) <= 0) {
        	key = uname + "?" + othername;
        } else {
        	key = othername + "?" + uname;
        }
        
        sessionUsernameMap.put(session, uname);
        usernameSessionMap.put(uname, session);
        
        List<String> q = conversationHistory.get(key);
        if(q != null) {
        	for(int i = 0; i < q.size(); i++){
        		String msg = q.get(i);
        		sendMessageToPArticularUser(uname, msg);
        	}
        }
		
    }
 
    
    
    
    @OnMessage
    public void onMessage(Session session, String message) throws IOException 
    {
        // Handle new messages
    	String username = sessionUsernameMap.get(session);
    	
    	if (message.startsWith("@")) 
    	{
    		String destUsername = message.split(" ")[0].substring(1); // don't do this in your code!
    		String tmp = "@" + destUsername;
    		String msg = message.substring(message.indexOf(tmp)  + tmp.length(), message.length());
    		msg = msg.trim();
    		
    		String key;
            if(username.compareTo(destUsername) <= 0) {
            	key = username + "?" + destUsername;
            } else {
            	key = destUsername + "?" + username;
            }
            
    		List<String> history = conversationHistory.get(key);
    		if(history != null) {
    			history.add(username + ": " + msg);
    			conversationHistory.put(key, history);
    		} else {
    			List<String> q = new LinkedList<>();
    			q.add(username + ": " + msg);
    			conversationHistory.put(key, q);
    		}
    		
    		sendMessageToPArticularUser(destUsername, username + ": " + msg);
    		sendMessageToPArticularUser(username, username + ": " + msg);
    	}
    	else // Message to whole chat
    	{
	    	//do nothing
    	}
    }
 
    @OnClose
    public void onClose(Session session) throws IOException
    {
    	System.out.print("Entered into Close");
    	
    	String username = sessionUsernameMap.get(session);
    	sessionUsernameMap.remove(session);
    	usernameSessionMap.remove(username);
        
    	String message= username + " disconnected";
        System.out.print(message);
    }
 
    @OnError
    public void onError(Session session, Throwable throwable) 
    {
        // Do error handling here
    	System.out.print("Entered into Error");
    }
    
    
    
    
    
    /**
     * Sending message to a specific user 
     * @param username The user you want to send message to 
     * @param message The message you want to send
     **/
	private boolean sendMessageToPArticularUser(String username, String message) 
    {	
		if(usernameSessionMap.get(username) != null) {
			try {
				usernameSessionMap.get(username).getBasicRemote().sendText(message);
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		} else {
			//it doesnt matter if code reaches here.
			return false;
		}
    }
    
	
	
	
	/**
	 * Sending messages to all users 
	 * @param message The message you want to send
	 * @throws IOException 
	 **/
    private static void broadcast(String message) 
    	      throws IOException 
    {	  
    	sessionUsernameMap.forEach((session, username) -> {
    		synchronized (session) {
	            try {
	                session.getBasicRemote().sendText(message);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    });
	}
    
    
    
    /**
     * Show the online people 
     * @return The number of people online
     **/
    public static synchronized int getOnlineCount()
    {
	     return usernameSessionMap.size();	
    }
   
}
    
    
    
    
    
    

