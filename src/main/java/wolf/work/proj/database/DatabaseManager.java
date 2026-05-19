package wolf.work.proj.database;

import wolf.work.proj.lab.*;
import wolf.work.proj.lab.Record;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(Configuration.DB_URL);
    }
    // инициализация таблиц
    public static void initTables() {
        String sqlIndividual = """
            CREATE TABLE IF NOT EXISTS individual_records (
                id INTEGER PRIMARY KEY,
                spawn_time REAL,
                lifespan INTEGER,
                x REAL,
                y REAL,
                dest_x REAL,
                dest_y REAL,
                normal_x REAL,
                normal_y REAL,
                speed REAL,
                on_destination INTEGER
            );
        """;
        String sqlLegal = """
            CREATE TABLE IF NOT EXISTS legal_records (
                id INTEGER PRIMARY KEY,
                spawn_time REAL,
                lifespan INTEGER,
                x REAL,
                y REAL,
                dest_x REAL,
                dest_y REAL,
                normal_x REAL,
                normal_y REAL,
                speed REAL,
                on_destination INTEGER
            );
        """;
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlIndividual);
            stmt.execute(sqlLegal);
            System.out.println("Таблицы БД готовы");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Сохранить всех IndividualRecord (перезапись)
    public static void saveIndividualRecords(List<Record> individuals) {
        String deleteSql = "DELETE FROM individual_records";
        String insertSql = """
            INSERT INTO individual_records (id, spawn_time, lifespan, x, y, dest_x, dest_y,
                normal_x, normal_y, speed, on_destination)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = connect();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            conn.setAutoCommit(false);
            deleteStmt.executeUpdate();

            for (Record r : individuals) {
                if (!(r instanceof IndividualRecord)) continue;
                insertStmt.setInt(1, r.getID());
                insertStmt.setDouble(2, r.getSpawnTime());
                insertStmt.setInt(3, r.getLifespan());
                insertStmt.setDouble(4, r.getX());
                insertStmt.setDouble(5, r.getY());
                insertStmt.setDouble(6, r.getDestinationX());
                insertStmt.setDouble(7, r.getDestinationY());
                insertStmt.setDouble(8, r.getNormalX());
                insertStmt.setDouble(9, r.getNormalY());
                insertStmt.setDouble(10, r.getSpeed());
                insertStmt.setInt(11, r.isOnDestination() ? 1 : 0);
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();
            conn.commit();
            System.out.println("Сохранено физических лиц: " + individuals.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Сохранить всех LegalRecord (перезапись)
    public static void saveLegalRecords(List<Record> legals) {
        String deleteSql = "DELETE FROM legal_records";
        String insertSql = """
            INSERT INTO legal_records (id, spawn_time, lifespan, x, y, dest_x, dest_y,
                normal_x, normal_y, speed, on_destination)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = connect();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            conn.setAutoCommit(false);
            deleteStmt.executeUpdate();

            for (Record r : legals) {
                if (!(r instanceof LegalRecord)) continue;
                insertStmt.setInt(1, r.getID());
                insertStmt.setDouble(2, r.getSpawnTime());
                insertStmt.setInt(3, r.getLifespan());
                insertStmt.setDouble(4, r.getX());
                insertStmt.setDouble(5, r.getY());
                insertStmt.setDouble(6, r.getDestinationX());
                insertStmt.setDouble(7, r.getDestinationY());
                insertStmt.setDouble(8, r.getNormalX());
                insertStmt.setDouble(9, r.getNormalY());
                insertStmt.setDouble(10, r.getSpeed());
                insertStmt.setInt(11, r.isOnDestination() ? 1 : 0);
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();
            conn.commit();
            System.out.println("Сохранено юридических лиц: " + legals.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Импорт физических лиц из внешнего файла в основную БД (с заменой)
    public static boolean importIndividualRecordsFromFile(String sourceDbPath) {
        String sourceUrl = "jdbc:sqlite:" + sourceDbPath;
        List<Record> imported = new ArrayList<>();

        // Читаем из внешнего файла
        String selectSql = "SELECT * FROM individual_records";
        try (Connection sourceConn = DriverManager.getConnection(sourceUrl);
             Statement stmt = sourceConn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSql)) {

            while (rs.next()) {
                IndividualRecord rec = new IndividualRecord(rs.getDouble("spawn_time"), rs.getInt("lifespan"));
                rec.setID(rs.getInt("id"));
                rec.setX(rs.getDouble("x"));
                rec.setY(rs.getDouble("y"));
                rec.setDestinationX(rs.getDouble("dest_x"));
                rec.setDestinationY(rs.getDouble("dest_y"));
                rec.setNormalX(rs.getDouble("normal_x"));
                rec.setNormalY(rs.getDouble("normal_y"));
                rec.setSpeed(rs.getDouble("speed"));
                rec.setOnDestination(rs.getInt("on_destination") == 1);
                imported.add(rec);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка чтения из файла: " + e.getMessage());
            return false;
        }

        // Если файл не содержит таблицу или она пуста, imported может быть пустым
        // Сохраняем в основную БД
        saveIndividualRecords(imported);
        System.out.println("Импортировано физических лиц: " + imported.size());
        return true;
    }

    // Импорт юридических лиц из внешнего файла в основную БД
    public static boolean importLegalRecordsFromFile(String sourceDbPath) {
        String sourceUrl = "jdbc:sqlite:" + sourceDbPath;
        List<Record> imported = new ArrayList<>();

        String selectSql = "SELECT * FROM legal_records";
        try (Connection sourceConn = DriverManager.getConnection(sourceUrl);
             Statement stmt = sourceConn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSql)) {

            while (rs.next()) {
                LegalRecord rec = new LegalRecord(rs.getDouble("spawn_time"), rs.getInt("lifespan"));
                rec.setID(rs.getInt("id"));
                rec.setX(rs.getDouble("x"));
                rec.setY(rs.getDouble("y"));
                rec.setDestinationX(rs.getDouble("dest_x"));
                rec.setDestinationY(rs.getDouble("dest_y"));
                rec.setNormalX(rs.getDouble("normal_x"));
                rec.setNormalY(rs.getDouble("normal_y"));
                rec.setSpeed(rs.getDouble("speed"));
                rec.setOnDestination(rs.getInt("on_destination") == 1);
                imported.add(rec);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка чтения из файла: " + e.getMessage());
            return false;
        }

        saveLegalRecords(imported);
        System.out.println("Импортировано юридических лиц: " + imported.size());
        return true;
    }
}