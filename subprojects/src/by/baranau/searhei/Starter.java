package by.baranau.searhei;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;


public class Starter {
    private static final String URL_OBMENNIK = "http://www.obmennik.by/xml/" ;


	public static void main(String[] args) {

		Timer refreshTimer = new Timer();
		refreshTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				 try {
			            URL url =  new URL(URL_OBMENNIK);
			            URLConnection conn = url.openConnection();
			            InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
			            Parser.parseXml(inputStreamReader);
			        } catch (IOException e) {
			            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			        }

			}
		}, 0, 12 * 60 * 60 * 1000);
	}

}
