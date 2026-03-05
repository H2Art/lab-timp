package wolf.work.proj.lab;

import wolf.work.proj.front.LabController;
import javafx.application.Platform;


public class Timer implements Runnable {
    private final Habitat hb;
    LabController controller;
    private boolean running = true;

    public Timer(LabController controller) {
        this.controller = controller;
        this.hb = new Habitat(controller);
    }
    @Override
    public void run() {
        int counter = 0;
        while (running) {
            try {
                Thread.sleep(1000);
                if (!running) {
                    break;
                }
                counter++;
                hb.Update(counter);
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
}
