package org.allthegoodstuff.ebaytools.view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.allthegoodstuff.ebaytools.EBayToolsMain;
import org.allthegoodstuff.ebaytools.model.SaleItem;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SaleItemsViewController {
    @FXML
    private TableView<SaleItem> saleItemTable;

    @FXML
    private TableColumn<SaleItem, Integer> itemNumberColumn;
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



    // Reference to the main application.
    private EBayToolsMain mainApp;
}
