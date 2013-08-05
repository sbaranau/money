package by.siarhei.baranau.server.DB;

import by.siarhei.baranau.server.Entity.Money;

import java.sql.*;
import java.sql.PreparedStatement;

/**
 * Created with IntelliJ IDEA.
 * User: siarhei
 * Date: 8/2/13
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class Dbmanager {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    public Dbmanager() {
        try {
            this.connection = Connector.createConnection();
            String sqlInsert = "INSERT INTO PRICE " +
                    "(bankId,date,time,moneyName,sell,buy) " +
                    "VALUES (?,?,?,?,?,?) ";
            preparedStatement = connection.prepareStatement(sqlInsert);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getDate (){
        return  null;
    }
    public int saveInBase(Money money) throws SQLException {
        Statement statement = null;

        try{
            System.out.println(money.getBuyPrice() + money.getName());
            preparedStatement.setInt(1, money.getBankId());
            preparedStatement.setInt(2, money.getDate());
            preparedStatement.setInt(3, money.getTime());
            preparedStatement.setString(4, money.getName());
            preparedStatement.setBigDecimal(5, money.getSellPrice());
            preparedStatement.setBigDecimal(6, money.getBuyPrice());
            boolean result = preparedStatement.execute();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        return 0;
    }

}
