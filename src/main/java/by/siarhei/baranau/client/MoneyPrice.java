package by.siarhei.baranau.client;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: siarhei
 * Date: 7/27/13
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class MoneyPrice implements IsSerializable {

	private int bankName;
	private int date;
	private int time;
	private BigDecimal priceUsdBuy;
    private BigDecimal priceUsdSell;
    private BigDecimal changeUsd;
    private BigDecimal priceEurBuy;
    private BigDecimal priceEurSell;
    private BigDecimal changeEur;
    private BigDecimal priceRurBuy;
    private BigDecimal priceRurSell;
    private BigDecimal changeRur;


    
    public BigDecimal getPriceUsdBuy() {
		return priceUsdBuy;
	}
	public void setPriceUsdBuy(BigDecimal priceUsdBuy) {
		this.priceUsdBuy = priceUsdBuy;
	}
	public BigDecimal getPriceUsdSell() {
		return priceUsdSell;
	}
	public void setPriceUsdSell(BigDecimal priceUsdSell) {
		this.priceUsdSell = priceUsdSell;
	}
	public BigDecimal getChangeUsd() {
		return changeUsd;
	}
	public void setChangeUsd(BigDecimal changeUsd) {
		this.changeUsd = changeUsd;
	}
	public BigDecimal getPriceEurBuy() {
		return priceEurBuy;
	}
	public void setPriceEurBuy(BigDecimal priceEurBuy) {
		this.priceEurBuy = priceEurBuy;
	}
	public BigDecimal getPriceEurSell() {
		return priceEurSell;
	}
	public void setPriceEurSell(BigDecimal priceEurSell) {
		this.priceEurSell = priceEurSell;
	}
	public BigDecimal getChangeEur() {
		return changeEur;
	}
	public void setChangeEur(BigDecimal changeEur) {
		this.changeEur = changeEur;
	}
	public BigDecimal getPriceRurBuy() {
		return priceRurBuy;
	}
	public void setPriceRurBuy(BigDecimal priceRurBuy) {
		this.priceRurBuy = priceRurBuy;
	}
	public BigDecimal getPriceRurSell() {
		return priceRurSell;
	}
	public void setPriceRurSell(BigDecimal priceRurSell) {
		this.priceRurSell = priceRurSell;
	}
	public BigDecimal getChangeRur() {
		return changeRur;
	}
	public void setChangeRur(BigDecimal changeRur) {
		this.changeRur = changeRur;
	}
	public void setBankName(int bankName) {
		this.bankName = bankName;
	}
	public BigDecimal getChangePercentUsd() {
        return this.changeUsd.divide(this.priceUsdBuy, 3, RoundingMode.HALF_DOWN);
    }
    public BigDecimal getChangePercentEur() {
        return this.changeEur.divide(this.priceEurBuy, 3, RoundingMode.HALF_DOWN);
    }
    public BigDecimal getChangePercentRur() {
        return this.changeRur.divide(this.priceRurBuy, 3, RoundingMode.HALF_DOWN);
    }
	public double getBankName() {
		return bankName;
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
