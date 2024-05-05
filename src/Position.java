public class Position {
    public int intersection;
    public int distance;
    public Alley alley;

    public Position(int intersection, int distance) {
        this.intersection = intersection;
        this.distance = distance;
        this.alley = null;
    }
    public int getIntersection() {
        return intersection;
    }
}
