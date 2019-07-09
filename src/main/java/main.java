import events.Beep;
import events.Shutdown;
import events.TextToSpeech;
import model.Alarm;
import model.ExecutableEvent;
import services.AlarmService;
import services.LoadService;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class main {

    public static void main(String[] args) throws IOException {

        Shutdown shutdown = new Shutdown();

        ExecutableEvent tts = new TextToSpeech();
        ExecutableEvent beep = new Beep();

        List<Alarm> alarms = new ArrayList<>();
        alarms.add(new Alarm(LocalTime.now().plusSeconds(1), tts, "Welcome to TTS timer", true));
        alarms.add(new Alarm(LocalTime.now().plusSeconds(5), tts, "instant alarm", false));
        alarms.add(new Alarm(LocalTime.of(10, 0), tts, "Get ready for the stand up meeting"));
        alarms.add(new Alarm(LocalTime.of(10, 30), tts, "Go get some water", true));
        alarms.add(new Alarm(LocalTime.of(11, 30), tts, "Lunch time!"));
        alarms.add(new Alarm(LocalTime.of(14, 55), tts, "Go get some water", true));
        alarms.add(new Alarm(LocalTime.of(16, 15), tts, "get packing"));

        LoadService ls = new LoadService();
        alarms.addAll(ls.loadAlarms());

        AlarmService service = new AlarmService(alarms);
        Thread t = new Thread(service);
        shutdown.addThread(t);
        t.start();

        Thread shutdownThread = new Thread(shutdown);
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }

}
