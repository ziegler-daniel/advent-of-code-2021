package day20;

import common.InputUtils;

import java.io.IOException;
import java.util.List;

public class Day20 {

    private static final int IMAGE_SIZE = 500;
    private static final int IMAGE_OFFSET = 250;

    public static void main(String[] args) throws IOException {
        List<String> lines = new InputUtils().readLines("/day20.txt");

        char[] c = lines.get(0).toCharArray();
        int[] algorithm = new int[c.length];
        for (int i = 0; i < c.length; ++i) {
            algorithm[i] = c[i] == '#' ? 1 : 0;
        }

        int[][] image = new int[IMAGE_SIZE][IMAGE_SIZE];
        int y = IMAGE_OFFSET;

        for (String line : lines.subList(1, lines.size())) {
            char[] chars = line.toCharArray();
            for (int x = 0; x < chars.length; ++x) {
                image[x + IMAGE_OFFSET][y] = chars[x] == '#' ? 1 : 0;
            }
            ++y;
        }

        solve(image, algorithm);
    }

    private static void solve(int[][] image, int[] algorithm) {
        for (int i = 0; i < 50; ++i) {
            if (i == 2) {
                System.out.printf("Solution part 1: %d%n", countLightPixels(image, 3));
            }
            image = enhance(image, algorithm, i + 1);
        }
        System.out.printf("Solution part 2: %d%n", countLightPixels(image, 51));
    }

    private static int[][] enhance(int[][] image, int[] algorithm, int offset) {
        int[][] result = new int[image.length][image[0].length];
        for (int x = offset; x < image.length - offset; ++x) {
            for (int y = offset; y < image[0].length - offset; ++y) {
                result[x][y] = algorithm[computeKernel(image, x, y)];
            }
        }
        return result;
    }

    private static int computeKernel(int[][] image, int xp, int yp) {
        StringBuilder number = new StringBuilder();
        for (int y = -1; y <= 1; ++y) {
            for (int x = -1; x <= 1; ++x) {
                number.append(image[x + xp][y + yp]);
            }
        }
        return Integer.parseInt(number.toString(), 2);
    }

    private static int countLightPixels(int[][] image, int offset) {
        int sum = 0;
        for (int x = offset; x < image.length - offset; ++x) {
            for (int y = offset; y < image[0].length - offset; ++y) {
                sum += image[x][y];
            }
        }
        return sum;
    }

}
