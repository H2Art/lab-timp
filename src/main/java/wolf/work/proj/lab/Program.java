package wolf.work.proj.lab;

import javafx.fxml.FXML;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Program {
    void main()
    {
        testInsert();
    }
    @FXML
    public void testInsert() {
        try (Connection conn = DriverManager.getConnection(Configuration.DB_URL)) {
            String sql = "INSERT INTO individual_records (id, spawn_time, lifespan, x, y, dest_x, dest_y, normal_x, normal_y, speed, on_destination) VALUES (1, 0.0, 5, 10.0, 20.0, 100.0, 200.0, 0.5, 0.5, 150.0, 0)";
            Statement stmt = conn.createStatement();
            int rows = stmt.executeUpdate(sql);
            System.out.println("Вставлено строк: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
