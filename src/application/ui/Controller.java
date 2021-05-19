package application.ui;

import java.net.URL;
import java.util.ResourceBundle;

import application.db.AccountDb;
import application.db.BankDb;
import application.model.Account;
import application.model.Bank;
import application.state.AwaitingCardState;
import application.state.EnterPinState;
import application.state.State;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Controller implements Initializable {
	@FXML
	public TabPane tabs;
	@FXML
	public ComboBox<Bank> bankCombo;
	@FXML
	public Label lblInstruction, lblError, lblInput, lblOptionTL, lblOptionBL, lblOptionTR, lblOptionBR;
	@FXML
	public TextField txtFirstName, txtLastName;
	@FXML
	public ImageView imgBank;
	@FXML
	public GridPane gpCards;
	public Account insertedCard;
	public String enteredPin = "";
	public String transactionAmount = "0";
	private State state;
	
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	this.state = new AwaitingCardState(this);
    	
		for (Bank bank: BankDb.getBanks()) {
			bankCombo.getItems().add(bank);
		}
    	
		tabs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
				if (newTab.getText().equals("Cash Machine")) {
					loadAccounts();
				}
			}
		});
    	
        System.out.println("View is now loaded!");
    }
    
    public void onBankComboChange() {
    	Bank selected = bankCombo.getValue();
    	if (selected == null) {
    		imgBank.setImage(null);
    	} else {
    		Image image = new Image(getClass().getResourceAsStream("/images/" + selected.getName() + ".png"));
        	imgBank.setImage(image);
    	}
    }
    
    public void registrationClicked() {
    	String firstName = txtFirstName.getText();
    	String lastName = txtLastName.getText();
    	Bank selected = bankCombo.getValue();
    	
    	if (bankCombo.getSelectionModel().isEmpty()) {
    		lblError.setText("Please select a bank.");
    	} else if (firstName.length() <= 2 || lastName.length() <= 2) {
    		lblError.setText("Please complete both first name and last name fields.");
    	} else {
    		Account account = new Account(firstName, lastName, selected);
    		account = AccountDb.addAccount(account);
    		if (account != null) {
    			Alert alert = new Alert(AlertType.INFORMATION);
    			alert.setTitle("Account Added");
    			alert.setHeaderText("Account added!");
    			alert.setContentText("Account number: " + account.getAccountNumber() + "\nPin: " + account.getPin());

    			alert.showAndWait();
    			lblError.setText("");
    			txtFirstName.setText("");
    			txtLastName.setText("");
    			bankCombo.getSelectionModel().clearSelection();
    		} else {
    			lblError.setText("Error creating account...");
    		}
    	}
    }
    
    private void loadAccounts() {
    	gpCards.getChildren().clear();
    	for (Account account: AccountDb.getAccounts()) {
    		
    		Label label = new Label(account.toString());
    		label.setMaxWidth(Double.MAX_VALUE);
    		label.setAlignment(Pos.CENTER);
    		gpCards.add(label, 0, gpCards.getRowCount(), 1, 1);
    		
    		Button button = new Button("Insert");
    		button.setMaxWidth(Double.MAX_VALUE);
    		button.setOnAction(value ->  {
    			insertCard(account);
    		});

    		gpCards.add(button, 1, gpCards.getRowCount() - 1, 1, 1); // -1 as new row was added above!
    	}
    }
    
    public void disableCards() {
    	gpCards.setDisable(true);
    }
    
    public void enableCards() {
    	gpCards.setDisable(false);
    }
    
    public void insertCard(Account account) {
    	insertedCard = account;
    	this.state = new EnterPinState(this);
    }
    
    public void ejectCard() {
    	this.insertedCard = null;
    	Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Card Ejected");
		alert.setHeaderText("Card has been ejected!");
		alert.setContentText(" Please make sure you have removed your card.");
		alert.showAndWait();
    }
    
    public void clearOptions() {
    	this.lblOptionTL.setText("");
    	this.lblOptionBL.setText("");
    	this.lblOptionTR.setText("");
    	this.lblOptionBR.setText("");
    }
    
    @FXML
    public void numberPressed(ActionEvent event) {
    	Button clickedButton = (Button)event.getSource();
    	int value = Integer.parseInt(clickedButton.getText());
    	state.numberPressed(value);
    }
    
    @FXML
    public void cancelPressed(ActionEvent event) {
    	state.cancelPressed();
    }
    
    @FXML
    public void backPressed(ActionEvent event) {
    	state.backPressed();
    }
    
    @FXML
    public void acceptPressed(ActionEvent event) {
    	state.acceptPressed();
    }
    
    @FXML
    public void optionTopLeft(ActionEvent event) {
    	state.optionTopLeft();
    }
    
    @FXML
    public void optionBottomLeft(ActionEvent event) {
    	state.optionBottomLeft();
    }
    
    @FXML
    public void optionTopRight(ActionEvent event) {
    	state.optionTopRight();
    }
    
    @FXML
    public void optionBottomRight(ActionEvent event) {
    	state.optionBottomRight();
    }
    
    public void changeState(State state) {
    	this.state = state;
    }
}
