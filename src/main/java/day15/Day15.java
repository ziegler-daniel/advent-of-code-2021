package day15;

import common.InputUtils;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

public class Day15 {

    private static final List<Point> NEIGHBOR_KERNEL = Arrays.asList(
            new Point(-1, 0), // left
            new Point(1, 0), // right
            new Point(0, -1), // up
            new Point(0, 1) // below
    );

    public static void main(String[] args) throws IOException {
        List<String> lines = new InputUtils().readLines("/day15.txt");

        int[][] map = new int[lines.size()][lines.get(0).length()];
        for (int x = 0; x < lines.size(); ++x) {
            for (int y = 0; y < lines.get(x).length(); ++y) {
                map[x][y] = Integer.parseInt(String.valueOf(lines.get(x).charAt(y)));
            }
        }

        System.out.printf("Solution part 1: %d%n", findShortestPath(map));
        System.out.printf("Solution part 2: %d%n", findShortestPath(expandMap(map)));
    }

    private static int findShortestPath(int[][] map) {
        HashSet<Point> visitedNodes = new HashSet<>();
        TreeSet<Node> nodes = new TreeSet<>();
        nodes.add(new Node(new Point(0, 0), 0));

        while (!nodes.isEmpty()) {
            Node currentNode = nodes.pollFirst();

            if (!visitedNodes.contains(currentNode.point)) {
                NEIGHBOR_KERNEL.forEach(kernel -> {
                    Point p = new Point(currentNode.point.x + kernel.x, currentNode.point.y + kernel.y);

                    if (isValidPoint(map, p) && !visitedNodes.contains(p)) {
                        nodes.add(new Node(p, currentNode.cost + map[p.x][p.y]));
                    }
                });

                visitedNodes.add(currentNode.point);

                if (currentNode.point.x == map.length - 1 && currentNode.point.y == map[0].length - 1) {
                    return currentNode.cost;
                }
            }
        }

        return -1;
    }

    private static int[][] expandMap(int[][] map) {
        int width = map.length;
        int height = map[0].length;
        int[][] expanded = new int[map.length * 5][map[0].length * 5];

        for (int x = 0; x < map.length; ++x) {
            for (int y = 0; y < map[0].length; ++y) {
                for (int a = 0; a < 5; ++a) {
                    for (int b = 0; b < 5; ++b) {
                        int riskLevel = map[x][y] + a + b;
                        expanded[x + a * width][y + b * height] = riskLevel > 9 ? riskLevel % 10 + 1 : riskLevel;
                    }
                }
            }
        }

        return expanded;
    }

    private static boolean isValidPoint(int[][] map, Point p) {
        return p.x >= 0 && p.x < map.length && p.y >= 0 && p.y < map[0].length;
    }

    private static class Node implements Comparable<Node> {
        Point point;
        int cost;

        public Node(Point point, int cost) {
            this.point = point;
            this.cost = cost;
        }

        @Override
        public int compareTo(Node o) {
            if (cost == o.cost) {
                if (point.x == o.point.x) {
                    return Integer.compare(point.y, o.point.y);
                } else {
                    return Integer.compare(point.x, o.point.x);
                }
            } else {
                return Integer.compare(cost, o.cost);
            }
        }

    }
}
