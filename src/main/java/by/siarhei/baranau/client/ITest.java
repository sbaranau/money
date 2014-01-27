package by.siarhei.baranau.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface ITest extends RemoteService {
    ArrayList<MoneyPrice> getPrices(String[] symbols);
    HashMap<String, String> getBanks();
    HashMap<String, String> getDateForMoney();
    LinkedHashMap<String, String> getDateForMoney(String money);
    LinkedHashMap<String, String> getDateForMoney(String money,String startDate, String finishDate);
}
