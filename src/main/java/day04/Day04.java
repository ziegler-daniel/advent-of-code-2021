package day04;

import common.InputUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day04 {

    public static void main(String[] args) throws IOException {
        List<String> lines = new InputUtils().readLines("/day04.txt");
        List<Integer> calledNumbers = Arrays.stream(lines.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
        List<Board> boards = new ArrayList<>();

        for (int i = 1; i < lines.size(); i += 5) {
            Board b = new Board();
            for (int r = 0; r < 5; ++r) {
                b.addRow(lines.get(i + r), r);
            }
            boards.add(b);
        }


        // part1(calledNumbers, boards);
        part2(calledNumbers, boards);
    }

    private static void part1(List<Integer> calledNumbers, List<Board> boards) {
        for (int i = 0; i < calledNumbers.size(); ++i) {
            int cn = calledNumbers.get(i);

            for (int b = 0; b < boards.size(); ++b) {
                if (boards.get(b).markNumber(cn)) {
                    System.out.printf("Board %d won in step %d on number %d%n", b, i, cn);
                    System.out.printf("Score %d%n", boards.get(b).computeScore() * cn);
                    return;
                }
            }
        }
    }

    private static void part2(List<Integer> calledNumbers, List<Board> boards) {
        Set<Integer> winnerBoards = new HashSet<>();

        for (int i = 0; i < calledNumbers.size(); ++i) {
            int cn = calledNumbers.get(i);

            for (int b = 0; b < boards.size(); ++b) {
                if (!winnerBoards.contains(b) ) {
                    if (boards.get(b).markNumber(cn)) {
                        winnerBoards.add(b);

                        if (winnerBoards.size() == boards.size()) {
                            System.out.printf("Board %d won in step %d on number %d%n", b, i, cn);
                            System.out.printf("Score %d%n", boards.get(b).computeScore() * cn);
                            return;
                        }
                    }
                }

            }
        }
    }


}

class Board {

    int[][] numbers;
    boolean[][] marked;

    public Board() {
        numbers = new int[5][5];
        marked = new boolean[5][5];
    }

    public void addRow(String line, int row) {
        String[] split = line.trim().split("\\s+");
        for (int i = 0; i < 5; ++i) {
            numbers[row][i] = Integer.parseInt(split[i]);
        }
    }

    public boolean markNumber(int n) {
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                if (numbers[r][c] == n) {
                    marked[r][c] = true;
                }
            }
        }

        return checkWin();
    }

    public boolean checkWin() {
        for (int a = 0; a < 5; a++) {
            boolean colWin = true;
            boolean rowWin = true;

            for (int b = 0; b < 5; b++) {
                colWin = marked[a][b] && colWin;
                rowWin = marked[b][a] && rowWin;
            }

            if (rowWin || colWin) {
                return true;
            }
        }
        return false;
    }

    public int computeScore() {
        int sum = 0;

        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                if (!marked[r][c]) {
                    sum += numbers[r][c];
                }
            }
        }

        return sum;
    }
}
