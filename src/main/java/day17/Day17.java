package day17;

import lombok.AllArgsConstructor;

import java.io.IOException;

public class Day17 {

    public static void main(String[] args) throws IOException {
        // Target sampleTarget = new Target(20, 30, -10, -5);
        Target target = new Target(150, 193, -136, -86);

        part1(target);
        part2(target);
    }

    private static void part1(Target t) {
        int yB = 0;
        int xB = 0;
        int score = -1;

        for (int x = 1; x <= t.xE; ++x) {
            for (int y = 1; y < 1000; ++y) {
                int r = simulate(x, y, t);
                if (r > score) {
                    xB = x;
                    yB = y;
                    score = r;
                }
            }
        }

        System.out.printf("Solution part 1: %d for %d %d%n", score, xB, yB);
    }

    private static void part2(Target t) {
        int counter = 0;

        for (int x = -t.xE; x <= t.xE; ++x) {
            for (int y = -1000; y < 1000; ++y) {
                if (simulate(x, y, t) != -1) {
                    ++counter;
                }
            }
        }

        System.out.printf("Solution part 2: %d%n", counter);
    }

    private static int simulate(int vX, int vY, Target target) {
        int pX = 0;
        int pY = 0;
        int yMax = 0;
        boolean targetReached = false;

        while (pX <= target.xE && pY >= target.yS) {
            pX += vX;
            pY += vY;

            vX += vX > 0 ? -1 : 0;
            vY -= 1;

            yMax = Math.max(pY, yMax);

            if (target.xS <= pX && pX <= target.xE && target.yS <= pY && pY <= target.yE) {
                targetReached = true;
            }
        }

        return targetReached ? yMax : -1;
    }

    @AllArgsConstructor
    static class Target {
        int xS;
        int xE;
        int yS;
        int yE;
    }
}
