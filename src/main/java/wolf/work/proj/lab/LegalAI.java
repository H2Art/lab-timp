package wolf.work.proj.lab;

public class LegalAI extends BaseAI {
    @Override
    public void UpdateAI() {
        ObjectsArraySingleton.getInstance().updateObjectsCoordinates("Legal");
    }
}
