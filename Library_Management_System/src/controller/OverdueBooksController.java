package controller;

import domain.IssueRecord;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import service.ReportService;

import java.util.List;

public class OverdueBooksController {

    @FXML private TableView<IssueRecord> overdueTable;
    @FXML private TableColumn<IssueRecord, Integer> colBookId;
    @FXML private TableColumn<IssueRecord, String> colTitle;
    @FXML private TableColumn<IssueRecord, String> colMember;
    @FXML private TableColumn<IssueRecord, String> colReturnDate;

    private ReportService reportService = new ReportService();

    public void initialize() {
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colMember.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        List<IssueRecord> list = reportService.getOverdueBooks();
        overdueTable.getItems().setAll(list);
    }
}
