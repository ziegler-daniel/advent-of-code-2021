package day18;

import common.InputUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Day18 {

    public static void main(String[] args) throws IOException {
        List<String> lines = new InputUtils().readLines("/day18.txt");

        List<Node> nodeList = lines.stream().map(Day18::parseTree).collect(Collectors.toList());

        part1(nodeList);
        part2(nodeList);
    }

    private static void part1(List<Node> nodeList) {
        Node tree = nodeList.get(0).cloneNode();

        for (Node n : nodeList.subList(1, nodeList.size())) {
            tree = add(tree, n.cloneNode());
        }

        System.out.printf("Solution part 1: %d%n", tree.magnitude());
    }

    private static void part2(List<Node> list) {
        long max = 0;

        for (int i=0; i<list.size(); ++i){
            for(int k=0; k<list.size(); ++k) {
                if (i != k) {
                    long sum = add(list.get(i).cloneNode(), list.get(k).cloneNode()).magnitude();
                    max = Math.max(sum, max);
                }
            }
        }

        System.out.printf("Solution part 2: %d%n", max);
    }

    private static Node parseTree(String input) {
        if (input.length() == 1) {
            return new LeafNode(Integer.parseInt(input));
        }

        int splitIndex = getSplitIndex(input);
        return new Node(parseTree(input.substring(1, splitIndex)), parseTree(input.substring(splitIndex + 1, input.length() - 1)));
    }

    private static int getSplitIndex(String s) {
        int opened = -1;

        for (int i = 0; i < s.length() - 1; ++i) {
            if (s.charAt(i) == ',' && opened == 0) {
                return i;
            } else if (s.charAt(i) == '[') {
                ++opened;
            } else if (s.charAt(i) == ']') {
                --opened;
            }
        }

        throw new RuntimeException("No index found");
    }

    private static Node add(Node tree, Node node) {
        Node newTree = new Node(tree, node);

        boolean done = false;
        while (!done) {
            ExplosionResult result = newTree.explode(0);
            if (result == null) {
                done = !newTree.split();
            }
        }

        return newTree;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    static class Node {
        Node left;
        Node right;

        ExplosionResult explode(int depth) {
            if (depth == 4) {
                assert left instanceof LeafNode && right instanceof LeafNode;
                int leftValue = ((LeafNode) left).value;
                int rightValue = ((LeafNode) right).value;

                return new ExplosionResult(leftValue, rightValue);
            } else {
                ExplosionResult result = left.explode(depth + 1);
                if (result != null) {
                    if (!result.rightAdded) {
                        right.addLeft(result.right);
                        result.rightAdded = true;
                    }
                    if (!result.nodeRemoved) {
                        left = new LeafNode(0);
                        result.nodeRemoved = true;
                    }
                    return result;
                }

                result = right.explode(depth + 1);
                if (result != null) {
                    if (!result.leftAdded) {
                        left.addRight(result.left);
                        result.leftAdded = true;
                    }
                    if (!result.nodeRemoved) {
                        right = new LeafNode(0);
                        result.nodeRemoved = true;
                    }
                    return result;
                }
            }

            return null;
        }

        boolean split() {
            if (left instanceof LeafNode && ((LeafNode) left).value >= 10) {
                int value = ((LeafNode) left).value;
                left = new Node(new LeafNode(value / 2), new LeafNode((value + 1) / 2));
                return true;
            }
            if (left.split()) {
                return true;
            }
            if (right instanceof LeafNode && ((LeafNode) right).value >= 10) {
                int value = ((LeafNode) right).value;
                right = new Node(new LeafNode(value / 2), new LeafNode((value + 1) / 2));
                return true;
            }
            return right.split();
        }

        Node cloneNode() {
            return new Node(left.cloneNode(), right.cloneNode());
        }

        void addLeft(int value) {
            left.addLeft(value);
        }

        void addRight(int value) {
            right.addRight(value);
        }

        long magnitude() {
            return 3 * left.magnitude() + 2 * right.magnitude();
        }

    }

    @AllArgsConstructor
    static class LeafNode extends Node {
        int value;

        @Override
        ExplosionResult explode(int depth) {
            return null;
        }

        @Override
        void addLeft(int value) {
            this.value += value;
        }

        @Override
        void addRight(int value) {
            this.value += value;
        }

        @Override
        boolean split() {
            return false;
        }

        @Override
        long magnitude() {
            return value;
        }

        @Override
        Node cloneNode() {
            return new LeafNode(value);
        }
    }

    @RequiredArgsConstructor
    static class ExplosionResult {
        final int left;
        final int right;

        boolean leftAdded;
        boolean rightAdded;
        boolean nodeRemoved;
    }

}
