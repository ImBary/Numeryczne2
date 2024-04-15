import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Park park = new Park();
        File starting = new File(System.getProperty("user.dir"));
        File file = new File(starting,"test_case.txt");
        Scanner sc = new Scanner(file);

        int n = sc.nextInt();
        int m = sc.nextInt();

        for (int i = 0; i < m; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            int length = sc.nextInt();
            park.addAlley(a, b, length);
        }

        int oskCount = sc.nextInt();
        ArrayList<Integer> osk = new ArrayList<>();
        for (int i = 0; i < oskCount; i++) {
            osk.add(sc.nextInt());
        }

        int exitCount = sc.nextInt();
        ArrayList<Integer> exits = new ArrayList<>();
        for (int i = 0; i < exitCount; i++) {
            exits.add(sc.nextInt());
        }

        int wanderer = sc.nextInt();
        park.saveToTxt();
        park.valPark(n, osk, exits);

        sc.close();

        System.out.println("Gaaus");
        park.printGauss();
        System.out.println("Gaus seidel");
        park.printGaussSiedel();
        System.out.println("gaus choice");
        park.printGaussWithChoice();

        park.getExecTime();

        //System.out.println("Monte carlo:"+park.monte(10000, wanderer));
    }
}
