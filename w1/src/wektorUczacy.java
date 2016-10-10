
public class wektorUczacy {
    private double a;
    private double b;
    private double d;

    public wektorUczacy(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public wektorUczacy(double b, double a, double d) {
        this.b = b;
        this.a = a;
        this.d = d;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getD() {
        return d;
    }
}
