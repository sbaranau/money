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

	private double bankName;
	private double priceUsdBuy;
    private double priceUsdSell;
    private double changeUsd;
    private double priceEurBuy;
    private double priceEurSell;
    private double changeEur;
    private double priceRurBuy;
    private double priceRurSell;
    private double changeRur;


    public MoneyPrice(double priceUsdBuy, double priceUsdSell,
			double changeUsd, double priceEurBuy, double priceEurSell,
			double changeEur, double priceRurBuy, double priceRurSell,
			double changeRur) {
		super();
		this.priceUsdBuy = priceUsdBuy;
		this.priceUsdSell = priceUsdSell;
		this.changeUsd = changeUsd;
		this.priceEurBuy = priceEurBuy;
		this.priceEurSell = priceEurSell;
		this.changeEur = changeEur;
		this.priceRurBuy = priceRurBuy;
		this.priceRurSell = priceRurSell;
		this.changeRur = changeRur;
	}
    

	public MoneyPrice(double bankName, double priceUsdBuy, double priceUsdSell,
			double changeUsd, double priceEurBuy, double priceEurSell,
			double changeEur, double priceRurBuy, double priceRurSell,
			double changeRur) {
		super();
		this.bankName = bankName;
		this.priceUsdBuy = priceUsdBuy;
		this.priceUsdSell = priceUsdSell;
		this.changeUsd = changeUsd;
		this.priceEurBuy = priceEurBuy;
		this.priceEurSell = priceEurSell;
		this.changeEur = changeEur;
		this.priceRurBuy = priceRurBuy;
		this.priceRurSell = priceRurSell;
		this.changeRur = changeRur;
	}


	public double getPriceUsdBuy() {
		return priceUsdBuy;
	}

	public void setPriceUsdBuy(double priceUsdBuy) {
		this.priceUsdBuy = priceUsdBuy;
	}

	public double getPriceUsdSell() {
		return priceUsdSell;
	}

	public void setPriceUsdSell(double priceUsdSell) {
		this.priceUsdSell = priceUsdSell;
	}

	public double getChangeUsd() {
		return changeUsd;
	}

	public void setChangeUsd(double changeUsd) {
		this.changeUsd = changeUsd;
	}

	public double getPriceEurBuy() {
		return priceEurBuy;
	}

	public void setPriceEurBuy(double priceEurBuy) {
		this.priceEurBuy = priceEurBuy;
	}

	public double getPriceEurSell() {
		return priceEurSell;
	}

	public void setPriceEurSell(double priceEurSell) {
		this.priceEurSell = priceEurSell;
	}

	public double getChangeEur() {
		return changeEur;
	}

	public void setChangeEur(double changeEur) {
		this.changeEur = changeEur;
	}

	public double getPriceRurBuy() {
		return priceRurBuy;
	}

	public void setPriceRurBuy(double priceRurBuy) {
		this.priceRurBuy = priceRurBuy;
	}

	public double getPriceRurSell() {
		return priceRurSell;
	}

	public void setPriceRurSell(double priceRurSell) {
		this.priceRurSell = priceRurSell;
	}

	public double getChangeRur() {
		return changeRur;
	}

	public void setChangeRur(double changeRur) {
		this.changeRur = changeRur;
	}

	public MoneyPrice() {
    }

    public double getChangePercentUsd() {
        return 10.0 * this.changeUsd / this.priceUsdBuy;
    }
    public double getChangePercentEur() {
        return 10.0 * this.changeEur / this.priceEurBuy;
    }
    public double getChangePercentRur() {
        return 10.0 * this.changeRur / this.priceRurBuy;
    }
	public double getBankName() {
		return bankName;
	}

	public void setBankName(double bankName) {
		this.bankName = bankName;
	}

}
