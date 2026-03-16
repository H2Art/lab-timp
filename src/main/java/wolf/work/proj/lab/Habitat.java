package wolf.work.proj.lab;
import wolf.work.proj.front.SimController;
import java.lang.Math;

public class Habitat {
    private final SimController controller;
    Habitat(SimController controller) {
        this.controller = controller;
    }
    static int currentTime = 0;
    // Константы
    public static int LEGAL_PERIOD = 3;
    public static int INDIVIDUAL_PERIOD = 5;
    public static double LEGAL_SPAWN_CHANCE = 0.5;
    public static double INDIVIDUAL_SPAWN_CHANCE = 0.5;
    public final static int WIDTH = 1800;
    public final static int HEIGHT = 900;



    // Метод, работающий постоянно
    void Update(int counter) {
        currentTime = counter;
        if (counter % LEGAL_PERIOD == 0) {
            SpawnObject("Legal");
        }
        if (counter % INDIVIDUAL_PERIOD == 0) {
            SpawnObject("Individual");
        }
        controller.changeCounter(currentTime);
    }
    public static void clearObjArray() {
        Record.objCount = 0;
        IndividualRecord.indCount = 0;
        LegalRecord.legCount = 0;
        ObjectsArraySingleton.getInstance().clear();
    }
    void SpawnObject(String type) {
        double rand = Math.random();
        Record record;
        if (type.equals("Legal") && rand <= LEGAL_SPAWN_CHANCE) {
            record = new LegalRecord();
            controller.instantiateObj(record);
        }
        else if (type.equals("Individual") && rand <= INDIVIDUAL_SPAWN_CHANCE) {
            record = new IndividualRecord();
            controller.instantiateObj(record);
        } else {
            record = null;
        }
        if (record != null) {
            ObjectsArraySingleton.getInstance().addRecord(record);
        }
    }
}
