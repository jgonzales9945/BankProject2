package com.revature.bankdaos;

import java.util.ArrayList;

import com.revature.bankpojos.BankUser;

public interface UserDAO {
	public ArrayList<BankUser> getAllUsers();
	public boolean getUserById(int id);
	public BankUser getUserByName(String name, String pswd);
	public boolean checkUserByName(String name, String email);
	
	public BankUser addUser(BankUser newUser);
	public boolean updateUser(BankUser updatedUser);
	
}
