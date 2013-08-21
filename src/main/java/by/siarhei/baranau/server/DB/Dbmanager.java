package by.siarhei.baranau.server.DB;

import by.siarhei.baranau.client.MoneyPrice;
import by.siarhei.baranau.server.Entity.Money;

import java.sql.*;

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
    public Dbmanager(int step) {
        try {
            this.connection = Connector.createConnection();
            String sql = "";
            if (step == 1) {
            	sql = "INSERT INTO PRICE " +
                    "(bankId,date,time,sellusd,buyusd,selleur,buyeur,sellrur,buyrur) " +
                    "VALUES (?,?,?,?,?,?,?,?,?) ";
            } else if (step == 2) {
            	sql = "SELECT * FROM PRICE WHERE bankId=? ORDER BY DATE DESC";
            }
            preparedStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MoneyPrice getDate (String bank) throws SQLException{
        MoneyPrice moneyPrice = null;
        Statement statement = null;
        ResultSet rs = null; 
        try{
             System.out.println("Get prices for: " + bank);
             preparedStatement.setString(1, bank);
             rs = preparedStatement.executeQuery();
             int count = 0;
             while (rs.next()) {
            	 count ++;
            	// moneyPrice.setPriceEurSell(rs.getBigDecimal(""));
            	 if (count > 2) {
            		 break;
            	 }
             }
             
         } catch (SQLException e) {
        		 e.printStackTrace();
         } finally {
        	 if (statement != null) {
                 statement.close();
             }
        	 if (rs != null) {
                 rs.close();
             }
         }
    	return  null;
    }
    
    public int saveInBase(Money money) throws SQLException {
        Statement statement = null;

        try{
     /*       System.out.println(money.getBuyPrice() + money.getName());
            preparedStatement.setInt(1, money.getBankId());
            preparedStatement.setInt(2, money.getDate());
            preparedStatement.setInt(3, money.getTime());
            preparedStatement.setString(4, money.getName());
            preparedStatement.setBigDecimal(5, money.getSellPrice());
            preparedStatement.setBigDecimal(6, money.getBuyPrice());*/
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
