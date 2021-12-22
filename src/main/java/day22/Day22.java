package day22;

import common.InputUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day22 {

    public static void main(String[] args) throws IOException {
        List<String> lines = new InputUtils().readLines("/day22.txt");

        List<Command> commands = lines.stream().map(line -> {
            Cube cube = new Cube();
            String[] split = line.split(" ")[1].split(",");
            for (int i = 0; i < 3; ++i) {
                String[] values = split[i].substring(2).split("\\.\\.");
                cube.start[i] = Integer.parseInt(values[0]);
                cube.end[i] = Integer.parseInt(values[1]);
            }
            return new Command(line.startsWith("on"), cube);
        }).collect(Collectors.toList());

        part1(commands);
        part2(commands);
    }

    private static void part1(List<Command> commands) {
        boolean[][][] on = new boolean[102][102][102];

        for (Command command : commands) {
            for (int x = Math.max(command.cube.start[0], -50); x <= Math.min(command.cube.end[0], 50); ++x) {
                for (int y = Math.max(command.cube.start[1], -50); y <= Math.min(command.cube.end[1], 50); ++y) {
                    for (int z = Math.max(command.cube.start[2], -50); z <= Math.min(command.cube.end[2], 50); ++z) {
                        on[x + 50][y + 50][z + 50] = command.on;
                    }
                }
            }
        }

        int count = 0;
        for (int x = 0; x < 102; ++x) {
            for (int y = 0; y < 102; ++y) {
                for (int z = 0; z < 102; ++z) {
                    count += on[x][y][z] ? 1 : 0;
                }
            }
        }
        System.out.printf("Solution part 1: %d%n", count);
    }

    private static void part2(List<Command> commands) {
        List<Cube> cubesOn = new ArrayList<>();

        for (Command command : commands) {
            for (int i = 0; i < cubesOn.size(); ++i) {
                Cube c = cubesOn.get(i);

                if (c.computeOverlap(command.cube) != null) {
                    List<Cube> difference = c.subtract(command.cube);
                    cubesOn.remove(i);
                    cubesOn.addAll(difference);
                    --i;
                }
            }

            if (command.on) {
                cubesOn.add(command.cube);
            }
        }

        long size = cubesOn.stream().mapToLong(Cube::size).sum();
        System.out.printf("Solution part 2: %d%n", size);
    }

    static class Command {
        boolean on;
        Cube cube;

        public Command(boolean on, Cube cube) {
            this.on = on;
            this.cube = cube;
        }
    }

    static class Cube {
        int[] start;
        int[] end;

        public Cube() {
            start = new int[3];
            end = new int[3];
        }

        public Cube(int[] start, int[] end) {
            this.start = start;
            this.end = end;
        }

        public Cube(Cube cube) {
            this.start = Arrays.copyOf(cube.start, 3);
            this.end = Arrays.copyOf(cube.end, 3);
        }

        List<Cube> subtract(Cube other) {
            Cube overlap = computeOverlap(other);
            List<Cube> result = new ArrayList<>();

            if (equals(overlap)) {
                return result;
            }

            Cube remaining = new Cube(this);
            for (int i = 0; i < 3; ++i) {
                result.addAll(subtractInDimension(overlap, remaining, i));
            }

            return result;
        }

        private List<Cube> subtractInDimension(Cube other, Cube remaining, int axis) {
            List<Cube> result = new ArrayList<>();

            if (other.end[axis] < remaining.end[axis]) {
                Cube c = new Cube(Arrays.copyOf(remaining.start, 3), Arrays.copyOf(remaining.end, 3));
                c.start[axis] = other.end[axis] + 1;
                remaining.end[axis] = other.end[axis];
                result.add(c);
            }

            if (other.start[axis] > remaining.start[axis]) {
                Cube c = new Cube(Arrays.copyOf(remaining.start, 3), Arrays.copyOf(remaining.end, 3));
                c.end[axis] = other.start[axis] - 1;
                remaining.start[axis] = other.start[axis];
                result.add(c);
            }

            return result;
        }

        boolean equals(Cube other) {
            for (int i = 0; i < 3; ++i) {
                if (start[i] != other.start[i] || end[i] != other.end[i]) {
                    return false;
                }
            }
            return true;
        }


        long size() {
            long size = 1;
            for (int i = 0; i < 3; ++i) {
                size *= (end[i] - start[i] + 1);
            }
            return size;
        }

        Cube computeOverlap(Cube other) {
            int[] startOverlap = new int[3];
            int[] endOverlap = new int[3];

            for (int i = 0; i < 3; ++i) {
                startOverlap[i] = Math.max(start[i], other.start[i]);
                endOverlap[i] = Math.min(end[i], other.end[i]);

                if (endOverlap[i] < startOverlap[i]) {
                    return null;
                }
            }

            return new Cube(startOverlap, endOverlap);
        }
    }
}
