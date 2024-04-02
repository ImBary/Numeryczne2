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
        System.out.println("Napisz ile jest OSK oraz gdzie sie znajduja(zmien te linie w kodzie gdy dodasz wiecej osk)");
        int oskCount = sc.nextInt();
        int osk = sc.nextInt();
        System.out.println("Napisz ile jest wyjsc oraz gdzie sie znajduja");
        int exitCount = sc.nextInt();
        int exit = sc.nextInt();
        System.out.println("Napisz ile jest wedrowcow oraz gdzie startuja");
        int wandererCount = sc.nextInt();
        int wanderer = sc.nextInt();
    }

}