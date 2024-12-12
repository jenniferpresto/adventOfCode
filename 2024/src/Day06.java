package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Day06 {
    public enum Directions {
            UP, DOWN, LEFT, RIGHT
    };

    public static class DirectionPointer {
        public Directions direction = Directions.UP;

        public void setDirection(Directions dir) {
            direction = dir;
        }
    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/day06.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        final char[][] labMap = new char[data.get(0).length()][data.size()];
        AtomicInteger xPos = new AtomicInteger(0);
        AtomicInteger yPos = new AtomicInteger(0);
        DirectionPointer dir = new DirectionPointer();

        for (int x = 0; x < data.get(0).length(); x++) {
            for (int y = 0; y < data.size(); y++) {
                labMap[x][y] = data.get(y).charAt(x);
                if (labMap[x][y] == '^') {
                    xPos.set(x);
                    yPos.set(y);
                    labMap[x][y] = 'X';
                }
            }
        }

        while (moveOne(labMap, xPos, yPos, dir)) {
            // printLabMap(labMap);
            //  noop

        }

        int totalPart1 = 0;
        for (int x = 0; x < data.getFirst().length(); x++) {
            for (int y = 0; y < data.size(); y++) {
                if (labMap[x][y] == 'X') {
                    totalPart1++;
                }
            }
        }

        printLabMap(labMap);
        System.out.println("Part 1: " + totalPart1);
    }

    public static boolean isOnMap(char [][] labMap, int xPos, int yPos) {
        return xPos >= 0 && xPos < labMap[0].length && yPos >= 0 && yPos < labMap.length;
    }

    public static boolean moveOne(char[][] labMap, AtomicInteger xPos, AtomicInteger yPos, DirectionPointer dir) {
        int newX = xPos.get();
        int newY = yPos.get();

        switch (dir.direction) {
            case Directions.UP:
                newY--;
                break;
            case Directions.RIGHT:
                newX++;
                break;
            case Directions.DOWN:
                newY++;
                break;
            case Directions.LEFT:
            default:
                newX--;
                break;
        }
        if (!isOnMap(labMap, newX, newY)) {
            return false;
        }
        if (labMap[newX][newY] == '.' || labMap[newX][newY] == 'X' || labMap[newX][newY] == '^') {
            xPos.set(newX);
            yPos.set(newY);
            labMap[newX][newY] = 'X';
        } else if (labMap[newX][newY] == '#') {
            setNewDir(dir);
        } else {
            System.out.println("Something's gone wrong");
        }
        return true;
    }

    public static void printLabMap(char[][] labMap) {
        for (int y = 0; y < labMap.length; y++) {
            for (int x = 0; x < labMap[0].length; x++) {
                System.out.print(labMap[x][y]);
            }
            System.out.println();
        }
        System.out.println("*****************************************");
    }

    public static void setNewDir(DirectionPointer dir) {
        switch (dir.direction) {
            case Directions.UP:
                dir.setDirection(Directions.RIGHT);
                break;
            case Directions.RIGHT:
                dir.setDirection(Directions.DOWN);
                break;
            case Directions.DOWN:
                dir.setDirection(Directions.LEFT);
                break;
            case Directions.LEFT:
            default:
                dir.setDirection(Directions.UP);
                break;
        }
    }
}
