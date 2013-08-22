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
            } else if (step == 3) {
            	 sql = "SELECT bankId FROM PRICE WHERE bankId=? AND DATE=? AND TIME=?";
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
    
    public boolean saveInBase(Money money) throws SQLException {
        Statement statement = null;
        boolean result = false;
        if (dateExist(money)) {
        	return false;
        }
        try {
            System.out.println(money.getBankId() + money.getDate());
            preparedStatement.setInt(1, money.getBankId());
            preparedStatement.setInt(2, money.getDate());
            preparedStatement.setInt(3, money.getTime());
            preparedStatement.setBigDecimal(4, money.getSellUSDPrice());
            preparedStatement.setBigDecimal(5, money.getBuyUSDPrice());
            preparedStatement.setBigDecimal(6, money.getSellEURPrice());
            preparedStatement.setBigDecimal(7, money.getBuyEURPrice());
            preparedStatement.setBigDecimal(8, money.getSellRURPrice());
            preparedStatement.setBigDecimal(9, money.getBuyRURPrice());
            result = preparedStatement.execute();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        return result;
    }
    
    private boolean dateExist(Money money) throws SQLException {
    	Statement statement = null;
        ResultSet rs = null;
        try {
	    	preparedStatement.setInt(1, money.getBankId());
	        preparedStatement.setInt(2, money.getDate());
	        preparedStatement.setInt(3, money.getTime());
	        rs = preparedStatement.executeQuery();
	        if (rs.next()) {
	        	return true;
	        } else {
	        	return false;
	        }
        } catch (SQLException e) {
        	e.printStackTrace();
        	return false;
        } finally {
	       	 if (statement != null) {
	             statement.close();
	         }
	    	 if (rs != null) {
	             rs.close();
	         }
        }
    }
    

}
