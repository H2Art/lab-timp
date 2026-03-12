package wolf.work.proj.lab;

import java.util.ArrayList;

public class ObjectsArraySingleton {
    private static ObjectsArraySingleton instance;
    private final ArrayList<Record> objArray;
    private ObjectsArraySingleton() {
        this.objArray = new ArrayList<>();
    }
    public static ObjectsArraySingleton getInstance() {
        if (instance == null) {
            instance = new ObjectsArraySingleton();
        }
        return instance;
    }
    public void addRecord(Record record) {
        objArray.add(record);
    }
    public void clear() {
        objArray.clear();
    }

}
