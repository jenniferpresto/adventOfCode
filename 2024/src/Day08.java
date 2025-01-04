package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Day08 {
    public static class Location {
        int x;
        int y;

        Location(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Location location = (Location) o;
            return x == location.x && y == location.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/day08.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        Map<Character, List<Location>> antennae = new HashMap<>();
        Set<Location> antinodes = new HashSet<>();
        Set<Location> antinodesWithResonance = new HashSet<>();

        final int mapWidth = data.getFirst().length();
        final int mapHeight = data.size();

        for (int y = 0; y < data.size(); y++) {
            for (int x = 0; x < data.get(y).length(); x++) {
                char c = data.get(y).charAt(x);
                if (c != '.' && c != '#') {
                    List<Location> locations = antennae.getOrDefault(c, new ArrayList<>());
                    locations.add(new Location(x, y));
                    antennae.put(c, locations);
                }
            }
        }

        for (final Map.Entry<Character, List<Location>> entry : antennae.entrySet()) {
            if (entry.getValue().size() < 2) {
                System.out.println("Only one location for " +  entry.getKey());
                continue;
            }
            List<Location> locations = entry.getValue();
            for (int i = 0; i < locations.size(); i++) {
                for (int j = i + 1; j < locations.size(); j++) {
                    Location loc1 = locations.get(i);
                    Location loc2 = locations.get(j);

                    //  Part 1
                    Location antinode1 = getAntinode(loc1, loc2);
                    Location antinode2 = getAntinode(loc2, loc1);
                    if (locationIsOnMap(antinode1, mapWidth, mapHeight)) {
                        antinodes.add(antinode1);
                    }
                    if (locationIsOnMap(antinode2, mapWidth, mapHeight)) {
                        antinodes.add(antinode2);
                    }

                    //  Part 2
                    List<Location> antinodesFromLocations = getAllAntinodesOnMap(loc1, loc2, mapWidth, mapHeight);
                    antinodesWithResonance.addAll(antinodesFromLocations);

                }
            }
        }

        System.out.println("There are " + antinodes.size() + " antinodes");
        System.out.println("There are " + antinodesWithResonance.size() + " antinodes with resonance");

    }

    public static Location getAntinode(final Location loc1, final Location loc2) {
        int x = (2 * loc1.x) - loc2.x;
        int y = (2 * loc1.y) - loc2.y;
        return new Location(x, y);
    }

    public static List<Location> getAllAntinodesOnMap(final Location loc1, final Location loc2, final int width, final int height) {
        List<Location> locations = new ArrayList<>();
        locations.add(loc1);
        locations.add(loc2);
        final int xDist = loc1.x - loc2.x;
        final int yDist = loc1.y - loc2.y;

        int currentX = loc1.x;
        int currentY = loc1.y;
        while(true) {
            currentX -= xDist;
            currentY -= yDist;

            if (coordinatesAreOnMap(currentX, currentY, width, height)) {
                locations.add(new Location(currentX, currentY));
            } else {
                break;
            }
        }

        currentX = loc1.x;
        currentY = loc1.y;
        while(true) {
            currentX += xDist;
            currentY += yDist;

            if (coordinatesAreOnMap(currentX, currentY, width, height)) {
                locations.add(new Location(currentX, currentY));
            } else {
                break;
            }
        }
        return locations;
    }

    public static boolean locationIsOnMap(final Location loc, final int width, final int height) {
        return loc.x >= 0 && loc.x < width && loc.y >= 0 && loc.y < height;
    }

    public static boolean coordinatesAreOnMap(final int x, final int y, final int width, final int height) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

}
