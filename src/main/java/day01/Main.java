package day01;

import common.InputUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        List<Integer> numbers = new InputUtils().readLines("/day01.txt").stream().map(Integer::parseInt)
                .collect(Collectors.toList());

        part1(numbers);
        part2(numbers);
    }

    private static void part1(List<Integer> numbers) {
        int counter = 0;

        for (int i = 1; i < numbers.size(); i++) {
            if (numbers.get(i) > numbers.get(i - 1)) {
                ++counter;
            }
        }

        System.out.println(counter);
    }

    private static void part2(List<Integer> numbers) {
        int lastWindow = numbers.get(0) + numbers.get(1) + numbers.get(2);
        int counter = 0;

        for (int i = 1; i < numbers.size() - 2; i += 1) {
            int currentWindow = numbers.get(i) + numbers.get(i + 1) + numbers.get(i + 2);

            if (currentWindow > lastWindow) {
                ++counter;
            }

            lastWindow = currentWindow;
        }

        System.out.println(counter);
    }
}
