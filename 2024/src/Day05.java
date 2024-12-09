package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Day05 {
    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/day05test.txt"))) {
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

        for (String line : data) {
            if (line.isEmpty()) {
                System.out.println("Empty!");
                pt1 = false;
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
                break;
            }
        }

        


    }

}
