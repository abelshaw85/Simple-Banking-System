package application.state;

import java.math.BigDecimal;

import application.db.AccountDb;
import application.ui.Controller;

public class WithdrawMoneyState extends AbstractManageMoneyState {
	
	public WithdrawMoneyState(Controller controller) {
		super(controller);
		this.controller.lblInstruction.setText("How Much to Withdraw?");
	}

	@Override
	public void acceptPressed() {
		BigDecimal amountToWithdraw = new BigDecimal(controller.transactionAmount);
		if (amountToWithdraw == BigDecimal.ZERO) {
			controller.lblInstruction.setText("Must Enter a Value");
		} else {
			boolean success = AccountDb.withdraw(controller.insertedCard, amountToWithdraw);
			if (success) {
				controller.lblInstruction.setText("Money Withdrawn.");
			} else {
				controller.lblInstruction.setText("Insufficient Funds.");
			}
		}
		controller.transactionAmount = "0";
		this.setInputLabel();
	}

}
