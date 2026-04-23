package wolf.work.proj.lab;

public class IndividualAI extends BaseAI{
    public IndividualAI (boolean is_paused) {
        super(is_paused);
    }
    @Override
    public void UpdateAI() {
        ObjectsArraySingleton.getInstance().updateObjectsCoordinates("Individual");
    }
}
