package day03;

import common.InputUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day03 {

    public static void main(String[] args) throws IOException {
        List<String> lines = new InputUtils().readLines("/day03.txt");

        //part1(lines);
        part2(lines);
    }

    private static void part1(List<String> lines) {
        String gamma = "";
        String epsilon = "";

        int length = 12;
        int lineCount = lines.size();
        int[] counterOne = new int[12];

        for (String line: lines) {
            for (int i=0; i<length; ++i) {
                counterOne[i] += line.charAt(i) == '1' ? 1 : 0;
            }
        }

        for (int i=0; i<length; i++) {
            System.out.printf("line %d, ones: %s, zeros: %d%n", i, counterOne[i], lineCount - counterOne[i]);

            if (counterOne[i] > (lineCount - counterOne[i])) {
                gamma += "1";
                epsilon += "0";
            } else {
                gamma += "0";
                epsilon += "1";
            }
        }

        System.out.printf("gamma %s, epsilon %s%n", gamma, epsilon);
        System.out.printf("Produkt: %d%n", Integer.parseInt(gamma, 2) * Integer.parseInt(epsilon, 2));
    }

    private static void part2(List<String> lines) {
        String oxygen = computeOxygen(lines);
        String co2 = computeCo2(lines);

        System.out.printf("oygen %s, co2 %s%n", oxygen, co2);
        System.out.printf("Produkt: %d%n", Integer.parseInt(oxygen, 2) * Integer.parseInt(co2, 2));
    }

    private static String computeOxygen(List<String> lines) {
        List<String> oxygenList = new ArrayList<>(lines);
        int pos = 0;

        while(oxygenList.size() > 1) {
            long oneCount = countOnes(oxygenList, pos);
            long zeroCount = oxygenList.size() - oneCount;
            char charToKeep = oneCount >= zeroCount ? '1' : '0';

            oxygenList = filterList(oxygenList, pos, charToKeep);

            pos += 1;
        }

        return oxygenList.get(0);
    }

    private static String computeCo2(List<String> lines) {
        List<String> co2list = new ArrayList<>(lines);
        int pos = 0;

        while(co2list.size() > 1) {
            long oneCount = countOnes(co2list, pos);
            long zeroCount = co2list.size() - oneCount;
            char charToKeep = oneCount >= zeroCount ? '0' : '1';

            co2list = filterList(co2list, pos, charToKeep);

            pos += 1;
        }

        return co2list.get(0);
    }

    private static long countOnes(List<String> list, int pos) {
        return list.stream().filter(s -> s.charAt(pos) == '1').count();
    }

    private static List<String> filterList(List<String> list, int pos, char charToKeep) {
        return list.stream()
                .filter(s -> s.charAt(pos) == charToKeep)
                .collect(Collectors.toList());
    }
}
