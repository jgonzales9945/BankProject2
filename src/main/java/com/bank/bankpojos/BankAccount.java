package com.revature.bankpojos;

public class BankAccount {
	
	private int accountID;//PK
	private int userID;//FK
	private String accountType;
	private double amount;
	
	public BankAccount(int accountID, int userID, double amount, String accountType) {
		super();
		this.accountID = accountID;
		this.userID = userID;
		this.accountType = accountType;
		this.amount = amount;
	}

	public BankAccount() {
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

	public int getAccountID() {
		return accountID;
	}

	public void setAccountID(int accountID) {
		this.accountID = accountID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setDepositBalance(double de) {
		if(this.accountType.equals("loan")) this.amount -= de;
		else this.amount += de;
	}

	public void setWithdrawBalance(double wd) {
		this.amount -= wd;
	}

}
