package by.siarhei.baranau.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import by.siarhei.baranau.client.ITest;
import by.siarhei.baranau.client.MoneyPrice;
import by.siarhei.baranau.client.PriceNotEvalExp;
import by.siarhei.baranau.server.DB.Dbmanager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TestImpl extends RemoteServiceServlet implements ITest {

    private static final String URL_OBMENNIK = "http://www.obmennik.by/xml/" ;

    public ArrayList<MoneyPrice> getPrices(String[] bank) throws PriceNotEvalExp {
        getXml();
        ArrayList<MoneyPrice> prices = new ArrayList<MoneyPrice>();
        for (int i = 0; i < bank.length; i++) {
        	Dbmanager dbmanager = new Dbmanager(2);
        	try {
				prices.add(dbmanager.getDate(bank[i]));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
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

	public Hashtable<String, String> getBanks() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
