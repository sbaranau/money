package by.siarhei.baranau.server;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: siarhei
 * Date: 7/27/13
 * Time: 3:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ObmennikHandler extends DefaultHandler {

    Money money = null;
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
        if (qName.equalsIgnoreCase("bank-id")) {
            bankEnd = true;
            System.out.println("End Bank");
        }

    }

    public void characters(char ch[], int start, int length)
            throws SAXException {

        System.out.println(new String(ch, start, length));


        if (bankStart) {
            money = new Money();
            bankStart = false;
        }

        if (bankId) {
            money.setBankId(new String(ch, start, length));
            bankId = false;
        }

        if (date) {
            money.setDate(new String(ch, start, length));
            date = false;
        }

        if (time) {
            money.setTime(new String(ch, start, length));
            time = false;
        }
        if (usd) {
            System.out.println("usd : ");

            money.setName("USD");
            usd = false;
        }
        if (eur) {
            System.out.println("eur : ");

            money.setName("EUR");
            eur = false;
        }
        if (rur) {
            System.out.println("RUR: ");
            money.setName("RUR");
            rur = false;
        }
        if (buy) {
            System.out.println("BUY: " + new String(ch, start, length));
            money.setBuyPrice(new BigDecimal(new String(ch, start, length)));
            buy = false;
        }
        if (sell) {
            System.out.println("SELL: " + new String(ch, start, length));
            money.setSellPrice(new BigDecimal(new String(ch, start, length)));
            sell = false;
        }

    }
}
