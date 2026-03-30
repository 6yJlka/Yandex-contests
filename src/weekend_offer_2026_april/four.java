package weekend_offer_2026_april;

import java.io.*;
import java.util.*;
import org.json.simple.parser.JSONParser;

//для контекста надо менять название на Solution
public class four {
    static String s;
    static int n;
    static JSONParser parser = new JSONParser();

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        s = reader.readLine();
        if (s == null) s = "";
        n = s.length();

        List<int[]> intervals = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);

            if (c == '{' || c == '[') {
                int end = findContainerEnd(i);
                if (end != -1 && isValidJson(i, end)) {
                    intervals.add(new int[]{i, end});
                }
            } else if (c == '"') {
                int end = tryParseString(i);
                if (end != -1) {
                    intervals.add(new int[]{i, end});
                }
            } else if (c == '-' || Character.isDigit(c)) {
                int end = tryParseNumber(i);
                if (end != -1) {
                    intervals.add(new int[]{i, end});
                }
            }
        }

        intervals.sort((a, b) -> a[1] != b[1] ? a[1] - b[1] : a[0] - b[0]);

        int m = intervals.size();

        int[] dp = new int[m + 1];
        boolean[] taken = new boolean[m + 1];
        int[] jump = new int[m + 1];

        for (int i = 1; i <= m; i++) {
            int[] cur = intervals.get(i - 1);
            int len = cur[1] - cur[0];

            int lo = 0, hi = i - 1, best = 0;
            while (lo <= hi) {
                int mid = (lo + hi) / 2;
                if (mid == 0 || intervals.get(mid - 1)[1] <= cur[0]) {
                    best = mid;
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            }
            jump[i] = best;

            int withCur = dp[best] + len;
            int withoutCur = dp[i - 1];

            if (withCur >= withoutCur) {
                dp[i] = withCur;
                taken[i] = true;
            } else {
                dp[i] = withoutCur;
                taken[i] = false;
            }
        }

        List<int[]> chosen = new ArrayList<>();
        int i = m;
        while (i > 0) {
            if (taken[i]) {
                chosen.add(intervals.get(i - 1));
                i = jump[i];
            } else {
                i--;
            }
        }

        writer.write(chosen.size() + " " + dp[m]);
        writer.newLine();
        for (int[] seg : chosen) {
            writer.write(seg[0] + " " + seg[1]);
            writer.newLine();
        }

        writer.flush();
        reader.close();
        writer.close();
    }

    static int findContainerEnd(int start) {
        char open = s.charAt(start);
        char close = open == '{' ? '}' : ']';
        int depth = 0;
        int i = start;

        while (i < n) {
            char c = s.charAt(i);
            if (c == '"') {
                int end = tryParseString(i);
                if (end == -1) return -1;
                i = end;
                continue;
            }
            if (c == open) depth++;
            else if (c == close) {
                depth--;
                if (depth == 0) return i + 1;
            }
            i++;
        }
        return -1;
    }

    static boolean isValidJson(int start, int end) {
        try {
            parser.parse(s.substring(start, end));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static int tryParseString(int start) {
        if (s.charAt(start) != '"') return -1;
        int i = start + 1;
        while (i < n) {
            char c = s.charAt(i);
            if (c == '\\') {
                i += 2;
                continue;
            }
            if (c == '"') return i + 1;
            if (c < 0x20) return -1;
            i++;
        }
        return -1;
    }

    static int tryParseNumber(int start) {
        int i = start;
        if (i < n && s.charAt(i) == '-') i++;

        if (i >= n || !Character.isDigit(s.charAt(i))) return -1;

        if (s.charAt(i) == '0') {
            i++;
            if (i < n && Character.isDigit(s.charAt(i))) return -1;
        } else {
            while (i < n && Character.isDigit(s.charAt(i))) i++;
        }

        if (i < n && (s.charAt(i) == '.' || s.charAt(i) == 'e' || s.charAt(i) == 'E')) return -1;

        return i;
    }
}
