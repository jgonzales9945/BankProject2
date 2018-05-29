package com.revature.bankdaos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.revature.bankaccess.BankAccess;
import com.revature.bankpojos.BankUser;

public class UserDAOconnect implements UserDAO{

	public UserDAOconnect() {
		// TODO Auto-generated constructor stub
	}
	//administrators only, user should be finding their account by user name
	@Override
	public ArrayList<BankUser> getAllUsers() {
		ArrayList<BankUser> users = new ArrayList<BankUser>();

		try(Connection conn =  BankAccess.getInstance().getConnection();) {

			String sql = "SELECT * FROM BankUser";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()) {
				BankUser temp = new BankUser();
				temp.setUserID(rs.getInt(1));
				temp.setUserName(rs.getString(2));
				users.add(temp);
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}  catch (NullPointerException e) {
			System.out.println("Could not reliably connect to the server");
		}

		return users;
	}
	//check if the user exists
	//don't want to transfer to a non-existent user account
	@Override
	public boolean getUserById(int id) {

		try(Connection conn = BankAccess.getInstance().getConnection();) {

			String sql = "SELECT * FROM BankUser WHERE UserID = ?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next()) {
				return true;
			}


		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("Could not reliably connect to the server");
		}
		return false;
	}
	//for checking logins
	//password checking and security is determined by the calling class
	@Override
	public BankUser getUserByName(String email, String pswd) {
		BankUser user = new BankUser();

		try(Connection conn = BankAccess.getInstance().getConnection();) {

			String sql = "SELECT UserID, UserName, UserEmail, Status FROM BankUser WHERE UserEmail = ? and UserPassword = ?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			pstmt.setString(2, pswd);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next()) {
				user.setUserID(rs.getInt(1));
				user.setUserName(rs.getString(2));
				user.setUserEmail(rs.getString(3));
				user.setStatus((short)rs.getInt(4));
			}


		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("Could not reliably connect to the server");
		}

		return user;
	}
	//send user data to the database
	//encrypt the user password with md5 for some security
	@Override
	public BankUser addUser(BankUser newUser) {
		BankUser user = new BankUser();

		try(Connection conn = BankAccess.getInstance().getConnection();) {

			conn.setAutoCommit(false);

			String sql = "INSERT INTO BankUser.BankUser (UserName, UserEmail, UserPassword, Status) VALUES (?, ?, ?, 1)";

			String[] keys = new String [1];
			keys[0] = "accountid";
			
			PreparedStatement pstmt = conn.prepareStatement(sql, keys);
			System.out.println("preparing");
			pstmt.setString(1, newUser.getUserName());
			pstmt.setString(2, newUser.getUserEmail());
			pstmt.setString(3, newUser.getUserPassword());
			System.out.println(pstmt.toString());
			int rowsUpdated = pstmt.executeUpdate();

			ResultSet rs = pstmt.getGeneratedKeys();

			if(rowsUpdated != 0) {

				while(rs.next()) {
					user.setUserID(rs.getInt(1));
				}

				user.setUserName(newUser.getUserName());
				user.setUserEmail(newUser.getUserEmail());
				user.setStatus((short)1);
				conn.commit();

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("Could not reliably connect to the server");
		}

		return user;
	}
	//update account info
	//expired emails or password changes only
	@Override
	public boolean updateUser(BankUser updatedUser) {
		try(Connection conn = BankAccess.getInstance().getConnection();) {

			conn.setAutoCommit(false);
			//update everything, even if only one or two things got changed
			String sql = "UPDATE BankUser SET UserEmail = ?, UserPassword = ? WHERE AccountID = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, updatedUser.getUserEmail());
			pstmt.setString(2, updatedUser.getUserPassword());
			pstmt.setInt(3, updatedUser.getUserID());

			int rowsUpdated = pstmt.executeUpdate();

			if(rowsUpdated != 0) {
				conn.commit();
				return true;
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("Could not reliably connect to the server");
		}
		return false;
	}
	@Override
	public boolean checkUserByName(String name, String email) {
		try(Connection conn = BankAccess.getInstance().getConnection();) {

			String sql = "SELECT * FROM BankUser WHERE UserName = ? or UserEmail = ?";

			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, email);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next()) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("Could not reliably connect to the server");
		}

		return false;
	}

}
