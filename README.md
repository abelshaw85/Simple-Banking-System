# Simple Banking System
The Banking System is a simple JavaFX application to simulate the generation of bank cards (such as credit/debit cards), their PIN codes, and card numbers.

## About Card Numbers ##
A bank card is broken down as follows:

[Example Card](readme-images/example-card.png)

- **System Number**: This identifies the type of card we are using. The most common numbers are 4 (Visa) and 5 (Mastercard).
	
- **IIN (Issuer Identification Number)**: The number used to identify the issuing bank. The System Number makes up part of this value.
	
- **Account Number**: The number used to identify the owner of the card. Will be unique for each bank.
	
- **Checksum**: A value generated using Luhn Algorithm based on all the previous numbers. Ensures the validity of the card number.
	
For this banking system, we have 2 fictional banks, each with their own IIN. When cards are created for either bank, the starting card number will be the IIN for that bank. Account numbers are randomly generated upon creation, with a checksum value also being generated based on the IIN and the account number.
  
## How to Use ##
When the app is opened, you will be presented with an account registration screen:

[Start of Application](readme-images/start.png)

Enter a username and password and select a bank. Once you click register, you will be shown your account number and randomly generated PIN. You should keep a note of this PIN to access the other features on the app.

[Card Added](readme-images/progress.png)

If you visit the 'Cash Machine' tab, you will be presented with an ATM interface and a list of your generated cards. Select the 'insert' button to simulate the insertion of your card, and then enter the PIN for that card when prompted. Confirmation of the PIN entry is performed by pressing the green CONFIRM button.

[Cash Machine](readme-images/added.png)

Once inserted, the interface responds in a similar fashion to an ATM. When options appear, pressing the button to the side of that option will trigger that action.
Actions that can be performed:

- Depositing/Withdrawing money. Note you will need to have that amount in the account to be able to withdraw it. All accounts start with £0.00.
	
- Check account balance.
	
- Eject the card.

## Frequently Asked Questions ##
### Q: Can I use this to generate free money? ###
No, the app is only pretending to create bank accounts. Sorry!
### Q: Where are the card details stored? ###
The app uses a <b>local</b> database to store your card details. This will be saved in a 'bankdb' folder. As such, any details you use (such as name) should be safe.
### Q: I forgot my pin! How can I access my card? ###
If you ran the program via command line, then you will be shown the PIN value when you enter an incorrect PIN.
