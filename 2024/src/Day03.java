package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 {
    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/day03.txt"))) {
            while(scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        //  Part 1
        long total = 0;
        for (String line : data) {
            List<String> matches = returnMatches(line);
            for (final String match : matches) {
                total += multiply(match);
            }
        }
        System.out.println(total);

        //  Part 2
        long totalPart2 = 0;
        boolean active = true;
        for (final String line : data) {
            final int[] pointers = {0, 0, 0}; // do, don't, and operation pointers
            final List<Integer> dos = getIndicesOfRegex(line, "do\\(\\)");
            final List<Integer> donts = getIndicesOfRegex(line, "don't\\(\\)");
            final List<Integer> operations = getIndicesOfRegex(line, "mul\\(\\d+,\\d+\\)");

            final List<String> operationStrings = returnMatches(line);
            while(true) {
                String nextOperation = getNextIndexType(dos, donts, operations, pointers);
                System.out.println(nextOperation);
                if (nextOperation.equals("done")) {
                    break;
                } else if (nextOperation.equals("do")) {
                    active = true;
                    pointers[0]++;
                } else if (nextOperation.equals("dont")) {
                    active = false;
                    pointers[1]++;
                } else if (nextOperation.equals("operation")) {
                    if (active) {
                        totalPart2 += multiply(operationStrings.get(pointers[2]));
                    }
                    pointers[2]++;
                }
            }
            System.out.println(totalPart2);
        }
    }

    public static String getNextIndexType (List<Integer> dos, List<Integer> donts, List<Integer> operations, int[] pointers) {
        if (pointers[2] >= operations.size()) {
            return "done";
        }
        int doVal = pointers[0] < dos.size() ? dos.get(pointers[0]) : Integer.MAX_VALUE;
        int dontVal = pointers[1] < donts.size() ? donts.get(pointers[1]) : Integer.MAX_VALUE;
        int operationVal = operations.get(pointers[2]);

        if (doVal < dontVal && doVal < operationVal) {
            return "do";
        } else if (dontVal < doVal && dontVal < operationVal) {
            return "dont";
        } else {
            return "operation";
        }
    }

    public static List<Integer> getIndicesOfRegex(String line, String regex) {
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(line);
        final List<Integer> indices = new ArrayList<>();
        while(matcher.find()) {
            indices.add(matcher.start());
        }
        return indices;
    }

    public static List<String> returnMatches(final String line) {
        final Pattern pattern = Pattern.compile("mul\\(\\d+,\\d+\\)");
        final Matcher matcher = pattern.matcher(line);
        final List<String> matches = new ArrayList<>();
        while(matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    public static long multiply(final String s) {
        final Pattern pattern = Pattern.compile("\\d+");
        final Matcher matcher = pattern.matcher(s);
        final long[] values = new long[2];
        int numMatches = 0;
        while(matcher.find()) {
            if (numMatches > 1) {
                System.out.println("We shouldn't get here");
            }
            values[numMatches] = Long.parseLong(matcher.group());
            numMatches++;
        }
        return values[0] * values[1];
    }
}
