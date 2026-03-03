package wolf.work.proj.front;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import wolf.work.proj.lab.*;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import wolf.work.proj.lab.Record;

public class LabController {
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


    private Timer timer;

    @FXML
    public Label timeDisplay;
    private boolean timeDisplayState = true;

    @FXML
    public void launch() {
        infoContainer.setVisible(false);
        initializeTimer();
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
//        instantiateObj(new IndividualRecord());
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
            ImageView preview = getImagePrefs(obj.getSprite());
            preview.setX(obj.getX());
            preview.setY(obj.getY());
            objGroup.getChildren().add(preview);
        } else {
            Platform.runLater(() -> instantiateObj(obj));
        }
    }

    public ImageView getImagePrefs(Image sprite) {
        ImageView preview = new ImageView(sprite);
        preview.setFitWidth(100);
        preview.setPreserveRatio(true);
        preview.setSmooth(false);
        return preview;
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
    public void setInfo() {
        infoTime.setText("Времени прошло: " + currentTime);
        infoRecords.setText("Записей создано: " + Record.getObjCount());
        infoLegalRecords.setText("Юр. лиц создано: " + LegalRecord.getTypeCount());
        infoIndividualRecords.setText("Физ. лиц создано: " + IndividualRecord.getTypeCount());
    }
}
