package by.siarhei.baranau.server;

import by.siarhei.baranau.client.ITest;
import by.siarhei.baranau.client.MoneyPrice;
import java.util.Random;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TestImpl extends RemoteServiceServlet implements ITest {

    private static final double MAX_PRICE = 100.0; // $100.00
    private static final double MAX_PRICE_CHANGE = 0.02; // +/- 2%

    public MoneyPrice[] getPrices(String[] symbols) {
        Random rnd = new Random();
        MoneyPrice[] prices = new MoneyPrice[symbols.length];
        for (int i = 0; i < symbols.length; i++) {

            double price = rnd.nextDouble() * MAX_PRICE;
            double change = price * MAX_PRICE_CHANGE * (rnd.nextDouble() * 2f - 1f);
            prices [i] = new MoneyPrice(symbols[i], price, change);
        }
        return prices;
    }
}
