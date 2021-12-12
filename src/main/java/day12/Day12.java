package day12;

import common.InputUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day12 {

    public static void main(String[] args) throws IOException {
        List<String> lines = new InputUtils().readLines("/day12.txt");

        HashMap<String, Set<String>> edges = new HashMap<>();

        lines.forEach(line -> {
            String[] split = line.split("-");

            edges.computeIfAbsent(split[0], k -> new HashSet<>());
            edges.computeIfAbsent(split[1], k -> new HashSet<>());

            edges.get(split[0]).add(split[1]);
            edges.get(split[1]).add(split[0]);
        });

        part1(edges);
        part2(edges);
    }

    private static void part1(HashMap<String, Set<String>> edges) {
        int result = findPaths("start", edges, new HashSet<>());
        System.out.printf("Solution part 1: %d%n", result);
    }

    private static void part2(HashMap<String, Set<String>> edges) {
        int result = findPathsPart2("start", edges, new HashSet<>(), new HashSet<>());
        System.out.printf("Solution part 2: %d%n", result);
    }

    private static int findPaths(String node, HashMap<String, Set<String>> edges, Set<String> visitedSmallNodes) {
        if ("end".equals(node)) {
            return 1;
        }

        if (visitedSmallNodes.contains(node)) {
            return 0;
        }

        if (node.toLowerCase().equals(node)) {
            visitedSmallNodes.add(node);
        }

        int sum = 0;
        for (String neighbor : edges.get(node)) {
            sum += findPaths(neighbor, edges, visitedSmallNodes);
        }

        visitedSmallNodes.remove(node);
        return sum;
    }

    private static int findPathsPart2(String node, HashMap<String, Set<String>> edges, Set<String> visitedSmallNodes, Set<String> visitedSmallNodesTwice) {
        if ("end".equals(node)) {
            return 1;
        }

        boolean nodeVisitedOnce = visitedSmallNodes.contains(node);

        if (visitedSmallNodesTwice.contains(node) || (!visitedSmallNodesTwice.isEmpty() && nodeVisitedOnce)) {
            return 0;
        }

        if (node.toLowerCase().equals(node)) {
            (nodeVisitedOnce ? visitedSmallNodesTwice : visitedSmallNodes).add(node);
        }

        int sum = 0;
        for (String neighbor : edges.get(node)) {
            if (!neighbor.equals("start")) {
                sum += findPathsPart2(neighbor, edges, visitedSmallNodes, visitedSmallNodesTwice);
            }
        }

        (nodeVisitedOnce ? visitedSmallNodesTwice : visitedSmallNodes).remove(node);
        return sum;
    }
}
