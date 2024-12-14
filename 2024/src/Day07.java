package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day07 {

    public static class Node {
        int idx;
        long val;
        Node times;
        Node plus;
        Node concat;
        boolean isFinal;

        Node(int idx, long val) {
            this.idx = idx;
            this.val = val;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "idx=" + idx +
                    ", val=" + val +
                    ", isFinal=" + isFinal +
                    '}';
        }
    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/day07.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        long start = System.currentTimeMillis();

        long[] results = new long[data.size()];
        List<List<Long>> valueLists = new ArrayList<>();

        int idx = 0;
        for (String line : data) {
            String [] firstSplit = line.split(": ");
            String [] secondSplit = firstSplit[1].split(" ");
            List<Long> values = new ArrayList<>();
            results[idx] = Long.parseLong(firstSplit[0]);
            for (String val : secondSplit) {
                values.add(Long.parseLong(val));
            }
            valueLists.add(values);
            idx++;
        }


        //  Part 1 -- Tree and depth-first traversal
        //  Part 2 -- Tree with third possibility
        long totalResultPartOne = 0L;
        long totalResultPartTwo = 0L;

        for (int i = 0; i < results.length; i++) {
            long result = results[i];
            List<Long> values = valueLists.get(i);

            Node rootPartTwo = createTree(result, values);
            if (treeIsValid(rootPartTwo, result, false)) {
                totalResultPartOne += result;
            }
            if (treeIsValid(rootPartTwo, result, true)) {
                totalResultPartTwo += result;
            }
        }

        System.out.println("Part 1: Total valid result: " + totalResultPartOne);
        System.out.println("Part 2: Total valid result part 2: " + totalResultPartTwo);
        long end = System.currentTimeMillis();
        System.out.println("Time: " + (end - start) + " ms");
    }

    /**
     * PART 2 METHODS
     */
    public static Node createTree(long result, List<Long> values) {
        Node root = new Node(0, values.getFirst());
        createLowerRungs(root, result, values, 1);
        return root;
    }

    public static void createLowerRungs(Node root, long result, List<Long> values, int idxToUse) {
        if (root == null) {
            root = new Node(0, values.getFirst());
        }
        if (root.isFinal) {
            return;
        }

        Node timesNode = new Node(idxToUse, root.val * values.get(idxToUse));
        Node plusNode = new Node(idxToUse, root.val + values.get(idxToUse));
        Node concatNode = new Node(idxToUse, Long.parseLong(Long.toString(root.val) + Long.toString(values.get(idxToUse))));
        if (idxToUse == values.size() - 1) {
            timesNode.isFinal = true;
            plusNode.isFinal = true;
            concatNode.isFinal = true;
        }
        if (timesNode.val <= result) {
            root.times = timesNode;
            createLowerRungs(root.times, result, values, idxToUse + 1);
        }
        if (plusNode.val <= result) {
            root.plus = plusNode;
            createLowerRungs(root.plus, result, values, idxToUse + 1);
        }
        if (concatNode.val <= result) {
            root.concat = concatNode;
            createLowerRungs(root.concat, result, values, idxToUse + 1);
        }
    }

    public static boolean treeIsValid(Node node, long result, boolean includeConcatenation) {
        if (node == null) {
            return false;
        }
        if (node.isFinal) {
            return node.val == result;
        }
        return treeIsValid(node.times, result, includeConcatenation)
                || treeIsValid(node.plus, result, includeConcatenation)
                || (includeConcatenation && treeIsValid(node.concat, result, true));
    }


    /**
     * Reference only: A much more concise version of what I was doing
     * See https://github.com/mmersic/advent2024/blob/main/src/main/java/org/mersic/Day07.java
     */
    public static long solve(long lhs, long total, long[] rhs, int ri, int ops) {
        if (lhs == total && rhs.length == ri) {
            return total;
        } else if (rhs.length == ri) {
            return 0;
        } else if (total > lhs) {
            return 0;
        } else {
            long result = solve(lhs, total + rhs[ri], rhs, ri+1, ops);
            if (result == lhs) {
                return lhs;
            }
            result = solve(lhs, total * rhs[ri], rhs, ri+1, ops);
            if (result == lhs) {
                return lhs;
            }
            if (ops == 3) {
                return solve(lhs, Long.parseLong(total + "" + rhs[ri]), rhs, ri + 1, ops);
            } else {
                return 0;
            }
        }
    }

//    Called in this loop:
//    for (String line : input) {
//        String[] S = line.split(": ");
//        long lhs = Long.parseLong(S[0]);
//        long[] rhs = Arrays.stream(S[1].split(" ")).mapToLong(Long::parseLong).toArray();
//        partOne += solve(lhs, rhs[0], rhs, 1, 2);
//        partTwo += solve(lhs, rhs[0], rhs, 1, 3);
//    }
}
