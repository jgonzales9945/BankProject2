package com.revature.bankpojos;

public class BankUser {
	
	private int userID;
	private String userName;
	private String userEmail;
	private String userPassword;
	private short status;
	public BankUser(String userName, String userEmail, String userPassword) {
		super();
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
	}
	//testing class
	public BankUser(int userID, String userName, String userEmail, String userPassword, short status) {
		super();
		this.userID = userID;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.status = status;
	}
	public BankUser() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public short getStatus() {
		return status;
	}
	public void setStatus(short userAccess) {
		this.status = userAccess;
	}

}
