package by.siarhei.baranau.server.DB;

import by.siarhei.baranau.server.Entity.Money;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Created with IntelliJ IDEA.
 * User: siarhei
 * Date: 8/2/13
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class Dbmanager {

    Connection connection = null;

    public Dbmanager() {
        this.connection = Connector.createConnection();
    }

    public ResultSet getDate (){
        return  null;
    }
    public int saveInBase(Money money) {
       return 0;
    }

}
