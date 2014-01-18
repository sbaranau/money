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

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Circle;

public class Test implements EntryPoint {

    Logger logger = Logger.getLogger(Test.class.getName());
    private VerticalPanel mainPanel = new VerticalPanel();
    private FlexTable stocksFlexTable = new FlexTable();
    private HorizontalPanel addPanel = new HorizontalPanel();
    private DrawingArea graphicModul = new DrawingArea(300, 300);
    private Button addButton = new Button("Add");
    private Label lastUpdatedLabel = new Label();
    private ArrayList<String> moneyList = new ArrayList<String>();
    private static final int REFRESH_INTERVAL = 50000;
    private Label errorMsgLabel = new Label();
    final ListBox dropBox = new ListBox(false);

    private ITestAsync moneyPriceSvc;
    
    public void onModuleLoad() {

        Timer refreshTimer = new Timer() {
            public void run() {
                refreshPrice();
            }
        };
     //   RootPanel.get().add(createChart()); 
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
        Circle circle = new Circle(100, 100, 50);
        circle.setFillColor("red");
        graphicModul.add(circle);
        mainPanel.add(dropBox);
        mainPanel.add(graphicModul);
        RootPanel.get("stockList").add(mainPanel);
    //    RootPanel.get("stockList").add(createChart());
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
    
  /*  public Chart createChart() {  
    	  
        final Chart chart = new Chart()  
            .setType(Series.Type.LINE)  
            .setMarginRight(130)  
            .setMarginBottom(25)  
            .setChartTitle(new ChartTitle()  
                .setText("Monthly Average Temperature")  
                .setX(-20)  // center  
            )  
            .setChartSubtitle(new ChartSubtitle()  
                .setText("Source: WorldClimate.com")  
                .setX(-20)  
            )  
            .setLegend(new Legend()  
                .setLayout(Legend.Layout.VERTICAL)  
                .setAlign(Legend.Align.RIGHT)  
                .setVerticalAlign(Legend.VerticalAlign.TOP)  
                .setX(-10)  
                .setY(100)  
                .setBorderWidth(0)  
            )  
            .setToolTip(new ToolTip()  
                .setFormatter(new ToolTipFormatter() {  
                    public String format(ToolTipData toolTipData) {  
                        return "<b>" + toolTipData.getSeriesName() + "</b><br/>" +  
                            toolTipData.getXAsString() + ": " + toolTipData.getYAsDouble() + "Â°C";  
                    }  
                })  
            );  
  
        chart.getXAxis()  
            .setCategories(  
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",  
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"  
            );  
  
        chart.getYAxis()  
            .setAxisTitleText("Temperature Â°C");  
  
        chart.addSeries(chart.createSeries()  
            .setName("Tokyo")  
            .setPoints(new Number[]{  
                7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6  
            })  
        );  
        chart.addSeries(chart.createSeries()  
            .setName("New York")  
            .setPoints(new Number[]{  
                -0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5  
            })  
        );  
        chart.addSeries(chart.createSeries()  
            .setName("Berlin")  
            .setPoints(new Number[]{  
                -0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0  
            })  
        );  
        chart.addSeries(chart.createSeries()  
            .setName("London")  
            .setPoints(new Number[]{  
                3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8  
            })  
        );  
  
        return chart;  
    }  */
  
}