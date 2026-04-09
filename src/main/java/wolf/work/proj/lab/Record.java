package wolf.work.proj.lab;
import javafx.scene.image.ImageView;
import wolf.work.proj.lab.Habitat;
import javafx.scene.image.Image;

import javax.imageio.stream.ImageInputStream;

public abstract class Record {
    //координаты в настоящий момент времени
    protected double x;
    protected double y;

    protected String type;

    // поля для логики движения
    protected boolean isOnDestination = false;
    protected double destinationX;
    protected double destinationY;
    protected double normalX;
    protected double normalY;
    protected double speed; // px/sec

    // поля для удаления объектов по таймеру
    protected double spawnTime;
    protected int lifespan;

    // картинка со всеми параметрами
    protected ImageView spriteView;

    // подсчет объектов
    static int objCount;
    static int indCount;
    static int legCount;

    // уникальный идентификатор
    private int ID;

    // конструктор
    Record(double time, int lifespanTime) {
        speed = 150.0;
        spawnTime = time;
        lifespan = lifespanTime;
        setID();
        setRandomCoordinates();
        objCount++;
        createSpriteView();
    }

    // геттер спрайта
    protected abstract Image getSprite();

    // геттер SpriteView
    public ImageView getSpriteView() {
        return spriteView;
    }

    //выставление параметров
    protected void createSpriteView() {
        ImageView view = new ImageView(getSprite());
        view.setFitWidth(100);
        view.setPreserveRatio(true);
        view.setSmooth(false);
        view.setX(this.x);
        view.setY(this.y);
        this.spriteView = view;
    }

    // геттеры x y
    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    // геттеры количества объектов
    public static int getObjCount() {
        return objCount;
    }

    static public int getTypeCount() { return objCount; }

    // постановка рандомных координат при спавне объектов
    public void setRandomCoordinates() {
        double f1 = Math.random();
        double f2 = Math.random();
        double x = (f1 * ((Habitat.WIDTH * 0.9) / 2));
        double y = (f2 * Habitat.HEIGHT);
        if (y > Habitat.HEIGHT * 0.9) {
            y -= Habitat.HEIGHT * 0.05;
        }
        this.x = x;
        this.y = y;
    }

    // постановка целевых координат
    public void setDestinationCoordinates() {
        double f1 = Math.random();
        double f2 = Math.random();
        double x = (double)Habitat.WIDTH / 4 * f1;
        double y = (double)Habitat.HEIGHT / 2 * f2;
        if (type.equals("Individual")) {
            x += (double)Habitat.WIDTH / 4;
            y += (double)Habitat.HEIGHT / 2;
        }
        this.destinationX = x;
        this.destinationY = y;
        checkSpawn();
        setNormalXY();
    }

    // проверяем, не заспавнился ли объект сразу в нужном квадранте
    public void checkSpawn() {
        if (type.equals("Legal")) {
            if (x >= 0 && x <= (double) Habitat.WIDTH / 4 && y >= 0 && y <= (double) Habitat.HEIGHT / 2) {
                isOnDestination = true;
            }
        }
        else if (type.equals("Individual")) {
            if (x >= (double) Habitat.WIDTH / 4 && x <= (double) Habitat.WIDTH / 2 && y >= (double) Habitat.HEIGHT / 2 && y <= Habitat.HEIGHT) {
                isOnDestination = true;
            }
        }
    }

    // достигли ли цели
    public void checkDestination() {
        if (Math.abs(y - destinationY) < 1 && Math.abs(x - destinationX) < 1) {
            isOnDestination = true;
        }
    }

    // получаем нормальный вектор
    public void setNormalXY() {
        double xLen = destinationX - x;
        double yLen = destinationY - y;
        double hypo = Math.sqrt(xLen * xLen + yLen * yLen);
        normalX = xLen / hypo;
        normalY = yLen / hypo;
    }
    public void makeStep() {
        checkDestination();
        if (isOnDestination) {
            return;
        }
        x += speed * normalX / 100;
        y += speed * normalY / 100;
    }

    // геттер и сеттер идентификатора
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

    // геттеры длины жизни и времени рождения
    public int getLifespan() {
        return lifespan;
    }

    public double getSpawnTime() {
        return spawnTime;
    }

    // геттер типа
    public String getType() { return type; }
}
