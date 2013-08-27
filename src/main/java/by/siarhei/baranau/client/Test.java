package by.siarhei.baranau.client;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Test implements EntryPoint {

    Logger logger = Logger.getLogger(Test.class.getName());
    private VerticalPanel mainPanel = new VerticalPanel();
    private FlexTable stocksFlexTable = new FlexTable();
    private HorizontalPanel addPanel = new HorizontalPanel();
    //private TextBox newSymbolTextBox = new TextBox();
    private Button addButton = new Button("Add");
    private Label lastUpdatedLabel = new Label();
    private ArrayList<String> moneyList = new ArrayList<String>();
    private static final int REFRESH_INTERVAL = 50000;
    private Label errorMsgLabel = new Label();
    final ListBox dropBox = new ListBox(false);
    private static HashMap<String, String> banksMap = new HashMap<String, String>();

    private ITestAsync moneyPriceSvc;
    
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
        createPriceService();
        moneyPriceSvc.getBanks(new AsyncCallback<HashMap<String, String>>() {
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
			public void onSuccess(HashMap<String, String> arg0) {
				System.out.println("utrue"); // TODO Auto-generated method stub
				setBanks(arg0); 
			}
        });
        
        //addPanel.add(newSymbolTextBox);
        addPanel.add(dropBox);
        errorMsgLabel.setStyleName("errorMessage");
        addPanel.add(addButton);
        errorMsgLabel.setVisible(false);
        mainPanel.add(errorMsgLabel);
        mainPanel.add(stocksFlexTable);
        mainPanel.add(addPanel);
        mainPanel.add(lastUpdatedLabel);

        RootPanel.get("stockList").add(mainPanel);
        refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
        //newSymbolTextBox.setFocus(true);
    }

    private void setBanks(HashMap<String, String> banksMap) {
    	for (String key : banksMap.keySet()) {
	          dropBox.addItem(banksMap.get(key), key);
	    }
    }
    
    private void addMoney() {
        final int symbol = dropBox.getSelectedIndex();
        
        int row = stocksFlexTable.getRowCount();
        String value = dropBox.getValue(symbol); 
        moneyList.add(value);
        if (moneyList.contains(symbol)) {
            Window.alert("'" + value + "' is allready in list");
            return;
        }
        stocksFlexTable.setText(row, 0, value);
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

    private void refreshPrice() {
    	createPriceService();
       
    	AsyncCallback <ArrayList<MoneyPrice>> callback = new AsyncCallback<ArrayList<MoneyPrice>>() {
            public void onFailure(Throwable caught) {
                String details = caught.getMessage();
                if (caught instanceof IOException) {
                    details = "Service is not available. Try Later";
                }
                errorMsgLabel.setText("Error: " + details);
                errorMsgLabel.setVisible(true);
            }
			public void onSuccess(ArrayList<MoneyPrice> prices) {
				// TODO Auto-generated method stub
				updateTable(prices);
				System.out.println(prices.size());
			}
        };

        moneyPriceSvc.getPrices(moneyList.toArray(new String[0]), callback);
        // change the last update timestamp
        lastUpdatedLabel.setText("Last update : " +
                DateTimeFormat.getMediumDateTimeFormat().format(new Date()));
    }

    private void updateTable(ArrayList<MoneyPrice> result) {

        NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");

    	for (MoneyPrice bankPrice : result) {
            if (!moneyList.contains(String.valueOf(bankPrice.getBankName()))) {
            	System.out.println("return");
            	continue;
            }
            int row = moneyList.indexOf(String.valueOf(bankPrice.getBankName())) + 1;

            // apply nice formatting to price and change
            String priceUSDBuy = NumberFormat.getFormat("#,##0.00").format(bankPrice.getPriceUsdBuy());
            String priceUSDSell = NumberFormat.getFormat("#,##0.00").format(bankPrice.getPriceUsdSell());
            String changeUSD = changeFormat.format(bankPrice.getChangeUsd());
            String changeUsdPercent = (bankPrice.getChangeUsd().compareTo(BigDecimal.ZERO) == 0)?"0":
            	changeFormat.format(bankPrice.getPriceUsdSell().divide(bankPrice.getChangeUsd(), 3, RoundingMode.HALF_UP));

            String priceEurBuy = NumberFormat.getFormat("#,##0.00").format(bankPrice.getPriceEurBuy());
            String priceEurSell = NumberFormat.getFormat("#,##0.00").format(bankPrice.getPriceEurSell());
            String changeEur = changeFormat.format(bankPrice.getChangeEur());
            String changeEurPercent = (bankPrice.getChangeEur().compareTo(BigDecimal.ZERO) == 0)?"0":
            	changeFormat.format(bankPrice.getPriceEurSell().divide(bankPrice.getChangeEur(), 3, RoundingMode.HALF_UP));
            
            String priceRurBuy = NumberFormat.getFormat("#,##0.00").format(bankPrice.getPriceRurBuy());
            String priceRurSell = NumberFormat.getFormat("#,##0.00").format(bankPrice.getPriceRurSell());
            String changeRur = changeFormat.format(bankPrice.getChangeRur());
            String changeRurPercent = (bankPrice.getChangeRur().compareTo(BigDecimal.ZERO) == 0)?"0":
            	changeFormat.format(bankPrice.getPriceRurSell().divide(bankPrice.getChangeRur(), 3, RoundingMode.HALF_UP));

            // update the watch list with the new values
            stocksFlexTable.setText(row, 1, priceUSDBuy);
            stocksFlexTable.setText(row, 2, priceUSDSell);
            stocksFlexTable.setText(row, 3, changeUSD + " (" + changeUsdPercent + "%)");

            stocksFlexTable.setText(row, 4, priceEurBuy);
            stocksFlexTable.setText(row, 5, priceEurSell);
            stocksFlexTable.setText(row, 6, changeEur + " (" + changeEurPercent + "%)");

            stocksFlexTable.setText(row, 7, priceRurBuy);
            stocksFlexTable.setText(row, 8, priceRurSell);
            stocksFlexTable.setText(row, 9, changeRur + " (" + changeRurPercent + "%)");
    	}
        errorMsgLabel.setVisible(false);
    }
    
  /*  private Hashtable<String, String> getBanksList() {
		return null;
    	
    }*/
    
    private void createPriceService(){
    	// lazy initialization of service proxy 
    	if (moneyPriceSvc == null) {
             moneyPriceSvc = GWT.create(ITest.class);
        }
    }
}