package wolf.work.proj.front;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LabController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onButtonClick() {

        welcomeText.setText("Welcome to JavaFX Application!");
    }

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
}
