package by.baranau.searhei;

import java.io.InputStreamReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

/**
 * Created with IntelliJ IDEA.
 * User: siarhei
 * Date: 8/1/13
 * Time: 11:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class Parser {

    public static boolean parseXml(InputStreamReader streamReader) {
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            ObmennikHandler handler = new ObmennikHandler();

            InputSource is = new InputSource(streamReader);
            is.setEncoding("UTF-8");

            saxParser.parse(is, handler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    return true;
    }
}
