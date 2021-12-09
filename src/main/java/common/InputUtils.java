package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class InputUtils {

    public List<String> readLines(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)))) {
            String line = br.readLine();
            while (line != null) {
                if (!line.isEmpty()) {
                    lines.add(line);
                }
                line = br.readLine();
            }
        }

        return lines;
    }
}
