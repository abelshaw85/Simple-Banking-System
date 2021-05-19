package application.model;

public class Bank {
	private String iin;
	private String name;
	
	public Bank(String iin, String name) {
		this.iin = iin;
		this.name = name;
	}

	public String getIin() {
		return iin;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	
}
