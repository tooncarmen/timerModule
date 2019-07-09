package events;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import model.ExecutableEvent;

import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TextToSpeech implements ExecutableEvent {
    private Voice voice;
    private boolean active = false;
    private String message;
    private final static int MAX_SOUNDING_SEC = 30;


    public TextToSpeech() {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        VoiceManager manager = VoiceManager.getInstance();
        voice = manager.getVoice("kevin16");
        voice.setVolume(200);
        voice.allocate();
    }

    @Override
    public void activate() {
        System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss | ")) + " Alarm");
        voice.speak("Your " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + " alarm went off.");
        this.run();
        startThis();
    }

    @Override
    public void activateOnce() {
        activate();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stop(false);
    }

    private void startThis() {
        active = true;
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void activate(String message) {
        this.message = message;
        startThis();
    }

    @Override
    public void activateOnce(String message) {
        activate(message);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stop(false);
    }

    @Override
    public void stop(boolean showStopMessage) {
        if (active) {
            if (showStopMessage) System.out.println("\r Stopping [" + message + "]");
            this.active = false;
        }
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void run() {
        LocalTime stopTime = LocalTime.now().plusSeconds(MAX_SOUNDING_SEC);
        while (active) {

            Runnable r = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
            r.run();
            System.out.println("\r" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss | ")) + message);
            voice.speak(message);
            if (stopTime.isBefore(LocalTime.now())) {
                stop(true);
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
