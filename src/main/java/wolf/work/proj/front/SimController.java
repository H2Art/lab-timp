package wolf.work.proj.front;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import wolf.work.proj.lab.*;
import javafx.fxml.FXML;

import wolf.work.proj.lab.Record;

import java.util.ArrayList;
import java.util.Optional;

public class SimController {
    public static int currentTime;
    public boolean showInfoState = true;
    @FXML
    public Group objGroup;
    @FXML
    public Button startButton;
    @FXML
    public Button stopButton;
    @FXML
    public Pane simObjects;
    @FXML
    public Pane mainMenuObjects;

    @FXML
    public TextField indPeriodField;
    public String oldIndPeriodValue = "5";
    @FXML
    public TextField legalPeriodField;
    public String oldLegalPeriodValue = "3";
    @FXML
    public ComboBox<String> indSpawnChanceBox;
    @FXML
    public ComboBox<String> legalSpawnChanceBox;
    @FXML
    public Button timeShowOn;
    @FXML
    public Button timeShowOff;
    
    private Timer timer;


    @FXML
    public Label timeDisplay;
    private boolean timeDisplayState = true;

    @FXML
    public void launch() {
        SimApplication.setSimulationState(true);
        setMainMenu(false);
        initializeTimer();
        startButton.setDisable(true);
        stopButton.setDisable(false);
        System.out.println("sim launched");

    }

    @FXML
    public void stop() {
        SimApplication.setSimulationState(false);
        timer.stop();
        Habitat.clearObjArray();
        objGroup.getChildren().clear();
        changeCounter(0);
        startButton.setDisable(false);
        stopButton.setDisable(true);
        setMainMenu(true);
        System.out.println("sim stopped");
    }
    @FXML
    public void pause() {
        if (!showInfoState) {
            stop();
            return;
        }
        System.out.println("paused");
        timer.togglePause();
        showPauseAlert();
    }

    @FXML
    public void toggleTimeShow() {
        timeDisplay.setVisible(!timeDisplayState);
        timeDisplayState = !timeDisplayState;
        timeShowOn.setDisable(timeDisplayState);
        timeShowOff.setDisable(!timeDisplayState);
        System.out.println("Time toggled");
    }

    @FXML
    public void debug() {
        System.out.println(showInfoState);
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
    public void setStartButton() {
        startButton.setDisable(true);
        stopButton.setDisable(false);
        launch();
    }
    public void setStopButton() {
        System.out.println("paused2");
        pause();
    }

    public void changeIndPeriod() {
        String newValue = indPeriodField.getText();
        if (validateValue(newValue)) {
            Habitat.INDIVIDUAL_PERIOD = Integer.parseInt(newValue);
        }
        else {
            indPeriodField.setText(oldIndPeriodValue);
        }
        oldIndPeriodValue = indPeriodField.getText();
        System.out.println("ind" + Habitat.INDIVIDUAL_PERIOD);
    }
    public void changeLegalPeriod() {
        String newValue = legalPeriodField.getText();
        if (validateValue(newValue)) {
            Habitat.LEGAL_PERIOD = Integer.parseInt(newValue);
        }
        else {
            legalPeriodField.setText(oldLegalPeriodValue);
        }
        oldLegalPeriodValue = legalPeriodField.getText();
        System.out.println("leg" + Habitat.LEGAL_PERIOD);
    }
    public boolean validateValue(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(text);
            return true;
        }
        catch (NumberFormatException e) {
            showInputAlert();
            return false;
        }
    }

    public void initComboBoxes() {
        indSpawnChanceBox.setItems(initObservableList());
        legalSpawnChanceBox.setItems(initObservableList());
        indSpawnChanceBox.setValue("0.5");
        legalSpawnChanceBox.setValue("0.5");
    }
    public ObservableList<String> initObservableList() {
        ArrayList<String> stringList = new ArrayList<String>();
        for (int i = 0; i <= 10; i++) {
            String elem = String.valueOf((double)i / 10);
            stringList.add(elem);
        }
        return FXCollections.observableArrayList(stringList);
    }
    public void setLegalChanceSpawnBox() {
        Habitat.LEGAL_SPAWN_CHANCE = Double.parseDouble(legalSpawnChanceBox.getValue());
    }
    public void setIndChanceSpawnBox() {
        Habitat.INDIVIDUAL_SPAWN_CHANCE = Double.parseDouble(indSpawnChanceBox.getValue());

    }

    public void showInputAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка ввода");
        alert.setContentText("Введите целое число!");
        alert.setHeaderText("Ошибка!");
        alert.showAndWait();
    }
    public void showPauseAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Пауза");
        alert.setHeaderText("Симуляция на паузе");
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefRowCount(5);
        textArea.setPrefColumnCount(30);
        textArea.setText("Симуляция остановлена\n" +
                "Время: " + currentTime + "\n" +
                "Создано объектов: " + Record.getObjCount() + "\n" +
                "Создано физ. лиц: " + IndividualRecord.getTypeCount() + "\n" +
                "Создано юр. лиц: " + LegalRecord.getTypeCount());
        alert.getDialogPane().setContent(textArea);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            startButton.setDisable(false);
            stopButton.setDisable(true);
            stop();
        } else {
            timer.togglePause();
        }
    }

    public void setMainMenu(boolean flag) {
        mainMenuObjects.setVisible(flag);
        mainMenuObjects.setDisable(!flag);
        simObjects.setVisible(!flag);
        simObjects.setDisable(flag);
        objGroup.setVisible(!flag);
        objGroup.setDisable(flag);
    }
    public void setShowInfoState() {
        showInfoState = !showInfoState;
    }

}
