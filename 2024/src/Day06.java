package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
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

        public char getDirectionChar() {
            return switch (direction) {
                case Directions.UP -> 'U';
                case Directions.RIGHT -> 'R';
                case Directions.DOWN -> 'D';
                default -> 'L';
            };
        }

        public char getNextDirectionChar() {
            return switch (direction) {
                case Directions.UP -> 'R';
                case Directions.RIGHT -> 'D';
                case Directions.DOWN -> 'L';
                default -> 'U';
            };
        }
    }

    public static class Position {
        public int x;
        public int y;
        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Position{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
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

        final char[][] labMap = new char[data.getFirst().length()][data.size()];
        DirectionPointer dir = new DirectionPointer();

        List<List<Set<Character>>> history = new ArrayList<>();

        int startX = 0;
        int startY = 0;

        for (int x = 0; x < data.getFirst().length(); x++) {
            List<Set<Character>> xList = new ArrayList<>();
            history.add(xList);
            for (int y = 0; y < data.size(); y++) {
                Set<Character> ySet = new HashSet<>();
                xList.add(ySet);
                labMap[x][y] = data.get(y).charAt(x);
                if (labMap[x][y] == '^') {
                    startX = x;
                    startY = y;
                }
            }
        }

        final Position guardPos = new Position(startX, startY);
        history.get(startX).get(startY).add('U');

        String isMoving = "move";

        //  Part 1
        AtomicInteger numMovesPt1 = new AtomicInteger(1);
        while (isMoving.equals("move")) {
            isMoving = moveOne(labMap, guardPos, dir, history,  numMovesPt1);
        }

        int totalPart1 = 0;

        for (int x = 0; x < data.getFirst().length(); x++) {
            for (int y = 0; y < data.size(); y++) {
                if (labMap[x][y] != '#' && labMap[x][y] != '.') {
                    totalPart1++;
                }
            }
        }

        printLabMap(labMap);
        System.out.println("Part 1: " + numMovesPt1.get());
        System.out.println("Part 1b: " + totalPart1);


        //  Part 2
        int numPossibleObstacles = 0;
        clearLabMap(labMap, data);
        clearHistory(history);
        AtomicInteger numMovesPt2 = new AtomicInteger(1);
        for (int x = 0; x < labMap[0].length; x++) {
            for (int y = 0; y < labMap.length; y++) {
                if (labMap[x][y] == '#' || labMap[x][y] == '^') {
                    continue;
                }
                guardPos.x = startX;
                guardPos.y = startY;
                dir.direction = Directions.UP;

                labMap[x][y] = '#';

                String moveStatus = "move";
                while (moveStatus.equals("move")) {
                    moveStatus = moveOne(labMap, guardPos, dir, history, numMovesPt2);
                }
                if (moveStatus.equals("loop")) {
                    numPossibleObstacles++;
//                    printLabMap(labMap);
                }
                clearLabMap(labMap, data);
                clearHistory(history);
            }
        }
        System.out.println("Part 2: number of possible obstacles: " + numPossibleObstacles);
    }

    public static boolean isOnMap(char [][] labMap, int xPos, int yPos) {
        return xPos >= 0 && xPos < labMap[0].length && yPos >= 0 && yPos < labMap.length;
    }

    public static boolean isOnEdgeOfMapForDirection(char [][] labMap, int xPos, int yPos, DirectionPointer dir) {
        return switch (dir.direction) {
            case Directions.UP -> yPos == 0;
            case Directions.RIGHT -> xPos == labMap[0].length - 1;
            case Directions.DOWN -> yPos == labMap.length - 1;
            case Directions.LEFT -> xPos == 0;
        };
    }

    public static String moveOne(char[][] labMap, Position pos, DirectionPointer dir, List<List<Set<Character>>> history, AtomicInteger numMoves) {
        int newX = pos.x;
        int newY = pos.y;

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
                newX--;
                break;
        }

        if (!isOnMap(labMap, newX, newY)) {
            return "exit";
        }

        if (labMap[newX][newY] != '#') {
            pos.x = newX;
            pos.y = newY;
            if(history.get(newX).get(newY).contains(dir.getDirectionChar())) {
                return "loop";
            }
            history.get(newX).get(newY).add(dir.getDirectionChar());
            if (labMap[newX][newY] == '.') {
                numMoves.getAndIncrement();
            }
            labMap[newX][newY] = dir.getDirectionChar();
        } else if (labMap[newX][newY] == '#') {
            setNewDir(dir);
        } else {
            System.out.println("Something's gone wrong");
        }
        return "move";
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

    public static void clearHistory(List<List<Set<Character>>> history) {
        for (List<Set<Character>> row : history) {
            for (Set<Character> set : row) {
                set.clear();
            }
        }
    }

    public static void clearLabMap(char[][] labMap, List<String> data) {
        for (int x = 0; x < data.getFirst().length(); x++) {
            for (int y = 0; y < data.size(); y++) {
                labMap[x][y] = data.get(y).charAt(x);
            }
        }
    }
}
