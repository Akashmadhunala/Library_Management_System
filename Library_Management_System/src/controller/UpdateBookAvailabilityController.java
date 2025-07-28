package controller;

import dao.bookDao;
import domain.AvailabilityStatus;
import domain.Book;
import exceptions.DatabaseException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.BookService;

public class UpdateBookAvailabilityController {

    @FXML private TextField idField;
    @FXML private TextField availabilityField;

    private final BookService bookService = new BookService(new bookDao());
    private int bookId; 

    public void setBookData(Book book) {
        this.bookId = book.getBookId();
        idField.setText(String.valueOf(book.getBookId()));
        idField.setEditable(false); 
        availabilityField.setText(book.getAvailability().name());
    }

    @FXML
    private void handleUpdateBook() {
        try {
            String availText = availabilityField.getText();
            if (availText == null || availText.trim().isEmpty()) {
                showAlert("Error", "Please enter availability (A or I).");
                return;
            }

            AvailabilityStatus availability;
            try {
                availability = AvailabilityStatus.valueOf(availText.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                showAlert("Error", "Invalid availability status. Use only A or I.");
                return;
            }

            if (!bookService.bookExists(bookId)) {
                showAlert("Book Not Found", "No book found with ID: " + bookId);
                return;
            }

            bookService.updateBookAvailability(bookId, availability);

            showAlert("Success", "Book availability updated successfully.");
            handleBack();

        } catch (DatabaseException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while updating.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unexpected error: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/ViewBooks.fxml"));
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
