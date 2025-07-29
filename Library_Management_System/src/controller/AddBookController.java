package controller;

import dao.BookDao;
import domain.Book;
import domain.BookStatus;
import domain.AvailabilityStatus;
import exceptions.DatabaseException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.BookService;

public class AddBookController {

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField categoryField;
    @FXML private ComboBox<BookStatus> statusCombo;
    @FXML private ComboBox<AvailabilityStatus> availabilityCombo;

    private final BookService bookService = new BookService(new BookDao());

    @FXML
    public void initialize() {
        statusCombo.setItems(FXCollections.observableArrayList(BookStatus.values()));
        availabilityCombo.setItems(FXCollections.observableArrayList(AvailabilityStatus.values()));
    }

    @FXML
    private void handleAddBook() {
        try {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String category = categoryField.getText().trim();
            BookStatus status = statusCombo.getValue();
            AvailabilityStatus availability = availabilityCombo.getValue();

            if (title.isEmpty() || author.isEmpty() || category.isEmpty() || status == null || availability == null) {
                showAlert("Error", "Please fill all fields.");
                return;
            }

            Book book = new Book(title, author, category, status, availability);
            bookService.addBook(book);

            showAlert("Success", "Book added successfully.");
            clearFields();
        } catch (DatabaseException e) {
            showAlert("Database Error", e.getMessage());
        } catch (Exception e) {
            showAlert("Error", "Unexpected error: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/BookManagement.fxml"));
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        titleField.clear();
        authorField.clear();
        categoryField.clear();
        statusCombo.getSelectionModel().clearSelection();
        availabilityCombo.getSelectionModel().clearSelection();
    }
}
