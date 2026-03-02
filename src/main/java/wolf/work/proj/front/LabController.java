package wolf.work.proj.front;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import wolf.work.proj.lab.Habitat;
import javafx.fxml.FXML;
import wolf.work.proj.lab.IndividualRecord;
import wolf.work.proj.lab.Record;
import wolf.work.proj.lab.Timer;

public class LabController {

    @FXML
    public Group objGroup;

    private Timer timer;

    @FXML
    public void launch() {
        initializeTimer();
        System.out.println("sim launched");
    }

    @FXML
    public void stop() {
        timer.stop();
        Habitat.showInfo();
        Habitat.clearObjArray();
        objGroup.getChildren().clear();
        System.out.println("sim stopped");
    }

    @FXML
    public void toggleTimeShow() {
        System.out.println("Time toggled");
    }

    @FXML
    public void debug() {
        instantiateObj(new IndividualRecord());
    }

    public void initializeTimer() {
        timer = new Timer(this);
        Thread timerThread = new Thread(timer);
        timerThread.setDaemon(true);
        timerThread.start();
    }
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

}
