package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day01 {

    public static void main(String[] args) {

        List<String> data = new ArrayList<>();

        try (final Scanner scanner = new Scanner(new File("data/day01.txt"))) {
            while(scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();

        for (final String line : data) {
            String[] values = line.split("\\s+");
            left.add(Integer.parseInt(values[0]));
            right.add(Integer.parseInt(values[1]));
        }
        Collections.sort(left);
        Collections.sort(right);

        //  Part 1
        int distance = 0;
        for (int i = 0; i < data.size(); i++) {
            int diff = Math.abs(left.get(i) - right.get(i));
            distance += diff;
        }
        System.out.println("Distance is " + distance);

        //  Part 2
        int similarity = 0;
        Map<Integer, Integer> rightValues = new HashMap<>();
        for (final Integer value : right) {
            rightValues.put(value, rightValues.getOrDefault(value, 0) + 1);
        }
        for (final Integer value : left) {
            similarity += value * rightValues.getOrDefault(value, 0);
        }

        System.out.println("Similar is " + similarity);

    }
}
