package model;

public interface ExecutableEvent extends Runnable {

    void activate();

    void activateOnce();

    void activate(String message);

    void activateOnce(String message);

    void stop();

    boolean isActive();
}
