package wolf.work.proj.front;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import wolf.work.proj.lab.*;
import javafx.fxml.FXML;

import wolf.work.proj.lab.Record;
import wolf.work.proj.network.NetworkMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Vector;

public class SimController {
    @FXML
    public ListView<String> clientListView;
    @FXML
    public Button syncProbButton;
    // Сеть
    private ObjectOutputStream networkOut;
    private String myClientId;
    private ObservableList<String> connectedClients = FXCollections.observableArrayList();

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
    public Button connectionButton;

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
        System.out.println("debug");
    }

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

        clientListView.setItems(connectedClients);
//        showConnectionDialog();
    }

    public void instantiateObj(Record obj) {
        if (Platform.isFxApplicationThread()) {
            ImageView preview = obj.getSpriteView();
            objGroup.getChildren().add(preview);
        } else {
            Platform.runLater(() -> instantiateObj(obj));
        }
    }

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

    public void changeIndPeriod() {
        String newValue = indPeriodField.getText();
        if (validateValue(newValue)) {
            Habitat.INDIVIDUAL_PERIOD = Integer.parseInt(newValue);
        } else {
            indPeriodField.setText(oldIndPeriodValue);
        }
        oldIndPeriodValue = indPeriodField.getText();
        System.out.println("ind" + Habitat.INDIVIDUAL_PERIOD);
    }

    public void changeLegalPeriod() {
        String newValue = legalPeriodField.getText();
        if (validateValue(newValue)) {
            Habitat.LEGAL_PERIOD = Integer.parseInt(newValue);
        } else {
            legalPeriodField.setText(oldLegalPeriodValue);
        }
        oldLegalPeriodValue = legalPeriodField.getText();
        System.out.println("leg" + Habitat.LEGAL_PERIOD);
    }

    public void changeIndLifespan() {
        String newValue = indLifespanField.getText();
        if (validateValue(newValue)) {
            Habitat.INDIVIDUAL_LIFESPAN = Integer.parseInt(newValue);
        } else {
            indLifespanField.setText(oldIndLifespanValue);
        }
        oldIndLifespanValue = indLifespanField.getText();
        System.out.println("ind_lifespan" + Habitat.INDIVIDUAL_LIFESPAN);
    }

    public void changeLegalLifespan() {
        String newValue = legalLifespanField.getText();
        if (validateValue(newValue)) {
            Habitat.LEGAL_LIFESPAN = Integer.parseInt(newValue);
        } else {
            legalLifespanField.setText(oldLegalLifespanValue);
        }
        oldLegalLifespanValue = legalLifespanField.getText();
        System.out.println("leg_lifespan" + Habitat.LEGAL_LIFESPAN);
    }

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
        } catch (NumberFormatException e) {
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
        // Загружаем снапшот
        SimulationSnapshot snapshot = SimulationSerializer.load((Stage) objGroup.getScene().getWindow());
        if (snapshot == null) return;

        if (SimApplication.getSimulationState()) {
            if (timer != null) timer.stop();
            if (legalAI != null) legalAI.stop();
            if (individualAI != null) individualAI.stop();
            SimApplication.setSimulationState(false);
        }

        // Очищаем симуляцию и счётчики
        ObjectsArraySingleton.getInstance().clear();
        Habitat.clearObjArray();
        clearAllObjectsFromView();
        Record.objCountCreated = 0;
        Record.indCountAlive = 0;
        Record.legCountAlive = 0;
        IndividualRecord.indCountCreated = 0;
        LegalRecord.legCountCreated = 0;

        // Восстанавливаем время симуляции
        Habitat.currentTimeInSec = snapshot.getCurrentTime();
        currentCounter = (int)(Habitat.currentTimeInSec * 100);
        changeCounter(Habitat.currentTimeInSec);
        System.out.println("Время симуляции восстановлено: " + Habitat.currentTimeInSec + " сек");

        // Восстанавливаем объекты
        for (Record record : snapshot.getIndividualRecords()) {
            record.recreateSpriteView(); // SpriteView пересоздаётся
            ObjectsArraySingleton.getInstance().addRecord(record);
        }
        for (Record record : snapshot.getLegalRecords()) {
            record.recreateSpriteView();
            ObjectsArraySingleton.getInstance().addRecord(record);
        }

        Record.indCountAlive = snapshot.getIndividualRecords().size();
        Record.legCountAlive = snapshot.getLegalRecords().size();

        // Перерисовываем объекты
        Platform.runLater(() -> {
            objGroup.getChildren().clear();
            for (Record r : ObjectsArraySingleton.getInstance().getAllObjects()) {
                ImageView view = r.getSpriteView();
                if (view != null) objGroup.getChildren().add(view);
            }
            redrawAllObjects();
        });

        // Обновляем состояние кнопок
        startButton.setDisable(false);
        stopButton.setDisable(true);
        menuStartButton.setDisable(false);
        menuStopButton.setDisable(true);

        System.out.println("Загружено объектов: " +
                (snapshot.getIndividualRecords().size() + snapshot.getLegalRecords().size()));
    }

    private void connectToServer(String host, int port) {
        new Thread(() -> {
            try {
                Socket socket = new Socket(host, port);
                networkOut = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream networkIn = new ObjectInputStream(socket.getInputStream());
                myClientId = socket.getLocalSocketAddress().toString();
                System.out.println("Подключено к серверу, ID: " + myClientId);

                while (true) {
                    NetworkMessage msg = (NetworkMessage) networkIn.readObject();
                    Platform.runLater(() -> handleNetworkMessage(msg));
                }
            } catch (Exception e) {
                System.err.println("Ошибка сети: " + e.getMessage());
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка подключения");
                    alert.setHeaderText("Не удалось подключиться к серверу синхронизации");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                });
            }
        }).start();
    }

    private void handleNetworkMessage(NetworkMessage msg) {
        switch (msg.type) {
            case CLIENT_LIST:
                connectedClients.setAll(msg.clientList);
                connectedClients.remove(myClientId);
                break;
            case SYNC_PROB:
                Habitat.INDIVIDUAL_SPAWN_CHANCE = msg.individualSpawnChance;
                Habitat.LEGAL_SPAWN_CHANCE = msg.legalSpawnChance;
                indSpawnChanceBox.setValue(String.valueOf(Habitat.INDIVIDUAL_SPAWN_CHANCE));
                legalSpawnChanceBox.setValue(String.valueOf(Habitat.LEGAL_SPAWN_CHANCE));
                System.out.println("Получены вероятности: Ind=" + Habitat.INDIVIDUAL_SPAWN_CHANCE + ", Legal=" + Habitat.LEGAL_SPAWN_CHANCE);
                break;
        }
    }

    @FXML
    public void syncProbabilities() {
        String selected = clientListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Синхронизация");
            alert.setHeaderText(null);
            alert.setContentText("Выберите клиента из списка");
            alert.showAndWait();
            return;
        }
        try {
            NetworkMessage msg = new NetworkMessage();
            msg.type = NetworkMessage.Type.SYNC_PROB;
            msg.targetId = selected;
            msg.individualSpawnChance = Habitat.INDIVIDUAL_SPAWN_CHANCE;
            msg.legalSpawnChance = Habitat.LEGAL_SPAWN_CHANCE;
            networkOut.writeObject(msg);
            networkOut.flush();
            System.out.println("Отправлены вероятности клиенту " + selected);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showConnectionDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Подключение к серверу синхронизации");
        dialog.setHeaderText("Введите IP-адрес и порт сервера");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField hostField = new TextField(Configuration.SERVER_HOST);
        hostField.setPromptText("localhost");
        TextField portField = new TextField(String.valueOf(Configuration.SERVER_PORT));
        portField.setPromptText("порт");

        grid.add(new Label("IP-адрес:"), 0, 0);
        grid.add(hostField, 1, 0);
        grid.add(new Label("Порт:"), 0, 1);
        grid.add(portField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String host = hostField.getText().trim();
            String portText = portField.getText().trim();
            if (host.isEmpty()) host = Configuration.SERVER_HOST;
            int port;
            try {
                port = Integer.parseInt(portText);
            } catch (NumberFormatException e) {
                port = Configuration.SERVER_PORT;
            }
            connectToServer(host, port);
        } else {
            System.out.println("Подключение отменено пользователем");
        }
    }
}