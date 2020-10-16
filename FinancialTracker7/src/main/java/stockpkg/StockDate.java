package stockpkg;

import java.util.Objects;

public class StockDate {
    private String date;
    private double open;
    private double high;
    private double low;
    private double close;
    private int volume;

    public StockDate(String date, double open, double high, double low, double close, int volume) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public double getOpen() {
        return open;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public int getVolume() {
        return volume;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "{" +
                "date='" + date + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockDate dateClass = (StockDate) o;
        return Double.compare(dateClass.open, open) == 0 &&
                Double.compare(dateClass.high, high) == 0 &&
                Double.compare(dateClass.low, low) == 0 &&
                Double.compare(dateClass.close, close) == 0 &&
                volume == dateClass.volume &&
                Objects.equals(date, dateClass.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, open, high, low, close, volume);
    }
}
