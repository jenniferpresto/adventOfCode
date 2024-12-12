package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Day05 {
    static class PageComparator implements Comparator<Integer> {
        private final Map<Integer, Set<Integer>> beforeMap;
        public PageComparator(Map<Integer, Set<Integer>> beforeMap) {
            this.beforeMap = beforeMap;
        }

        @Override
        public int compare(Integer i1, Integer i2) {
            if (beforeMap.containsKey(i1)) {
                if (beforeMap.get(i1).contains(i2)) {
                    return 1;
                } else {
                    return -1;
                }
            }
            System.out.println("Shouldn't reach here");
            return 0;
        }

    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/day05.txt"))) {
            while(scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        boolean pt1 = true;
        int breakIdx = 0;
        Map<Integer, Set<Integer>> befores = new HashMap<>();
        Map<Integer, Set<Integer>> afters = new HashMap<>();
        List<List<Integer>> updates = new ArrayList<>();

        for (String line : data) {
            if (line.isEmpty()) {
                System.out.println("Empty!");
                pt1 = false;
                continue;
            }
            if (pt1) {
                String[] pages = line.split("\\|");
                int left = Integer.parseInt(pages[0]);
                int right = Integer.parseInt(pages[1]);
                if (!befores.containsKey(right)) {
                    befores.put(right, new HashSet<>());
                }
                befores.get(right).add(left);
                if (!afters.containsKey(left)) {
                    afters.put(left, new HashSet<>());
                }
                afters.get(left).add(right);
                breakIdx++;
            } else {
                String[] pagesInUpdate = line.split(",");
                List<Integer> eachUpdate = new ArrayList<>();

                for (String page : pagesInUpdate) {
                    eachUpdate.add(Integer.parseInt(page));
                }
                updates.add(eachUpdate);
            }
        }

        int totalPt1 = 0;
        int totalPt2 = 0;
        for (final List<Integer> update : updates) {
            if (isValid(update, befores)) {
                totalPt1 += getMiddleValue(update);
            } else {
                update.sort(new PageComparator(befores));
                totalPt2 += getMiddleValue(update);
            }
        }
        System.out.println("Total part 1: " + totalPt1);
        System.out.println("Total part 2: " + totalPt2);
    }

    public static boolean isValid(List<Integer> update, Map<Integer, Set<Integer>> befores) {
        final Set<Integer> emptySet = new HashSet<>();
        for (int i = 0; i < update.size() - 1; i++) {
            if (!befores.getOrDefault(update.get(i+1), emptySet).contains(update.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static int getMiddleValue(List<Integer> update) {
        return update.get(update.size() / 2);
    }
}
