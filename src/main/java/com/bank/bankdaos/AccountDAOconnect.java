package com.revature.bankdaos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.bankaccess.BankAccess;
import com.revature.bankpojos.BankAccount;
import com.revature.bankpojos.BankUser;

public class AccountDAOconnect implements AccountDAO {

	public AccountDAOconnect() {
	}
	//returns the account information based on user id
	//should be used for querying by the calling class
	//this should be used at least once, other times should be after transaction changes
	@Override
	public final ArrayList<BankAccount> getAllAccountsByUser(BankUser currentUser) {
		ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();

		try(Connection conn =  BankAccess.getInstance().getConnection();) {

			String sql = "SELECT * FROM BankAccount WHERE UserID = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, currentUser.getUserID());
			ResultSet rs = pstmt.executeQuery();

			while(rs.next()) {
				BankAccount temp = new BankAccount();
				temp.setAccountID(rs.getInt(1));
				temp.setUserID(rs.getInt(2));
				temp.setAmount(rs.getDouble(3));
				temp.setAccountType(rs.getString(4));
				accounts.add(temp);
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return accounts;
	}
	//get a specific account
	//used for getting information of that account, which should be based on the logged in users own id
	@Override
	public final BankAccount getAccountById(int id) {
		BankAccount act = new BankAccount();

		try(Connection conn = BankAccess.getInstance().getConnection();) {

			String sql = "SELECT * FROM BankAccount WHERE AccountID = ?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next()) {
				act.setAccountID(id);
				act.setAccountType(rs.getString(4));
			}


		} catch (SQLException e) {
			e.printStackTrace();
		}

		return act;
	}
	//get balance by account id
	//should be used if the account balance information needs to be updated
	@Override
	public final Double getAccountBalance(int id) {
		double act = 0.0;

		try(Connection conn = BankAccess.getInstance().getConnection();) {

			String sql = "SELECT balance FROM BankAccount WHERE AccountID = ?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next()) {
				act = rs.getDouble(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return act;
	}
	//create a loan account, the number in the argument gets a specific amount and returns it
	@Override
	public BankAccount addLoanAccount(BankUser newUser, String type, double borrow) {
		BankAccount act = new BankAccount();
		try(Connection conn = BankAccess.getInstance().getConnection();) {

			conn.setAutoCommit(false);
			//insert that loan balance now instead, the type should make sure we know it's a loan
			String sql = "INSERT INTO BankAccount (UserID, Balance, AccountType) VALUES (?, ?, ?)";

			String[] keys = new String [1];
			keys[0] = "accountid";
			
			PreparedStatement pstmt = conn.prepareStatement(sql, keys);
			pstmt.setInt(1, newUser.getUserID());
			pstmt.setDouble(2, borrow);
			pstmt.setString(3, type);

			int rowsUpdated = pstmt.executeUpdate();

			ResultSet rs = pstmt.getGeneratedKeys();

			if(rowsUpdated != 0) {
				//get id
				while(rs.next()) {
					act.setAccountID(rs.getInt(1));
				}

				act.setUserID(newUser.getUserID());
				act.setAmount(0.00);
				conn.commit();

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return act;
	}
	//create a bank account, should be used after creating an account
	//defaults: $0.00 balance
	//type should be a checking, savings, or loan account, anything else is outside of the scope of this bank
	@Override
	public final BankAccount addBankAccount(BankUser newUser, String type) {
		BankAccount act = new BankAccount();

		try(Connection conn = BankAccess.getInstance().getConnection();) {

			conn.setAutoCommit(false);

			String sql = "INSERT INTO BankAccount (UserID, Balance, AccountType) VALUES (?, 0.00, ?)";

			String[] keys = new String [1];
			keys[0] = "accountid";
			
			PreparedStatement pstmt = conn.prepareStatement(sql, keys);
			pstmt.setLong(1, newUser.getUserID());
			pstmt.setString(2, type);

			int rowsUpdated = pstmt.executeUpdate();

			ResultSet rs = pstmt.getGeneratedKeys();

			if(rowsUpdated != 0) {

				while(rs.next()) {
					act.setAccountID(rs.getInt(1));
				}

				act.setUserID(newUser.getUserID());
				act.setAmount(0.00);
				conn.commit();

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return act;
	}

	//update balance in the database according to the user
	//balance calculations should be handled by calling class
	@Override
	public final boolean updateBalance(BankAccount updatedAccount) {
		try(Connection conn = BankAccess.getInstance().getConnection();) {

			conn.setAutoCommit(false);

			String sql = "UPDATE BankAccount SET balance = ? WHERE AccountID = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, updatedAccount.getAmount());
			pstmt.setInt(2, updatedAccount.getAccountID());

			int rowsUpdated = pstmt.executeUpdate();

			if(rowsUpdated != 0) {
				conn.commit();
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	//This should be used after verifying the id of the foreign user
	//the foreign user account should exist, returns false if the operation failed
	@Override
	public final boolean transferToAccount(double amount, int accountID) {
		try(Connection conn = BankAccess.getInstance().getConnection();) {

			conn.setAutoCommit(false);

			String sql = "UPDATE BankAccount SET balance = balance + ? WHERE AccountID = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, amount);
			pstmt.setInt(2, accountID);

			int rowsUpdated = pstmt.executeUpdate();

			if(rowsUpdated != 0) {
				conn.commit();
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public boolean transact() {
		try(Connection conn = BankAccess.getInstance().getConnection();) {
			conn.commit();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
