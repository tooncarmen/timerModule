package model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Alarm implements Comparable {
    private LocalTime time;
    private ExecutableEvent event;
    private String message;
    private boolean soundOnce;

    public Alarm(LocalTime time, ExecutableEvent event) {
        this.time = time;
        this.event = event;
    }

    public Alarm(LocalTime time, ExecutableEvent event, String message) {
        this(time, event);
        this.message = message;
    }

    public Alarm(LocalTime time, ExecutableEvent event, String message, boolean soundOnce) {
        this.time = time;
        this.event = event;
        this.message = message;
        this.soundOnce = soundOnce;
    }

    public void goOff() {

        if (message != null) {
            if (soundOnce) {
                event.activateOnce(message);
            } else {
                event.activate(message);
            }
        } else {
            if (soundOnce) {
                event.activateOnce();
            } else {
                event.activate();
            }
        }

    }

    public void stop() {
        event.stop(true);
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public ExecutableEvent getEvent() {
        return event;
    }

    public void setEvent(ExecutableEvent event) {
        this.event = event;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int compareTo(Object o) {
        return ((Alarm) o).getTime().compareTo(this.getTime());
    }

    @Override
    public String toString() {
        return time.format(DateTimeFormatter.ofPattern("[HH:mm:ss]")) + " " + message;
    }

    public void setSoundOnce(boolean soundOnce) {
        this.soundOnce = soundOnce;
    }

    public boolean isSoundOnce() {
        return soundOnce;
    }
}
