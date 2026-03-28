package wolf.work.proj.lab;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class IndividualRecord extends Record {
    private static final Image sprite = new Image(Objects.requireNonNull(IndividualRecord.class.getResourceAsStream("/sprites/IndividualRecord.png")));
    private static final double SPRITE_WIDTH = 100;
    private static final boolean PRESERVE_RATIO = true;
    private static final boolean SMOOTH = false;

    public IndividualRecord(double currentTime, int lifespanTime) {
        super(currentTime, lifespanTime);
        indCount++;
    }

    @Override
    protected Image getSprite() {
        return sprite;
    }

    @Override
    public ImageView getSpriteView() {
        // Возвращаем существующий spriteView, а не создаём новый
        if (spriteView == null) {
            createSpriteView();
        }
        return spriteView;
    }

    static public int getTypeCount() {
        return indCount;
    }
}