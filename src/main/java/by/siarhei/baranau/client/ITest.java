package by.siarhei.baranau.client;

import java.util.ArrayList;
import java.util.Hashtable;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface ITest extends RemoteService {
    ArrayList<MoneyPrice> getPrices(String[] symbols) throws PriceNotEvalExp;
    Hashtable<String, String> getBanks();
}
