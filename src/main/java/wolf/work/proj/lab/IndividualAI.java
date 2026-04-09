package wolf.work.proj.lab;

public class IndividualAI extends BaseAI{
    @Override
    public void UpdateAI() {
        ObjectsArraySingleton.getInstance().updateObjectsCoordinates("Individual");
    }
}
