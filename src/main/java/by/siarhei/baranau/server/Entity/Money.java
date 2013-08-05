package by.siarhei.baranau.server.Entity;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: siarhei
 * Date: 8/1/13
 * Time: 10:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class Money {

    private int bankId;
    private int date;
    private int time;
    private String name;
    private BigDecimal buyPrice;
    private BigDecimal sellPrice;

    public Money() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
