package day02;

import common.InputUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {



    public static void main(String[] args) throws IOException {
        List<String> lines = new InputUtils().readLines("/day02.txt");

        part1(lines);
        part2(lines);
    }

    private static void part1(List<String> lines) {
        long depth = 0;
        long hor = 0;

        for (String line : lines) {
            String[] split = line.split(" ");
            int arg = Integer.parseInt(split[1]);

            switch(split[0]) {
                case "forward":
                    hor += arg;
                    break;
                case "down":
                    depth += arg;
                    break;
                case "up":
                    depth -= arg;
                    break;
                default:
                    System.err.println("unknown command");
            }
        }

        System.out.printf("d: %d, h: %d, multiplied: %d%n", depth, hor, depth * hor);
    }

    private static void part2(List<String> lines) {
        long aim = 0;
        long depth = 0;
        long hor = 0;

        for (String line : lines) {
            String[] split = line.split(" ");
            int arg = Integer.parseInt(split[1]);

            switch(split[0]) {
                case "forward":
                    hor += arg;
                    depth += (aim * arg);
                    break;
                case "down":
                    // depth += arg;
                    aim += arg;
                    break;
                case "up":
                    // depth -= arg;
                    aim -= arg;
                    break;
                default:
                    System.err.println("unknown command");
            }
        }

        System.out.printf("d: %d, h: %d, multiplied: %d%n", depth, hor, depth * hor);
    }
}
