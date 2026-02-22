package wolf.work.proj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LabApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader root = new FXMLLoader(LabApplication.class.getResource("lab-view.fxml"));
        //code
        Scene scene = new Scene(root.load());

        stage.setTitle("Simulation");
        stage.setScene(scene);
        stage.show();
    }
}
