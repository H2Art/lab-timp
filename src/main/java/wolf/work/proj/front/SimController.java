package wolf.work.proj.front;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import wolf.work.proj.lab.*;
import javafx.fxml.FXML;

import wolf.work.proj.lab.Record;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Vector;

public class SimController {
    private LegalAI legalAI;
    private IndividualAI individualAI;

    public static double currentTime;
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
    public Button currentObjectsButton;

    @FXML
    public ComboBox<String> legalPriorityBox;
    @FXML
    public ComboBox<String> individualPriorityBox;

    @FXML
    public TextField indPeriodField;
    public String oldIndPeriodValue = "5";
    @FXML
    public TextField legalPeriodField;
    public String oldLegalPeriodValue = "3";

    @FXML
    public TextField indLifespanField;
    public String oldIndLifespanValue = "5";
    @FXML
    public TextField legalLifespanField;
    public String oldLegalLifespanValue = "5";

    @FXML
    public ComboBox<String> indSpawnChanceBox;
    @FXML
    public ComboBox<String> legalSpawnChanceBox;
    @FXML
    public RadioButton timeShowOn;
    @FXML
    public RadioButton timeShowOff;
    @FXML
    public MenuItem menuStartButton;
    @FXML
    public MenuItem menuStopButton;
    
    private Timer timer;

    @FXML
    public CheckBox alertCheckBox;

    @FXML
    public Label timeDisplay;
    private boolean timeDisplayState = true;

    @FXML
    public CheckBox toggleIndividualAI;

    @FXML
    public CheckBox toggleLegalAI;

    @FXML
    public void launch() {
        SimApplication.setSimulationState(true);
        initializeTimer();
        startButton.setDisable(true);
        stopButton.setDisable(false);
        menuStartButton.setDisable(true);
        menuStopButton.setDisable(false);
        System.out.println("sim launched");

    }

    @FXML
    public void stop() {
        SimApplication.setSimulationState(false);
        // остановка потоков
        timer.stop();
        if (legalAI != null) legalAI.stop();
        if (individualAI != null) individualAI.stop();
        Habitat.clearObjArray();
        objGroup.getChildren().clear();
        changeCounter(0);
        startButton.setDisable(false);
        stopButton.setDisable(true);
        menuStartButton.setDisable(false);
        menuStopButton.setDisable(true);
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
        timeShowOn.setSelected(timeDisplayState);
        timeShowOff.setSelected(!timeDisplayState);
        System.out.println("Time toggled");
    }

    @FXML
    public void debug() {
        System.out.println(showInfoState);
    }
    //создали запустили таймер
    public void initializeTimer() {
        legalAI = new LegalAI();
        individualAI = new IndividualAI();
        legalAI.start();
        individualAI.start();
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
    public void changeCounter(double currTime) {
        Platform.runLater(() -> {
            if (timeDisplay != null) {
                currentTime = currTime;
                timeDisplay.setText(String.format("%.2f", currentTime));
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


    //НАЙТИ СПОСОБ СОКРАТИТЬ!
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
    public void changeIndLifespan() {
        String newValue = indLifespanField.getText();
        if (validateValue(newValue)) {
            Habitat.INDIVIDUAL_LIFESPAN = Integer.parseInt(newValue);
        }
        else {
            indLifespanField.setText(oldIndLifespanValue);
        }
        oldIndLifespanValue = indLifespanField.getText();
        System.out.println("ind_lifespan" + Habitat.INDIVIDUAL_LIFESPAN);
    }
    public void changeLegalLifespan() {
        String newValue = legalLifespanField.getText();
        if (validateValue(newValue)) {
            Habitat.LEGAL_LIFESPAN = Integer.parseInt(newValue);
        }
        else {
            legalLifespanField.setText(oldLegalLifespanValue);
        }
        oldLegalLifespanValue = legalLifespanField.getText();
        System.out.println("leg_lifespan" + Habitat.LEGAL_LIFESPAN);
    }
    // !

    public boolean validateValue(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        try {
            int n = Integer.parseInt(text);
            if (n <= 0) {
                showInputAlert();
                return false;
            }
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
        legalPriorityBox.setItems(initObservablePriorityList());
        individualPriorityBox.setItems(initObservablePriorityList());
        legalPriorityBox.setValue("5");
        individualPriorityBox.setValue("5");
    }
    public ObservableList<String> initObservableList() {
        ArrayList<String> stringList = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            String elem = String.valueOf((double)i / 10);
            stringList.add(elem);
        }
        return FXCollections.observableArrayList(stringList);
    }
    public ObservableList<String> initObservablePriorityList() {
        ArrayList<String> stringList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            String elem = String.valueOf(i);
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
    public void setShowInfoState() {
        showInfoState = !showInfoState;
        alertCheckBox.setSelected(showInfoState);
    }
    public void abort() {
        Platform.exit();
    }

    public void removeObjectFromView(Record obj) {
        Platform.runLater(() -> {
            ImageView view = obj.getSpriteView();
            if (view != null && objGroup.getChildren().contains(view)) {
                objGroup.getChildren().remove(view);
            }
        });
    }
    public void clearAllObjectsFromView() {
        Platform.runLater(() -> {
            objGroup.getChildren().clear();
        });
    }
    public void showCurrentObjects() {
        DialogAliveObjects dialog = new DialogAliveObjects(ObjectsArraySingleton.getInstance().getObjectsByBirthTime());
        dialog.initOwner(objGroup.getScene().getWindow());
        dialog.showAndWait();
    }

    public void redrawAllObjects() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::redrawAllObjects);
            return;
        }

        Vector<Record> objects = ObjectsArraySingleton.getInstance().getAllObjects();
        for (Record r : objects) {
            ImageView view = r.getSpriteView();
            if (view != null) {
                view.setX(r.getX());
                view.setY(r.getY());
            }
        }
    }

    public void setToggleIndividualAI() {
        individualAI.togglePause();
    }
    public void setToggleLegalAI() {
        legalAI.togglePause();
    }

    public void setLegalPriorityBox() {
        legalAI.getThread().setPriority(Integer.parseInt(legalPriorityBox.getValue()));
    }

    public void setIndividualPriorityBox() {
        individualAI.getThread().setPriority(Integer.parseInt(legalPriorityBox.getValue()));
    }

}
