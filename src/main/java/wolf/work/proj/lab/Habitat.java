package wolf.work.proj.lab;
import java.lang.Math;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Habitat {
//    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//    final Runnable runnable = new Runnable() {
//        int start = 0;
//        @Override
//        public void run() {
//
//        }
//    }
    // Константы
    final double N1 = 3.0;
    final double N2 = 5.0;
    final double P1 = 0.7;
    final double P2 = 0.6;
    final static int WIDTH = 500;
    final static int HEIGHT = 500;
    // Спрайты

    // Массив, содержащий ссылки на объекты
    public Record[] Objects = new Record[50];

    // Метод, работающий постоянно?
    void Update() {
        // генерация объектов
        // помещение в рандомное место окна
    }

    void SpawnObject() {
        double rand = Math.random();
        if (rand <= P1) // определяем тип объекта, случайно
        {
            LegalRecord obj = new LegalRecord();
        }
        else {
            IndividualRecord obj = new IndividualRecord();
        }
    }
}
