package by.siarhei.baranau.server;

import by.siarhei.baranau.client.IEntry;
import by.siarhei.baranau.client.MoneyPrice;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import by.siarhei.baranau.client.PriceNotEvalExp;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class EntryImpl extends RemoteServiceServlet implements IEntry {

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
            InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
            Parser.parseXml(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
