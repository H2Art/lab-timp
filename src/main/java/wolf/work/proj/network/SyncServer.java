package wolf.work.proj.network;

import wolf.work.proj.lab.Configuration;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class SyncServer {

    private static final ConcurrentHashMap<String, ObjectOutputStream> clients = new ConcurrentHashMap<>();
    private static int PORT;
    private static String IP;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws UnknownHostException {
        Configuration config = new Configuration();
        config.readConfig();
        System.out.print("Введите IP: ");//
        IP = scanner.nextLine();
        System.out.print("Введите порт: ");// читаем остальные настройки, если нужно
        PORT = scanner.nextInt();
        InetAddress address = InetAddress.getByName(IP);

        try (ServerSocket serverSocket = new ServerSocket(PORT, 50, address)) { // порт 0 = автоназначение
            int actualPort = PORT;

            System.out.println("Сервер синхронизации запущен на порту " + actualPort);
            System.out.println("Сообщите этот порт клиентам для подключения.");

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
    public static int getPort() {
        return PORT;
    }
}