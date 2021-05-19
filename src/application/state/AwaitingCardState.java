package application.state;

import application.ui.Controller;

public class AwaitingCardState implements State {
	private Controller controller;
	
	public AwaitingCardState(Controller controller) {
		this.controller = controller;
		this.controller.lblInstruction.setText("Please Insert Card");
		this.controller.lblInput.setText("");
		this.controller.clearOptions();
		this.controller.enteredPin = "";
		this.controller.enableCards();
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
