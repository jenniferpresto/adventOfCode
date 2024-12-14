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
        boolean visited;

        Node(int idx, long val) {
            this.idx = idx;
            this.val = val;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "idx=" + idx +
                    ", val=" + val +
                    ", times=" + times +
                    ", plus=" + plus +
                    ", visited=" + visited +
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

        long totalResult = 0L;

        for (Map.Entry<Long, List<Long>> entrySet : testValues.entrySet()) {
            long result = entrySet.getKey();
            List<Long> values = entrySet.getValue();
            Node root = createTree(result, values);
            if(treeIsValid(root, result, values.size() - 1)) {
                totalResult += result;
            }
        }

        System.out.println("Part 1: Total valid result: " + totalResult);
    }

    public static Node createTree(long result, List<Long> values) {
        int idx = 0;
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
}
