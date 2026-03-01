package wolf.work.proj.front;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import wolf.work.proj.lab.Habitat;
import javafx.fxml.FXML;
import wolf.work.proj.lab.IndividualRecord;
import wolf.work.proj.lab.Record;

public class LabController {

    @FXML
    public Group objGroup;

    Habitat hb = new Habitat();
    @FXML
    public void launch() {
        System.out.println("sim launched");
    }

    @FXML
    public void stop() {
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

    public void instantiateObj(Record obj) {
        ImageView preview = getImagePrefs(obj.getSprite());
        preview.setX(obj.getX());
        preview.setY(obj.getY());
        objGroup.getChildren().add(preview);
    }

    public ImageView getImagePrefs(Image sprite) {
        ImageView preview = new ImageView(sprite);
        preview.setFitWidth(50);
        preview.setPreserveRatio(true);
        preview.setSmooth(false);
        return preview;
    }

}
