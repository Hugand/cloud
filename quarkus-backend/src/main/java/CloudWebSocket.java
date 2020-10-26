package org.ugomes.websockets;

import java.util.Map;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;
import java.util.Arrays; 
import java.util.Timer;
import java.util.TimerTask;

import org.ugomes.controllers.CloudDirectoryController;

import com.google.gson.Gson; 

@ServerEndpoint("/chat/{username}")         
@ApplicationScoped
public class CloudWebSocket {
    // Map<String, Session> sessions = new ConcurrentHashMap<>(); 
    private ArrayList<Session> sessions = new ArrayList<>();
    private Timer timer = new Timer();

    CloudWebSocket() {
        timer.schedule(new CloudFilesTimer(this), 0, 1000);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        // sessions.put(username, session);
        sessions.add(session);
        broadcast("User " + username + " joined");
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessions.remove(session);
        broadcast("User " + username + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        sessions.remove(session);
        broadcast("User " + username + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username) {
        // broadcast(">> " + username + ": " + message);
    }

    public void broadcast(String message) {
        sessions.forEach(s -> {
            s.getAsyncRemote().sendObject(message, result -> {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }
}

class CloudFilesTimer extends TimerTask {
    private String[] lastFileList;
    private CloudDirectoryController cloudDirectoryController = new CloudDirectoryController();
    CloudWebSocket cloudWebSocket;

    public CloudFilesTimer(CloudWebSocket cloudWebSocket) {
        this.cloudWebSocket = cloudWebSocket;
    }

    public void run() {
        Gson gson = new Gson();
        String[] filesList = cloudDirectoryController.getFilesList();
        String jsonStringified = gson.toJson(filesList);

        if(!Arrays.equals(filesList, this.lastFileList)) {
            this.cloudWebSocket.broadcast(jsonStringified);
            this.lastFileList = filesList;
        }
    }
}