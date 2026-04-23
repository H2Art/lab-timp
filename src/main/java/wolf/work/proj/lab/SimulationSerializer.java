package wolf.work.proj.lab;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SimulationSerializer {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    public static void save(Stage owner, ObjectsArraySingleton world, double currentTime) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить симуляцию");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON files", "*.json")
        );
        fileChooser.setInitialFileName("simulation.json");

        File file = fileChooser.showSaveDialog(owner);
        if (file == null) return;

        try {
            SimulationSnapshot snapshot = new SimulationSnapshot();
            snapshot.setSaveTime(System.currentTimeMillis());
            snapshot.setCurrentTime(currentTime);

            ArrayList<RecordDTO> individualRecords = new ArrayList<>();
            ArrayList<RecordDTO> legalRecords = new ArrayList<>();

            // Проходим по всем живым объектам
            for (Record r : world.getAllObjects()) {
                if (r.getType().equals("Individual")) {
                    individualRecords.add(new RecordDTO(r));
                } else if (r.getType().equals("Legal")) {
                    legalRecords.add(new RecordDTO(r));
                }
            }

            snapshot.setIndividualRecords(individualRecords);
            snapshot.setLegalRecords(legalRecords);

            mapper.writeValue(file, snapshot);
            System.out.println("Симуляция сохранена: " + file.getAbsolutePath());
            System.out.println("Объектов сохранено: " + (individualRecords.size() + legalRecords.size()));

        } catch (IOException e) {
            System.err.println("Ошибка сохранения: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static SimulationSnapshot load(Stage owner) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузить симуляцию");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON files", "*.json")
        );

        File file = fileChooser.showOpenDialog(owner);
        if (file == null) return null;

        try {
            SimulationSnapshot snapshot = mapper.readValue(file, SimulationSnapshot.class);
            System.out.println("Симуляция загружена: " + file.getAbsolutePath());
            System.out.println("Физических объектов: " + snapshot.getIndividualRecords().size());
            System.out.println("Юридических объектов: " + snapshot.getLegalRecords().size());
            return snapshot;
        } catch (IOException e) {
            System.err.println("Ошибка загрузки: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}