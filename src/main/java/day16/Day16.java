package day16;

import common.InputUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Day16 {

    private static int versionNumberSum = 0;

    public static void main(String[] args) throws IOException {
        List<String> lines = new InputUtils().readLines("/day16.txt");

        Result result = parse(toBinary(lines.get(0)));
        System.out.printf("Solution part 1: %d%n", versionNumberSum);
        System.out.printf("Solution part 2: %d%n", result.values.get(0));
    }

    private static String toBinary(String hex) {
        StringBuilder binary = new StringBuilder();
        for (char c : hex.toCharArray()) {
            binary.append(String.format("%4s", Integer.toBinaryString(0xF & Character.getNumericValue(c))).replaceAll(" ", "0"));
        }
        return binary.toString();
    }

    private static Result parse(String input) {
        int index = 0;
        List<Long> values = new ArrayList<>();

        while (index < input.length()) {
            Result r = parsePackage(input.substring(index));
            index += r.length;
            values.addAll(r.values);
        }

        return new Result(index, values);
    }

    private static Result parsePackage(String binary) {
        if (binary.length() <= 6) {
            return new Result(binary.length(), Collections.emptyList());
        }

        int version = Integer.parseInt(binary.substring(0, 3), 2);
        int typeId = Integer.parseInt(binary.substring(3, 6), 2);

        versionNumberSum += version;

        Result result = typeId == 4 ? parseLiteral(binary.substring(6)) : parseOperator(binary.substring(6));
        return new Result(result.length + 6, Collections.singletonList(aggregateValues(typeId, result.values)));
    }

    private static long aggregateValues(int typeId, List<Long> values) {
        switch (typeId) {
            case 0:
                return values.stream().reduce(0L, Long::sum);
            case 1:
                return values.stream().reduce(1L, (a, b) -> a * b);
            case 2:
                return values.stream().min(Long::compareTo).orElse(0L);
            case 3:
                return values.stream().max(Long::compareTo).orElse(0L);
            case 4:
                assert values.size() == 1;
                return values.get(0);
            case 5:
                assert values.size() == 2;
                return values.get(0) > values.get(1) ? 1 : 0;
            case 6:
                assert values.size() == 2;
                return values.get(0) < values.get(1) ? 1 : 0;
            case 7:
                assert values.size() == 2;
                return Objects.equals(values.get(0), values.get(1)) ? 1 : 0;
            default:
                assert false;
                return 0;
        }
    }

    private static Result parseLiteral(String binary) {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 5) {
            if (i + 5 > binary.length()) {
                if (content.length() > 0) {
                    System.err.println("reached end while parsing literal");
                }
                break;
            }

            String current = binary.substring(i, i + 5);
            content.append(current.substring(1));

            if (current.startsWith("0")) {
                return new Result(i + 5, Collections.singletonList(Long.parseLong(content.toString(), 2)));
            }
        }

        return new Result(binary.length(), Collections.emptyList());
    }

    private static Result parseOperator(String binary) {
        if (binary.startsWith("0")) {
            if (binary.length() < 16) {
                return new Result(binary.length(), Collections.emptyList());
            }

            int length = Integer.parseInt(binary.substring(1, 16), 2);
            String content = binary.substring(16, 16 + length);
            Result result = parse(content);

            return new Result(result.length + 16, result.values);
        } else {
            int packageCount = Integer.parseInt(binary.substring(1, 12), 2);
            int index = 12;
            List<Long> values = new ArrayList<>();

            for (int i = 0; i < packageCount; ++i) {
                Result r = parsePackage(binary.substring(index));
                index += r.length;
                values.addAll(r.values);
            }

            return new Result(index, values);
        }
    }

    static class Result {
        int length;
        List<Long> values;

        public Result(int length, List<Long> values) {
            this.length = length;
            this.values = values;
        }
    }
}
