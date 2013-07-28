package by.siarhei.baranau.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: siarhei
 * Date: 7/27/13
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class MoneyPrice implements IsSerializable {

    private String symbol;
    private double price;
    private double change;

    public MoneyPrice(String symbol, double price, double change) {
        this.symbol = symbol;
        this.price = price;
        this.change = change;
    }

    public MoneyPrice() {
    }

    public double getChangePercent() {
        return 10.0 * this.change / this.price;
    }


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }
}
