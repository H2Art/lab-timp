package wolf.work.proj.lab;


abstract public class BaseAI implements Runnable {
    BaseAI(boolean is_paused) {
        paused = !is_paused;
    }
    private volatile boolean running = true;
    private volatile boolean paused;
    private Thread thread = new Thread(this);
    public void start() {
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
            notify();
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
