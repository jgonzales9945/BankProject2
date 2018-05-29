package com.revature.bankpojos;

import junit.framework.TestCase;

public class BankAccountTest extends TestCase {

	public void testGetAmount() {
		assertEquals(2.50, new BankAccount(1,1,2.5,"checking").getAmount());
	}

}
