package by.siarhei.baranau.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ITestAsync {
    void getPrices(String[] symbols, AsyncCallback<ArrayList<MoneyPrice>> async);

	void getBanks(AsyncCallback<HashMap<String, String>> callback);

	void getDateForMoney(String money, String startDate, String finishDate,
			AsyncCallback<LinkedHashMap<String, String>> callback);
}
