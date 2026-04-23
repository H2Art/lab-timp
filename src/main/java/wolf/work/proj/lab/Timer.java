package wolf.work.proj.lab;

import wolf.work.proj.front.SimController;

public class Timer implements Runnable {
    private final Habitat hb;
    SimController controller;
    private volatile boolean running = true;
    private boolean paused = false;
    private int startCounter; // начальное значение счётчика

    // Конструктор для нового запуска (счётчик с 0)
    public Timer(SimController controller) {
        this(controller, 0);
    }

    // Конструктор с указанием начального счётчика
    public Timer(SimController controller, int startCounter) {
        this.controller = controller;
        this.hb = new Habitat(controller);
        this.startCounter = startCounter;
    }

    @Override
    public void run() {
        int counter = startCounter; // используем переданное значение
        while (running) {
            try {
                Thread.sleep(10);
                if (!paused) {
                    counter += 1;
                    hb.Update(counter);
                }
            }
            catch (InterruptedException e) {
                running = false;
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stop() {
        running = false;
    }

    public void togglePause() {
        paused = !paused;
    }
}