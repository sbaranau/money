package by.baranau.searhei;

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
    private BigDecimal buyUSDPrice;
    private BigDecimal sellUSDPrice;
    private BigDecimal buyEURPrice;
    private BigDecimal sellEURPrice;
    private BigDecimal buyRURPrice;
    private BigDecimal sellRURPrice;

    public Money() {
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

	public BigDecimal getBuyUSDPrice() {
		return buyUSDPrice;
	}

	public void setBuyUSDPrice(BigDecimal buyUSDPrice) {
		this.buyUSDPrice = buyUSDPrice;
	}

	public BigDecimal getSellUSDPrice() {
		return sellUSDPrice;
	}

	public void setSellUSDPrice(BigDecimal sellUSDPrice) {
		this.sellUSDPrice = sellUSDPrice;
	}

	public BigDecimal getBuyEURPrice() {
		return buyEURPrice;
	}

	public void setBuyEURPrice(BigDecimal buyEURPrice) {
		this.buyEURPrice = buyEURPrice;
	}

	public BigDecimal getSellEURPrice() {
		return sellEURPrice;
	}

	public void setSellEURPrice(BigDecimal sellEURPrice) {
		this.sellEURPrice = sellEURPrice;
	}

	public BigDecimal getBuyRURPrice() {
		return buyRURPrice;
	}

	public void setBuyRURPrice(BigDecimal buyRURPrice) {
		this.buyRURPrice = buyRURPrice;
	}

	public BigDecimal getSellRURPrice() {
		return sellRURPrice;
	}

	public void setSellRURPrice(BigDecimal sellRURPrice) {
		this.sellRURPrice = sellRURPrice;
	}

}
