package org.allthegoodstuff.ebaytools.view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.allthegoodstuff.ebaytools.EBayToolsMain;
import org.allthegoodstuff.ebaytools.model.SaleItem;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SaleItemsViewController {
    @FXML
    private TableView<SaleItem> saleItemTable;

    @FXML
    private TableColumn<SaleItem, Integer> itemIDColumn;
    @FXML
    private TableColumn<SaleItem, String> itemTitleColumn;
    @FXML
    private TableColumn<SaleItem, BigDecimal> priceColumn;
    @FXML
    private TableColumn<SaleItem, LocalDate> startTimeColumn;
    @FXML
    private TableColumn<SaleItem, LocalDate> endTimeColumn;
    @FXML
    private TableColumn<SaleItem, String> sellerColumn;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // TODO: put this in 'root' control
    @FXML
    public void closeEventHandler(ActionEvent e) {
        stage.close();
    }

    // Reference to the main application.
    private EBayToolsMain mainApp;
}
