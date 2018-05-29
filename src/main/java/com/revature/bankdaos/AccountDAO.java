package com.revature.bankdaos;

import java.util.ArrayList;

import com.revature.bankpojos.BankAccount;
import com.revature.bankpojos.BankUser;

public interface AccountDAO {
	public ArrayList<BankAccount> getAllAccountsByUser(BankUser currentUser);
	public BankAccount getAccountById(int id);
	public Double getAccountBalance(int id);
	
	public BankAccount addBankAccount(BankUser newUser, String type);
	public BankAccount addLoanAccount(BankUser newUser, String type, double borrow);
	public boolean updateBalance(BankAccount updatedUser);
	public boolean transferToAccount(double amount, int accountID);
	

	public boolean transact();
	
}
