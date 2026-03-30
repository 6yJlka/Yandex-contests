package weekend_offer_2026_april;

import java.io.*;

public class one {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        String[] input = reader.readLine().split(" ");
        int n = Integer.parseInt(input[0]);
        int k = Integer.parseInt(input[1]);

        String[] stringValues = reader.readLine().split(" ");
        int[] intValues = new int[n];
        for(int i = 0; i < n; i++){
            intValues[i] = Integer.parseInt(stringValues[i]);
        }

        int answer = 0;

        for(int i = 0; i < n; i++){
            if(intValues[i] >= k){
                answer++;
            } else {
                break;
            }
        }

        writer.write(String.valueOf(answer));
        writer.newLine();
        writer.flush();

        reader.close();
        writer.close();
    }
}
