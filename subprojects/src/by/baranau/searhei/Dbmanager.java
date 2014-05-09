package by.baranau.searhei;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Created with IntelliJ IDEA. User: siarhei Date: 8/2/13 Time: 10:46 PM To
 * change this template use File | Settings | File Templates.
 */
public class Dbmanager {

	Connection connection = null;
	PreparedStatement preparedStatement = null;

	public Dbmanager() {
		try {
			this.connection = Connector.createConnection();
			String sql = "";
				sql = "INSERT INTO PRICE "
						+ "(bankId,date,time,sellusd,buyusd,selleur,buyeur,sellrur,buyrur) "
						+ "VALUES (?,?,?,?,?,?,?,?,?) ";
				preparedStatement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean saveInBase(Money money) throws SQLException {
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
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		}
		return result;
	}

	private boolean dateExist(Money money) throws SQLException {
		ResultSet rs = null;
		Statement stmt = null;
		String sql = "SELECT bankId FROM PRICE WHERE bankId="
				+ money.getBankId() + " AND DATE= " + money.getDate()
				+ " AND TIME=" + money.getTime();
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

}
