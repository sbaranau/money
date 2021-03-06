package by.siarhei.baranau.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface IEntry extends RemoteService {

	MoneyPrice[] getPrices(String[] symbols) throws PriceNotEvalExp;
}
