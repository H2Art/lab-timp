package wolf.work.proj.lab;
import javafx.scene.image.ImageView;
import wolf.work.proj.lab.Habitat;
import javafx.scene.image.Image;

import javax.imageio.stream.ImageInputStream;

public abstract class Record {
    protected double x;
    protected double y;
    protected double spawnTime;
    protected int lifespan;

    protected ImageView spriteView;

    static int objCount;
    static int indCount;
    static int legCount;

    private int ID;

    Record(double time, int lifespanTime) {
        spawnTime = time;
        lifespan = lifespanTime;
        setID();
        setRandomCoordinates();
        objCount++;
        createSpriteView();
    }

    protected abstract Image getSprite();

    public ImageView getSpriteView() {
        return spriteView;
    }
    protected void createSpriteView() {
        ImageView view = new ImageView(getSprite());
        view.setFitWidth(100);  // или используй свою константу
        view.setPreserveRatio(true);
        view.setSmooth(false);
        view.setX(this.x);
        view.setY(this.y);
        this.spriteView = view;
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

    static public int getTypeCount() { return objCount; }

    public void setRandomCoordinates() {
        double f1 = Math.random();
        double f2 = Math.random();
        int x = (int)(f1 * ((Habitat.WIDTH - 200) / 2));
        int y = (int)(f2 * Habitat.HEIGHT);
        if (y > 800) {
            y -= 50;
        }
        this.x = x;
        this.y = y;
    }
    public int getID() {
        return this.ID;
    }
    public void setID() {
        java.util.Random rand = new java.util.Random();
        int newId;
        do {
            newId = rand.nextInt(Integer.MAX_VALUE);
        } while (ObjectsArraySingleton.getInstance().containsId(newId));
        this.ID = newId;
    }
    public int getLifespan() {
        return this.lifespan;
    }
    public double getSpawnTime() {
        return this.spawnTime;
    }
}
