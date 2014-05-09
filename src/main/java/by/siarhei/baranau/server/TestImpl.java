package by.siarhei.baranau.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import by.siarhei.baranau.client.ITest;
import by.siarhei.baranau.client.MoneyPrice;
import by.siarhei.baranau.server.DB.Dbmanager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TestImpl extends RemoteServiceServlet implements ITest {


    public ArrayList<MoneyPrice> getPrices(String[] bank) {
        ArrayList<MoneyPrice> prices = new ArrayList<MoneyPrice>();
        for (int i = 0; i < bank.length; i++) {
        	Dbmanager dbmanager = new Dbmanager(2);
        	try {
				prices.add(dbmanager.getDate(bank[i]));
			} catch (SQLException e) {
				// TODO Auto-generated catch block USD
				e.printStackTrace();
			}
        }
        return prices;
    }

    public HashMap<String, String> getBanks() {
		HashMap<String, String> map = new HashMap<String, String>();
		Dbmanager dbmanager = new Dbmanager(4);
		try {
			map = dbmanager.getBanks();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO Auto-generated method stub
		return map;
	}

	public LinkedHashMap<String, String> getDateForMoney(String money,
			String startDate, String finishDate) {
		LinkedHashMap<String, String> data= new LinkedHashMap<String, String>();
		try {
			Dbmanager dbmanager = new Dbmanager(3);
			data = dbmanager.getPriceList(money, startDate, finishDate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
}
