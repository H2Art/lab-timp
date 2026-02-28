package wolf.work.proj.front;
import wolf.work.proj.lab.Habitat;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LabController {
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
        System.out.println("spawn obj");
    }
}
