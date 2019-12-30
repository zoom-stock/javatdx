package org.zoomdev.stock.tdx;

public class StockInfo {

    @Override
    public String toString() {
        return String.format("%s %s %.02f",code,name,price);
    }

    String code;
    double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;
    int decimalPoint;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDecimalPoint() {
        return decimalPoint;
    }

    public void setDecimalPoint(int decimalPoint) {
        this.decimalPoint = decimalPoint;
    }
}
