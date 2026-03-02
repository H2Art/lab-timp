package wolf.work.proj.lab;
import wolf.work.proj.lab.Habitat;
import javafx.scene.image.Image;

public abstract class Record {
    protected double x;
    protected double y;
    static int objCount;
    static int indCount;
    static int legCount;
    String type;
    Image sprite;
    Record(int x, int y) {
        objCount++;
        this.x = x;
        this.y = y;
    }

    Record() {
        setRandomCoordinates();
        objCount++;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public static int getObjCount() {
        return objCount;
    }

    public Image getSprite() {
        return this.sprite;
    }
    static public int getTypeCount() { return objCount; }

    public void setRandomCoordinates() {
        int WIDTH = Habitat.WIDTH;
        int HEIGHT = Habitat.HEIGHT;
        double f1 = Math.random();
        double f2 = Math.random();
        int x = (int)(f1 * WIDTH);
        int y = (int)(f2 * HEIGHT);
        this.x = x;
        this.y = y;
    }

}
