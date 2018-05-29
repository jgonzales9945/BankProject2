package com.revature.bankdaos;

import com.revature.bankpojos.BankUser;

import junit.framework.TestCase;

public class UserDAOconnectTest extends TestCase {

	public void testUserDAOconnect() {
		assertNull(new UserDAOconnect().addUser(new BankUser()));
		assertNull(new UserDAOconnect().addUser(new BankUser(" "," "," ")));
		assertNotNull(new UserDAOconnect().addUser(new BankUser(1,"DEFAULT","DEFAULT@chiru.no", "4c51ff40a043ceed97a72d6408147399ebabf82a335f855164dd3b95e8139529",(short)2)));
		
		assertFalse(new UserDAOconnect().checkUserByName(new BankUser().getUserName(), new BankUser().getUserEmail()));
		assertTrue(new UserDAOconnect().checkUserByName("DEFAULT","DEFAULT@chiru.no"));
		
		assertNotNull(new UserDAOconnect().getAllUsers());
		
		assertFalse(new UserDAOconnect().getUserById(0));
		assertTrue(new UserDAOconnect().getUserById(1));
		
		assertFalse(new UserDAOconnect().updateUser(new BankUser()));
		assertTrue(new UserDAOconnect().updateUser(new BankUser(1,"DEFAULT","DEFAULT@chiru.no", "4c51ff40a043ceed97a72d6408147399ebabf82a335f855164dd3b95e8139529",(short)2)));
	}

}
