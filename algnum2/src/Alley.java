public class Alley {
    private Intersection a;
    private Intersection b;
    private int length;

    public Alley(Intersection a, Intersection b, int length) {
        this.a = a;
        this.b = b;
        this.length = length;
    }
    public Intersection getA() {
        return a;
    }
    public Intersection getB() {
        return b;
    }
    public int getLength() {
        return length;
    }
}
