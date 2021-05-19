package application.state;

import application.ui.Controller;

public class EnterPinState implements State {
	private Controller controller;
	private final static int pinLength = 4;
	
	public EnterPinState(Controller controller) {
		this.controller = controller;
		this.controller.disableCards();
		this.controller.lblInstruction.setText("Please Enter Pin");
		this.controller.lblInput.setText("_ _ _ _");
		this.controller.clearOptions();
	}

	@Override
	public void numberPressed(int number) {
		if (controller.enteredPin.length() < pinLength) {
			controller.enteredPin += number;
			String pinInput = controller.lblInput.getText();
			pinInput = pinInput.replaceFirst("_", "*");
			controller.lblInput.setText(pinInput);
		}
	}

	@Override
	public void cancelPressed() {		
		controller.lblInstruction.setText("Ejecting Card...");
		controller.ejectCard();
		controller.changeState(new AwaitingCardState(controller));
	}

	@Override
	public void backPressed() {
		String enteredPin = controller.enteredPin;
		if (enteredPin.length() > 0) {
			controller.enteredPin = enteredPin.substring(0, enteredPin.length() - 1);
			String pinInput = controller.lblInput.getText();
			int lastAsterisk = pinInput.lastIndexOf("*");
			String newPinInput = pinInput.substring(0, lastAsterisk);
			newPinInput += "_";
			newPinInput += pinInput.substring(lastAsterisk + 1, pinInput.length());
			controller.lblInput.setText(newPinInput);
		}

	}

	@Override
	public void acceptPressed() {
		String enteredPin = controller.enteredPin;
		String accountPin = controller.insertedCard.getPin();
		System.out.println("Entered pin: " + enteredPin);
		System.out.println("Acc pin: " + accountPin);
		if (enteredPin.equals(accountPin)) {
			this.controller.changeState(new PinVerifiedState(controller));
		} else {
			this.controller.lblInstruction.setText("Incorrect Pin!");
			this.controller.lblInput.setText("_ _ _ _");
			this.controller.enteredPin = "";
		}
	}

	@Override
	public void optionTopLeft() {
	
	}

	@Override
	public void optionBottomLeft() {

	}

	@Override
	public void optionTopRight() {

	}

	@Override
	public void optionBottomRight() {

	}

}
