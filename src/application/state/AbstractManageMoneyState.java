package application.state;

import application.ui.Controller;

public abstract class AbstractManageMoneyState implements State {
	protected Controller controller;
	protected final static int MAX_DIGITS = 3;

	public AbstractManageMoneyState(Controller controller) {
		this.controller = controller;
		this.controller.lblInput.setText("£ _ _ 0");
		this.controller.clearOptions();
		this.controller.lblOptionTL.setText("£60");
		this.controller.lblOptionBL.setText("£40");
		this.controller.lblOptionTR.setText("£20");
		this.controller.lblOptionBR.setText("Back");
	}

	@Override
	public void numberPressed(int number) {
		if ((number != 0 || this.controller.transactionAmount.length() > 1) && this.controller.transactionAmount.length() < MAX_DIGITS) {
			String amountToWithdraw = this.controller.transactionAmount;
			amountToWithdraw = amountToWithdraw.substring(0, amountToWithdraw.length() - 1) + number + 0;
			this.controller.transactionAmount = amountToWithdraw;
			
			setInputLabel();
		}
	}

	@Override
	public void cancelPressed() {
		this.controller.changeState(new PinVerifiedState(controller));
	}

	@Override
	public void backPressed() {
		String amountToWithdraw = controller.transactionAmount;
		if (amountToWithdraw.length() > 1) {
			
			controller.transactionAmount = amountToWithdraw.substring(0, amountToWithdraw.length() - 2) + "0";
			
			setInputLabel();
		}
	}
	
	protected void setInputLabel() {
		String amountToWithdraw = this.controller.transactionAmount;
		
		int numberOfRequiredUnderscores = MAX_DIGITS - amountToWithdraw.length();
		String newAmountInput = "£ ";
		for (int i = 0; i < numberOfRequiredUnderscores; i++) {
			newAmountInput += "_ ";
		}
		for (int i = 0; i < amountToWithdraw.length(); i++) {
			newAmountInput += amountToWithdraw.charAt(i);
			if (i < amountToWithdraw.length() - 1) {
				newAmountInput += " ";
			}
		}
		controller.lblInput.setText(newAmountInput);
	}

	@Override
	public void optionTopLeft() {
		// 60
		this.controller.transactionAmount = "60";
		this.setInputLabel();
	}

	@Override
	public void optionBottomLeft() {
		// 40
		this.controller.transactionAmount = "40";
		this.setInputLabel();
	}

	@Override
	public void optionTopRight() {
		// 20
		this.controller.transactionAmount = "20";
		this.setInputLabel();
	}

	@Override
	public void optionBottomRight() {
		this.controller.changeState(new PinVerifiedState(controller));
	}

	@Override
	public abstract void acceptPressed();
}
