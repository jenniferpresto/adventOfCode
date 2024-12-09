package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day04 {
    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/day04.txt"))) {
            while(scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        //  Part 1
        int xmasCount = 0;
        for (int y = 0; y < data.size(); y++) {
            for (int x = 0; x < data.get(y).length(); x++) {
                xmasCount += howManyXmases(data, x, y);
            }
        }
        System.out.println("Number of xmases: " + xmasCount);

        //  Part 2
        int crossMasCount = 0;
        for (int y = 0; y < data.size(); y++) {
            for (int x = 0; x < data.get(y).length(); x++) {
                if (isCrossMas(data, x, y)) {
                    crossMasCount++;
                    System.out.println("(" + x + ", " + y + ")");
                }
            }
        }
        System.out.println("Number of cross-Mases: " + crossMasCount);
    }

    public static int howManyXmases(List<String> wordSearch, int x, int y) {
        if(wordSearch.get(y).charAt(x) != 'X') {
            return 0;
        }
        int total = 0;

        if(isXmasRight(wordSearch, x, y)) {
            total++;
        }

        if(isXmasLeft(wordSearch, x, y)) {
            total++;
        }

        if(isXmasUp(wordSearch, x, y)) {
            total++;
        }

        if(isXmasDown(wordSearch, x, y)) {
            total++;
        }

        if(isXmasUpAndRight(wordSearch, x, y)) {
            total++;
        }

        if(isXmasDownAndRight(wordSearch, x, y)) {
            total++;
        }

        if(isXmasUpAndLeft(wordSearch, x, y)) {
            total++;
        }

        if(isXmasDownAndLeft(wordSearch, x, y)) {
            total++;
        }
        return total;
    }

    public static boolean isXmasRight(List<String> wordSearch, int x, int y) {
        if (x + 3 > wordSearch.get(y).length() - 1) {
            return false;
        }
        return wordSearch.get(y).startsWith("XMAS", x);
    }

    public static boolean isXmasLeft(List<String> wordSearch, int x, int y) {
        if (x < 3) {
            return false;
        }
        return wordSearch.get(y).startsWith("SAMX", x - 3);
    }

    public static boolean isXmasUp(List<String> wordSearch, int x, int y) {
        if (y < 3) {
            return false;
        }
        return wordSearch.get(y).charAt(x) == 'X'
                && wordSearch.get(y-1).charAt(x) == 'M'
                && wordSearch.get(y-2).charAt(x) == 'A'
                && wordSearch.get(y-3).charAt(x) == 'S';

    }

    public static boolean isXmasDown(List<String> wordSearch, int x, int y) {
        if (y + 3 > wordSearch.size() - 1) {
            return false;
        }
        return wordSearch.get(y).charAt(x) == 'X'
                && wordSearch.get(y+1).charAt(x) == 'M'
                && wordSearch.get(y+2).charAt(x) == 'A'
                && wordSearch.get(y+3).charAt(x) == 'S';
    }

    public static boolean isXmasUpAndRight(List<String> wordSearch, int x, int y) {
        if (x + 3 > wordSearch.get(y).length() - 1
            || y < 3) {
            return false;
        }
        return wordSearch.get(y).charAt(x) == 'X'
                && wordSearch.get(y-1).charAt(x+1) == 'M'
                && wordSearch.get(y-2).charAt(x+2) == 'A'
                && wordSearch.get(y-3).charAt(x+3) == 'S';
    }

    public static boolean isXmasDownAndRight(List<String> wordSearch, int x, int y) {
        if (y + 3 > wordSearch.size() - 1
            || x + 3 > wordSearch.get(y).length() - 1) {
            return false;
        }
        return wordSearch.get(y).charAt(x) == 'X'
                && wordSearch.get(y+1).charAt(x+1) == 'M'
                && wordSearch.get(y+2).charAt(x+2) == 'A'
                && wordSearch.get(y+3).charAt(x+3) == 'S';
    }

    public static boolean isXmasUpAndLeft(List<String> wordSearch, int x, int y) {
        if (y < 3 || x < 3) {
            return false;
        }

        return wordSearch.get(y).charAt(x) == 'X'
                && wordSearch.get(y-1).charAt(x-1) == 'M'
                && wordSearch.get(y-2).charAt(x-2) == 'A'
                && wordSearch.get(y-3).charAt(x-3) == 'S';
    }

    public static boolean isXmasDownAndLeft(List<String> wordSearch, int x, int y) {
        if (y + 3 > wordSearch.size() - 1 || x < 3) {
            return false;
        }

        return wordSearch.get(y).charAt(x) == 'X'
                && wordSearch.get(y+1).charAt(x-1) == 'M'
                && wordSearch.get(y+2).charAt(x-2) == 'A'
                && wordSearch.get(y+3).charAt(x-3) == 'S';
    }

    /**
     * Part 2
     */
    public static boolean isCrossMas(List<String> wordSearch, int x, int y) {
        if (x == 0 || x == wordSearch.get(y).length() - 1
                || y == 0 || y == wordSearch.size() - 1) {
            return false;
        }
        if (wordSearch.get(y).charAt(x) != 'A') {
            return false;
        }

        return isDiagonalTopRight(wordSearch, x, y) && isDiagonalTopLeft(wordSearch, x, y);
    }

    //  whoops, misunderstood...
//    public static boolean isUpDown(List<String> wordSearch, int x, int y) {
//        return (wordSearch.get(y-1).charAt(x) == 'M' && wordSearch.get(y+1).charAt(x) == 'S')
//                || (wordSearch.get(y-1).charAt(x) == 'S' && wordSearch.get(y+1).charAt(x) == 'M');
//    }
//
//    public static boolean isLeftRight(List<String> wordSearch, int x, int y) {
//        return (wordSearch.get(y).charAt(x-1) == 'M' && wordSearch.get(y).charAt(x+1) == 'S')
//                || (wordSearch.get(y).charAt(x-1) == 'S' && wordSearch.get(y).charAt(x+1) == 'M');
//    }

    public static boolean isDiagonalTopRight(List<String> wordSearch, int x, int y) {
        return (wordSearch.get(y+1).charAt(x-1) == 'M' && wordSearch.get(y-1).charAt(x+1) == 'S')
                || (wordSearch.get(y+1).charAt(x-1) == 'S' && wordSearch.get(y-1).charAt(x+1) == 'M');
    }

    public static boolean isDiagonalTopLeft(List<String> wordSearch, int x, int y) {
        return (wordSearch.get(y-1).charAt(x-1) == 'M' && wordSearch.get(y+1).charAt(x+1) == 'S')
                || (wordSearch.get(y-1).charAt(x-1) == 'S' && wordSearch.get(y+1).charAt(x+1) == 'M');
    }
}
