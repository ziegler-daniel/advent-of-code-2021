package day06;

import common.InputUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Day06 {

    private static final String input = "3,5,2,5,4,3,2,2,3,5,2,3,2,2,2,2,3,5,3,5,5,2,2,3,4,2,3,5,5,3,3,5,2,4,5,4,3,5,3,2,5,4,1,1,1,5,1,4,1,4,3,5,2,3,2,2,2,5,2,1,2,2,2,2,3,4,5,2,5,4,1,3,1,5,5,5,3,5,3,1,5,4,2,5,3,3,5,5,5,3,2,2,1,1,3,2,1,2,2,4,3,4,1,3,4,1,2,2,4,1,3,1,4,3,3,1,2,3,1,3,4,1,1,2,5,1,2,1,2,4,1,3,2,1,1,2,4,3,5,1,3,2,1,3,2,3,4,5,5,4,1,3,4,1,2,3,5,2,3,5,2,1,1,5,5,4,4,4,5,3,3,2,5,4,4,1,5,1,5,5,5,2,2,1,2,4,5,1,2,1,4,5,4,2,4,3,2,5,2,2,1,4,3,5,4,2,1,1,5,1,4,5,1,2,5,5,1,4,1,1,4,5,2,5,3,1,4,5,2,1,3,1,3,3,5,5,1,4,1,3,2,2,3,5,4,3,2,5,1,1,1,2,2,5,3,4,2,1,3,2,5,3,2,2,3,5,2,1,4,5,4,4,5,5,3,3,5,4,5,5,4,3,5,3,5,3,1,3,2,2,1,4,4,5,2,2,4,2,1,4";

    public static void main(String[] args) throws IOException {
        List<String> lines = new InputUtils().readLines("/day06.txt");

        long[] initialState = new long[9];
        Arrays.stream(lines.get(0).split(",")).forEach(s -> initialState[Integer.parseInt(s)] += 1);

        simulate(initialState, 80); // 256 for part 2
    }

    private static void simulate(long[] state, int days) {
        for(int day=0; day<days; day++) {
            long fishWithOffspring = state[0];

            for (int i=0; i<8; i++) {
                state[i] = state[i+1];
            }

            state[6] += fishWithOffspring;
            state[8] = fishWithOffspring;

            System.out.printf("After day %d: %s%n", day + 1, Arrays.stream(state).sum());
        }
    }

}
