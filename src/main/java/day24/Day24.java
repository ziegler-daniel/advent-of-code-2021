package day24;

public class Day24 {

    private static final int[] v1 = {13, 15, 15, 11, -16, -11, -6, 11, 10, -10, -8, -11, 12, -15};
    private static final int[] v2 = {5, 14, 15, 16, 8, 9, 2, 13, 16, 6, 6, 9, 11, 5};

    public static void main(String[] args) {
        System.out.printf("Solution part 1: %d%n", solve(13, 0, false));
        System.out.printf("Solution part 2: %d%n", solve(13, 0, true));
    }

    public static long solve(int currentIndex, int resultZ, boolean min) {
        for (int i = 0; i < 9; ++i) {
            int digit = min ? (i + 1) : (9 - i);

            long result = solveForDigitAndX(currentIndex, resultZ, digit, 0, min);
            if (result > 0) {
                return result;
            }

            result = solveForDigitAndX(currentIndex, resultZ, digit, 1, min);
            if (result > 0) {
                return result;
            }
        }

        return -1;
    }

    private static long solveForDigitAndX(int currentIndex, int resultZ, int digit, int x, boolean min) {
        int previousZ = resultZ;
        previousZ -= (digit + v2[currentIndex]) * x;
        int q = x * 25 + 1;

        if (previousZ % q == 0) {
            previousZ /= q;
            int factor = (v1[currentIndex] > 0) ? 1 : 26;
            previousZ *= factor;

            if (factor == 26) {
                for (int f = 0; f < 26; ++f) {
                    if ((((previousZ + f) % 26 + v1[currentIndex] == digit) ? 0 : 1) == x) {
                        if (currentIndex == 0) {
                            return digit;
                        }

                        long result = solve(currentIndex - 1, previousZ + f, min);
                        if (result > 0) {
                            return result * 10 + digit;
                        }
                    }
                }
            } else {
                if (((previousZ % 26 + v1[currentIndex] == digit) ? 0 : 1) == x) {
                    if (currentIndex == 0) {
                        return digit;
                    }

                    long result = solve(currentIndex - 1, previousZ, min);
                    if (result > 0) {
                        return result * 10 + digit;
                    }
                }
            }
        }

        return -1;
    }

}
