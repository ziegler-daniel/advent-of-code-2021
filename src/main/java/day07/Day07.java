package day07;

import common.InputUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day07 {

    public static void main(String[] args) throws IOException {
        List<String> lines = new InputUtils().readLines("/day07.txt");

        List<Integer> positions = Arrays.stream(lines.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
        int min = positions.stream().mapToInt(i -> i).min().orElse(0);
        int max = positions.stream().mapToInt(i -> i).max().orElse(0);

        // part1(positions, min, max);
        part2(positions, min, max);
    }

    private static void part1(List<Integer> positions, int min, int max) {
        int minCost = Integer.MAX_VALUE;
        int bestPos = 0;

        for (int i = min; i <= max; ++i) {
            int costSum = 0;

            for (int p : positions) {
                costSum += Math.abs(p - i);
            }

            if (costSum < minCost) {
                minCost = costSum;
                bestPos = i;
            }
        }

        System.out.printf("Best pos %d cost %d%n", bestPos, minCost);
    }

    private static void part2(List<Integer> positions, int min, int max) {
        int minCost = Integer.MAX_VALUE;
        int bestPos = 0;

        for (int i = min; i <= max; ++i) {
            int costSum = 0;

            for (int p : positions) {
                int dist = Math.abs(p - i);
                costSum += (dist * (dist + 1)) / 2;
            }

            if (costSum < minCost) {
                minCost = costSum;
                bestPos = i;
            }
        }

        System.out.printf("Best pos %d cost %d%n", bestPos, minCost);
    }

}
