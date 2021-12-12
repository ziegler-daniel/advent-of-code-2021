package day11;

import common.InputUtils;

import java.io.IOException;
import java.util.List;

public class Day11 {

    public static void main(String[] args) throws IOException {
        List<String> lines = new InputUtils().readLines("/day11.txt");

        part1(parseInput(lines));
        part2(parseInput(lines));
    }

    private static void part1(int[][] map) {
        int counter = 0;
        for (int i = 0; i < 100; ++i) {
            counter += simulateStep(map);
        }

        System.out.printf("Solution part 1: %d%n", counter);
    }

    private static void part2(int[][] map) {
        for (int i = 0; i < 10000; ++i) {
            int count = simulateStep(map);
            if (count == 100 && allZero(map)) {
                System.out.printf("Solution part 2: %d%n", i + 1);
                return;
            }
        }
    }

    private static int[][] parseInput(List<String> lines) {
        int[][] map = new int[lines.size()][lines.get(0).length()];
        for (int x = 0; x < lines.size(); ++x) {
            for (int y = 0; y < lines.get(x).length(); ++y) {
                map[x][y] = Integer.parseInt(String.valueOf(lines.get(x).charAt(y)));
            }
        }
        return map;
    }

    private static boolean isValidPoint(int[][] map, int x, int y) {
        return x >= 0 && x < map.length && y >= 0 && y < map[0].length;
    }

    private static boolean allZero(int[][] map) {
        for (int x = 0; x < map.length; ++x) {
            for (int y = 0; y < map[0].length; ++y) {
                if (map[x][y] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int simulateStep(int[][] map) {
        for (int x = 0; x < map.length; ++x) {
            for (int y = 0; y < map[0].length; ++y) {
                map[x][y] += 1;
            }
        }

        int counter = 0;
        boolean[][] triggered = new boolean[map.length][map[0].length];

        for (int x = 0; x < map.length; ++x) {
            for (int y = 0; y < map[0].length; ++y) {
                if (map[x][y] > 9) {
                    counter += handleFlash(map, triggered, x, y);
                }
            }
        }

        return counter;
    }

    private static int handleFlash(int[][] map, boolean[][] triggered, int x, int y) {
        int counter = 1;
        map[x][y] = 0;
        triggered[x][y] = true;

        for (int xd = -1; xd < 2; ++xd) {
            for (int yd = -1; yd < 2; ++yd) {
                int xn = x + xd;
                int yn = y + yd;

                if (isValidPoint(map, xn, yn) && !triggered[xn][yn]) {
                    map[xn][yn] += 1;

                    if (map[xn][yn] > 9) {
                        counter += handleFlash(map, triggered, xn, yn);
                    }


                }
            }
        }

        return counter;
    }
}
