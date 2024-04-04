import java.util.*;
public class Park {
    private Map<Integer, Intersection> intersections = new HashMap<>();
    private List<Alley> alleys = new ArrayList<>();


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

    private int[][] createLengthsMatrix(int n, ArrayList<Integer> osk, ArrayList<Integer> exits){
        int[][] lengths = new int[n + 1][n + 1];
        for (Alley alley : alleys) {
            if (!osk.contains(alley.getA().getId()) && !exits.contains(alley.getA().getId())) {
                lengths[alley.getA().getId()][alley.getB().getId()] = alley.getLength();
            }
            if (!osk.contains(alley.getB().getId()) && !exits.contains(alley.getB().getId())) {
                lengths[alley.getB().getId()][alley.getA().getId()] = alley.getLength();
            }
        }
        return lengths;
    }

    public void createProbabilityMatrix(int n, ArrayList<Integer> osk, ArrayList<Integer> exits) {
        int[][] lengths = createLengthsMatrix(n, osk, exits);
        double[][] probabilities = new double[n + 1][n + 1];

        for (int i = 1; i <= n; i++) {
            int totalDistance = 0;
            for (int j = 1; j <= n; j++) {
                totalDistance += lengths[i][j];
            }

            for (int j = 1; j <= n; j++) {
                if (lengths[i][j] == 0) {
                    if (i == j) {
                        probabilities[i][j] = 1.0;
                    }
                } else {
                    probabilities[i][j] = (double) lengths[i][j] / (double) totalDistance;
                }
            }
        }

        double[] rightSide = new double[n + 1];
        for (Integer exit : exits) {
            rightSide[exit] = 1.0;
        }

        System.out.println("Gaus:");

        double[][] matrixForGaus = new double[n + 1][n + 2];      
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                matrixForGaus[i][j] = probabilities[i][j];
            }
        }        
        for (int i = 1; i <= n; i++) {
            matrixForGaus[i][n + 1] = rightSide[i];
        }
        
        System.out.println("Macierz prawdopodobieństwa:");
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n + 1; j++) {
                System.out.print("[" + matrixForGaus[i][j] + "]");
            }
            System.out.println();
        }
        solveGaus(matrixForGaus);
    }

    private void solveGaus(double[][] matrix) {
        int n = matrix.length;
    
        for (int i = 0; i < n; i++) {
            // maax w kolumnie
            double maxElement = Math.abs(matrix[i][i]);
            int maxRow = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(matrix[j][i]) > maxElement) {
                    maxElement = Math.abs(matrix[j][i]);
                    maxRow = j;
                }
            }
            //zamieniamy wiersze
            double[] tmpRow = matrix[maxRow];
            matrix[maxRow] = matrix[i];
            matrix[i] = tmpRow;
    
            // sprawdzamy zeby nie dzielic przez zero 
            if (Math.abs(matrix[i][i]) <= 1e-10) {
                matrix[i][i] = 1e-10;
            }
    
            
            for (int j = i + 1; j < n; j++) {
                double factor = matrix[j][i] / matrix[i][i];
                for (int k = i; k <= n; k++) {
                    matrix[j][k] -= factor * matrix[i][k];
                }
            }
        }
    
        
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            x[i] = matrix[i][n];
            for (int j = i + 1; j < n; j++) {
                x[i] -= matrix[i][j] * x[j];
            }
            x[i] /= matrix[i][i];
        }
    
        
        System.out.println("Wyniki:");
        for (int i = 1; i < n; i++) {
            System.out.println("x" + (i ) + " : " + Math.abs(x[i]));
        }
    }



    //MONTE
    public double monte(int N, int wanderer, ArrayList<Integer> exits, ArrayList<Integer> osk){
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





}

