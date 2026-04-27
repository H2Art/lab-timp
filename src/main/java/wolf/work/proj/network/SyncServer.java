package wolf.work.proj.network;

import wolf.work.proj.lab.Configuration;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class SyncServer {

    private static final ConcurrentHashMap<String, ObjectOutputStream> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        // Подгружаем конфигурацию, чтобы получить порт
        Configuration config = new Configuration();
        config.readConfig();  // загрузит / создаст config.properties с серверными настройками
        int port = Configuration.SERVER_PORT;
        System.out.println("Сервер синхронизации запущен на порту " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        String clientId = socket.getRemoteSocketAddress().toString();
        System.out.println("Подключился клиент: " + clientId);
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            clients.put(clientId, out);
            broadcastClientList();

            while (true) {
                NetworkMessage msg = (NetworkMessage) in.readObject();
                msg.senderId = clientId;
                if (msg.type == NetworkMessage.Type.SYNC_PROB) {
                    ObjectOutputStream targetOut = clients.get(msg.targetId);
                    if (targetOut != null) {
                        targetOut.writeObject(msg);
                        targetOut.flush();
                        System.out.println("Пересланы вероятности от " + clientId + " к " + msg.targetId);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Клиент отключился: " + clientId);
        } finally {
            clients.remove(clientId);
            broadcastClientList();
        }
    }

    private static void broadcastClientList() {
        NetworkMessage listMsg = new NetworkMessage();
        listMsg.type = NetworkMessage.Type.CLIENT_LIST;
        listMsg.clientList = new java.util.ArrayList<>(clients.keySet());

        for (ObjectOutputStream out : clients.values()) {
            try {
                out.writeObject(listMsg);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}