package by.siarhei.baranau.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: siarhei
 * Date: 7/28/13
 * Time: 11:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class PriceNotEvalExp extends Exception implements IsSerializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4116018838398344806L;
	private String symbol;
    public PriceNotEvalExp() {
    }
    public PriceNotEvalExp(String symbol) {
        this.symbol = symbol;
    }
    public String getSymbol() {
        return this.symbol;
    }
}
