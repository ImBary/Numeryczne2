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

    public void createMatrix(int n){
        int[][] lengths = new int[n][n ];
        for (int i = 0; i < n; i++) {
            lengths[i][i] = 1;
        }
        /*for (Alley alley : alleys) {
            lengths[alley.getA().getId()][alley.getB().getId()] = alley.getLength();
            lengths[alley.getB().getId()][alley.getB().getId()] = alley.getLength();
        }*/
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(lengths[i][j]);
            }
            System.out.println();
        }
    }
/*
 4 5
 1 2 4
 2 3 4
 3 4 4
 1 3 6
 1 4 4
 1 1
 1 4
 1 3
 0
* */


}
