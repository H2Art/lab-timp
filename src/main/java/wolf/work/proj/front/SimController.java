package wolf.work.proj.front;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import wolf.work.proj.lab.*;
import javafx.fxml.FXML;

import wolf.work.proj.lab.Record;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Vector;

public class SimController {
    private int currentCounter = 0;
    private boolean isLoadingSimulation = false;
    private LegalAI legalAI;
    private IndividualAI individualAI;

    public static double currentTime;
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
    public RadioMenuItem menuShowTimeOn;
    @FXML
    public RadioMenuItem menuShowTimeOff;
    @FXML
    public MenuItem menuStartButton;
    @FXML
    public MenuItem menuStopButton;
    
    private Timer timer;

    @FXML
    public CheckBox alertCheckBox;
    @FXML
    public RadioMenuItem menuShowInfoOn;
    @FXML
    public RadioMenuItem menuShowInfoOff;

    @FXML
    public Label timeDisplay;
    private boolean SHOW_TIME_STATE = true;
    public boolean SHOW_INFO_STATE = true;

    @FXML
    public CheckBox toggleIndividualAI;

    @FXML
    public CheckBox toggleLegalAI;

    public Terminal terminal;

    @FXML
    public void launch() {
        SimApplication.setSimulationState(true);
        legalAI = new LegalAI(Habitat.LEGAL_AI_ON);
        individualAI = new IndividualAI(Habitat.INDIVIDUAL_AI_ON);
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
        if (timer != null) {
            timer.stop();
        }
        if (legalAI != null) legalAI.stop();
        if (individualAI != null) individualAI.stop();

        Habitat.clearObjArray();
        objGroup.getChildren().clear();

        // Сбрасываем счётчик ТОЛЬКО если это НЕ загрузка симуляции
        if (!isLoadingSimulation) {
            currentCounter = 0;
            changeCounter(0);
        }

        startButton.setDisable(false);
        stopButton.setDisable(true);
        menuStartButton.setDisable(false);
        menuStopButton.setDisable(true);
        System.out.println("sim stopped");
    }
    @FXML
    public void pause() {
        if (!SHOW_INFO_STATE) {
            stop();
            return;
        }
        System.out.println("paused");
        timer.togglePause();
        pauseAI();
        showPauseAlert();
        pauseAI();
    }

    @FXML
    public void toggleTimeShow() {
        timeDisplay.setVisible(!SHOW_TIME_STATE);
        SHOW_TIME_STATE = !SHOW_TIME_STATE;
        timeShowOn.setSelected(SHOW_TIME_STATE);
        timeShowOff.setSelected(!SHOW_TIME_STATE);
        menuShowTimeOn.setDisable(SHOW_TIME_STATE);
        menuShowTimeOff.setDisable(!SHOW_TIME_STATE);
        menuShowTimeOn.setSelected(SHOW_TIME_STATE);
        menuShowTimeOff.setSelected(!SHOW_TIME_STATE);

        Habitat.SHOW_TIME_STATE = SHOW_TIME_STATE;
        System.out.println("Time toggled");
    }

    @FXML
    public void debug() {
        Configuration configuration = new Configuration();
        configuration.writeConfig();
        System.out.println("debug");
    }
    //создали запустили таймер
    public void initializeTimer() {
        legalAI.start();
        individualAI.start();
        legalAI.setPriority(Habitat.LEGAL_AI_PRIORITY);
        individualAI.setPriority(Habitat.INDIVIDUAL_AI_PRIORITY);

        // Используем конструктор с начальным счётчиком
        timer = new Timer(this, currentCounter);
        Thread timerThread = new Thread(timer);
        timerThread.setDaemon(true);
        timerThread.start();
    }
    public void initTextFields() {
        indPeriodField.setText(String.valueOf(Habitat.INDIVIDUAL_PERIOD));
        legalPeriodField.setText(String.valueOf(Habitat.LEGAL_PERIOD));
        indLifespanField.setText(String.valueOf(Habitat.INDIVIDUAL_LIFESPAN));
        legalLifespanField.setText(String.valueOf(Habitat.LEGAL_LIFESPAN));
    }
    public void initOthers() {
        SHOW_INFO_STATE = Habitat.SHOW_INFO_STATE;
        if (!Habitat.SHOW_TIME_STATE) {
            toggleTimeShow();
        }
        toggleLegalAI.setSelected(Habitat.LEGAL_AI_ON);
        toggleIndividualAI.setSelected(Habitat.INDIVIDUAL_AI_ON);
        alertCheckBox.setSelected(SHOW_INFO_STATE);
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
        indSpawnChanceBox.setValue(String.valueOf(Habitat.INDIVIDUAL_SPAWN_CHANCE));
        legalSpawnChanceBox.setValue(String.valueOf(Habitat.LEGAL_SPAWN_CHANCE));
        legalPriorityBox.setItems(initObservablePriorityList());
        individualPriorityBox.setItems(initObservablePriorityList());
        legalPriorityBox.setValue(String.valueOf(Habitat.LEGAL_AI_PRIORITY));
        individualPriorityBox.setValue(String.valueOf(Habitat.INDIVIDUAL_AI_PRIORITY));
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
                "Создано объектов: " + Record.getObjCountCreated() + "\n" +
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
        SHOW_INFO_STATE = !SHOW_INFO_STATE;
        alertCheckBox.setSelected(SHOW_INFO_STATE);
        menuShowInfoOn.setSelected(SHOW_INFO_STATE);
        menuShowInfoOff.setSelected(!SHOW_INFO_STATE);
        menuShowInfoOn.setDisable(SHOW_INFO_STATE);
        menuShowInfoOff.setDisable(!SHOW_INFO_STATE);
        Habitat.SHOW_INFO_STATE = SHOW_INFO_STATE;
    }
    public void abort() {
        SimApplication.onClose(this);
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
        Habitat.INDIVIDUAL_AI_ON = !Habitat.INDIVIDUAL_AI_ON;
        if (SimApplication.getSimulationState()) {
            individualAI.togglePause();
        }
    }
    public void setToggleLegalAI() {
        Habitat.LEGAL_AI_ON = !Habitat.LEGAL_AI_ON;
        if (SimApplication.getSimulationState()) {
            legalAI.togglePause();
        }
    }

    public void setLegalPriorityBox() {
        legalAI.getThread().setPriority(Integer.parseInt(legalPriorityBox.getValue()));
        Habitat.LEGAL_AI_PRIORITY = Integer.parseInt(legalPriorityBox.getValue());
    }

    public void setIndividualPriorityBox() {
        individualAI.getThread().setPriority(Integer.parseInt(individualPriorityBox.getValue()));
        Habitat.INDIVIDUAL_AI_PRIORITY = Integer.parseInt(individualPriorityBox.getValue());
    }

    public void openTerminal() {
        terminal = new Terminal();
        terminal.initOwner(objGroup.getScene().getWindow());
        terminal.show();
        System.out.println("Terminal created");
    }
    public void pauseAI() {
        individualAI.togglePause();
        legalAI.togglePause();
    }




    @FXML
    public void saveSimulation() {
        boolean wasRunning = SimApplication.getSimulationState();

        if (wasRunning && timer != null) {
            timer.togglePause();
        }
        pauseAI();
        SimulationSerializer.save(
                (Stage) objGroup.getScene().getWindow(),
                ObjectsArraySingleton.getInstance(),
                Habitat.currentTimeInSec
        );

        if (wasRunning && timer != null) {
            timer.togglePause();
        }
        pauseAI();
    }

    @FXML
    public void loadSimulation() {
        isLoadingSimulation = true;

        if (SimApplication.getSimulationState()) {
            stop();
        }

        SimulationSnapshot snapshot = SimulationSerializer.load((Stage) objGroup.getScene().getWindow());
        if (snapshot == null) {
            isLoadingSimulation = false;
            return;
        }

        ObjectsArraySingleton.getInstance().clear();
        Habitat.clearObjArray();
        clearAllObjectsFromView();

        Record.objCountCreated = 0;
        Record.indCountAlive = 0;
        Record.legCountAlive = 0;
        IndividualRecord.indCountCreated = 0;
        LegalRecord.legCountCreated = 0;

        Habitat.currentTimeInSec = snapshot.getCurrentTime();
        currentCounter = (int)(Habitat.currentTimeInSec * 100);
        changeCounter(Habitat.currentTimeInSec);

        long saveTime = snapshot.getSaveTime();
        long currentTime = System.currentTimeMillis();
        double elapsedSeconds = (currentTime - saveTime) / 1000.0;
        System.out.println("Прошло с момента сохранения: " + elapsedSeconds + " сек");

        for (RecordDTO dto : snapshot.getIndividualRecords()) {
            IndividualRecord record = new IndividualRecord(
                    dto.getSpawnTime() + elapsedSeconds,
                    dto.getLifespan()
            );
            restoreRecordFromDTO(record, dto);
            ObjectsArraySingleton.getInstance().addRecord(record);
            Record.objCountCreated++;
            Record.indCountAlive++;
            IndividualRecord.indCountCreated++;
        }

        for (RecordDTO dto : snapshot.getLegalRecords()) {
            LegalRecord record = new LegalRecord(
                    dto.getSpawnTime() + elapsedSeconds,
                    dto.getLifespan()
            );
            restoreRecordFromDTO(record, dto);
            ObjectsArraySingleton.getInstance().addRecord(record);
            Record.objCountCreated++;
            Record.legCountAlive++;
            LegalRecord.legCountCreated++;
        }

        Platform.runLater(() -> {
            for (Record r : ObjectsArraySingleton.getInstance().getAllObjects()) {
                ImageView view = r.getSpriteView();
                if (view != null && !objGroup.getChildren().contains(view)) {
                    objGroup.getChildren().add(view);
                }
            }
            redrawAllObjects();
        });

        isLoadingSimulation = false;

        System.out.println("Симуляция загружена. Всего объектов: " +
                (snapshot.getIndividualRecords().size() + snapshot.getLegalRecords().size()));
    }

    private void restoreRecordFromDTO(Record record, RecordDTO dto) {
        record.setID(dto.getId());
        record.setX(dto.getX());
        record.setY(dto.getY());
        record.setDestinationX(dto.getDestinationX());
        record.setDestinationY(dto.getDestinationY());
        record.setOnDestination(dto.isOnDestination());
        record.setNormalXY();
        record.recreateSpriteView();
    }

}
