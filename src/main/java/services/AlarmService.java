package services;


import model.Alarm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.Collections.sort;

public class AlarmService implements Runnable {

    private static int spinner;
    private List<Alarm> alarmList;
    private boolean stop;
    private BufferedReader br;

    public AlarmService(List<Alarm> alarmList) {
        this.alarmList = alarmList;
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    @Override
    public void run() {
        sort(alarmList);
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.printf("╠ %-37s ╣\n", "RUNNING ALARMS");
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.printf("╠ %-37s ╣\n", "Active alarms");
        alarmList.forEach(a -> System.out.printf("╠─ %-36s ╣\n", a.toString()));
        System.out.println("╚═══════════════════════════════════════════════╝");
        while (!stop) {
            doSpinner("□□□□□□□□□□□□", "■");
            alarmList.stream()
                    .filter(a -> !a.getEvent().isActive())
                    .filter(a -> formattedTime(a.getTime()).equals(formattedTime(LocalTime.now()))).findFirst()
                    .ifPresent((a) -> {
                        clearLine();
                        a.goOff();
                        if (!a.isSoundOnce()) StopIfActive(a);
                    });
            stopIfLast();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void StopIfActive(Alarm a) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("[ENTER] Stop '" + a.getMessage() + "' ?");
                try {
                    br.read();
                    a.stop();
                    Thread.currentThread().interrupt();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void clearLine() {
        System.out.print("\r");
    }

    private void doSpinner(String path, String change) {
        int pathSize = (int) path.chars().count();

        if (spinner >= (pathSize * 2) - 2) {
            spinner = 0;
        }
        int charcounter = spinner;
        if (spinner >= pathSize) charcounter = pathSize + (pathSize - spinner - 2);

        StringBuilder animatedPath = new StringBuilder();
        for (int i = 0; i < path.chars().count(); i++) {
            if (i == charcounter) {
                animatedPath.append(change);
            } else {
                animatedPath.append(path.charAt(i));
            }
        }
        clearLine();
        System.out.print(animatedPath.toString());
        spinner += 1;
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
