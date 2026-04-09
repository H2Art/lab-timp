package wolf.work.proj.lab;


abstract public class BaseAI implements Runnable {
    BaseAI() {}
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private Thread thread;
    public void start() {
        thread = new Thread(this);
        thread.start();
    }
    @Override
    public void run() {
        while (running) {
            synchronized (this) {
                if (paused) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                UpdateAI();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public abstract void UpdateAI();

    public void stop() {
        running = false;
        synchronized (this) {
            if (!paused) {
                notify();
            }
        }
    }
    public void togglePause() {
        synchronized (this) {
            paused = !paused;
            if (!paused) {
                notify();
            }
        }
    }
    public Thread getThread() {
        return thread;
    }
    public void setPriority(int priority) {
        if (thread != null) {
            thread.setPriority(priority);
        }
    }
}
