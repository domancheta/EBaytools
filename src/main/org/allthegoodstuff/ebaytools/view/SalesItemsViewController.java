package org.allthegoodstuff.ebaytools.view;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.allthegoodstuff.ebaytools.EBayToolsMain;
import org.allthegoodstuff.ebaytools.model.SaleItem;

import java.time.LocalDate;

public class SalesItemsViewController {
    @FXML
    private TableView<SaleItem> saleItemTable;

    @FXML
    private TableColumn<SaleItem, Number> itemIDColumn;
    @FXML
    private TableColumn<SaleItem, String> titleColumn;
    @FXML
    private TableColumn<SaleItem, String> priceColumn;
    @FXML
    private TableColumn<SaleItem, LocalDate> startTimeColumn;
    @FXML
    private TableColumn<SaleItem, LocalDate> endTimeColumn;
    @FXML
    private TableColumn<SaleItem, String> sellerColumn;

    @FXML
    private void initialize() {
       itemIDColumn.setCellValueFactory(celldata -> celldata.getValue().itemIDProperty());
       titleColumn.setCellValueFactory(celldata -> celldata.getValue().titleProperty());
       priceColumn.setCellValueFactory(celldata -> Bindings.format("%.2f", celldata.getValue().priceProperty()));
       startTimeColumn.setCellValueFactory(celldata -> celldata.getValue().startTimeProperty());
       endTimeColumn.setCellValueFactory(celldata -> celldata.getValue().endTimeProperty());
       sellerColumn.setCellValueFactory(celldata -> celldata.getValue().sellerInfoProperty());
    }

    private EBayToolsMain mainApp;

    public void setMainApp(EBayToolsMain mainApp) {
        this.mainApp = mainApp;
        saleItemTable.setItems(mainApp.getSalesData());
    }
}