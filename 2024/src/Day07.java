package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        try (final Scanner scanner = new Scanner(new File("data/day07test.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        Map<Long, List<Long>> testValues = new HashMap<>();
        for (String line : data) {
            String [] firstSplit = line.split(": ");
            String [] secondSplit = firstSplit[1].split(" ");
            List<Long> values = new ArrayList<>();
            for (String val : secondSplit) {
                values.add(Long.parseLong(val));
            }
            testValues.put(Long.parseLong(firstSplit[0]), values);
        }


        //  Part 1 -- Tree and depth-first traversal
        //  Part 2 -- Tree with third possibility
        long totalResult = 0L;
        long totalResultPartTwo = 0L;

        for (Map.Entry<Long, List<Long>> entrySet : testValues.entrySet()) {
            long result = entrySet.getKey();
            List<Long> values = entrySet.getValue();
            Node root = createTree(result, values);

            if(treeIsValid(root, result, values.size() - 1)) {
                totalResult += result;
            }

            Node rootPartTwo = createTreePartTwo(result, values);
            if (treeIsValidPartTwo(rootPartTwo, result)) {
                totalResultPartTwo += result;
            }
        }

        System.out.println("Part 1: Total valid result: " + totalResult);
        System.out.println("Part 2: Total valid result part 2: " + totalResultPartTwo);


//        Node root = createTreePartTwo(7290L, testValues.get(7290L));
//        boolean isValue = treeIsValidPartTwo(root, 7290L);
//        int jennifer = 9;
    }

    /**
     * PART 1 METHODS
     */
    public static Node createTree(long result, List<Long> values) {
        Node root = new Node(0, values.getFirst());
        createLowerRungs(root, result, values, 1);
        return root;
    }

    public static void createLowerRungs(Node root, long result, List<Long> values, int newNodeIdx) {
        if (newNodeIdx > values.size() - 1) {
            return;
        }
        Node timesNode = new Node(newNodeIdx, root.val * values.get(newNodeIdx));
        Node plusNode = new Node(newNodeIdx, root.val + values.get(newNodeIdx));
        if (timesNode.val <= result) {
            root.times = timesNode;
            createLowerRungs(root.times, result, values, newNodeIdx + 1);
        }
        if (plusNode.val <= result) {
            root.plus = plusNode;
            createLowerRungs(root.plus, result, values, newNodeIdx + 1);
        }
    }

    public static boolean treeIsValid(Node node, long result, int maxIdx) {
        if (node == null) {
            return false;
        }
        if (node.idx == maxIdx) {
            return node.val == result;
        }
        return treeIsValid(node.times, result, maxIdx) || treeIsValid(node.plus, result, maxIdx);
    }

    /**
     * PART 2 METHODS
     */
    public static Node createTreePartTwo(long result, List<Long> values) {
        Node root = new Node(0, values.getFirst());
        createLowerRungsPartTwo(root, result, values, 1);
        return root;
    }

    public static void createLowerRungsPartTwo(Node root, long result, List<Long> values, int idxToUse) {
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
            createLowerRungsPartTwo(root.times, result, values, idxToUse + 1);
        }
        if (plusNode.val <= result) {
            root.plus = plusNode;
            createLowerRungsPartTwo(root.plus, result, values, idxToUse + 1);
        }
        if (concatNode.val <= result) {
            root.concat = concatNode;
            createLowerRungsPartTwo(root.concat, result, values, idxToUse + 1);
        }
    }

    public static boolean treeIsValidPartTwo(Node node, long result) {
        if (node == null) {
            return false;
        }
        if (node.isFinal) {
            return node.val == result;
        }
        return treeIsValidPartTwo(node.times, result) || treeIsValidPartTwo(node.plus, result) || treeIsValidPartTwo(node.concat, result);
    }

}
