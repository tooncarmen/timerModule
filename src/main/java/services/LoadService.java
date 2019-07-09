package services;

import events.Beep;
import events.TextToSpeech;
import model.Alarm;
import model.ExecutableEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class LoadService {

    private ExecutableEvent tts = new TextToSpeech();
    private ExecutableEvent beep = new Beep();

    public List<Alarm> loadAlarms() throws IOException {

        List<Alarm> alarms = new ArrayList<>();

        String path = "./alarms.txt";
        Path p = Paths.get(path);
        System.out.println("Loading alarms from " + p.getFileName().toAbsolutePath());
        if (!Files.exists(p)) {
            System.out.println("Creating " + p.getFileName().toAbsolutePath());
            Files.createFile(p);
        }
        try (Stream<String> stream = Files.lines(p)) {
            stream.forEach(s ->
                    alarms.add(new Alarm(getTimeFromLine(s), getEvent(s), getMessage(s)))
            );
        }

        return alarms;
    }

    private LocalTime getTimeFromLine(String s) {
        Pattern p = Pattern.compile("(\\d{2}:\\d{2}:\\d{2})");
        Matcher m = p.matcher(s);

        if (m.find()) {
            try{
                return LocalTime.parse(m.group(1));
            } catch (DateTimeParseException e){
                System.err.println("Can't parse "+ m.group(1));
                return LocalTime.now().minusMinutes(1);
            }
        }
        System.err.println("~ can't parse date from " + s);
        return LocalTime.now().minusMinutes(1);
    }

    private String getMessage(String s) {

        Pattern p = Pattern.compile("(\\d{2}:\\d{2}:\\d{2}) \"?([\\w ]*)\"?");
        Matcher m = p.matcher(s);

        if (m.find()) {
            return m.group(2);
        }
        return s;
    }

    private ExecutableEvent getEvent(String s) {
        Pattern p = Pattern.compile("(\"[\\w ]*\")");
        Matcher m = p.matcher(s);

        if (m.find()) {
            return tts;
        }

        return beep;
    }
}
