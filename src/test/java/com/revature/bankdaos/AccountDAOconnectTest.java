package com.revature.bankdaos;

import com.revature.bankpojos.BankAccount;
import com.revature.bankpojos.BankUser;

import junit.framework.TestCase;

public class AccountDAOconnectTest extends TestCase {

	public void testAccountDAOconnect() {
		assertNull(new AccountDAOconnect().addBankAccount(new BankUser(), "loan"));
		assertNotNull(new AccountDAOconnect().addBankAccount(new BankUser(" "," "," "), "loan"));
		assertNotNull(new AccountDAOconnect().addBankAccount(new BankUser(1,"DEFAULT","DEFAULT@chiru.no", "4c51ff40a043ceed97a72d6408147399ebabf82a335f855164dd3b95e8139529",(short)2), "checking"));
		
		assertFalse(new AccountDAOconnect().updateBalance(new BankAccount()));
		assertTrue(new AccountDAOconnect().updateBalance(new BankAccount(1,1,2.00,"checking")));
		
		
	}

}
