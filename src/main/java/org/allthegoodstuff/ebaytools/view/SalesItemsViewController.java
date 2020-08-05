package org.allthegoodstuff.ebaytools.view;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.allthegoodstuff.ebaytools.db.Database;
import org.allthegoodstuff.ebaytools.model.SaleItem;

import java.time.LocalDateTime;

public class SalesItemsViewController {
    @FXML
    private TableView<SaleItem> saleItemTable;

    @FXML
    private TableColumn<SaleItem, String> itemIDColumn;
    @FXML
    private TableColumn<SaleItem, String> titleColumn;
    @FXML
    private TableColumn<SaleItem, String> priceColumn;
    @FXML
    private TableColumn<SaleItem, LocalDateTime> startTimeColumn;
    @FXML
    private TableColumn<SaleItem, LocalDateTime> endTimeColumn;
    @FXML
    private TableColumn<SaleItem, String> sellerColumn;

    /**
     * The data as an observable list of Persons.
     */
    private static ObservableList<SaleItem> saleItemData = FXCollections.observableArrayList();

    private static Database db;

    @FXML
    private void initialize() {
       itemIDColumn.setCellValueFactory(celldata -> celldata.getValue().itemIDProperty());
       titleColumn.setCellValueFactory(celldata -> celldata.getValue().titleProperty());
       priceColumn.setCellValueFactory(celldata -> Bindings.format("%.2f", celldata.getValue().priceProperty()));
       startTimeColumn.setCellValueFactory(celldata -> celldata.getValue().startTimeProperty());
       endTimeColumn.setCellValueFactory(celldata -> celldata.getValue().endTimeProperty());
       sellerColumn.setCellValueFactory(celldata -> celldata.getValue().sellerInfoProperty());

        // Add some sample data
//        addItemToSalesList(new SaleItem("12345", "ipod classic", "classic ipod",
//                "apple_lover", new BigDecimal("99.98"), LocalDateTime.now(), LocalDateTime.now()));
        saleItemTable.setItems(saleItemData);
    }

    public static void addItemToSalesList(SaleItem saleItem) {
        saleItemData.add(saleItem);
        //todo: action to enter into database should be actually done on 'add to watchlist' button action event
        db.insertSaleItemRow(saleItem);
    }

    public static boolean itemExists (String itemID) {
        for (SaleItem item : saleItemData) {
            if ( item.getItemID().equals(itemID))
                return true;
        }

        return false;
    }

    public void setDBAccess (Database db) {
        this.db = db;

        saleItemData.addAll( db.getAllSalesItemRows() );
    }
}