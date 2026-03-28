package wolf.work.proj.front;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import wolf.work.proj.lab.Record;

import java.util.Map;
import java.util.Vector;

public class DialogAliveObjects extends Stage {
    public DialogAliveObjects(Map<Double, Vector<Record>> birthTimeMap) {
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Текущие объекты (по времени рождения)");
        setMinWidth(600);
        setMinHeight(400);

        TextArea textArea = new TextArea();
        textArea.setEditable(false);

        StringBuilder content = new StringBuilder();
        if (birthTimeMap.isEmpty()) {
            content.append("Нет живых объектов");
        } else {
            content.append(String.format("%-15s | %-28s | %-20s%n", "Время рождения", "ID", "Тип"));
            content.append("-".repeat(50)).append("\n");

            // Сортируем по времени рождения
            birthTimeMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        double birthTime = entry.getKey();
                        for (Record r : entry.getValue()) {
                            content.append(String.format("%-27.2f | %-20d | %-20s%n",
                                    birthTime,
                                    r.getID(),
                                    r.getClass().getSimpleName()
                            ));
                        }
                    });
            content.append("\nВсего объектов: ")
                    .append(birthTimeMap.values().stream().mapToInt(Vector::size).sum());
        }

        textArea.setText(content.toString());

        Button closeButton = new Button("Закрыть");
        closeButton.setOnAction(e -> close());

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));
        vbox.getChildren().addAll(
                new Label("Список объектов, сгруппированных по времени рождения:"),
                textArea,
                closeButton
        );

        Scene scene = new Scene(vbox);
        setScene(scene);
    }
}
