package controller;

import dao.bookDao;
import domain.Book;
import domain.BookStatus;
import exceptions.DatabaseException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.BookService;

public class UpdateBookController {

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField categoryField;
    @FXML private TextField statusField;

    private final BookService bookService = new BookService(new bookDao());
    private int bookId;

    public void setBookData(Book book) {
        this.bookId = book.getBookId();
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        categoryField.setText(book.getCategory());
        statusField.setText(book.getStatus().name());
    }

    @FXML
    private void handleUpdateBook() {
        try {
            String title = titleField.getText();
            String author = authorField.getText();
            String category = categoryField.getText();
            String stat = statusField.getText();

            BookStatus status = null;
            if (stat != null && !stat.trim().isEmpty()) {
                try {
                    status = BookStatus.valueOf(stat.trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    showAlert("Validation Error", "Invalid status entered.");
                    return;
                }
            }

            bookService.updateBookDetails(bookId,
                    title != null && !title.trim().isEmpty() ? title.trim() : null,
                    author != null && !author.trim().isEmpty() ? author.trim() : null,
                    category != null && !category.trim().isEmpty() ? category.trim() : null,
                    status);

            showAlert("Success", "Book updated successfully.");
            handleBack();

        } catch (DatabaseException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to update book.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unexpected error occurred.");
        }
    }

    @FXML
    private void handleBack() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/ViewBooks.fxml"));
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
