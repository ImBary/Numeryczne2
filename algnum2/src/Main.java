import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Park park = new Park();
        File starting = new File(System.getProperty("user.dir"));
        File file = new File(starting,"src/test_case.txt");
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

        Map<Map<Integer, Integer>, Double> matrix = park.createProbabilityMatrix();
        Map<Map<Integer, Integer>, Double> matrix2 = park.transposeMatrix(matrix);
        //System.out.println("Gaaus");
        park.printGauss(matrix2);
        //System.out.println("Gaus seidel");
        park.printGaussSiedel(matrix2);
        //System.out.println("gaus choice");
        park.printGaussWithChoice(matrix2);

        System.out.println("markov chain solver");
        park.markovChainSolver(wanderer);

        //park.getExecTime(matrix);

        System.out.println("Monte carlo:" + park.monte(10000, wanderer));
        System.out.println("exits" + exits);
        System.out.println("Osk" + osk);
    }
}
/*
x1 : 0.4999999999999999
x2 : 0.7499999999999999
x3 : 1.0
x4 : 0.0
x5 : 0.0
x6 : 0.0
x7 : 0.0
 */