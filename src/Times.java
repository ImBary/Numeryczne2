import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System;
import java.util.List;
import java.util.function.Function;

public class Times {

    public double countTime(Function<Void, Void> function){

        long startTime = System.nanoTime();

        function.apply(null);

        long stopTime = System.nanoTime();
        long res = stopTime - startTime;

        return res;
    }

    public static void writeTimesToCSV(List<Double> times) {
        
        try (PrintWriter writer = new PrintWriter(new FileWriter("czasy.csv",true))) {
            
            for (int i = 0; i < times.size(); i++) {
                writer.print(times.get(i));
                if (i < times.size() - 1) {
                    writer.print(", ");
                }
            }
            writer.println();
        } catch (IOException e) {
            System.err.println( e.getMessage());
        }
    }

    public static void writeTimesToCSVResullts(List<Double> gauses,String path) {
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(path,true))) {
            
            for (int i = 1; i < gauses.size(); i++) {
                writer.print(Math.abs(gauses.get(i)));
                if (i < gauses.size() - 1) {
                    writer.print(", ");
                }
            }
            writer.println();
        } catch (IOException e) {
            System.err.println( e.getMessage());
        }
    }


}
