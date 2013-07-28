package by.siarhei.baranau.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ITestAsync {
    void getPrices(String[] symbols, AsyncCallback<MoneyPrice[]> async);
}
