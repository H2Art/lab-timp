package wolf.work.proj.front;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import wolf.work.proj.lab.Habitat;
import wolf.work.proj.lab.LegalRecord;
import wolf.work.proj.lab.IndividualRecord;
import wolf.work.proj.lab.Record;
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


public class LabApplication extends Application {

    // Импорт спрайтов

    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");
        System.setProperty("prism.subpixeltext", "false");
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(LabApplication.class.getResource("lab-view.fxml"));
        Parent root = loader.load();
        LabController controller = loader.getController();
        Scene scene = new Scene(root);
        LegalRecord lr = new LegalRecord();
        IndividualRecord ir = new IndividualRecord();


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
                    case D:
                        controller.debug();
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
