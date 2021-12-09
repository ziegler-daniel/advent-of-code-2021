package day05;

import common.InputUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Day05 {

    private static final int SIZE = 1000;

    public static void main(String[] args) throws IOException {
        List<String> lines = new InputUtils().readLines("/day05.txt");
        List<int[]> cords = lines.stream()
                .map(l -> {
                    String[] splits = l.split(" -> ");
                    int[] c = new int[4];
                    c[0] = Integer.parseInt(splits[0].split(",")[0]);
                    c[1] = Integer.parseInt(splits[0].split(",")[1]);
                    c[2] = Integer.parseInt(splits[1].split(",")[0]);
                    c[3] = Integer.parseInt(splits[1].split(",")[1]);
                    return c;
                })
                .collect(Collectors.toList());


        //part1(cords);
        part2(cords);
    }

    private static void part1(List<int[]> cords) {
        int[][] points = new int[SIZE][SIZE];

        cords.stream()
                .filter(c -> c[0] == c[2] || c[1] == c[3])
                .forEach(c -> {
                    if (c[0] == c[2]) {
                        int x = c[0];
                        for (int y = Math.min(c[1], c[3]); y <= Math.max(c[1], c[3]); ++y) {
                            points[x][y] += 1;
                        }
                    } else {
                        int y = c[1];
                        for (int x = Math.min(c[0], c[2]); x <= Math.max(c[0], c[2]); ++x) {
                            points[x][y] += 1;
                        }
                    }
                });

        System.out.printf("Count %d%n", countCrossings(points));
    }

    private static void part2(List<int[]> cords) {
        int[][] points = new int[SIZE][SIZE];

        cords.forEach(c -> {
            if (c[0] == c[2]) {
                int x = c[0];
                for (int y = Math.min(c[1], c[3]); y <= Math.max(c[1], c[3]); ++y) {
                    points[x][y] += 1;
                }
            } else if (c[1] == c[3]) {
                int y = c[1];
                for (int x = Math.min(c[0], c[2]); x <= Math.max(c[0], c[2]); ++x) {
                    points[x][y] += 1;
                }
            } else {
                // diagonal
                int x = c[0];
                int y = c[1];

                while (x != c[2]) {
                    points[x][y] += 1;
                    if (c[0] < c[2]) {
                        ++x;
                    } else {
                        --x;
                    }

                    if (c[1] < c[3]) {
                        ++y;
                    } else {
                        --y;
                    }
                }
                points[x][y] += 1;
            }
        });

        System.out.printf("Count %d%n", countCrossings(points));
    }

    private static int countCrossings(int[][] points) {
        int count = 0;

        for (int x = 0; x < SIZE; ++x) {
            for (int y = 0; y < SIZE; ++y) {
                if (points[x][y] > 1) {
                    ++count;
                }
            }
        }

        return count;
    }

}
