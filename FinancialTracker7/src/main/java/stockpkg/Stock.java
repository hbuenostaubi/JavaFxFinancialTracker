package stockpkg;


public enum Stock {
    LOWEST(0, 75),
    LOW(76,125),
    MID(126, 175),
    HIGH(176,999);

    private final double min;
    private final double max;

    Stock(double min, double max){
        this.min=min;
        this.max=max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}
