package by.siarhei.baranau.server.DB;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;

import by.siarhei.baranau.client.MoneyPrice;
import by.siarhei.baranau.server.Entity.Money;

/**
 * Created with IntelliJ IDEA. User: siarhei Date: 8/2/13 Time: 10:46 PM To
 * change this template use File | Settings | File Templates.
 */
public class Dbmanager {

	Connection connection = null;
	PreparedStatement preparedStatement = null;

	public Dbmanager(int step) {
		try {
			this.connection = Connector.createConnection();
			String sql = "";
			if (step == 1) {
				sql = "INSERT INTO PRICE "
						+ "(bankId,date,time,sellusd,buyusd,selleur,buyeur,sellrur,buyrur) "
						+ "VALUES (?,?,?,?,?,?,?,?,?) ";
				preparedStatement = connection.prepareStatement(sql);
			} else if (step == 2) {
				sql = "SELECT * FROM PRICE WHERE bankId=? ORDER BY date DESC, time DESC";
				preparedStatement = connection.prepareStatement(sql);
			} else if (step == 3) {
				sql = "SELECT date, sellusd, selleur,sellrur FROM PRICE WHERE bankId='1' AND date >=? ORDER BY date";
				preparedStatement = connection.prepareStatement(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public MoneyPrice getDate(String bank) throws SQLException {
		MoneyPrice moneyPrice = new MoneyPrice();
		moneyPrice.setPriceEurSell(BigDecimal.ZERO);
		moneyPrice.setPriceUsdSell(BigDecimal.ZERO);
		moneyPrice.setPriceRurSell(BigDecimal.ZERO);
		ResultSet rs = null;
		BigDecimal previosUsd = BigDecimal.ONE;
		BigDecimal previosEur = BigDecimal.ONE;
		BigDecimal previosRur = BigDecimal.ONE;
		int count = 0;
		try {
			System.out.println("Get prices for: " + bank);
			preparedStatement.setString(1, bank);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				count++;
				if (count == 1) {
					moneyPrice.setBankName(rs.getInt("bankId"));
					moneyPrice.setDate(rs.getInt("bankId"));
					moneyPrice.setTime(rs.getInt("time"));
					moneyPrice.setPriceEurSell(rs.getBigDecimal("selleur"));
					moneyPrice.setPriceEurBuy(rs.getBigDecimal("buyeur"));
					moneyPrice.setPriceUsdSell(rs.getBigDecimal("sellusd"));
					moneyPrice.setPriceUsdBuy(rs.getBigDecimal("buyusd"));
					moneyPrice.setPriceRurSell(rs.getBigDecimal("sellrur"));
					moneyPrice.setPriceRurBuy(rs.getBigDecimal("buyrur"));
				} else {
					previosEur = rs.getBigDecimal("selleur");
					previosRur = rs.getBigDecimal("sellrur");
					previosUsd = rs.getBigDecimal("sellusd");
				}
				if (count > 2) {
					break;
				}
			}
			moneyPrice.setChangeEur(moneyPrice.getPriceEurSell().subtract(
					previosEur));
			moneyPrice.setChangeUsd(moneyPrice.getPriceUsdSell().subtract(
					previosUsd));
			moneyPrice.setChangeRur(moneyPrice.getPriceRurSell().subtract(
					previosRur));
			System.out.println(previosEur
					+ moneyPrice.getPriceEurSell().toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		if (count == 0) {
			moneyPrice.setBankName(0);
			moneyPrice.setDate(rs.getInt(0));
			moneyPrice.setTime(0);
			moneyPrice.setPriceEurSell(BigDecimal.ZERO);
			moneyPrice.setPriceEurBuy(BigDecimal.ZERO);
			moneyPrice.setPriceUsdSell(BigDecimal.ZERO);
			moneyPrice.setPriceUsdBuy(BigDecimal.ZERO);
			moneyPrice.setPriceRurSell(BigDecimal.ZERO);
			moneyPrice.setPriceRurBuy(BigDecimal.ZERO);
		}
		return moneyPrice;
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

	public HashMap<String, String> getBanks() throws SQLException {
		ResultSet rs = null;
		Statement stmt = null;
		HashMap<String, String> banksMap = new HashMap<String, String>();
		String sql = "SELECT * FROM bank ORDER BY idbank";
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				banksMap.put(String.valueOf(rs.getInt("idbank")),
						rs.getString("namebank"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		return banksMap;
	}

	public LinkedHashMap<String, String> getPriceList(String money, String date) throws SQLException {
		ResultSet rs = null;
		Statement stmt = null;
		LinkedHashMap<String, String> moneyMap = new LinkedHashMap<String, String>();
		try {
			preparedStatement.setInt(1, Integer.parseInt("2014000"));
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if ("USD".equals(money)) {
					moneyMap.put(rs.getString("date"), rs.getString("sellusd"));
				} else if ("EUR".equals(money)) {
					moneyMap.put(rs.getString("date"), rs.getString("selleur"));
				} else if ("RUR".equals(money)) {
					moneyMap.put(rs.getString("date"), rs.getString("sellrur"));
				}  
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		}

		return moneyMap;

	}

}
