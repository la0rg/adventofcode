package com.la0rg.adventofcode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.ClassLoader.getSystemClassLoader;

/**
 * Created by la0rg on 03.01.2016.
 */

class Reindeer implements Runnable {
    public int speed;
    public int timeToRest;
    public int timeToFly;
    private boolean isRest = false;
    private int total;
    private int passedTimeRest;
    private int passedTimeFly;
    private int numberOfLoopes;
    public int points = 0;

    public Reindeer(int speed, int timeToFly, int timeToRest, int numberOfLoops) {
        this.speed = speed;
        this.timeToRest = timeToRest;
        this.timeToFly = timeToFly;
        this.numberOfLoopes = numberOfLoops;
    }

    public void tick() {
        if (isRest) {
            passedTimeRest++;
            if (passedTimeRest >= timeToRest) {
                passedTimeRest = 0;
                isRest = false;
            }
        } else {
            passedTimeFly++;
            total += speed;
            if (passedTimeFly >= timeToFly) {
                passedTimeFly = 0;
                isRest = true;
            }
        }
    }

    int getTotal() {
        return total;
    }

    void reset() {
        isRest = false;
        passedTimeFly = 0;
        passedTimeRest = 0;
        total = 0;
        points = 0;
    }

    @Override
    public void run() {
        for (int i = 0; i < numberOfLoopes; i++) {
            tick();
        }
    }
}

public class Day14 {
    public static void main(String[] args) throws FileNotFoundException {
        List<Thread> threadList = new ArrayList<>();
        List<Reindeer> reindeerList = new ArrayList<>();
        InputStream fileStream = getSystemClassLoader().getResourceAsStream("Day14data.txt");
        if (fileStream == null) {
            throw new FileNotFoundException();
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(fileStream))) {
            // parse params and create Reindeer
            for (String line; (line = br.readLine()) != null; ) {
                String[] parts = line.split(" ");
                int speed = Integer.parseInt(parts[3]);
                int timeToFly = Integer.parseInt(parts[6]);
                int timeToRest = Integer.parseInt(parts[13]);
                Reindeer r = new Reindeer(speed, timeToFly, timeToRest, 2503);
                reindeerList.add(r);
                threadList.add(new Thread(r));
            }

            // Start all Reindeer and wait for the end of loops
            threadList.forEach(Thread::start);
            threadList.forEach((thread) -> {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("Winner: " + reindeerList.stream().mapToInt(Reindeer::getTotal).max().getAsInt());

            // Part 2
            reindeerList.forEach(Reindeer::reset);
            threadList = null; // no more need the list
            for (int i = 0; i < 2503; i++) {
                reindeerList.forEach(Reindeer::tick);
                int maxTotal = reindeerList.stream().mapToInt(Reindeer::getTotal).max().getAsInt();
                reindeerList.stream().filter(r -> r.getTotal() == maxTotal).forEach(r -> r.points++);
            }
            System.out.println("Winner2: " + reindeerList.stream().mapToInt(r -> r.points).max().getAsInt());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
