package wolf.work.proj.lab;

import wolf.work.proj.front.LabApplication;

public class Program {
    void main()
    {
        Habitat hb = new Habitat();
        int[] xy = hb.GetRandomCoordinates();
        LegalRecord rc = new LegalRecord(xy[0], xy[1]);
        System.out.println(rc.x + " " + rc.y);
        System.out.println(rc.type);
        LabApplication.instanciateObj(rc);
    }

}
