package day13;

import common.InputUtils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day13 {

    public static void main(String[] args) throws IOException {
        List<String> lines = new InputUtils().readLines("/day13.txt");

        HashSet<Point> points = new HashSet<>();
        List<String> instructions = new ArrayList<>();

        lines.forEach(line -> {
            if (line.startsWith("fold")) {
                instructions.add(line);
            } else {
                String[] split = line.split(",");
                points.add(new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
            }
        });

        part1(points, instructions);
        part2(points, instructions);
    }

    private static void part1(HashSet<Point> points, List<String> instructions) {
        System.out.printf("Solution part 1: %d%n", fold(points, instructions.get(0)).size());
    }

    private static void part2(HashSet<Point> points, List<String> instructions) {
        Set<Point> result = points;

        for (String instruction : instructions) {
            result = fold(result, instruction);
        }

        System.out.printf("Solution part 2: %n");
        printPoints(result);
    }

    private static Set<Point> fold(Set<Point> points, String instruction) {
        int value = Integer.parseInt(instruction.split("=")[1]);
        Set<Point> result = new HashSet<>();

        points.forEach(point -> {
            Point copy = new Point(point);

            if (instruction.contains("y=")) {
                if (copy.y >= value) {
                    copy.y = -1 * copy.y + 2 * value;
                }
            } else {
                if (copy.x >= value) {
                    copy.x = -1 * copy.x + 2 * value;
                }
            }

            result.add(copy);
        });

        return result;
    }

    private static void printPoints(Set<Point> points) {
        int xMax = points.stream().mapToInt(p -> p.x).max().orElse(0);
        int yMax = points.stream().mapToInt(p -> p.y).max().orElse(0);

        for (int y = 0; y <= yMax; ++y) {
            for (int x = 0; x <= xMax; ++x) {
                System.out.print(points.contains(new Point(x, y)) ? "#" : ".");
            }
            System.out.println();
        }
    }
}
