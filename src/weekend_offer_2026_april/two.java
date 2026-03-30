package weekend_offer_2026_april;

import java.io.*;

public class two {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = Integer.parseInt(reader.readLine());

        if(isPrime(n)){
            writer.write("1");
            writer.newLine();
            writer.write(String.valueOf(n));
        } else if(n % 2 == 0){
            for(int i = 2; i <= n; i++){
                int second = n - i;
                if(isPrime(i) && isPrime(second)){
                    writer.write("2");
                    writer.newLine();
                    writer.write(i + " " + second);
                    break;
                }
            }
        } else if(isPrime(n - 2)){
            writer.write("2");
            writer.newLine();
            writer.write("2 " + (n - 2));
        } else {
            for(int i = 2; i <= n; i++){
                int third = n - 3 - i;
                if(isPrime(i) && isPrime(third)){
                    writer.write("3");
                    writer.newLine();
                    writer.write("3 " + i + " " + third);
                    break;
                }
            }
        }

        writer.newLine();
        writer.flush();

        reader.close();
        writer.close();
    }

    public static boolean isPrime(int number){
        if(number < 2){
            return false;
        }

        for(int i = 2; i * i <= number; i++){
            if(number % i == 0){
                return false;
            }
        }

        return true;
    }
}
