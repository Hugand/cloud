import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;
import java.util.*;
import controllers.CloudDirectoryController;
import com.google.gson.Gson; 
import java.nio.file.NoSuchFileException;

@ServerEndpoint("/cloud_websocket")         
@ApplicationScoped
public class CloudWebSocket {
    private final ArrayList<Session> sessionsBuff = new ArrayList<>();
    private final Map<String, ArrayList<Session>> sessions = new HashMap<>();
    private final Gson gson = new Gson();

    CloudWebSocket() {
        sessions.put("./", new ArrayList<>());
        Timer timer = new Timer();
        timer.schedule(new CloudFilesTimer(this), 0, 1000);
    }

    @OnOpen
    public void onOpen(Session session) {
        sessionsBuff.add(session);
        System.out.println(sessions);
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

        for(String dir : sessions.keySet()) {
            try {
                Set<Map<String, String>> filesList = cloudDirectoryController.getFilesList(dir);
                String jsonStringified = gson.toJson(filesList);

                this.cloudWebSocket.broadcastDir(dir, jsonStringified);

                /*
                * LEAVE THIS HERE FOR NOW
                */
                // if(!filesList.equals(this.lastFileList.get(dir))) {
                //     this.cloudWebSocket.broadcastDir(dir, jsonStringified);
                //     this.lastFileList.put(dir, filesList);
                // }

            } catch (NoSuchFileException e) {
                System.out.println(e);
                Map<String, String> errorDataMap = new HashMap<>();
                errorDataMap.put("type", "error");
                errorDataMap.put("error", "invalid_dir");

                String errorData = gson.toJson(errorDataMap);

                cloudWebSocket.broadcastDir(dir, errorData);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }
}