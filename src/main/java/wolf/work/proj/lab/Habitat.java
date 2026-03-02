package wolf.work.proj.lab;
import javafx.application.Platform;
import wolf.work.proj.front.LabApplication;
import wolf.work.proj.front.LabController;

import java.lang.Math;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Habitat {
    private final LabController controller;
    Habitat(LabController controller) {
        this.controller = controller;
    }
    static int currentTime = 0;
    // Константы
    final int N1 = 3;
    final int N2 = 5;
    final double P1 = 0.7;
    final double P2 = 0.6;
    final static int WIDTH = 800;
    final static int HEIGHT = 650;


    // Массив, содержащий ссылки на объекты
    public static Record[] objects = new Record[500];

    // Метод, работающий постоянно?
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
        int c = 0;
        Record.objCount = 0;
        IndividualRecord.indCount = 0;
        LegalRecord.legCount = 0;
        for (Record record: objects) {
            if (record == null) {
                break;
            }
            objects[c] = null;
            record = null;
            c++;
        }
    }
    void SpawnObject(String type) {
        double rand = Math.random();
        Record record;
        if (type == "Legal" && rand <= P1) {
            record = new LegalRecord();
            controller.instantiateObj(record);
        }
        else if (type == "Individual" && rand <= P2) {
            record = new IndividualRecord();
            controller.instantiateObj(record);
        } else {
            record = null;
        }
        if (record != null) {
            Platform.runLater(() -> {
                controller.instantiateObj(record);
                objects[Record.getObjCount() - 1] = record;
            });

        }
    }
    public static void showInfo() {
        System.out.println("Общее количество записей: " + Record.getObjCount());
        System.out.println("ind: " + IndividualRecord.getTypeCount());
        System.out.println("legal: " + LegalRecord.getTypeCount());
        System.out.println("Time: " + currentTime);
    }
}
