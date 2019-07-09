package services;

import events.Beep;
import events.TextToSpeech;
import model.Alarm;
import model.ExecutableEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.Collections.sort;

public class AlarmService implements Runnable {

    private static int spinner;
    private static List<Alarm> alarmList;
    private boolean stop;
    private static BufferedReader br;

    public AlarmService(List<Alarm> alarmList) {
        this.alarmList = alarmList;
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    @Override
    public void run() {
        printAlarms();
        readOptions(true);
        while (!stop) {
            System.out.print(doSpinner("═════════════════════", "╩", "╠", "╣"));
            alarmList.stream()
                    .filter(a -> !a.getEvent().isActive())
                    .filter(a -> formattedTime(a.getTime()).equals(formattedTime(LocalTime.now()))).findFirst()
                    .ifPresent((a) -> {
                        clearLine();
                        a.goOff();
                        if (!a.isSoundOnce()) {
                            readOptions(false);
                            StopIfActive(a);
                        }
                    });

            stopIfLast();
            readOptions(true);
            sleep();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void readOptions(boolean active) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (active) {
                    try {
                        String line = br.readLine();
                        System.out.println(line);
                        if (line.contains("m")) {
                            clearLine();
                            printAlarms();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private static void printAlarms() {
        sort(alarmList);
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.printf("╠ %-45s ╣\n", "RUNNING ALARMS");
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.printf("╠ %-45s ╣\n", "Active alarms ");
        alarmList.forEach(a -> System.out.printf("╠─%s %-43s ╣\n", getTypeIdenifier(a.getEvent()), a.toString()));
        System.out.println("╚═══════════════════════════════════════════════╝");
    }

    private static char getTypeIdenifier(ExecutableEvent event) {

        if (event instanceof Beep) {
            return '>';
        }
        if (event instanceof TextToSpeech) {
            return '»';
        }
        return ' ';
    }

    private static void StopIfActive(Alarm a) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("[ENTER] Stop '" + a.getMessage() + "' ?");
                while (true) {
                    try {
                        br.readLine();
                        a.stop();
                        Thread.currentThread().interrupt();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private static void clearLine() {
        System.out.print("\r");
    }

    private String doSpinner(String path, String change) {
        int pathSize = (int) path.chars().count();

        if (spinner >= (pathSize * 2) - 2) {
            spinner = 0;
        }
        int charcounter = spinner;
        if (spinner >= pathSize) {
            charcounter = pathSize + (pathSize - spinner - 2);
        }

        StringBuilder animatedPath = new StringBuilder();
        for (int i = 0; i < path.chars().count(); i++) {
            if (i == charcounter) {
                animatedPath.append(change);
            } else {
                animatedPath.append(path.charAt(i));
            }
        }
        clearLine();
        spinner += 1;
        return animatedPath.toString();
    }

    private String doSpinner(String path, String change, String begin, String end) {
        return begin + doSpinner(path, change) + end;
    }

    private void stopIfLast() {

        if (LocalTime.now().isAfter(alarmList.get(0).getTime())) {
            clearLine();
            System.out.println("Last alarm has sounded - shutting down");
            stop = true;
        }
    }

    private String formattedTime(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
