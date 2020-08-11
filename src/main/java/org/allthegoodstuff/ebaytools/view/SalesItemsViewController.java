package org.allthegoodstuff.ebaytools.view;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
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
     * The data as an observable list of sale items.
     */
    private final ObservableList<SaleItem> saleItemData = FXCollections.observableArrayList();

    private Database db;

    RootLayoutController rootLayoutController;

    // add tooltip popups to table cells
    private <T> void addTooltipToColumnCells(TableColumn<SaleItem,T> column) {

        Callback<TableColumn<SaleItem, T>, TableCell<SaleItem,T>> existingCellFactory
                = column.getCellFactory();

        column.setCellFactory(c -> {
            TableCell<SaleItem, T> cell = existingCellFactory.call(c);

            final Tooltip tooltip = new Tooltip();
            // can use arbitrary binding here to make text depend on cell
            // in any way you need:
            tooltip.textProperty().bind(cell.itemProperty().asString());

            // disallow 'null' text tooltip for empty cells
            cell.tooltipProperty().bind(Bindings.when(Bindings.or(cell.emptyProperty(), cell.itemProperty()
                    .isNull())).then((Tooltip) null).otherwise(tooltip));
            return cell ;
        });
    }

    @FXML
    private void initialize() {
        itemIDColumn.setCellValueFactory(celldata -> celldata.getValue().itemIDProperty());
        titleColumn.setCellValueFactory(celldata -> celldata.getValue().titleProperty());
        priceColumn.setCellValueFactory(celldata -> Bindings.format("%.2f", celldata.getValue().priceProperty()));
        startTimeColumn.setCellValueFactory(celldata -> celldata.getValue().startTimeProperty());
        endTimeColumn.setCellValueFactory(celldata -> celldata.getValue().endTimeProperty());
        sellerColumn.setCellValueFactory(celldata -> celldata.getValue().sellerInfoProperty());

        saleItemTable.setItems(saleItemData);

        // show tooltips for cell items
        saleItemTable.getColumns().forEach(this::addTooltipToColumnCells);

        // add action to display selected row in browser
        saleItemTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                rootLayoutController.showItemBrowserPage(newSelection.getItemID());
            }
        } );

        // context menu popup binding and actions
        saleItemTable.setRowFactory(new Callback<TableView<SaleItem>, TableRow<SaleItem>>() {
            @Override
            public TableRow<SaleItem> call(TableView<SaleItem> tableView) {
                final TableRow<SaleItem> row = new TableRow<>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem removeMenuItem = new MenuItem("Remove");

                //todo: pop up confirmation with checkbox option and hook up to preferences to remember choice
                // also when confirmed, not only row removed but data in db removed
                removeMenuItem.setOnAction(event -> saleItemTable.getItems().remove(row.getItem()));
                contextMenu.getItems().add(removeMenuItem);
                // Set context menu on row, but use a binding to make it only show for non-empty rows:
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu)null)
                                .otherwise(contextMenu)
                );
                return row ;
            }
        });
    }

    public void addItemToSalesList(SaleItem saleItem) {
        saleItemData.add(saleItem);
        //todo: action to enter into database should be actually done on 'add to watchlist' button action event
        db.insertSaleItemRow(saleItem);
    }

    public boolean itemExists (String itemID) {
        for (SaleItem item : saleItemData) {
            if ( item.getItemID().equals(itemID))
                return true;
        }

        return false;
    }

    public void setDatabaseAccess(Database db) {
        this.db = db;

        saleItemData.addAll( db.getAllSalesItemRows() );
    }

    public void setRootLayoutControllerAccess(RootLayoutController rootLayoutController) {
        this.rootLayoutController = rootLayoutController;
    }

}