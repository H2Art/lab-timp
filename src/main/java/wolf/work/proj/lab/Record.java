package wolf.work.proj.lab;
import wolf.work.proj.lab.Habitat;
import javafx.scene.image.Image;

public abstract class Record {
    protected double x;
    protected double y;
    String type;
    Image sprite;
    Record(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Record() {
        setRandomCoordinates();
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public Image getSprite() {
        return this.sprite;
    }

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
