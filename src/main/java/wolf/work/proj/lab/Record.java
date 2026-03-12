package wolf.work.proj.lab;
import javafx.scene.image.ImageView;
import wolf.work.proj.lab.Habitat;
import javafx.scene.image.Image;

import javax.imageio.stream.ImageInputStream;

public abstract class Record {
    protected double x;
    protected double y;
    static int objCount;
    static int indCount;
    static int legCount;
    String type;

    Record() {
        setRandomCoordinates();
        objCount++;
    }

    public abstract ImageView getSpriteView();

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public static int getObjCount() {
        return objCount;
    }

    static public int getTypeCount() { return objCount; }

    public void setRandomCoordinates() {
        double f1 = Math.random();
        double f2 = Math.random();
        int x = (int)(f1 * Habitat.WIDTH);
        int y = (int)(f2 * Habitat.HEIGHT);
        this.x = x;
        this.y = y;
    }
}
