package events;

import java.util.LinkedList;
import java.util.List;

public class Shutdown implements Runnable {
    private List<Thread> threadList;

    public Shutdown() {
        threadList = new LinkedList<>();
    }

    public void addThread(Thread t) {
        threadList.add(t);
    }

    @Override
    public void run() {
        System.out.println("_Shutting down all services");
        threadList.forEach(Thread::interrupt);
    }
}
