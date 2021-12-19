package day19;

import common.InputUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day19 {

    public static void main(String[] args) throws IOException {
        List<String> lines = new InputUtils().readLines("/day19.txt");

        List<Scanner> scanners = new ArrayList<>();
        Scanner s = null;

        for (String line : lines) {
            if (line.startsWith("---")) {
                if (s != null) {
                    scanners.add(s);
                }
                s = new Scanner(Integer.parseInt(line.split(" ")[2]));
            } else if (line.contains(",")) {
                String[] split = line.split(",");
                int[] points = new int[3];
                for (int i = 0; i < 3; ++i) {
                    points[i] = Integer.parseInt(split[i]);
                }
                s.beacons.add(points);
            }
        }
        scanners.add(s);
        scanners.forEach(Scanner::computeDistances);

        solve(scanners);
    }

    private static void solve(List<Scanner> scanners) {
        List<int[]> scannerPositions = new ArrayList<>();
        scannerPositions.add(new int[3]);

        while (scanners.size() > 1) {
            for (int i = 1; i < scanners.size(); ++i) {
                HashMap<Integer, Integer> mapping = scanners.get(0).computeMapping(scanners.get(i));

                if (mapping.size() >= 12) {
                    Scanner removed = scanners.remove(i);
                    scannerPositions.add(mergeScanner(scanners.get(0), removed, mapping));
                }
            }
        }

        System.out.printf("Solution part 1: %d%n", scanners.get(0).beacons.size());
        System.out.printf("Solution part 2: %d%n", maxManhattanDistance(scannerPositions));
    }

    private static int maxManhattanDistance(List<int[]> scannerPositions) {
        int max = 0;

        for (int i = 0; i < scannerPositions.size(); ++i) {
            for (int k = i + 1; k < scannerPositions.size(); ++k) {
                max = Math.max(max, manhattanDistance(scannerPositions.get(i), scannerPositions.get(k)));
            }
        }

        return max;
    }

    private static int manhattanDistance(int[] a, int[] b){
        int sum = 0;
        for (int i=0; i<3; ++i) {
            sum += Math.abs(a[i] - b[i]);
        }
        return sum;
    }

    private static int[] mergeScanner(Scanner sa, Scanner sb, HashMap<Integer, Integer> mapping) {
        Transformation transformation = computeTransformation(sa, sb, mapping);

        for (int i = 0; i < sb.beacons.size(); ++i) {
            if (!mapping.containsValue(i)) {
                sa.beacons.add(transformation.transform(sb.beacons.get(i)));
            }
        }

        sa.computeDistances();
        return transformation.transform(new int[3]);
    }

    private static Transformation computeTransformation(Scanner sa, Scanner sb, HashMap<Integer, Integer> mapping) {
        Transformation result = new Transformation();

        for (int beaconA1 : mapping.keySet()) {
            for (int beaconA2 : mapping.keySet()) {
                if (beaconA1 != beaconA2) {
                    int beaconB1 = mapping.get(beaconA1);
                    int beaconB2 = mapping.get(beaconA2);

                    int[] distancesA = new int[3];
                    int[] distancesB = new int[3];
                    for (int i = 0; i < 3; ++i) {
                        distancesA[i] = sa.beacons.get(beaconA1)[i] - sa.beacons.get(beaconA2)[i];
                        distancesB[i] = sb.beacons.get(beaconB1)[i] - sb.beacons.get(beaconB2)[i];
                    }

                    if (allDifferentAndNotNull(distancesA)) {
                        for (int i = 0; i < 3; ++i) {
                            for (int k = 0; k < 3; ++k) {
                                if (Math.abs(distancesA[i]) == Math.abs(distancesB[k])) {
                                    result.indexMapping[i] = k;
                                    result.factor[i] = distancesA[i] == distancesB[k] ? 1 : -1;
                                    result.offset[i] = sa.beacons.get(beaconA1)[i] - (sb.beacons.get(beaconB1)[k] * result.factor[i]);
                                }
                            }
                        }
                        return result;
                    }
                }
            }
        }

        throw new RuntimeException("No transformation found");
    }

    private static boolean allDifferentAndNotNull(int[] values) {
        Set<Integer> s = new HashSet<>();
        for (int v : values) {
            s.add(Math.abs(v));
        }
        return s.size() == values.length && !s.contains(0);
    }

    static class Scanner {
        List<int[]> beacons;
        int id;
        long[][] distances;

        public Scanner(int id) {
            this.id = id;
            beacons = new ArrayList<>();
        }

        void computeDistances() {
            distances = new long[beacons.size()][beacons.size()];

            for (int i = 0; i < beacons.size(); ++i) {
                for (int k = 0; k < beacons.size(); ++k) {
                    long d = distance(beacons.get(i), beacons.get(k));
                    distances[i][k] = d;
                    // distances[k][i] = d;
                }
            }
        }

        long distance(int[] a, int[] b) {
            long sum = 0;
            for (int i = 0; i < 3; ++i) {
                sum += ((long) a[i] - b[i]) * ((long) a[i] - b[i]);
            }
            return sum;
        }

        HashMap<Integer, Integer> computeMapping(Scanner s) {
            HashMap<Integer, Integer> mapping = new HashMap<>();

            for (int i = 0; i < beacons.size(); ++i) {
                for (int k = 0; k < s.beacons.size(); ++k) {
                    long overlap = computeOverlap(distances[i], s.distances[k]);
                    if (overlap >= 12) {
                        if (mapping.containsKey(i)) {
                            throw new RuntimeException("Duplicate mapping found.");
                        }
                        mapping.put(i, k);
                    }
                }
            }
            return mapping;
        }

        long computeOverlap(long[] a, long[] b) {
            Set<Long> set = toSet(a);
            set.retainAll(toSet(b));
            return set.size();
        }

        Set<Long> toSet(long[] a) {
            Set<Long> set = new HashSet<>();
            for (long i : a) {
                set.add(i);
            }
            return set;
        }
    }

    static class Transformation {
        int[] indexMapping;
        int[] factor;
        int[] offset;

        public Transformation() {
            indexMapping = new int[3];
            factor = new int[3];
            offset = new int[3];
        }

        int[] transform(int[] pos) {
            int[] result = new int[3];

            for (int i = 0; i < 3; ++i) {
                result[i] = pos[indexMapping[i]];
                result[i] *= factor[i];
                result[i] += offset[i];
            }

            return result;
        }
    }
}
