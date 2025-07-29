package controller;

import dao.BookDao;
import domain.Book;
import domain.BookStatus;
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

public class UpdateBookController {

    @FXML private TextField idField;
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField categoryField;
    @FXML private ComboBox<BookStatus> statusCombo;

    private final BookService bookService = new BookService(new BookDao());
    private int bookId;

    @FXML
    public void initialize() {
        statusCombo.setItems(FXCollections.observableArrayList(BookStatus.values()));
    }

    public void setBookData(Book book) {
        this.bookId = book.getBookId();
        idField.setText(String.valueOf(book.getBookId()));
        idField.setEditable(false);
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        categoryField.setText(book.getCategory());
        statusCombo.setValue(book.getStatus());
    }

    @FXML
    private void handleUpdateBook() {
        try {
            if (bookId == 0 && !idField.getText().trim().isEmpty()) {
                bookId = Integer.parseInt(idField.getText().trim());
            }

            if (!bookService.bookExists(bookId)) {
                showAlert("Error", "No book found with ID: " + bookId);
                return;
            }

            Book updatedBook = new Book();
            updatedBook.setBookId(bookId);
            updatedBook.setTitle(titleField.getText().trim().isEmpty() ? null : titleField.getText().trim());
            updatedBook.setAuthor(authorField.getText().trim().isEmpty() ? null : authorField.getText().trim());
            updatedBook.setCategory(categoryField.getText().trim().isEmpty() ? null : categoryField.getText().trim());
            updatedBook.setStatus(statusCombo.getValue());

            bookService.updateBook(updatedBook);

            showAlert("Success", "Book updated successfully.");
            handleBack();
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
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
        alert.setContentText(message);
        alert.showAndWait();
    }
}
