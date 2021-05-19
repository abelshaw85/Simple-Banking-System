package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import application.db.AccountDb;
import application.db.BankDb;
import application.db.DbManager;

public class Main extends Application {

	public static void main(String[] args) {
		DbManager.initialise();
		BankDb.initialise();
		AccountDb.initialise();
		
		launch(args);
		DbManager.shutdown();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
		primaryStage.setTitle("Banking Application");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
}
