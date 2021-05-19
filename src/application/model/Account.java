package application.model;

import java.math.BigDecimal;

public class Account {
	private String accountNumber;
	private String firstName;
	private String lastName;
	private BigDecimal amount = new BigDecimal(0);
	private Bank bank;
	private int checksum;
	private String pin;
	
	public Account(String firstName, String lastName, Bank bank) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.bank = bank;
	}
	
	public Account(String accountNumber, String firstName, String lastName, Bank bank) {
		this(firstName, lastName, bank);
		this.accountNumber = accountNumber;
	}
	
	public Account(String accountNumber, String firstName, String lastName, BigDecimal amount, Bank bank, int checksum,
			String pin) {
		this(accountNumber, firstName, lastName, bank);
		this.amount = amount;
		this.checksum = checksum;
		this.pin = pin;
	}

	public void deposit(BigDecimal amount) {
		this.amount = this.amount.add(amount);
	}
	
	public BigDecimal withdraw(BigDecimal amount) {
		// Account has insufficient funds
		if (this.amount.compareTo(amount) < 0) {
			return new BigDecimal(-1);
		}
		this.amount = amount.subtract(amount);
		return amount;
	}

	public String getAccountNumber() {
		return accountNumber;
	}
	
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public Bank getBank() {
		return bank;
	}

	public int getChecksum() {
		return checksum;
	}

	public void setChecksum(int checksum) {
		this.checksum = checksum;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	@Override
	public String toString() {
		return bank.getIin() + this.accountNumber + this.checksum;
	}
	
	
}
