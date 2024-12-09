package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day02 {
    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/day02.txt"))) {
            while(scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        List<List<Integer>> reports = new ArrayList<>();
        for (String line : data) {
            final String[] levels = line.split("\\s+");
            List<Integer> report = new ArrayList<>();
            for (final String level : levels) {
                report.add(Integer.parseInt(level));
            }
            reports.add(report);
        }

        //  Part 1
        int numSafeReports = 0;
        for (final List<Integer> report : reports) {
            if (failsAtIndex(report) == -1) {
                numSafeReports++;
            }
        }
        System.out.println("Num safe reports: " + numSafeReports);

        //  Part 2
        int numSafeToleranceReports = 0;
        for (final List<Integer> report : reports) {
            final int idx = failsAtIndex(report);
            if (idx == -1) {
                numSafeToleranceReports++;
            } else if (isSafeWithTolerance(report, idx)) {
                numSafeToleranceReports++;
                System.out.println("Was safe with tolerance around idx: " + idx + ", " + report);
            }
        }
        System.out.println("Num safe tolerance reports: " + numSafeToleranceReports);

    }

    public static int failsAtIndex(final List<Integer> report) {
        if (report.size() < 2) {
            System.out.println("We have a one-level report");
            return -1;
        }
        if (report.get(0).equals(report.get(1))) {
            return 0;
        }

        boolean isIncreasing = report.get(0) < report.get(1);
        for (int i = 0; i < report.size() - 1; i++) {
            int diff = report.get(i) - report.get(i + 1);
            if (isIncreasing && (diff > -1 || diff < -3)) {
                return i;
            }
            if (!isIncreasing && (diff < 1 || diff > 3)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isSafeWithTolerance(final List<Integer> report, int idx) {
        boolean passes = false;
        if (idx - 1 >= 0) {
            passes = isSafeWithoutIndex(report, idx - 1);
        }
        if (!passes) {
            passes = isSafeWithoutIndex(report, idx);
        }
        if (!passes && idx + 1 < report.size()) {
            passes = isSafeWithoutIndex(report, idx + 1);
        }
        return passes;

    }

    public static boolean isSafeWithoutIndex(final List<Integer> report, final int idx) {
        final List<Integer> toleranceReport = new ArrayList<>();
        for (int i = 0; i < report.size(); i++) {
            if (i == idx) {
                continue;
            }
            toleranceReport.add(report.get(i));
        }
        return failsAtIndex(toleranceReport) == -1;
    }
}
