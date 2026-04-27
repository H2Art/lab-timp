package wolf.work.proj.network;

import java.io.Serializable;

public class NetworkMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Type {
        CONNECT,       // при подключении (сервер может прислать ID)
        CLIENT_LIST,   // список клиентов
        SYNC_PROB      // синхронизация вероятностей
    }

    public Type type;
    public String senderId;
    public String targetId;
    public double individualSpawnChance;  // для варианта 10
    public double legalSpawnChance;       // для варианта 10
    public java.util.List<String> clientList;
}