package wolf.work.proj.lab;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SimulationSerializer {

    public static void save(Stage owner, ObjectsArraySingleton world, double currentTime) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить симуляцию");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Serialized simulation", "*.ser"));
        fileChooser.setInitialFileName("simulation.ser");

        File file = fileChooser.showSaveDialog(owner);
        if (file == null) return;

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            SimulationSnapshot snapshot = new SimulationSnapshot();
            snapshot.setCurrentTime(currentTime);
            snapshot.setSaveTime(System.currentTimeMillis());

            List<Record> inds = new ArrayList<>();
            List<Record> legs = new ArrayList<>();
            for (Record r : world.getAllObjects()) {
                if ("Individual".equals(r.getType())) {
                    inds.add(r);
                } else if ("Legal".equals(r.getType())) {
                    legs.add(r);
                }
            }
            snapshot.setIndividualRecords(inds);
            snapshot.setLegalRecords(legs);

            oos.writeObject(snapshot);
            System.out.println("Симуляция сохранена: " + file.getAbsolutePath());
            System.out.println("Объектов сохранено: " + (inds.size() + legs.size()));
        } catch (IOException e) {
            System.err.println("Ошибка сохранения: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static SimulationSnapshot load(Stage owner) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузить симуляцию");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Serialized simulation", "*.ser"));

        File file = fileChooser.showOpenDialog(owner);
        if (file == null) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            SimulationSnapshot snapshot = (SimulationSnapshot) ois.readObject();
            System.out.println("Симуляция загружена: " + file.getAbsolutePath());
            return snapshot;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка загрузки: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}