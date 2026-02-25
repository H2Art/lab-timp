package wolf.work.proj.lab;
import java.lang.Math;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Habitat {
    // Константы
    final double N1 = 3.0;
    final double N2 = 5.0;
    final double P1 = 0.7;
    final double P2 = 0.3;
    final int WIDTH = 500;
    final int HEIGHT = 500;
    Record[] Objects;
    //
    void Update() {
        // генерация объектов
        // помещение в рандомное место окна
    }

    void SpawnObject() {
        double rand = Math.random();
        int[] coords = GetRandomCoordinates();
        int x = coords[0];
        int y = coords[1];
        if (rand <= P1)
        {
            LegalRecord obj = new LegalRecord(x, y);
        }
        else {
            IndividualRecord obj = new IndividualRecord(x, y);
        }
    }

    // Функция, возвращающая рандомную пару координат окна симуляции
    int[] GetRandomCoordinates()
    {
        double f1 = Math.random();
        double f2 = Math.random();
        int[] xy = new int[2];
        xy[0] = (int)(f1 * WIDTH);
        xy[1] = (int)(f2 * HEIGHT);
        return xy;
    }
}
