package by.siarhei.baranau.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ITestAsync {
    void getPrices(String[] symbols, AsyncCallback<ArrayList> async);
}
