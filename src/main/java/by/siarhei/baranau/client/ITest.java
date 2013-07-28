package by.siarhei.baranau.client;

import by.siarhei.baranau.server.PriceNotEvalExp;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface ITest extends RemoteService {
    MoneyPrice[] getPrices(String[] symbols);
}
