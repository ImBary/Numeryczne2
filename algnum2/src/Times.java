import java.lang.System;
import java.util.function.Function;

public class Times {

    public double countTime(Function<Void, Void> function){

        long startTime = System.nanoTime();

        function.apply(null);

        long stopTime = System.nanoTime();
        long res = stopTime - startTime;

        return res;
    }

}
