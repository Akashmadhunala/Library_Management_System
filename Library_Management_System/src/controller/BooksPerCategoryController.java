package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import service.ReportService;
import javafx.util.Pair;

public class BooksPerCategoryController {

    @FXML private TableView<Pair<String, Long>> categoryTable;
    @FXML private TableColumn<Pair<String, Long>, String> colCategory;
    @FXML private TableColumn<Pair<String, Long>, Long> colCount;

    private ReportService reportService = new ReportService();

    public void initialize() {
        colCategory.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getKey()));
        colCount.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(data.getValue().getValue()).asObject());

        categoryTable.getItems().setAll(reportService.getBooksCountPerCategory());
    }
}
