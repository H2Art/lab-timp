package wolf.work.proj.front;
import wolf.work.proj.lab.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;



public class SimApplication extends Application {

    public static void main(String[] args) {
        launch(args);
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
        controller.initializeButtons();

        scene.setOnKeyPressed(event -> {
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
