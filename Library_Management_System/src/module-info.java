module LibraryManagementSystem {

    requires javafx.controls; 
    requires javafx.fxml;     
    requires javafx.graphics; 
    requires java.sql;
	requires javafx.base;
    opens controller to javafx.fxml;
    opens resources to javafx.fxml;
    opens domain to javafx.base;

    exports main;
    exports controller;

}