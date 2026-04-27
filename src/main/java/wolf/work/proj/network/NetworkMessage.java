package wolf.work.proj.network;

import java.io.Serializable;
import java.util.List;

public class NetworkMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Type {
        CONNECT,
        CLIENT_LIST,
        SYNC_PROB
    }

    public Type type;
    public String senderId;
    public String targetId;
    public double individualSpawnChance;
    public double legalSpawnChance;
    public List<String> clientList;
}