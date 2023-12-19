package org.example.app.JavaFx.Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {


	public static String findString;

	@Override
	public void start(Stage primaryStage) throws IOException {
		try {
			Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/hello-view.fxml")));

			Scene scene = new Scene(root);
			scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fxml/hello-view.css")).toExternalForm());
			primaryStage.setTitle("NFT Page Menu");
			primaryStage.setScene(scene); // Sử dụng Scene đã tạo
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	public void initialize() throws IOException {System.out.println("Controller initialized!");}

	public static void main(String[] args) {
		launch(args);
	}
}
