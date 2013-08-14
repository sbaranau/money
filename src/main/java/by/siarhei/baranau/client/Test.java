package by.siarhei.baranau.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.rpc.AsyncCallback;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

public class Test implements EntryPoint {

    Logger logger = Logger.getLogger(Test.class.getName());
    private VerticalPanel mainPanel = new VerticalPanel();
    private FlexTable stocksFlexTable = new FlexTable();
    private HorizontalPanel addPanel = new HorizontalPanel();
    private TextBox newSymbolTextBox = new TextBox();
    private Button addButton = new Button("Add");
    private Label lastUpdatedLabel = new Label();
    private ArrayList<String> moneyList = new ArrayList<String>();
    private static final int REFRESH_INTERVAL = 50000;
    private Label errorMsgLabel = new Label();

    public void onModuleLoad() {

        Timer refreshTimer = new Timer() {
            public void run() {
                refreshPrice();
            }
        };
        stocksFlexTable.setText(0, 0, "Bank");
        stocksFlexTable.setText(0, 1, "USD buy");
        stocksFlexTable.setText(0, 2, "USD sell");
        stocksFlexTable.setText(0, 3, "Change");
        stocksFlexTable.setText(0, 4, "EUR buy");
        stocksFlexTable.setText(0, 5, "EUR sell");
        stocksFlexTable.setText(0, 6, "Change");
        stocksFlexTable.setText(0, 7, "RUR buy");
        stocksFlexTable.setText(0, 8, "RUR sell");
        stocksFlexTable.setText(0, 9, "Change");

        addButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                addMoney();
            }
        });
        newSymbolTextBox.addKeyPressHandler(new KeyPressHandler() {
            public void onKeyPress(KeyPressEvent event) {
                if (event.getUnicodeCharCode() == 0) {
                    System.out.println(String.valueOf(event.getUnicodeCharCode()));
                    addMoney();
                }
            }

        });

        addPanel.add(newSymbolTextBox);
        errorMsgLabel.setStyleName("errorMessage");
        addPanel.add(addButton);
        errorMsgLabel.setVisible(false);
        mainPanel.add(errorMsgLabel);
        mainPanel.add(stocksFlexTable);
        mainPanel.add(addPanel);
        mainPanel.add(lastUpdatedLabel);

        RootPanel.get("stockList").add(mainPanel);

        refreshTimer.scheduleRepeating(REFRESH_INTERVAL);

        newSymbolTextBox.setFocus(true);
    }

    private void addMoney() {
        final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
        newSymbolTextBox.setFocus(true);
        // symbol must be 3 chars that are numbers, letters, or dots
        if (!symbol.matches("^[a-zA-Z\\.]{3}$")) {
            Window.alert("'" + symbol + "' is not a valid bank.");
            newSymbolTextBox.selectAll();
            return;
        }
        if (moneyList.contains(symbol)) {
            Window.alert("'" + symbol + "' is allready in list");
            newSymbolTextBox.selectAll();
            return;
        }
        int row = stocksFlexTable.getRowCount();
        moneyList.add(symbol);
        stocksFlexTable.setText(row, 0, symbol);
        newSymbolTextBox.setText("");

        Button removeStock = new Button("x");
        removeStock.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                int removedIndex = moneyList.indexOf(symbol);
                moneyList.remove(removedIndex);
                stocksFlexTable.removeRow(removedIndex + 1);
            }
        });
        stocksFlexTable.setWidget(row, 10, removeStock);
        refreshPrice();
    }

    private ITestAsync moneyPriceSvc;

    private void refreshPrice() {


        // lazy initialization of service proxy
        if (moneyPriceSvc == null) {
            moneyPriceSvc = GWT.create(ITest.class);
        }
        AsyncCallback<MoneyPrice[]> callback = new AsyncCallback<MoneyPrice[]>() {
            public void onFailure(Throwable caught) {
                String details = caught.getMessage();
                if (caught instanceof PriceNotEvalExp) {
                    details = "Price for " + ((PriceNotEvalExp) caught).getSymbol() + "not found";
                }
                if (caught instanceof IOException) {
                    details = "Service is not available. Try Later";
                }
                errorMsgLabel.setText("Error: " + details);
                errorMsgLabel.setVisible(true);
            }
            public void onSuccess(MoneyPrice[] result) {
                updateTable(result);
            }

        };

        moneyPriceSvc.getPrices(moneyList.toArray(new String[0]), callback);
        // change the last update timestamp
        lastUpdatedLabel.setText("Last update : " +
                DateTimeFormat.getMediumDateTimeFormat().format(new Date()));
    }

    private void updateTable(MoneyPrice[] result) {

        for (MoneyPrice price : result) {
            if (!moneyList.contains(price.getSymbol())) {
                continue;
            }
            int row = moneyList.indexOf(price.getSymbol()) + 1;

            // apply nice formatting to price and change
            String priceText = NumberFormat.getFormat("#,##0.00").format(price.getPrice());
            NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
            String changeText = changeFormat.format(price.getChange());
            String changePercentText = changeFormat.format(price.getChangePercent());

            // update the watch list with the new values
            stocksFlexTable.setText(row, 1, priceText);
            stocksFlexTable.setText(row, 2, changeText + " (" + changePercentText + "%)");
        }
        errorMsgLabel.setVisible(false);
    }
}