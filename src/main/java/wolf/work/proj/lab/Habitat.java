package wolf.work.proj.lab;
import wolf.work.proj.front.LabController;
import java.lang.Math;
import java.util.ArrayList;

public class Habitat {
    private final LabController controller;
    Habitat(LabController controller) {
        this.controller = controller;
    }
    static int currentTime = 0;
    // Константы
    final int N1 = 3;
    final int N2 = 5;
    final double P1 = 0.9;
    final double P2 = 0.9;
    public final static int WIDTH = 1200;
    public final static int HEIGHT = 800;



    // Метод, работающий постоянно
    void Update(int counter) {
        currentTime = counter;
        if (counter % N1 == 0) {
            SpawnObject("Legal");
        }
        if (counter % N2 == 0) {
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
        if (type.equals("Legal") && rand <= P1) {
            record = new LegalRecord();
            controller.instantiateObj(record);
        }
        else if (type.equals("Individual") && rand <= P2) {
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
