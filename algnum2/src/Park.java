import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Function;

/*
    TODO:
        1. Gauss seidel
        2. zmiana tablicy 2D na map<map<>, double>
            a. zmienione w lengths zmienone w prawdopodobienstwie
            b. gauss no choice gauss choice
*/
public class Park extends Times {
    private Map<Integer, Intersection> intersections = new HashMap<>();
    private List<Alley> alleys = new ArrayList<>();
    
    private int n;
    private ArrayList<Integer> osk;
    private ArrayList<Integer> exits;
    public void valPark(int n, ArrayList<Integer> osk, ArrayList<Integer> exits){
        this.n = n;
        this.osk = osk;
        this.exits = exits;
    }
    public Park(){}

    public void addIntersection(Intersection intersection) {
        intersections.put(intersection.getId(), intersection);
    }
    public void addAlley(Alley alley) {
        alleys.add(alley);
    }
    public void addAlley(int aId, int bId, int length) {
        Intersection a = intersections.getOrDefault(aId, new Intersection(aId));
        Intersection b = intersections.getOrDefault(bId, new Intersection(bId));
        Alley alley = new Alley(a, b, length);
        addIntersection(a);
        addIntersection(b);
        addAlley(alley);
    }

    private Map<Map<Integer, Integer>, Double> createLengthsMatrix( ArrayList<Integer> osk, ArrayList<Integer> exits){
        Map<Map<Integer, Integer>, Double> lengthsMap = new HashMap<>();

        for (Alley alley : alleys) {
            if (!osk.contains(alley.getA().getId()) && !exits.contains(alley.getA().getId())) {
                Map<Integer, Integer> idMap = new HashMap<>();
                idMap.put(alley.getA().getId(), alley.getB().getId());
                lengthsMap.put(idMap, (double) alley.getLength());
            }
            if (!osk.contains(alley.getB().getId()) && !exits.contains(alley.getB().getId())) {
                Map<Integer, Integer> idMap = new HashMap<>();
                idMap.put(alley.getB().getId(), alley.getA().getId());
                lengthsMap.put(idMap, (double) alley.getLength());
            }
        }
        return lengthsMap;
    }

    public Map<Map<Integer, Integer>, Double> createProbabilityMatrix() {

        Map<Map<Integer, Integer>, Double> lengths = createLengthsMatrix(osk, exits);
        Map<Map<Integer, Integer>, Double> probabilities = new HashMap<>();

        double[] inverseSums = new double[n + 1];
        for (Map.Entry<Map<Integer, Integer>, Double> entry : lengths.entrySet()) {
            Map<Integer, Integer> idMap = entry.getKey();
            double length = entry.getValue();

            int fromVertex = idMap.keySet().iterator().next();
            int toVertex = idMap.values().iterator().next();

            if (fromVertex != toVertex && length != 0) {
                inverseSums[fromVertex] += 1.0 / length;
            }
        }

        for (Map.Entry<Map<Integer, Integer>, Double> entry : lengths.entrySet()) {
            Map<Integer, Integer> idMap = entry.getKey();
            double length = entry.getValue();

            int fromVertex = idMap.keySet().iterator().next();
            int toVertex = idMap.values().iterator().next();

            double probability;
            if (fromVertex == toVertex) {
                probability = 1.0;
            } else if (length != 0) {
                probability = (1.0 / length) / inverseSums[fromVertex];
            } else {
                probability = 0.0;
            }

            probabilities.put(idMap, probability);
        }

        for (int i = 1; i <= n; i++) {
            Map<Integer, Integer> id = new HashMap<>();
            id.put(i, i);
            probabilities.put(id, 1.0);
        }

        for (int i = 1; i <= n; i++) {
            if (exits.contains(i)) {
                Map<Integer, Integer> idMap = new HashMap<>();
                idMap.put(i, n+1);
                probabilities.put(idMap, 1.0);
            }
        }

        //DEBUG probabilities
        /*for (Map.Entry<Map<Integer, Integer>, Double> entry : probabilities.entrySet()) {
            Map<Integer, Integer> idMap = entry.getKey();
            Double value = entry.getValue();

            System.out.print("Keys: ");
            idMap.forEach((k, v) -> System.out.print("(" + k + ", " + v + ") "));
            System.out.println("val: " + value);
        }*/

        return probabilities;
    }
    
    //---------------------------------------Gauss with choice------------------------------------

    private double[] solveGausWithChoice(Map<Map<Integer, Integer>, Double> matrix) {
        double[] x = new double[n + 1];

        // Gaussian elimination with partial pivoting
        for (int i = 1; i <= n; i++) {
            // Find the row with the maximum absolute value in the current column
            int maxRow = i;
            for (int j = i + 1; j <= n; j++) {
                if (matrix.get(createPair(j, i)) != null &&
                        (matrix.get(createPair(maxRow, i)) == null ||
                                Math.abs(matrix.get(createPair(j, i))) > Math.abs(matrix.get(createPair(maxRow, i))))) {
                    maxRow = j;
                }
            }

            // Swap rows if necessary
            if (maxRow != i) {
                for (int k = i; k <= n + 1; k++) {
                    double temp = matrix.getOrDefault(createPair(i, k), 0.0);
                    matrix.put(createPair(i, k), matrix.getOrDefault(createPair(maxRow, k), 0.0));
                    matrix.put(createPair(maxRow, k), temp);
                }
            }

            // Eliminate entries below the pivot
            for (int j = i + 1; j <= n; j++) {
                double factor = 0.0;
                Double valueJ = matrix.get(createPair(j, i));
                Double valueI = matrix.get(createPair(i, i));

                if (valueJ != null && valueI != null && valueI != 0.0) {
                    factor = valueJ / valueI;
                    for (int k = i; k <= n + 1; k++) {
                        double val = matrix.getOrDefault(createPair(j, k), 0.0) - factor * matrix.getOrDefault(createPair(i, k), 0.0);
                        matrix.put(createPair(j, k), val);
                    }
                }
            }
        }

        for (int i = n; i >= 1; i--) {
            double sum = 0.0;
            for (int j = i + 1; j <= n; j++) {
                Double valueIJ = matrix.get(createPair(i, j));
                if (valueIJ != null) {
                    sum += valueIJ * x[j];
                }
            }

            Double value1 = matrix.get(createPair(i, n + 1));
            Double value2 = matrix.get(createPair(i, i));

            if (value2 != null && value1 != null && value2 != 0.0) {
                x[i] = (value1 - sum) / value2;
            }
        }

        return x;
    }

    public void printGaussWithChoice() {
        double[] wmatrix = solveGausWithChoice(createProbabilityMatrix());
        int n = wmatrix.length - 1;
        for (int i = 1; i <= n; i++) {
            System.out.println("x" + (i) + " : " + Math.abs(wmatrix[i]));
        }
        List<Double> wm = new ArrayList<Double>();
        for (double value : wmatrix) {
            wm.add(value);
        }
        writeTimesToCSVResullts(wm, "gausChoice.csv");
    }


    //--------------------------------------------------------------------------------------------
    //------------------------------------Gauss Seidel--------------------------------------------

    private double[] solveGaussSeidel(Map<Map<Integer, Integer>, Double> matrix) {
        double[] x = new double[n + 2];
        double[] xNew = new double[n + 2];
        double epsilon = 1e-10;
        int maxIterations = 1000;

        for (int iter = 0; iter < maxIterations; iter++) {
            double error = 0.0;

            for (int i = 1; i <= n; i++) {
                double sum = 0.0;
                double diagonalValue = matrix.getOrDefault(createPair(i, i), 0.0);

                for (int j = 1; j <= n; j++) {
                    if (j != i) {
                        sum += matrix.getOrDefault(createPair(i, j), 0.0) * ((iter == 0) ? 0.0 : xNew[j]);
                    }
                }

                if (diagonalValue != 0.0) {
                    sum += matrix.getOrDefault(createPair(i, n + 1), 0.0) * ((iter == 0) ? 0.0 : xNew[n+1]);
                    xNew[i] = (matrix.getOrDefault(createPair(i, n + 1), 0.0) - sum) / diagonalValue;
                } else {
                    xNew[i] = 0.0;
                }

                error += Math.abs(xNew[i] - x[i]);
            }

            if (error < epsilon) {
                break;
            }

            System.arraycopy(xNew, 1, x, 1, n); // Update x for the next iteration
        }

        return x;
    }

    public void printGaussSiedel() {
        double[] wmatrix = solveGaussSeidel(createProbabilityMatrix());
        int n = wmatrix.length - 2;
        for (int i = 1; i <= n; i++) {
            System.out.println("x" + (i) + " : " + Math.abs(wmatrix[i]));
        }
        List<Double> wm = new ArrayList<Double>();
        for (double value : wmatrix) {
            wm.add(value);
        }
        writeTimesToCSVResullts(wm, "gausSiedel.csv");
    }

    //--------------------------------------------------------------------------------------------
    //---------------------------------GAUSS NORMAL-----------------------------------------------

    public double[] solveGaus(Map<Map<Integer, Integer>, Double> matrix) {
        if (matrix == null) {
            throw new IllegalArgumentException("Matrix cannot be null.");
        }

        for (int i = 1; i <= n; i++) {
            Map<Integer, Integer> key = createPair(i, i);
            if (!matrix.containsKey(key) || matrix.get(key) <= 1e-10) {
                matrix.put(key, 1e-10);
                System.out.println("Blad");
            }
            for (int j = i + 1; j <= n; j++) {
                double divisor = matrix.get(key);
                if (divisor == 0.0) {
                    throw new IllegalArgumentException("Divide by zero error");
                }
                Map<Integer, Integer> keyJ = createPair(j, i);
                if (!matrix.containsKey(keyJ)) {
                    continue;
                }
                double factor = matrix.get(keyJ) / divisor;
                for (int k = i; k <= n + 1; k++) {
                    Map<Integer, Integer> keyJK = createPair(j, k);
                    Map<Integer, Integer> keyIK = createPair(i, k);
                    double val = matrix.getOrDefault(keyJK, 0.0) - factor * matrix.getOrDefault(keyIK, 0.0);
                    matrix.put(keyJK, val);
                }
            }
        }
        double[] x = new double[n + 1];
        for (int i = n; i >= 1; i--) {
            double sum = 0.0;
            for (int j = i + 1; j <= n; j++) {
                Map<Integer, Integer> keyIJ = createPair(i, j);
                if (!matrix.containsKey(keyIJ)) {
                    continue;
                }
                sum += matrix.get(keyIJ) * x[j];
            }
            Map<Integer, Integer> key2 = createPair(i, i);
            if (!matrix.containsKey(key2)) {
                continue;
            }
            double diagonalValue = matrix.get(key2);
            if (diagonalValue == 0.0) {
                throw new IllegalArgumentException("Divide by zero error");
            }
            Map<Integer, Integer> keyIN = createPair(i, n + 1);
            x[i] = (matrix.getOrDefault(keyIN, 0.0) - sum) / diagonalValue;
        }
        return x;
    }

    private static Map<Integer, Integer> createPair(int i, int j) {
        Map<Integer, Integer> pair = new HashMap<>();
        pair.put(i, j);
        return pair;
    }

    public void printGauss() {
        double[] solution = solveGaus(createProbabilityMatrix());
        int n = solution.length - 1;
        for (int i = 1; i <= n; i++) {
            System.out.println("x" + i + " : " + Math.abs(solution[i]));
        }
        List<Double> wm = new ArrayList<Double>();
        for (double value : solution) {
            wm.add(value);
        }
        writeTimesToCSVResullts(wm, "gaus.csv");
    }

    //--------------------------------------------------------------------------------------------

    //MONTE
    public double monte(int N, int wanderer){
        double prob=0.0;
        for(int i = 0; i < N; i++) {
            if (singleTest(wanderer, exits, osk)) {
                prob++;
            }
        }
        return prob/N;
    }


    public Boolean singleTest(int start, ArrayList<Integer> exits, ArrayList<Integer> osk) {
        Random rand = new Random();
        Position wanderer = new Position(start, 0);
        while (!osk.contains(wanderer.getIntersection()) && !exits.contains(wanderer.getIntersection())) {
            if (wanderer.alley == null) {
                List<Alley> possibleAlleys = new ArrayList<>();
                for (Alley alley : alleys) {
                    if (alley.getA().getId() == wanderer.getIntersection() || alley.getB().getId() == wanderer.getIntersection()) {
                        possibleAlleys.add(alley);
                    }
                }
                wanderer.alley = possibleAlleys.get(rand.nextInt(possibleAlleys.size()));
                wanderer.distance++;
            } else {
                int step = rand.nextInt(2);
                if (step == 0) {
                    wanderer.distance--;
                } else {
                    wanderer.distance++;
                }
                if (wanderer.distance == 0) {
                    wanderer.alley = null;
                } else if ( wanderer.distance == wanderer.alley.getLength()) {
                    if (wanderer.alley.getA().getId() != wanderer.intersection) {
                        wanderer.intersection = wanderer.alley.getA().getId();
                    } else {
                        wanderer.intersection = wanderer.alley.getB().getId();
                    }
                }
            }
        }
        if (osk.contains(wanderer.getIntersection())){
            return false;
        } else {
            return true;
        }
    }

    //CSV

    public void saveToTxt() {
        try (FileWriter writer = new FileWriter("park.txt")) {
            for (Alley alley : alleys) {
                writer.write(alley.getA().getId() + " " + alley.getB().getId() + " " + alley.getLength() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getExecTime(){
        Map<Map<Integer, Integer>, Double> matrix = createProbabilityMatrix();
        countExecTime(matrix);
    }

    private void countExecTime(Map<Map<Integer, Integer>, Double> matrix){
        
        double executionTime;

        List<Double> list = new ArrayList<>();
        Function<Void,Void> solveGausFunction = (Void) ->{
            solveGaus(matrix);
            return null;
        };
        executionTime = countTime(solveGausFunction);
        System.out.println("gauss without choice exec time: "+ executionTime);
        list.add(executionTime);
        Function<Void,Void> solveGaussWithChoiceFunction = (Void) ->{
            solveGausWithChoice(matrix);
            return null;
        };
        executionTime = countTime(solveGaussWithChoiceFunction);
        System.out.println("gauss with choice exec time: "+ executionTime);
        list.add(executionTime);

        Function<Void,Void> solveGausSiedelFunction = (Void) ->{
            solveGaussSeidel(matrix);
            return null;
        };

        executionTime = countTime(solveGausSiedelFunction);
        list.add(executionTime);
        System.out.println("gauss seidel exec time: "+ executionTime);
        Times.writeTimesToCSV(list);
    }
}

