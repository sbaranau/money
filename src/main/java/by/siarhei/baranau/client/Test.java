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
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.LineChart;
import com.google.gwt.visualization.client.visualizations.LineChart.Options;
import com.google.gwt.visualization.client.visualizations.PieChart;

public class Test implements EntryPoint {

    Logger logger = Logger.getLogger(Test.class.getName());
    private VerticalPanel mainPanel = new VerticalPanel();
    private FlexTable stocksFlexTable = new FlexTable();
    private HorizontalPanel addPanel = new HorizontalPanel();
    private HorizontalPanel chartPanel = new HorizontalPanel();
    private VerticalPanel actionPanel = new VerticalPanel();
    private HorizontalPanel graphicOptionsPanel = new HorizontalPanel();
    private Button addButton = new Button("Add");
    private Button graphicButton = new Button();
    private boolean showGraphic = false;
    private Label lastUpdatedLabel = new Label();
    private ArrayList<String> moneyList = new ArrayList<String>();
    private static final int REFRESH_INTERVAL = 50000;
    private Label errorMsgLabel = new Label();
    final ListBox dropBox = new ListBox(false);
    final ListBox dropBoxMoney = new ListBox(false);
    final ListBox dropBoxPeriod = new ListBox(false);

    private ITestAsync moneyPriceSvc;
    
    {
    	dropBoxMoney.addItem("USD", "U");
    	dropBoxMoney.addItem("EUR", "E");
    	dropBoxMoney.addItem("RUR", "R");
    	
    	dropBoxPeriod.addItem("week", "w");
    	dropBoxPeriod.addItem("mounth", "m");
    	dropBoxPeriod.addItem("year", "y");
    	
    	graphicOptionsPanel.add(dropBoxMoney);
    	graphicOptionsPanel.add(dropBoxPeriod);
    	graphicOptionsPanel.add(graphicButton);
    }
    public void onModuleLoad() {

        Timer refreshTimer = new Timer() {
            public void run() {
                refreshPrice();
            }
        };
        Runnable onLoadCallback = new Runnable() {
            public void run() {
            LineChart lineChart = getLineChart();
            chartPanel.add(lineChart);
            }
         };

        stocksFlexTable.setText(0, 0, "Bank");
        stocksFlexTable.setText(0, 1, "USD sell");
        stocksFlexTable.setText(0, 2, "USD buy");
        stocksFlexTable.setText(0, 3, "Change");
        stocksFlexTable.setText(0, 4, "EUR sell");
        stocksFlexTable.setText(0, 5, "EUR buy");
        stocksFlexTable.setText(0, 6, "Change");
        stocksFlexTable.setText(0, 7, "RUR sell");
        stocksFlexTable.setText(0, 8, "RUR buy");
        stocksFlexTable.setText(0, 9, "Change");
        stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
        stocksFlexTable.addStyleName("watchList");

        addButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                addMoney();
            }
        });
        graphicButton.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				if (showGraphic == false) {
					showGraphic = true;
					graphicButton.setText("Hide graphic");
				} else {
					showGraphic = false;
					graphicButton.setText("Show graphic");
				}
			}
		});
        
        actionPanel.add(graphicOptionsPanel);
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
				logger.info("get banks list"); 
				setBanks(arg0); 
			}
        });
        
        addPanel.add(dropBox);
        errorMsgLabel.setStyleName("errorMessage");
        addPanel.add(addButton);
        errorMsgLabel.setVisible(false);
        mainPanel.add(errorMsgLabel);
        mainPanel.add(stocksFlexTable);
        mainPanel.add(addPanel);
        mainPanel.add(lastUpdatedLabel);
       // mainPanel.add(dropBox);
        mainPanel.add(chartPanel);
        RootPanel.get("stockList").add(mainPanel);
        RootPanel.get("stockList").add(actionPanel);
     // Load the visualization api, passing the onLoadCallback to be called
        // when loading is done.
        VisualizationUtils.loadVisualizationApi(onLoadCallback, PieChart.PACKAGE);

        refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
    }

    private void setBanks(HashMap<String, String> banksMap) {
    	for (String key : banksMap.keySet()) {
	          dropBox.addItem(banksMap.get(key), key);
	    }
    }
    
    private void addMoney() {
        int symbol = dropBox.getSelectedIndex();
        
        int row = stocksFlexTable.getRowCount();
        final String value = dropBox.getValue(symbol); 
        String name = dropBox.getItemText(symbol);
        if (moneyList.contains(value)) {
            Window.alert("'" + value + "' is allready in list");
            return;
        }
        moneyList.add(value);

        stocksFlexTable.setText(row, 0, name);
        Button removeStock = new Button("x");
        removeStock.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                int removedIndex = moneyList.indexOf(value);
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
				updateTable(prices);
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
            if ("0".equals(bankPrice.getBankName())) {
            	errorMsgLabel.setText("Error: not all banks are shown");
            	continue;
            }
            int row = moneyList.indexOf(String.valueOf(bankPrice.getBankName())) + 1;

            // apply nice formatting to price and change
            String priceUSDBuy = NumberFormat.getFormat("#,##0.00").format(bankPrice.getPriceUsdBuy());
            String priceUSDSell = NumberFormat.getFormat("#,##0.00").format(bankPrice.getPriceUsdSell());
            String changeUSD = changeFormat.format(bankPrice.getChangeUsd());
            String changeUsdPercent = (bankPrice.getChangeUsd().compareTo(BigDecimal.ZERO) == 0)?"0":
            	changeFormat.format(bankPrice.getChangeUsd().multiply(new BigDecimal("100")).divide(bankPrice.getPriceUsdSell(), 3, RoundingMode.HALF_UP));

            String priceEurBuy = NumberFormat.getFormat("#,##0.00").format(bankPrice.getPriceEurBuy());
            String priceEurSell = NumberFormat.getFormat("#,##0.00").format(bankPrice.getPriceEurSell());
            String changeEur = changeFormat.format(bankPrice.getChangeEur());
            String changeEurPercent = (bankPrice.getChangeEur().compareTo(BigDecimal.ZERO) == 0)?"0":
            	changeFormat.format(bankPrice.getChangeEur().multiply(new BigDecimal("100")).divide(bankPrice.getPriceEurSell(), 3, RoundingMode.HALF_UP));
            
            String priceRurBuy = NumberFormat.getFormat("#,##0.00").format(bankPrice.getPriceRurBuy());
            String priceRurSell = NumberFormat.getFormat("#,##0.00").format(bankPrice.getPriceRurSell());
            String changeRur = changeFormat.format(bankPrice.getChangeRur());
            String changeRurPercent = (bankPrice.getChangeRur().compareTo(BigDecimal.ZERO) == 0)?"0":
            	changeFormat.format(bankPrice.getChangeRur().multiply(new BigDecimal("100")).divide(bankPrice.getPriceRurSell(), 3, RoundingMode.HALF_UP));

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
    
    private void createPriceService(){
    	// lazy initialization of service proxy 
    	if (moneyPriceSvc == null) {
             moneyPriceSvc = GWT.create(ITest.class);
        }
    }
    
    private LineChart getLineChart() {
    	
    	DataTable data = DataTable.create();
        data.addColumn(ColumnType.STRING, "X");
        data.addColumn(ColumnType.NUMBER, "Chanel 1");
        data.addColumn(ColumnType.NUMBER, "Channel 2");
        data.addRows(2);
        data.setValue(0, 0, "0");
        data.setValue(0, 1, 0);
        data.setValue(0, 2, 0);
        data.setValue(1, 0, "1");
        data.setValue(1, 1, 4);
        data.setValue(1, 2, 1);

        Options options = Options.create();
        options.setWidth(1000);
        options.setHeight(700);
        options.setTitle("Test");
        options.setEnableTooltip(false);
        options.setPointSize(0);

        LineChart pie = new LineChart(data, options);
        return pie;
    }
    
}