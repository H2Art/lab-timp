package wolf.work.proj.lab;

import javafx.scene.image.Image;

public class IndividualRecord extends Record {
    public IndividualRecord(int x, int y) {
        super(x, y);
        this.type = "Individual";
        this.sprite = new Image(getClass().getResourceAsStream("/sprites/IndividualRecord.png"));
    }
    public IndividualRecord() {
        super();
        this.type = "Individual";
        this.sprite = new Image(getClass().getResourceAsStream("/sprites/IndividualRecord.png"));
    }
}
