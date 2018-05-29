/**
 * @author Joseph Gonzales
 */
package com.revature.bankpackage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.revature.bankpojos.*;
import com.revature.bankdaos.*;

public class StartingClass {
	
	//one or more words with/out numbering or symbols are accepted(denoted by \\w), then one @, one or more words and any extra dots, then one last dot followed by 2-6 letters
	//this should support domains with .com/.org and domains like .co.uk/.co.ca
	private final String PATTERN = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,6})$";
	//need global access, nullpointer exceptions are a pain
	private BankUser user;
	private ArrayList<BankAccount> bac;
		
	public static void main(String[] args) {
		//instantiate the class
		StartingClass c = new StartingClass();
		System.out.println("Welcome to your personal bank deluxe edition!");
		//get out of the static method and let the class have execution
		c.beginBankInteraction();
	}

	private void beginBankInteraction() {
		
		Scanner scan = new Scanner(System.in);
		
		for(;;) {
			boolean t = bankLoginCreate(scan);
			//if bank login returns false, exit the for loop and quit out the program
			//account management doesn't need to return anything, it should just go back to login menu so the user can quit there
			if(t == true)accountManipulate(scan);
			else break;
		}
		//this messed up e v e r y t h i n g
		scan.close();
		
	}
	

	private boolean bankLoginCreate(Scanner scan) {
		//set up access on every iteration of the method, but not in the loop
		//looping this would be a performance issue, especially with failed login/create account attempts
		for(;;) {
			Boolean check = false;
			System.out.print("Choose an option:\n");
			System.out.print("1) Sign in to existing account\n2) Create a new account\n3) Exit the program\n");
			//lesson for using scanner, if using sysin multiple times, only use readLine() to avoid overflow
			switch(scan.nextLine()) {
			case "1":
				//System.out.println("login to existing account...");
				check = login(this.user, scan);
				break;
				
			case "2":
				//System.out.println("creating new user...");
				check = createAccout(this.user, scan);
				break;
				
			case "3":
				//close the program
				System.out.println("Thank you for using this program, now exiting");
				return false;
			default:
				System.out.println("Invalid option");
				break;
			}
			//login or create a new user result in true, return and go to the account method
			//case 3 should be the only one to return false, ending the program
			if(check) return true;
		}
		
	}
	
	private boolean createAccout(BankUser user, Scanner scan) {
		Pattern pt = Pattern.compile(PATTERN, Pattern.CASE_INSENSITIVE);
		boolean flag = true;
		//System.out.println("Are we there yet?");//debug
		String name=null, email=null, password=null;

		System.out.print("Enter your prefered name: ");
		name = scan.nextLine();
		System.out.print("\nEnter your email address: ");
		email = scan.nextLine();
		System.out.print("\nEnter your password: ");
		password = scan.nextLine();
		System.out.print("\nRe-enter your password again: ");
		//password re-input does not match the previous password
		String ps = scan.nextLine();
		if(!password.equals(ps)) {
			System.out.println("Password does not match the re-entered password.");
			flag = false;
		}
		if(password.length() < 6 || password.length() > 16) {
			System.out.println("Your password should be between 6-16 characters long.\nPlease use numbers and/or symbols to enhance security");
			flag = false;
		}
		//sc.close();
		//email pattern is invalid, don't use this
		Matcher matcher = pt.matcher(email);
		if(!matcher.matches()) {
			System.out.println("Malformatted e-mail entered. Try again");
			flag = false;
		}
		//nothing was entered, can't be null
		if(name == null || name == password) {
			System.out.println("Invalid user name entered.");
			flag = false;
		}
		//everything looks all right, go ahead and enter the new user into the bankuser
		else {
			//System.out.println(this.user.toString());//debug
			if(new UserDAOconnect().checkUserByName(name, email)) {
				System.out.println("That username or email address is already in use, consider using a new one instead.");
				flag = false;
			}
			else {
				//hash the password, the bank administrator doesn't need to know peoples passwords
				String s = SecurePassword(password);
				this.user = new UserDAOconnect().addUser(new BankUser(name,email,s));
				bac.add(new AccountDAOconnect().addBankAccount(this.user, "checking"));
				
			}
		}
		//System.out.println("Are we out?");
		//account creation successful
		return flag;
	}
	
	private boolean login(BankUser user, Scanner scan) {
		String email, password;

		System.out.print("\nEnter your username or email address: ");
		email = scan.nextLine();
		System.out.print("\nEnter your password: ");
		password = scan.nextLine();
		
		//System.out.println(file.getPath());//debug
		//put in the hash version of the password for proper verification between the hashes
		this.user = new UserDAOconnect().getUserByName(email, SecurePassword(password));
		
		if(this.user == null) {
			System.out.println("Invalid credentials. Try again later");
			return false;
		}
		//login successful
		bac = new AccountDAOconnect().getAllAccountsByUser(this.user);
		return true;
		
	}
	

	private void accountManipulate(Scanner scan) {
		System.out.println("Now loading account information...");
		System.out.printf("Welcome back %s!\n\nPlease note, your transactions are temporary until you accept them or logout\n", this.user.getUserName());
		for(;;) {
			//Scanner scn = new Scanner(System.in);
			System.out.printf("Choose an option:\n");
			System.out.print("1) Inquery balances from accounts\n2) Withdraw money\n3) Deposit or pay loan balance\n4) Create an account\n5) Save your transactions(use this after depositing/withdrawing!)\n6) Save and Logout of account\n");
			//scan.reset();
			switch(scan.nextLine()) {
			case "1":
				//printf to format the double for decimal point
				getBalances();
				break;
				
			case "2":
				if(bac.size() > 1) {
					int i = 0, j = -1;
					System.out.println("Which account would you like to withdraw from?");
					for(BankAccount c : bac) System.out.println((j++)+") Account #"+ c.getAccountID() + " " + c.getAccountType());
					try {
				          i = Integer.parseInt(scan.nextLine());
				    } catch (Exception e) {
				          System.out.println("Couldn't parse input, please try again");
				    }
					withdraw(scan, bac.get(i), 0);
				}
				else {
					withdraw(scan, bac.get(0), 0);
				}
				break;
				
			case "3":
				if(bac.size() > 1) {
					int i = 0, j = -1;
					System.out.println("Which account would you like to withdraw from?");
					for(BankAccount c : bac) System.out.println((j++)+") Account #"+ c.getAccountID() + " " + c.getAccountType());
					try {
				          i = Integer.parseInt(scan.nextLine());
				    } catch (Exception e) {
				          System.out.println("Couldn't parse input, please try again");
				    }
					deposit(scan, bac.get(i));
				}
				else {
					deposit(scan, bac.get(0));
				}
				break;
				
			case "4":
				System.out.println("What kind of account would you like to create?\n1) Checking\n2) Savings\n3) Loan (loans must be between $100-2000)\nAny other input will exit this menu");
				switch(scan.nextLine()) {
				case "1"://checking account
					bac.add(new AccountDAOconnect().addBankAccount(this.user, "checking"));
					System.out.println("New Checking account created!");
					break;
				case "2":
					bac.add(new AccountDAOconnect().addBankAccount(this.user, "savings"));
					System.out.println("New Savings account created!");
					break;
				case "3":
					loan(scan);
					
					break;
				}
				break;
			case "5":
				transferFunds(scan);
				break;
			case "6":
				//do the clean up
				System.out.println("Logging out of your Account...");
				for(BankAccount b : bac) new AccountDAOconnect().updateBalance(b);
				//this.user = null;//needed?
				System.out.println("Saving your settings...");
				new UserDAOconnect().updateUser(user);
				new AccountDAOconnect().transact();
				return;
			default:
				System.out.println("Invalid choice");
				break;
			}
		}
	}
	
	private void getBalances() {
		for(BankAccount ac : bac)System.out.printf("1) Current balance for account %d: $%.2f\n", ac.getAccountID(), ac.getAmount());
		
	}

	private void transferFunds(Scanner scan) {
		int tacc = 0, pacc = 0;
		double k = 0;
		System.out.println("What account would you like to transfer to?\nSpecifying one of your own account numbers can be used\nBe aware that incorrect account number may result in lost funds");
		try {
			tacc = Integer.parseInt(scan.nextLine());
			System.out.println("Which account would you like to transfer from?");
			getBalances();
			pacc = Integer.parseInt(scan.nextLine());
			if(bac.get(pacc).getAccountType().equals("loan")) {
				System.out.println("You can not transfer from a loan account!");
				return;
			}
			System.out.println("How much would you like to transfer? (Amount must be between $10 to $2000)");
			k = Double.parseDouble(scan.nextLine());
			//lets not give all our money away
			if(k > 2001 && k < 10) {
				System.out.println("You are borrowing an out of bounds amount! Transfer amounts must be between $10 to $2000!");
				return;
			}
			BankAccount foreign = new AccountDAOconnect().getAccountById(tacc);
			if(foreign != null) {
				//withdraw from specified account
				withdraw(scan, bac.get(pacc), k);
				System.out.println("Your funds have been taken out from account: "+ bac.get(pacc).getAccountID());
				//put money into specied account
				new AccountDAOconnect().transferToAccount(k, foreign.getAccountID());
				System.out.println("Funds have been successfully transfered!");
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("The account you tried to select does not exist.");
		} catch (Exception e) {
	        System.out.println("Couldn't parse input, please try again.");
	    }
		
	}

	private void loan(Scanner scan) {
		int pacc = 0;
		double k = 0;
		System.out.println("How much would you like to borrow?");
		try {
			k = Double.parseDouble(scan.nextLine());
			System.out.println("Which account would you like to transfer from?");
			getBalances();
			pacc = Integer.parseInt(scan.nextLine());
			if(bac.get(pacc).getAccountType().equals("loan")) {
				System.out.println("You can not transfer from a loan account!");
				return;
			}
			//lets not give all our money away
			if(k > 2001 && k < 100) {
				System.out.println("You are borrowing a out of bounds amount! We only loan from $100 up to $2000!");
				return;
			}
			bac.add(new AccountDAOconnect().addLoanAccount(this.user, "loan",k));
			System.out.println("Your funds have been deposited to account: "+ bac.get(pacc).getAccountID());
			//should always be the first account to transfer to
			bac.get(pacc).setAmount(k);
			//commit balance update
			new AccountDAOconnect().updateBalance(bac.get(pacc));
			System.out.println("Please remember to pay us back or we'll file you under delinquency!");
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("The account you tried to select does not exist");
		} catch (Exception e) {
	        System.out.println("Couldn't parse input, please try again");
	    }
	}
	
	private boolean withdraw(Scanner scan, BankAccount bankAccount, double i) {
		if(bankAccount.getAccountType().equals("Loan")) {
			System.out.println("Can not withdraw from a loan!");
			return false;
		}
		else if(i == 0.0) {
			System.out.print("Please enter an amount to withdraw from your account: ");
			//scan for double, do a nextline to prevent skips
			double wd = scan.nextDouble();
			scan.nextLine();
			
			//they can put a negative number, it will be set to positive to give the illusion that the symbol matters
			if(wd <= 0) wd = Math.abs(wd);
			else if(wd > bankAccount.getAmount()) {
				System.out.println("You can not withdraw more than you have stored!");
				return false;
			}
			else {
				bankAccount.setWithdrawBalance(wd);//set new balance
				new AccountDAOconnect().updateBalance(bankAccount);
			}
		}
		else {
			//they can put a negative number, it will be set to positive to give the illusion that the symbol matters
			if(i <= 0) i = Math.abs(i);
			else if(i > bankAccount.getAmount()) {
				System.out.println("You can not withdraw more than you have stored!");
				return false;
			}
			else {
				bankAccount.setWithdrawBalance(i);//set new balance
				new AccountDAOconnect().updateBalance(bankAccount);
			}
		}
		return true;
	}

	private void deposit(Scanner scan, BankAccount bankAccount) {
		System.out.print("Please enter an amount to deposit to your account: ");
		//scan for double, do a nextline to prevent skips
		double de = scan.nextDouble();
		scan.nextLine();
		//they can put a negative number, it will be set to positive to give the illusion that the symbol matters
		if(de <= 0.0) de = Math.abs(de);
		else {
			bankAccount.setDepositBalance(de);//set new balance
			new AccountDAOconnect().updateBalance(bankAccount);
		}
		
	}

	//secure the password for storage
	private static String SecurePassword(String passwordToHash)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //create the returning string for saving to the database
            StringBuilder sb = new StringBuilder();
            //append the produced byte array using hex conversion into the string builder
            for(byte b : bytes) sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
