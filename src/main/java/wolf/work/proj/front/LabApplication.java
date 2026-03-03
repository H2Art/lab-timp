package wolf.work.proj.front;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import wolf.work.proj.lab.*;
//import wolf.work.proj.lab.*;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import wolf.work.proj.lab.Record;


public class LabApplication extends Application {

    // Импорт спрайтов

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(LabApplication.class.getResource("lab-view.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/sprites/FNS-icon.png")));
        Parent root = loader.load();
        LabController controller = loader.getController();
        Scene scene = new Scene(root);

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
