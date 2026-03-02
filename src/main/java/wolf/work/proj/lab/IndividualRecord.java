package wolf.work.proj.lab;

import javafx.scene.image.Image;

public class IndividualRecord extends Record {
    public IndividualRecord(int x, int y) {
        super(x, y);
        indCount++;
        this.type = "Individual";
        this.sprite = new Image(getClass().getResourceAsStream("/sprites/IndividualRecord.png"));
    }
    public IndividualRecord() {
        super();
        indCount++;
        this.type = "Individual";
        this.sprite = new Image(getClass().getResourceAsStream("/sprites/IndividualRecord.png"));
    }
    static public int getTypeCount() {
        return indCount;
    }
    static public void decrementTypeCount() {
        indCount--;
    }
}
