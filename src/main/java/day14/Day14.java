package day14;

import common.InputUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Day14 {

    public static void main(String[] args) throws IOException {
        List<String> lines = new InputUtils().readLines("/day14.txt");

        String startPolymer = lines.get(0);
        HashMap<String, String> rules = new HashMap<>();

        lines.forEach(line -> {
            if (line.contains(" -> ")) {
                String[] split = line.split(" -> ");
                rules.put(split[0], split[1]);
            }
        });

        System.out.printf("Solution part 2: %d%n", computeResult(startPolymer, rules, 10));
        System.out.printf("Solution part 2: %d%n", computeResult(startPolymer, rules, 40));
    }

    private static long computeResult(String startPolymer, HashMap<String, String> rules, int stepCount) {
        long[][] pairCount = countPairsInPolymer(startPolymer);

        for (int i = 0; i < stepCount; ++i) {
            pairCount = applyStep(pairCount, rules);
        }

        long[] charCounts = pairToCharCount(pairCount, startPolymer);
        long mostCommon = Arrays.stream(charCounts).max().orElse(0L);
        long leastCommon = Arrays.stream(charCounts).filter(v -> v > 0).min().orElse(0L);

        System.out.printf("Most %d, least %d%n", mostCommon, leastCommon);
        return mostCommon - leastCommon;
    }

    private static long[][] countPairsInPolymer(String polymer) {
        long[][] result = new long[26][26];

        for (int i = 1; i < polymer.length(); ++i) {
            ++result[polymer.charAt(i - 1) - 'A'][polymer.charAt(i) - 'A'];
        }

        return result;
    }

    private static long[][] applyStep(long[][] pairCount, HashMap<String, String> rules) {
        long[][] result = new long[26][26];

        rules.forEach((String left, String right) -> {
            long count = pairCount[left.charAt(0) - 'A'][left.charAt(1) - 'A'];

            result[left.charAt(0) - 'A'][right.charAt(0) - 'A'] += count;
            result[right.charAt(0) - 'A'][left.charAt(1) - 'A'] += count;
        });

        return result;
    }

    private static long[] pairToCharCount(long[][] pairCount, String startPolymer) {
        long[] charCount = new long[26];

        for (int i = 0; i < 26; ++i) {
            for (int k = 0; k < 26; ++k) {
                charCount[i] += pairCount[i][k];
                charCount[k] += pairCount[i][k];
            }
        }

        charCount[startPolymer.charAt(0) - 'A'] += 1;
        charCount[startPolymer.charAt(startPolymer.length() - 1) - 'A'] += 1;

        for (int i = 0; i < 26; ++i) {
            charCount[i] /= 2;
        }

        return charCount;
    }
}
