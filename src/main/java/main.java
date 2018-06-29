import events.Beep;
import events.Shutdown;
import events.TextToSpeech;
import model.Alarm;
import model.ExecutableEvent;
import services.AlarmService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {

        Shutdown shutdown = new Shutdown();

        ExecutableEvent tts = new TextToSpeech();
        ExecutableEvent beep = new Beep();

        List<Alarm> alarms = new ArrayList<>();
        alarms.add(new Alarm(LocalTime.now().plusSeconds(1), tts, "Welcome to TTS timer", true));
        alarms.add(new Alarm(LocalTime.now().plusSeconds(2), beep, "Welcome to TTS timer", true));
        alarms.add(new Alarm(LocalTime.of(10,6), tts, "Get ready for the stand up meeting"));
        alarms.add(new Alarm(LocalTime.of(11, 30), tts, "Lunch time!"));
        alarms.add(new Alarm(LocalTime.of(10, 30), tts, "Go get some water", true));
        alarms.add(new Alarm(LocalTime.of(14, 55), tts, "Go get some water", true));
        alarms.add(new Alarm(LocalTime.of(13, 0), tts, "Retrospective"));

        AlarmService service = new AlarmService(alarms);
        Thread t = new Thread(service);
        shutdown.addThread(t);
        t.start();


        Thread shutdownThread = new Thread(shutdown);
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }


}
