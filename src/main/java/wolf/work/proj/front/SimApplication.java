package wolf.work.proj.front;
import wolf.work.proj.lab.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;



public class SimApplication extends Application {
    private static boolean SIMULATION_STATE = false;
    public static void main(String[] args) {
        launch(args);
    }

    public static void setSimulationState(boolean newState) {
        SIMULATION_STATE = newState;
    }
    public static boolean getSimulationState() {
        return SIMULATION_STATE;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setWidth(Habitat.WIDTH);
        stage.setHeight(Habitat.HEIGHT);
        FXMLLoader loader = new FXMLLoader(SimApplication.class.getResource("lab-view.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/sprites/FNS-icon.png")));
        Parent root = loader.load();
        SimController controller = loader.getController();
        Scene scene = new Scene(root);
        controller.initComboBoxes();

        scene.setOnKeyPressed(event -> {
            switch(event.getCode()) {
                case B:
                    if (getSimulationState()) {
                        break;
                    }
                    controller.launch();
                    break;
                case E:
                    if (!getSimulationState()) {
                        break;
                    }
                    controller.pause();
                    break;
                case T:
                    controller.toggleTimeShow();
                    break;
                case D:
                    controller.debug();
                    break;
                default:
                    break;
            }
        });
        stage.setTitle("Debtor spawner");
        stage.setScene(scene);
        stage.show();
    }
}
