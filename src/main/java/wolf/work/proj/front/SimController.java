package wolf.work.proj.front;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import wolf.work.proj.lab.*;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import wolf.work.proj.lab.Record;

public class SimController {
    public static int currentTime;
    @FXML
    public VBox infoContainer;
    @FXML
    public Label infoTime;
    @FXML
    public Label infoRecords;
    @FXML
    public Label infoLegalRecords;
    @FXML
    public Label infoIndividualRecords;
    @FXML
    public Group objGroup;
    @FXML
    public Button startButton;
    @FXML
    public Button stopButton;
    @FXML
    public Pane simObjects;
    
    private Timer timer;


    @FXML
    public Label timeDisplay;
    private boolean timeDisplayState = true;

    @FXML
    public void launch() {
        objGroup.setVisible(true);
        objGroup.setDisable(false);
        simObjects.setVisible(true);
        simObjects.setDisable(false);
        infoContainer.setVisible(false);
        initializeTimer();
        startButton.setDisable(true);
        stopButton.setDisable(false);
        System.out.println("sim launched");

    }

    @FXML
    public void stop() {
        timer.stop();
        setInfo();
        infoContainer.setVisible(true);
        Habitat.clearObjArray();
        objGroup.getChildren().clear();
        changeCounter(0);
        startButton.setDisable(false);
        stopButton.setDisable(true);
        System.out.println("sim stopped");
    }

    @FXML
    public void toggleTimeShow() {
        timeDisplay.setVisible(!timeDisplayState);
        timeDisplayState = !timeDisplayState;
        System.out.println("Time toggled");
    }

    @FXML
    public void debug() {
        System.out.println("aaaaaaaaaaaa");
    }
    public void initializeButtons() {
        startButton.setOnAction(actionEvent -> {
            launch();
        });
        stopButton.setOnAction(actionEvent -> {
            stop();
        });
    }
    //создали запустили таймер
    public void initializeTimer() {
        
        timer = new Timer(this);
        Thread timerThread = new Thread(timer);
        timerThread.setDaemon(true);
        timerThread.start();
    }
    //рисуем объект
    public void instantiateObj(Record obj) {
        if (Platform.isFxApplicationThread()) {
            ImageView preview = obj.getSpriteView();
            objGroup.getChildren().add(preview);
        } else {
            Platform.runLater(() -> instantiateObj(obj));
        }
    }
    //Счетчик времени
    public void changeCounter(int currTime) {
        Platform.runLater(() -> {
            if (timeDisplay != null) {
                currentTime = currTime;
                timeDisplay.setText(String.valueOf(currentTime));
            } else {
                System.err.println("timeDisplay is null!");
            }
        });
    }
    // Вывод информации в окно
    public void setInfo() {
        infoTime.setText("Времени прошло: " + currentTime);
        infoRecords.setText("Записей создано: " + Record.getObjCount());
        infoLegalRecords.setText("Юр. лиц создано: " + LegalRecord.getTypeCount());
        infoIndividualRecords.setText("Физ. лиц создано: " + IndividualRecord.getTypeCount());
    }
    public void setStartButton() {
        startButton.setDisable(true);
        stopButton.setDisable(false);
        launch();

    }
    public void setStopButton() {
        startButton.setDisable(false);
        stopButton.setDisable(true);
        stop();
    }
}
