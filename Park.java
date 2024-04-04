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
        int [][] lengths = createLengthsMatrix(n, osk, exits);
        double [][] probabilities = new double[n + 1][n + 1];
        for (int i = 1; i <= 4; i++) {
            int totalDistance = 0;
            double totalProbability = 0.0;
            for (int j = 1; j <= 4; j++) {
                totalDistance += lengths[i][j];
            }
            for (int j = 1; j <= 4; j++) {
                if (lengths[i][j] == 0) {
                    if (i == j) {
                        probabilities[i][j] = 1.0;
                    }
                    continue;
                }
                probabilities[i][j] = (double) (totalDistance - lengths[i][j]) / (double) totalDistance;
                totalProbability += probabilities[i][j];
            }
            if (totalProbability > 1.0) {
                double scale = 1 / totalProbability;
                System.out.println(scale);
                for (int j = 1; j <= n; j++) {
                    if (i == j) {
                        continue;
                    }
                    probabilities[i][j] *= scale;
                }
            }
        }
        double[] rightSide = new double[n + 1];
        for (Integer exit : exits) {
            rightSide[exit] = 1.0;
        }
        System.out.println("Macierz prawdodopodibeństwa");
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                System.out.print("[" + probabilities[i][j] + "]");
            }
            System.out.println(" = [" + rightSide[i] + "]");
        }
    }

}
