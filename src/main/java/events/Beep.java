package events;

import model.ExecutableEvent;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Beep implements ExecutableEvent {
    private boolean active = false;


    @Override
    public void activate(String message) {
        System.out.println("*BEEP BEEP: " + message);
        startBeeper();
    }

    private void startBeeper() {
        active = true;
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void activate() {
        System.out.println("*BEEP BEEP: an alarm went off");
        startBeeper();
    }

    @Override
    public void activateOnce(String message) {
        activate(message);
        active = false;
    }

    @Override
    public void stop(boolean showStopMessage) {
        if (showStopMessage) System.out.println("Stopping Bleep");
        this.active = false;
    }

    @Override
    public void activateOnce() {
        activate();
        active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void run() {
        while (active) {
            //
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                        Beep.class.getResourceAsStream("/bell.wav"));
                clip.open(inputStream);
                clip.start();
                Thread.sleep(900);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }


    }

}

//Sound 1
//Sound 2
//Supported windows property names:
//win.sound.asterisk
//win.sound.close
//win.sound.default
//win.sound.exclamation
//win.sound.exit
//win.sound.hand
//win.sound.maximize
//win.sound.menuCommand
//win.sound.menuPopup
//win.sound.minimize
//win.sound.open
//win.sound.question
//win.sound.restoreDown
//win.sound.restoreUp
//win.sound.start

