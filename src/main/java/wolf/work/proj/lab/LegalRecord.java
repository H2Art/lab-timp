package wolf.work.proj.lab;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Objects;

public class LegalRecord extends Record {
    private static final Image sprite = new Image(Objects.requireNonNull(
            LegalRecord.class.getResourceAsStream("/sprites/LegalRecord.png")));
    private static final double SPRITE_WIDTH = 100;
    private static final boolean PRESERVE_RATIO = true;
    private static final boolean SMOOTH = false;

    public LegalRecord(double currentTime, int lifespanTime) {
        super(currentTime, lifespanTime);
        type = "Legal";
        this.setDestinationCoordinates();
        legCount++;
    }

    @Override
    protected Image getSprite() {
        return sprite;
    }

    @Override
    public ImageView getSpriteView() {
        if (spriteView == null) {
            createSpriteView();
        }
        return spriteView;
    }

    static public int getTypeCount() {
        return legCount;
    }
}