package wolf.work.proj.front;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.lang.ModuleLayer.Controller;

public class LabApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(LabApplication.class.getResource("lab-view.fxml"));
        Parent root = loader.load();
        LabController controller = loader.getController();
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch(event.getCode()) {
                    case B:
                        controller.launch();
                        break;
                    case E:
                        controller.stop();
                        break;
                    case T:
                        controller.toggleTimeShow();
                        break;
                    default:
                        break;
                }
            }
        });
        stage.setTitle("Simulation");
        stage.setScene(scene);
        stage.show();
    }
}
