package application.state;

import application.ui.Controller;

public class PinVerifiedState implements State {
	private Controller controller;
	
	public PinVerifiedState(Controller controller) {
		this.controller = controller;
		this.controller.lblInstruction.setText("Welcome to " + this.controller.insertedCard.getBank().getName() + "!");
		this.controller.lblInput.setText("");
		this.controller.clearOptions();
		this.controller.lblOptionTL.setText("Check Balance");
		this.controller.lblOptionBL.setText("Withdraw Money");
		this.controller.lblOptionTR.setText("Deposit Money");
		this.controller.lblOptionBR.setText("Eject Card");
	}

	@Override
	public void numberPressed(int number) {
		
	}

	@Override
	public void cancelPressed() {
		
	}

	@Override
	public void backPressed() {
		
	}

	@Override
	public void acceptPressed() {
		
	}

	@Override
	public void optionTopLeft() {
		// Check Balance
		this.controller.lblInput.setText("You have £" + this.controller.insertedCard.getAmount());
		
	}

	@Override
	public void optionBottomLeft() {
		//Withdraw Money
		this.controller.changeState(new WithdrawMoneyState(controller));
		
	}

	@Override
	public void optionTopRight() {
		// Deposit
		this.controller.changeState(new DepositMoneyState(controller));
		
	}

	@Override
	public void optionBottomRight() {
		this.controller.ejectCard();
		this.controller.changeState(new AwaitingCardState(controller));
	}

}
