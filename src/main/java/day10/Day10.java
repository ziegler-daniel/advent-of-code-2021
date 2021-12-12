package day10;

import common.InputUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class Day10 {

    private static final Set<Character> OPENING = Set.of('(', '[', '{', '<');
    private static final Set<Character> CLOSING = Set.of(')', ']', '}', '>');

    private static final Map<Character, Character> CLOSING_TO_OPENING = Map.of(
            ')', '(',
            ']', '[',
            '}', '{',
            '>', '<'
    );

    private static final Map<Character, Integer> CLOSING_TO_ERROR_SCORE = Map.of(
            ')', 3,
            ']', 57,
            '}', 1197,
            '>', 25137
    );

    private static final Map<Character, Integer> OPENING_TO_AUTOCOMPLETE_SCORE = Map.of(
            '(', 1,
            '[', 2,
            '{', 3,
            '<', 4
    );

    public static void main(String[] args) throws IOException {
        List<String> lines = new InputUtils().readLines("/day10.txt");

        part1(lines);
        part2(lines);
    }

    private static void part1(List<String> lines) {
        long sum = lines.stream().mapToInt(Day10::computeErrorScore).sum();
        System.out.printf("Solution part 1: %d%n", sum);
    }

    private static void part2(List<String> lines) {
        List<Long> scores = lines.stream().map(Day10::computeAutocompleteScore)
                .filter(score -> score > 0)
                .sorted(Long::compareTo)
                .collect(Collectors.toList());

        System.out.printf("Solution part 2: %d%n", scores.get(scores.size() / 2));
    }


    private static int computeErrorScore(String line) {
        Stack<Character> stack = new Stack<>();

        for (char c : line.toCharArray()) {
            if (OPENING.contains(c)) {
                stack.push(c);
            } else if (CLOSING.contains(c)) {
                char top = stack.pop();
                if (top != CLOSING_TO_OPENING.get(c)) {
                    return CLOSING_TO_ERROR_SCORE.get(c);
                }
            } else {
                System.err.println("Error");
            }
        }

        return 0;
    }

    private static long computeAutocompleteScore(String line) {
        Stack<Character> stack = new Stack<>();
        long score = 0;

        for (char c : line.toCharArray()) {
            if (OPENING.contains(c)) {
                stack.push(c);
            } else if (CLOSING.contains(c)) {
                char top = stack.pop();
                if (top != CLOSING_TO_OPENING.get(c)) {
                    return 0;
                }
            } else {
                System.err.println("Error");
            }
        }

        while (!stack.isEmpty()) {
            score = (score * 5) + OPENING_TO_AUTOCOMPLETE_SCORE.get(stack.pop());
        }

        return score;
    }
}
