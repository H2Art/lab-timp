package wolf.work.proj.lab;

import wolf.work.proj.front.SimController;
import java.util.*;

public class ObjectsArraySingleton {
    private static ObjectsArraySingleton instance;

    private final Vector<Record> objVector;
    private final TreeSet<Integer> idTree;
    private final HashMap<Integer,Double> birthDict;

    private SimController controller; // связываем контроллер

    private ObjectsArraySingleton() {
        this.objVector = new Vector<>(10, 5);
        this.idTree = new TreeSet<Integer>();
        this.birthDict = new HashMap<Integer,Double>();
    }
    public static synchronized ObjectsArraySingleton getInstance() {
        if (instance == null) {
            instance = new ObjectsArraySingleton();
        }
        return instance;
    }

    public void setController(SimController controller) {
        this.controller = controller;
    }

    public synchronized boolean containsId(int id) {
        return idTree.contains(id);
    }

    public synchronized void addRecord(Record record) {
        objVector.add(record);
        idTree.add(record.getID());
        birthDict.put(record.getID(), record.getSpawnTime());
    }
    public synchronized void removeByID(int id) {
        if (!idTree.contains(id)) {
            return;
        }
        Record objToRemove = null;
        for (Record r : objVector) {
            if (r.getID() == id) {
                objToRemove = r;
                break;
            }
        }
        idTree.remove(id);
        objVector.removeIf(record -> record.getID() == id);
        birthDict.remove(id);

        // Удаляем визуальное представление
        if (objToRemove != null && controller != null) {
            controller.removeObjectFromView(objToRemove);
        }
    }
    public synchronized void clear() {
        if (controller != null) {
            controller.clearAllObjectsFromView();
        }
        objVector.clear();
        idTree.clear();
        birthDict.clear();
    }
    public synchronized void deleteAllExpiredObjects(int time) {
        if (instance.objVector.isEmpty()) {
            return;
        }
        List<Integer> expiredIds = new ArrayList<>();
        for (Record r : instance.objVector) {
            if ((time / 100.0) - r.getSpawnTime() >= r.getLifespan()) {
                expiredIds.add(r.getID());
            }
        }
        for (int id : expiredIds) {
            removeByID(id);
        }
    }
    public synchronized Map<Double, Vector<Record>> getObjectsByBirthTime() {
        Map<Double, Vector<Record>> result = new HashMap<>();
        for (Record r : objVector) {
            double birthTime = r.getSpawnTime();
            result.computeIfAbsent(birthTime, k -> new Vector<>()).add(r);
        }
        return result;
    }
    public synchronized void updateObjectsCoordinates(String givenType) {
        if (getInstance().objVector.isEmpty()) {
            return;
        }
        for (Record r : getInstance().objVector) {
            if (r.getType().equals(givenType)) {
                r.makeStep();
            }
        }
    }
    public synchronized Vector<Record> getAllObjects() {
        return new Vector<>(objVector);  // возвращаем копию
    }
}