package day08;

import common.InputUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day08 {

    public static void main(String[] args) throws IOException {
        List<String> lines = new InputUtils().readLines("/day08.txt");

        part1(lines);
        part2(lines);
    }

    private static void part1(List<String> lines) {
        int counter = 0;
        for (String line : lines) {
            String[] split = line.split(" \\| ")[1].split(" ");

            for (int i = 0; i < 4; ++i) {
                if (split[i].length() == 2 || split[i].length() == 3 || split[i].length() == 4 || split[i].length() == 7) {
                    ++counter;
                }
            }
        }

        System.out.printf("Solution part 1: %d%n", counter);
    }

    private static void part2(List<String> lines) {
        int sum = 0;

        for (String line : lines) {
            sum += decode(line);
        }

        System.out.printf("Solution part 2: %d%n", sum);
    }

    private static int decode(String line) {
        String[] split = line.split(" \\| ");
        String[] inputs = split[0].split(" ");
        String[] output = split[1].split(" ");

        Set<Character>[] numbers = new Set[10];
        List<Set<Character>> lengthFive = new ArrayList<>();
        List<Set<Character>> lengthSix = new ArrayList<>();

        for (String s : inputs) {
            switch (s.length()) {
                case 2:
                    numbers[1] = toSet(s);
                    break;
                case 3:
                    numbers[7] = toSet(s);
                    break;
                case 4:
                    numbers[4] = toSet(s);
                    break;
                case 7:
                    numbers[8] = toSet(s);
                    break;

                case 5:
                    lengthFive.add(toSet(s));
                    break;
                case 6:
                    lengthSix.add(toSet(s));
                    break;
            }
        }

        lengthFive.forEach(s -> {
            if (s.containsAll(numbers[1])) {
                numbers[3] = s;
            } else {
                Set<Character> copy = new HashSet<>(s);
                copy.retainAll(numbers[4]);

                if (copy.size() == 3) {
                    numbers[5] = s;
                } else {
                    numbers[2] = s;
                }
            }
        });

        lengthSix.forEach(s -> {
            HashSet<Character> copy = new HashSet<>(s);
            copy.retainAll(numbers[4]);

            if (copy.size() == 4) {
                numbers[9] = s;
            } else {
                copy.retainAll(numbers[1]);

                if (copy.size() == 2) {
                    numbers[0] = s;
                } else {
                    numbers[6] = s;
                }
            }
        });

        int result = 0;
        for (String s : output) {
            for (int i = 0; i < 10; ++i) {
                if (numbers[i].size() == s.length() && numbers[i].containsAll(toSet(s))) {
                    result = 10 * result + i;
                }
            }
        }
        return result;
    }

    private static Set<Character> toSet(String s) {
        Set<Character> set = new HashSet<>();
        for (char c : s.toCharArray()) {
            set.add(c);
        }
        return set;
    }

}
