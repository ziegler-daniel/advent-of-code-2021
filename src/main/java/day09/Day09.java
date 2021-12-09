package day09;

import common.InputUtils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Day09 {

    private static final List<Point> NEIGHBOR_KERNEL = Arrays.asList(
            new Point(-1, 0), // left
            new Point(1, 0), // right
            new Point(0, -1), // up
            new Point(0, 1) // below
    );

    public static void main(String[] args) throws IOException {
        List<String> lines = new InputUtils().readLines("/day09.txt");

        int[][] map = new int[lines.size()][lines.get(0).length()];

        for (int a = 0; a < lines.size(); ++a) {
            for (int b = 0; b < lines.get(a).length(); ++b) {
                map[a][b] = Integer.parseInt(String.valueOf(lines.get(a).charAt(b)));
            }
        }

        part1(map);
        part2(map);
    }

    private static void part1(int[][] map) {
        int riskLevel = 0;

        for (int a = 0; a < map.length; ++a) {
            for (int b = 0; b < map[0].length; ++b) {
                if (isLocalMinima(map, a, b)) {
                    riskLevel += map[a][b] + 1;
                }
            }
        }

        System.out.printf("Solution part 1: %d%n", riskLevel);
    }

    private static void part2(int[][] map) {
        List<Integer> sizes = new ArrayList<>();

        for (int x = 0; x < map.length; ++x) {
            for (int y = 0; y < map[0].length; ++y) {
                if (isLocalMinima(map, x, y)) {
                    boolean[][] basinMap = new boolean[map.length][map[0].length];
                    floodFill(map, basinMap, x, y);
                    sizes.add(computeBasinSize(basinMap));
                }
            }
        }

        sizes.sort(Collections.reverseOrder());
        System.out.printf("Solution part 2: %d%n", sizes.get(0) * sizes.get(1) * sizes.get(2));
    }

    private static boolean isLocalMinima(int[][] map, int x, int y) {
        return NEIGHBOR_KERNEL.stream().allMatch(kernel -> {
            int xn = x + kernel.x;
            int yn = y + kernel.y;

            if (isValidPoint(map, xn, yn)) {
                return map[xn][yn] > map[x][y];
            } else {
                return true;
            }
        });
    }

    private static int computeBasinSize(boolean[][] basinMap) {
        int size = 0;

        for (int x = 0; x < basinMap.length; ++x) {
            for (int y = 0; y < basinMap[0].length; ++y) {
                size += basinMap[x][y] ? 1 : 0;
            }
        }

        return size;
    }

    private static void floodFill(int[][] map, boolean[][] basinMap, int x, int y) {
        if (basinMap[x][y] || map[x][y] == 9) {
            return;
        }
        basinMap[x][y] = true;

        NEIGHBOR_KERNEL.forEach(kernel -> {
            int xn = x + kernel.x;
            int yn = y + kernel.y;

            if (isValidPoint(map, xn, yn) && map[xn][yn] > map[x][y]) {
                floodFill(map, basinMap, xn, yn);
            }
        });
    }

    private static boolean isValidPoint(int[][] map, int x, int y) {
        return x >= 0 && x < map.length && y >= 0 && y < map[0].length;
    }
}
