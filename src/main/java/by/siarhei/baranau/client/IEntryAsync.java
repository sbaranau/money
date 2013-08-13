package by.siarhei.baranau.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IEntryAsync {
    void getPrices(String[] symbols, AsyncCallback<MoneyPrice[]> async);
}
