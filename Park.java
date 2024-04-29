import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Function;

public class Park extends Times {
    private Map<Integer, Intersection> intersections = new HashMap<>();
    private List<Alley> alleys = new ArrayList<>();

    private int n;
    private int lenOfAllAlleys = 0;
    private ArrayList<Integer> osk;
    private ArrayList<Integer> exits;

    private ArrayList<Integer> numberOfPaths = new ArrayList<>();

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
        lenOfAllAlleys += length;
        addIntersection(a);
        addIntersection(b);
        addAlley(alley);
    }


    private void fillNumberOfPaths() {
        for (int i = 1; i <= n; i++) {
            for (Alley alley : alleys) {
                if (alley.getA().getId() == i || alley.getB().getId() == i) {
                    numberOfPaths.add(i);
                }
            }
        }
    }

    private int countNumberOfPaths(int number) {
        return Collections.frequency(numberOfPaths, number);
    }

    /* TODO
         Mamy policzone ile jest wystapien kazdej sciezki z skrzyzowania 1/countNumberOfPaths(int i)
         ZOSTAŁO:
            Storzyc macierz
     */
    public Map<Map<Integer, Integer>, Double> matrixForGauss(ArrayList<Integer> osk, ArrayList<Integer> exits) {
        Map<Map<Integer, Integer>, Double> matrix = new HashMap<>();
        fillNumberOfPaths();
        int counter = n + 1;
        for (Alley alley : alleys) {
            int aID = alley.getA().getId();
            int bID = alley.getB().getId();

            //Wartosci pierwszego skrzyzowania
            int length = alley.getLength();
            if (!osk.contains(aID) && !exits.contains(aID)) {
                matrix.put(createPair(aID, counter), (double) 1 / countNumberOfPaths(aID));
            }

            for (int i = counter; i < counter + length; i++) {
                //Srodek
                matrix.put(createPair(i, i), (double) 1);

                //Lewy przypadek
                if (i == counter) {
                    matrix.put(createPair(i, aID), (double) 1 / 2);
                } else if (i == counter + length - 1) {
                    matrix.put(createPair(i, bID), (double) 1 / 2);
                } else {
                    matrix.put(createPair(i, i - 1), (double) 1 / 2);
                }

                //Prawy przypadek
                if (i == counter + length - 1) {
                    matrix.put(createPair(i, i - 1), (double) 1 / 2);
                } else {
                    matrix.put(createPair(i, i + 1), (double) 1 / 2);
                }

            }

            counter += length;
            if (!osk.contains(bID) && !exits.contains(bID)) {
                matrix.put(createPair(bID, counter - 1), (double) 1 / countNumberOfPaths(bID));
            }
        }

        for (int i = 1; i <= n; i++) {
            matrix.put(createPair(i, i), (double) 1);
            if (exits.contains(i)) {
                matrix.put(createPair(i, n + lenOfAllAlleys + 1), (double) 1);
            }
        }

        System.out.println(matrix);
        return  matrix;
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

            Double value1 = matrix.getOrDefault(createPair(i, n + 1), 0.0);
            Double value2 = matrix.get(createPair(i, i));

            if (0.0 != value2) {
                x[i] = (value1 - sum) / value2;
            }
        }

        return x;
    }

    public void printGaussWithChoice(Map<Map<Integer, Integer>, Double> matrix) {
        double[] wmatrix = solveGausWithChoice(matrix);
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

    public void printGaussSiedel(Map<Map<Integer, Integer>, Double> matrix) {
        double[] wmatrix = solveGaussSeidel(matrix);
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
        for (int i = 1; i <= n + lenOfAllAlleys; i++) {
            Map<Integer, Integer> key = createPair(i, i);
            if (!matrix.containsKey(key) || Math.abs(matrix.get(key)) <= 1e-10) {
                matrix.put(key, 1e-10);
                System.out.println("Blad" + key);
            }
            for (int j = i + 1; j <= n + lenOfAllAlleys; j++) {
                double divisor = matrix.get(key);
                if (divisor == 0.0) {
                    throw new IllegalArgumentException("Divide by zero error");
                }
                Map<Integer, Integer> keyJ = createPair(j, i);
                if (!matrix.containsKey(keyJ)) {
                    continue;
                }
                double factor = matrix.get(keyJ) / divisor;
                for (int k = i; k <= n + lenOfAllAlleys + 1; k++) {
                    Map<Integer, Integer> keyJK = createPair(j, k);
                    Map<Integer, Integer> keyIK = createPair(i, k);
                    double val = matrix.getOrDefault(keyJK, 0.0) - factor * matrix.getOrDefault(keyIK, 0.0);
                    matrix.put(keyJK, val);
                }
            }
        }
        double[] x = new double[n + lenOfAllAlleys + 1];
        for (int i = n + lenOfAllAlleys; i >= 1; i--) {
            double sum = 0.0;
            for (int j = i + 1; j <= n + lenOfAllAlleys; j++) {
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
            Map<Integer, Integer> keyIN = createPair(i, n + lenOfAllAlleys + 1);
            x[i] = (matrix.getOrDefault(keyIN, 0.0) - sum) / diagonalValue;
        }
        return x;
    }

    private static Map<Integer, Integer> createPair(int i, int j) {
        Map<Integer, Integer> pair = new HashMap<>();
        pair.put(i, j);
        return pair;
    }

    public void printGauss(Map<Map<Integer, Integer>, Double> matrix) {
        double[] solution = solveGaus(matrix);
        for (int i = 1; i <= n; i++) {
            System.out.println("x" + i + " : " + (Math.abs(solution[i])));
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
                wanderer.distance = 1;
            } else {
                int step = rand.nextInt(2);
                if (step == 0) {
                    wanderer.distance--;
                } else {
                    wanderer.distance++;
                }
                if (wanderer.distance == 0) {
                    if (wanderer.alley.getA().getId() == wanderer.intersection) {
                        wanderer.intersection = wanderer.alley.getA().getId();
                        wanderer.distance = 0;
                    } else {
                        wanderer.intersection = wanderer.alley.getB().getId();
                        wanderer.distance = 0;
                    }
                    wanderer.alley = null;
                } else if (wanderer.alley != null) {
                    if (wanderer.distance >= wanderer.alley.getLength()) {
                        if (wanderer.alley.getA().getId() != wanderer.intersection) {
                            wanderer.intersection = wanderer.alley.getA().getId();
                        } else {
                            wanderer.intersection = wanderer.alley.getB().getId();
                        }
                        wanderer.distance = 0;
                        wanderer.alley = null;
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

    public void getExecTime(Map<Map<Integer, Integer>, Double> matrix){
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














    private Map<Map<Integer, Integer>, Double> createLengthsMatrixForMarkov( ArrayList<Integer> osk, ArrayList<Integer> exits){
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
        System.out.println(lengthsMap);
        return lengthsMap;
    }

    public Map<Map<Integer, Integer>, Double> createProbabilityMatrixForMarkov() {

        Map<Map<Integer, Integer>, Double> lengths = createLengthsMatrixForMarkov(osk, exits);
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
            if (length != 0) {
                probability = (1.0 / length) / inverseSums[fromVertex];
            } else {
                probability = 0.0;
            }

            probabilities.put(idMap, probability);
        }

        for (int i = 1; i <= n; i++) {
            if (osk.contains(i) || exits.contains(i)) {
                Map<Integer, Integer> id = new HashMap<>();
                id.put(i, i);
                probabilities.put(id, 1.0);
            }
        }

        System.out.println(probabilities);
        return probabilities;
    }

    //Markov chain solver
    public void markovChainSolver(int start) {
        Map<Map<Integer, Integer>, Double> matrix = createProbabilityMatrixForMarkov();

        double[] steadyStateVector = calculateSteadyState(matrix, start);

        // Print the steady-state vector
        System.out.println("Steady-State Vector:");
        for (int i = 1; i <= n ; i++) {
            System.out.println("State " + i + ": " + steadyStateVector[i]);
        }
    }

    private double[] calculateSteadyState(Map<Map<Integer, Integer>, Double> matrix, int start) {
        double[] pi = new double[n  + 1]; // Initial guess for the steady-state vector
        pi[start] = 1.0; // Start with all probability in the specified start state

        // Power iteration method
        double epsilon = 1e-14; // Tolerance for convergence
        double error = Double.MAX_VALUE;
        while (error > epsilon) {
            double[] newPi = new double[n+1];
            for (int i = 1; i <= n ; i++) {
                double sum = 0.0;
                for (int j = 1; j <= n ; j++) {
                    sum += pi[j] * matrix.getOrDefault(createPair(j, i), 0.0);
                }
                newPi[i] = sum;
            }
            error = calculateError(pi, newPi);
            pi = newPi;
        }

        return pi;
    }

    // Helper method to calculate the error between two vectors
    private double calculateError(double[] v1, double[] v2) {
        double maxDiff = 0.0;
        for (int i = 0; i < v1.length; i++) {
            double diff = Math.abs(v1[i] - v2[i]);
            if (diff > maxDiff) {
                maxDiff = diff;
            }
        }
        return maxDiff;
    }
}



