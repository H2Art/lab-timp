package wolf.work.proj.lab;


public class RecordDTO {
    private int id;
    private String type;
    private double x;
    private double y;
    private double spawnTime;
    private int lifespan;
    private double destinationX;
    private double destinationY;
    private boolean isOnDestination;

    public RecordDTO() {}

    public RecordDTO(Record record) {
        this.id = record.getID();
        this.type = record.getType();
        this.x = record.getX();
        this.y = record.getY();
        this.spawnTime = record.getSpawnTime();
        this.lifespan = record.getLifespan();
        this.destinationX = record.getDestinationX();
        this.destinationY = record.getDestinationY();
        this.isOnDestination = record.isOnDestination();
    }

    // Геттеры
    public int getId() { return id; }
    public String getType() { return type; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getSpawnTime() { return spawnTime; }
    public int getLifespan() { return lifespan; }
    public double getDestinationX() { return destinationX; }
    public double getDestinationY() { return destinationY; }
    public boolean isOnDestination() { return isOnDestination; }

    // Сеттеры
    public void setId(int id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setSpawnTime(double time) { this.spawnTime = time; }
    public void setLifespan(int lifespan) { this.lifespan = lifespan; }
    public void setDestinationX(double x) { this.destinationX = x; }
    public void setDestinationY(double y) { this.destinationY = y; }
    public void setOnDestination(boolean on) { isOnDestination = on; }
}