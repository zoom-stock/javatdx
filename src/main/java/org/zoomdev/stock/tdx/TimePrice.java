package org.zoomdev.stock.tdx;

public class TimePrice {

    int vol;
    double close;

    @Override
    public String toString() {
        return new StringBuilder()
                .append(close).append(" ").append(vol).toString();
    }

    public int getVol() {
        return vol;
    }

    public void setVol(int vol) {
        this.vol = vol;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }
}
