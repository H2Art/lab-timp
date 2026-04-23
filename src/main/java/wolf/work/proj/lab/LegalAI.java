package wolf.work.proj.lab;

public class LegalAI extends BaseAI {
    public LegalAI (boolean is_paused) {
        super(is_paused);
    }
    @Override
    public void UpdateAI() {
        ObjectsArraySingleton.getInstance().updateObjectsCoordinates("Legal");
    }
}
