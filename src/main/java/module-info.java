module wolf.work.proj {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
//    requires wolf.work.proj;


    opens wolf.work.proj.front to javafx.fxml;
    exports wolf.work.proj.front;
    exports wolf.work.proj.lab;
    opens wolf.work.proj.lab to javafx.fxml;
}