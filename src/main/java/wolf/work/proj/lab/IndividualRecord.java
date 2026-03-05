package wolf.work.proj.lab;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IndividualRecord extends Record {
    private static Image sprite = new Image(IndividualRecord.class.getResourceAsStream("/sprites/IndividualRecord.png"));
    private static final double SPRITE_WIDTH = 100;
    private static final boolean PRESERVE_RATIO = true;
    private static final boolean SMOOTH = false;

    public IndividualRecord() {
        super();
        indCount++;
        this.type = "Individual";
    }

    @Override
    public ImageView getSpriteView() {
        ImageView view = new ImageView(sprite);
        view.setFitWidth(SPRITE_WIDTH);
        view.setPreserveRatio(PRESERVE_RATIO);
        view.setSmooth(SMOOTH);
        view.setX(this.x);
        view.setY(this.y);
        return view;
    }

    static public int getTypeCount() {
        return indCount;
    }
}