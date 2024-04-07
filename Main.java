import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Park park = new Park();
        Scanner sc = new Scanner(System.in);
        System.out.println("Napisz n oraz m");
        int n = sc.nextInt();
        int m = sc.nextInt();
        for (int i = 0; i < m; i++) {
            System.out.println("Napisz punkt startowy alejki, punkt koncowy alejki i jej dlugosc");
            int a = sc.nextInt();
            int b = sc.nextInt();
            int length = sc.nextInt();
            park.addAlley(a, b, length);
        }
        System.out.println("Napisz ile jest OSK oraz gdzie sie znajduja");
        int oskCount = sc.nextInt();
        ArrayList<Integer> osk = new ArrayList<>();
        for (int i = 0; i < oskCount; i++) {
            osk.add(sc.nextInt());
        }
        System.out.println("Napisz ile jest wyjsc oraz gdzie sie znajduja");
        int exitCount = sc.nextInt();
        ArrayList<Integer> exits = new ArrayList<>();
        for (int i = 0; i < exitCount; i++) {
            exits.add(sc.nextInt());
        }
        System.out.println("Napisz gdzie startuje wędrowiec");
        int wanderer = sc.nextInt();
        park.saveToTxt();
        park.valPark(n, osk, exits);

        double[][] matrix = park.createProbabilityMatrix();

       // park.printGauss(matrix);
        park.printGaussSiedel(matrix);
        //park.printGaussWithoutChoice(matrix);

        park.getExecTime();

        System.out.println("Monte carlo:"+park.monte(10000, wanderer, exits, osk));

    }

    // public static void testCase(){
    //     Park park = new Park();

    //     park.addAlley(1 ,2 ,4);
    //     park.addAlley(2 ,3, 4);
    //     park.addAlley(3, 4 ,4);
    //     park.addAlley(1 ,3, 6);
    //     park.addAlley(1, 4 ,4);

    //     ArrayList<Integer> osk = new ArrayList<>();
    //     osk.add(1);

    //     ArrayList<Integer> exits = new ArrayList<>();
    //     exits.add(4);
    //     park.createProbabilityMatrix(4, osk, exits);
    // }


}
/*
 4, 5
 1 ,2 ,4
 2 ,3, 4
 3, 4 ,4
 1 ,3, 6
 1, 4 ,4
 1 ,1
 1 ,4
 3
* */
