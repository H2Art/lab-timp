package wolf.work.proj.lab;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class SimulationSnapshot implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Record> individualRecords = new ArrayList<>();
    private List<Record> legalRecords = new ArrayList<>();
    private long saveTime;
    private double currentTime;

    public SimulationSnapshot() {}

    // Геттеры
    public List<Record> getIndividualRecords() { return individualRecords; }
    public List<Record> getLegalRecords() { return legalRecords; }
    public long getSaveTime() { return saveTime; }
    public double getCurrentTime() { return currentTime; }

    // Сеттеры
    public void setIndividualRecords(List<Record> records) { this.individualRecords = records; }
    public void setLegalRecords(List<Record> records) { this.legalRecords = records; }
    public void setSaveTime(long time) { this.saveTime = time; }
    public void setCurrentTime(double time) { this.currentTime = time; }
}