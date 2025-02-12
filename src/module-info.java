module hw2 {
	requires java.sql;
	requires javafx.graphics;
	requires javafx.controls;
	
	opens application to javafx.graphics, javafx.fxml;
	exports application;
}