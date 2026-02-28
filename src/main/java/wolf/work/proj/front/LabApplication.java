package wolf.work.proj.front;
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
import java.lang.ModuleLayer.Controller;


public class LabApplication extends Application {

    // Импорт спрайтов
    Image PHYS_SPRITE = new Image(getClass().getResourceAsStream("/sprites/PhysRecord.png"));
    Image LEGAL_SPRITE = new Image(getClass().getResourceAsStream("/sprites/LegalRecord.png"));

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(LabApplication.class.getResource("lab-view.fxml"));
        Parent root = loader.load();
        LabController controller = loader.getController();
        Scene scene = new Scene(root);

        // создание и настройка превью спрайтов
        ImageView PHYS_VIEW = new ImageView(PHYS_SPRITE);
        ImageView LEGAL_VIEW = new ImageView(LEGAL_SPRITE);
        PHYS_VIEW.setFitWidth(50);
        PHYS_VIEW.setPreserveRatio(true);
        LEGAL_VIEW.setFitWidth(50);
        LEGAL_VIEW.setPreserveRatio(true);

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
    public static void instanciateObj(Record obj) {
        System.out.println(obj.getX());
    }
}
