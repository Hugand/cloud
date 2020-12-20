package controllers;

import com.google.gson.Gson;
import configs.CloudProperties;
import helpers.CloudFileSorter;
import models.CloudFile;
import models.CloudStorage;
import models.responses.WSDataResponse;
import models.responses.WSResponse;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.nio.file.NoSuchFileException;
import java.util.*;

@ServerEndpoint("/cloud_websocket")         
//@ApplicationScoped
public class CloudWebSocket {
    private final ArrayList<Session> sessionsBuff = new ArrayList<>();
    private final Map<String, ArrayList<Session>> sessions = new HashMap<>();
    private final Gson gson = new Gson();

    CloudWebSocket() {
        sessions.put("./", new ArrayList<>());
        Timer timer = new Timer();
        timer.schedule(new CloudFilesTimer(this), 0, 1000);

        System.out.println(CloudProperties.MAX_AVAILABLE_SPACE + "");
	    System.out.println("WebS ocket running 2");
    }

    @OnOpen
    public void onOpen(Session session) {
        sessionsBuff.add(session);
        System.out.println(sessions);
        System.out.println(session);
    }

    @OnClose
    public void onClose(Session session) {
        this.deleteSession(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        this.deleteSession(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        Map clientData = gson.fromJson(message,  Map.class);

        Object type = clientData.get("type");
        if ("initialConn".equals(type)) {
            sessions.get("./").add(session);
            sessionsBuff.remove(session);
        } else if ("cd".equals(type)) {
            System.out.println(clientData);
            this.changeDirectory(session, clientData);
        }
        System.out.println(sessions);
    }

    public void broadcastAll(String message) {
        sessions.values().forEach(r -> r.forEach(s -> s.getAsyncRemote().sendObject(message, result -> {
            if (result.getException() != null) {
                System.out.println("Unable to send message: " + result.getException());
            }
        })));
    }

    public void broadcastDir(String currDir, String message) {
        sessions.get(currDir).forEach(s -> s.getAsyncRemote().sendObject(message, result -> {
            if (result.getException() != null) {
                System.out.println("Unable to send message: " + result.getException());
            }
        }));
    }

    public void broadcastSession(Session session, String message) {
        session.getAsyncRemote().sendObject(message, result -> {
            if (result.getException() != null) {
                System.out.println("Unable to send message: " + result.getException());
            }
        });
    }

    public Map<String, ArrayList<Session>> getSessions() { return this.sessions; }

    private void deleteSession(Session session) {
        for(String dir : sessions.keySet()) {
            sessions.get(dir).remove(session);
        }
    }

    private void changeDirectory(Session session, Map<String, String> clientData) {
        String currentDir = clientData.get("directory");
        String prevDir = clientData.get("prevDir");
        sessions.computeIfAbsent(currentDir, k -> new ArrayList<>());
        
        sessions.get(currentDir).add(session);
        sessions.get(prevDir).remove(session);
    }

    public void deleteDirInSessions(String currDir) {
        this.sessions.get("./").addAll(this.sessions.get(currDir));
        this.sessions.remove(currDir);
    }
}

class CloudFilesTimer extends TimerTask {
    private final CloudDirectoryController cloudDirectoryController = new CloudDirectoryController();
    private final CloudWebSocket cloudWebSocket;
    private final Gson gson = new Gson();

    public CloudFilesTimer(CloudWebSocket cloudWebSocket) {
        this.cloudWebSocket = cloudWebSocket;
    }

    public void run() {
        Map<String, ArrayList<Session>> sessions = this.cloudWebSocket.getSessions();
        //System.out.println("TIMERING");

        for(String dir : sessions.keySet()) {
            try {
                List<CloudFile> filesList = cloudDirectoryController.getFilesList(dir);
                filesList.sort(new CloudFileSorter());
                CloudStorage cloudStorage = cloudDirectoryController.getAvailableSpaceInBytes();
                WSDataResponse wsDataResponse = new WSDataResponse("success", filesList, cloudStorage);
                String wsDataResponseStringified = gson.toJson(wsDataResponse, WSDataResponse.class);

                //System.out.println("Broadcasting dir");
                this.cloudWebSocket.broadcastDir(dir, wsDataResponseStringified);
            } catch (NoSuchFileException e) {
                WSResponse wsResponse = new WSResponse("error", "INVALID_DIR");
                String errorData = gson.toJson(wsResponse, WSResponse.class);
                System.out.println("Broadcasting dir error");

                this.cloudWebSocket.broadcastDir(dir, errorData);

                this.cloudWebSocket.deleteDirInSessions(dir);
                System.out.println(e);
            } catch (Exception e) {
                this.cloudWebSocket.deleteDirInSessions(dir);
                System.out.println(e);
            }
        }
    }
}
