package day25;

import common.InputUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Day25 {

    public static void main(String[] args) throws IOException {
        List<String> lines = new InputUtils().readLines("/day25.txt");

        int[][] map = new int[lines.size()][lines.get(0).length()];

        for (int x = 0; x < lines.size(); ++x) {
            char[] chars = lines.get(x).toCharArray();

            for (int y = 0; y < chars.length; ++y) {
                if (chars[y] == '>') {
                    map[x][y] = 1;
                } else if (chars[y] == 'v') {
                    map[x][y] = 2;
                }
            }
        }

        part1(map);
    }

    private static void part1(int[][] map) {
        boolean done = false;
        int step = 0;

        while (!done) {
            int[][] old = map;
            map = stepVer(stepHor(map));
            done = areEqual(old, map);
            ++step;
        }

        System.out.printf("Solution part 1: %d%n", step);
    }

    private static int[][] stepHor(int[][] map) {
        int[][] result = new int[map.length][map[0].length];

        for (int x = 0; x < map.length; ++x) {
            for (int y = 0; y < map[0].length; ++y) {
                if (map[x][y] == 1) {
                    int yTarget = (y + 1) % map[0].length;
                    if (map[x][yTarget] == 0) {
                        result[x][yTarget] = 1;
                    } else {
                        result[x][y] = 1;
                    }
                } else if (map[x][y] == 2) {
                    result[x][y] = 2;
                }
            }
        }

        return result;
    }

    private static int[][] stepVer(int[][] map) {
        int[][] result = new int[map.length][map[0].length];

        for (int x = 0; x < map.length; ++x) {
            for (int y = 0; y < map[0].length; ++y) {
                if (map[x][y] == 1) {
                    result[x][y] = 1;
                } else if (map[x][y] == 2) {
                    int xTarget = (x + 1) % map.length;
                    if (map[xTarget][y] == 0) {
                        result[xTarget][y] = 2;
                    } else {
                        result[x][y] = 2;
                    }
                }
            }
        }

        return result;
    }

    private static boolean areEqual(int[][] a, int[][] b) {
        for (int x = 0; x < a.length; ++x) {
            if (!Arrays.equals(a[x], b[x])) {
                return false;
            }
        }
        return true;
    }
}
