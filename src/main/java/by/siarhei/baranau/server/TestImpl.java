package by.siarhei.baranau.server;

import by.siarhei.baranau.client.ITest;
import by.siarhei.baranau.client.MoneyPrice;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import by.siarhei.baranau.client.PriceNotEvalExp;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TestImpl extends RemoteServiceServlet implements ITest {

    private static final double MAX_PRICE = 100.0; // $100.00
    private static final double MAX_PRICE_CHANGE = 0.02; // +/- 2%
    private static final String URL_OBMENNIK = "http://www.obmennik.by/xml/" ;

    public MoneyPrice[] getPrices(String[] symbols) throws PriceNotEvalExp {
        Random rnd = new Random();
        MoneyPrice[] prices = new MoneyPrice[symbols.length];
        for (int i = 0; i < symbols.length; i++) {
            if (symbols[i].equals("ERR")) throw new PriceNotEvalExp("Err");
            double price = rnd.nextDouble() * MAX_PRICE;
            double change = price * MAX_PRICE_CHANGE * (rnd.nextDouble() * 2f - 1f);
            prices [i] = new MoneyPrice(symbols[i], price, change);
        }
        getXml();
        return prices;
    }

    private void getXml() {

        try {
            URL url =  new URL(URL_OBMENNIK);
            URLConnection conn = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            File outDir = new File("resources/archiv/");
            File outFile = new File(outDir + File.separator + Calendar.DATE);

            if (!outDir.exists()) {
                outDir.mkdirs();
            }
            if (!outFile.exists()) {
                outFile.createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
