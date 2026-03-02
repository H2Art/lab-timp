package wolf.work.proj.lab;

import javafx.scene.image.Image;

public class LegalRecord extends Record {
    public LegalRecord(int x, int y) {
        super(x, y);
        legCount++;
        this.type = "Legal";
        this.sprite = new Image(getClass().getResourceAsStream("/sprites/LegalRecord.png"));
    }
    public LegalRecord() {
        super();
        legCount++;
        this.type = "Legal";
        this.sprite = new Image(getClass().getResourceAsStream("/sprites/LegalRecord.png"));
    }
    static public int getTypeCount() {
        return legCount;
    }
}
