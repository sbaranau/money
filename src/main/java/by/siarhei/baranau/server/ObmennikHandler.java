package by.siarhei.baranau.server;

import by.siarhei.baranau.client.MoneyPrice;
import by.siarhei.baranau.server.DB.Dbmanager;
import by.siarhei.baranau.server.Entity.Money;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: siarhei
 * Date: 7/27/13
 * Time: 3:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ObmennikHandler extends DefaultHandler {

    Money money = null;
    String moneyName = "";
    boolean bankStart = false;
    boolean bankEnd = false;
    boolean bankId = false;
    boolean date = false;
    boolean time = false;
    boolean usd = false;
    boolean eur = false;
    boolean rur = false;
    boolean sell = false;
    boolean buy = false;


    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
            throws SAXException {

        System.out.println("Start Element :" + qName);

        if (qName.equalsIgnoreCase("bank-id")) {
            bankStart = true;
        }
        if (qName.equalsIgnoreCase("idbank")) {
            bankId = true;
        }
        if (qName.equalsIgnoreCase("date")) {
            date = true;
        }
        if (qName.equalsIgnoreCase("time")) {
            time = true;
        }
        if (qName.equalsIgnoreCase("usd")) {
            usd = true;
        }
        if (qName.equalsIgnoreCase("eur")) {
            eur = true;
        }
        if (qName.equalsIgnoreCase("rur")) {
            rur = true;
        }
        if (qName.equalsIgnoreCase("sell")) {
            sell = true;
        }
        if (qName.equalsIgnoreCase("buy")) {
            buy = true;
        }

    }

    public void endElement(String uri, String localName,
                           String qName)
            throws SAXException {

        System.out.println("End Element :" + qName);
        try {
            if (qName.equalsIgnoreCase("bank-id")) {
                Dbmanager dbmanager = new Dbmanager(1);
                dbmanager.saveInBase(money);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void characters(char ch[], int start, int length)
            throws SAXException {

        System.out.println(new String(ch, start, length));
        String value = "";
        try {
            if (bankStart) {
                money = new Money();
                bankStart = false;
            }

            if (bankId) {
                value = new String(ch, start, length);
                money.setBankId(Integer.parseInt(value));
                bankId = false;
            }

            if (date) {
                value = new String(ch, start, length);
                money.setDate(Integer.parseInt(value.replace("-", "")));
                date = false;
            }

            if (time) {
                value = new String(ch, start, length);
                money.setTime(Integer.parseInt(value.replace(":", "")));
                time = false;
            }
            if (sell) {
           	 	if (usd) {
                	money.setSellUSDPrice(toBigDecimal(new String(ch, start, length)));
                } else if (eur) {
                	money.setSellEURPrice(toBigDecimal(new String(ch, start, length)));
                }else if (rur) {
                	money.setSellRURPrice(toBigDecimal(new String(ch, start, length)));
                }
                sell = false;
           }
            if (buy) {
                if (usd) {
                	money.setBuyUSDPrice(toBigDecimal(new String(ch, start, length)));
                	usd = false;
                } else if (eur) {
                	money.setBuyEURPrice(toBigDecimal(new String(ch, start, length)));
                	eur = false;
                }else if (rur) {
                	money.setBuyRURPrice(toBigDecimal(new String(ch, start, length)));
                	rur = false;
                }
                buy = false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (bankEnd) {
            System.out.println("Save in Db need ");
            bankEnd = false;
        }

    }

    private String normalise(String value) {
        if (value != null) {
            return value;
        } else {
            return "";
        }
    }
    
    private BigDecimal toBigDecimal(String value) {
    	if (value == null) {
            return BigDecimal.ZERO;
        }
    	try {
    		return new BigDecimal(value); 
    	} catch (Exception e) {
    		e.printStackTrace();
    		return BigDecimal.ZERO;
    	}
    }
}
