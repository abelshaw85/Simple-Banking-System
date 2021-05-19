package application.state;

import java.math.BigDecimal;

import application.db.AccountDb;
import application.ui.Controller;

public class DepositMoneyState extends AbstractManageMoneyState {
	
	public DepositMoneyState(Controller controller) {
		super(controller);
		this.controller.lblInstruction.setText("How Much to Deposit?");
	}

	@Override
	public void acceptPressed() {
		System.out.println("Accept pressed!");
		BigDecimal amountToWithdraw = new BigDecimal(controller.transactionAmount);
		if (amountToWithdraw == BigDecimal.ZERO) {
			controller.lblInstruction.setText("Must Enter a Value");
		} else {
			boolean success = AccountDb.deposit(controller.insertedCard, amountToWithdraw);
			if (success) {
				controller.lblInstruction.setText("Money Deposited.");
			} else {
				controller.lblInstruction.setText("Error Depositing Funds.");
			}
		}
		controller.transactionAmount = "0";
		this.setInputLabel();
	}

}
