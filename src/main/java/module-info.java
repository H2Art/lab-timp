module wolf.work.proj {
    requires javafx.controls;
    requires javafx.fxml;


    opens wolf.work.proj to javafx.fxml;
    exports wolf.work.proj;
}