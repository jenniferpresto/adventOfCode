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

        for (int y = 0; y < data.size(); y++) {
            for (int x = 0; x < data.get(y).length(); x++) {
                char c = data.get(y).charAt(x);
                if (c != '.' && c != '#') {
                    List<Location> locations = antennae.getOrDefault(c, new ArrayList<Location>());
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

                    Location antinode1 = getAntinode(loc1, loc2);
                    Location antinode2 = getAntinode(loc2, loc1);
                    if (locationIsOnMap(antinode1, data.getFirst().length(), data.size())) {
                        antinodes.add(antinode1);
                    }
                    if (locationIsOnMap(antinode2, data.getFirst().length(), data.size())) {
                        antinodes.add(antinode2);
                    }
                }
            }
        }
        
        System.out.println("There are " + antinodes.size() + " antinodes");
        int jennifer = 0;

    }

    public static Location getAntinode(final Location loc1, final Location loc2) {
        int x = (2 * loc1.x) - loc2.x;
        int y = (2 * loc1.y) - loc2.y;
        return new Location(x, y);
    }

    public static boolean locationIsOnMap(final Location loc, final int width, final int height) {
        return loc.x >= 0 && loc.x < width && loc.y >= 0 && loc.y < height;
    }
}
