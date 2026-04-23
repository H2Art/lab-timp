package wolf.work.proj.lab;

import java.util.List;
import java.util.ArrayList;

public class SimulationSnapshot {
    private List<RecordDTO> individualRecords = new ArrayList<>();
    private List<RecordDTO> legalRecords = new ArrayList<>();
    private long saveTime;
    private double currentTime;

    public SimulationSnapshot() {}

    // Геттеры
    public List<RecordDTO> getIndividualRecords() { return individualRecords; }
    public List<RecordDTO> getLegalRecords() { return legalRecords; }
    public long getSaveTime() { return saveTime; }
    public double getCurrentTime() { return currentTime; }

    // Сеттеры
    public void setIndividualRecords(List<RecordDTO> records) { this.individualRecords = records; }
    public void setLegalRecords(List<RecordDTO> records) { this.legalRecords = records; }
    public void setSaveTime(long time) { this.saveTime = time; }
    public void setCurrentTime(double time) { this.currentTime = time; }
}