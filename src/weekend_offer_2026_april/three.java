package weekend_offer_2026_april;

import java.io.*;
import java.util.*;

public class three {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        String line = reader.readLine();
        if (line == null) return;
        int n = Integer.parseInt(line.trim());

        String nextLine = reader.readLine();
        if (nextLine == null) return;
        StringTokenizer st = new StringTokenizer(nextLine);

        long prev = Long.parseLong(st.nextToken());

        List<Integer> pos = new ArrayList<>();

        for (int i = 1; i < n; i++) {
            if (!st.hasMoreTokens()) {
                nextLine = reader.readLine();
                if (nextLine == null) break;
                st = new StringTokenizer(nextLine);
            }

            long cur = Long.parseLong(st.nextToken());

            if (cur - prev != 2) {
                pos.add(i);
            }
            prev = cur;
        }

        if (pos.size() == 1) {
            writer.write(pos.get(0) + " " + pos.get(0));
        } else if (pos.size() >= 2) {
            writer.write(pos.get(0) + " " + pos.get(1));
        }

        writer.newLine();
        writer.flush();

        writer.close();
        reader.close();
    }
}

