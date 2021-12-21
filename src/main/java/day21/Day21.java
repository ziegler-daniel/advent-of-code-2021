package day21;

import java.io.IOException;

public class Day21 {

    private static final long[][][][][][] dp = new long[11][11][21][21][2][2];

    public static void main(String[] args) throws IOException {
        // Sample: 4, 8
        part1(2, 1);
        part2(2, 1);
    }

    private static void part1(int p1, int p2) {
        int[] positions = {p1, p2};
        int[] score = new int[2];
        int dice = 1;
        int diceRolledCount = 0;

        while (true) {
            for (int p = 0; p < 2; ++p) {
                int sum = 0;

                for (int i = 0; i < 3; ++i) {
                    sum += dice;
                    ++diceRolledCount;
                    ++dice;
                    if (dice == 101) {
                        dice = 1;
                    }
                }

                positions[p] = positions[p] + sum;
                if (positions[p] % 10 == 0) {
                    positions[p] = 10;
                } else {
                    positions[p] = positions[p] % 10;
                }

                score[p] += positions[p];
                if (score[p] >= 1000) {
                    System.out.printf("Solution part 1: %d%n", diceRolledCount * score[(p + 1) % 2]);
                    return;
                }
            }
        }
    }

    private static void part2(int p1, int p2) {
        long[] result = compute(p1, p2, 0, 0, 0);
        System.out.printf("Solution part 2: %d %n", Math.max(result[0], result[1]));
    }

    private static long[] compute(int p1, int p2, int s1, int s2, int player) {
        if (dp[p1][p2][s1][s2][player][0] == 0 && dp[p1][p2][s1][s2][player][1] == 0) {
            long[] result = new long[2];

            for (int a = 1; a < 4; ++a) {
                for (int b = 1; b < 4; ++b) {
                    for (int c = 1; c < 4; ++c) {
                        int sum = a + b + c;
                        int[] p = {p1, p2};
                        int[] s = {s1, s2};

                        p[player] += sum;
                        if (p[player] > 10) {
                            p[player] -= 10;
                        }

                        s[player] += p[player];

                        if (s[player] >= 21) {
                            result[player]++;
                        } else {
                            long[] tmp = compute(p[0], p[1], s[0], s[1], (player + 1) % 2);
                            result[0] += tmp[0];
                            result[1] += tmp[1];
                        }
                    }
                }
            }

            dp[p1][p2][s1][s2][player] = result;
        }

        return dp[p1][p2][s1][s2][player];
    }

}
