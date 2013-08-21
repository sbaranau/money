package by.siarhei.baranau.client;

import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface ITest extends RemoteService {
    ArrayList<MoneyPrice> getPrices(String[] symbols) throws PriceNotEvalExp;
}
