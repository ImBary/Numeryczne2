import java.util.*;
public class Park {
    private Map<Integer, Intersection> intersections;
    private List<Alley> alleys;

    public Park() {
        this.intersections = intersections;
        this.alleys = alleys;
    }
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
    public void createMatrix(){
        double [][] matrix = new double[intersections.size()][alleys.size()];
        for (Alley alley : alleys) {
        }
    }


}
