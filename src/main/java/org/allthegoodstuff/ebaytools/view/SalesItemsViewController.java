package org.allthegoodstuff.ebaytools.view;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.allthegoodstuff.ebaytools.EBayToolsMain;
import org.allthegoodstuff.ebaytools.db.Database;
import org.allthegoodstuff.ebaytools.model.SaleItem;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

public abstract class SalesItemsViewController {

    @FXML
    protected TableView<SaleItem> saleItemTable;

    // table columns
    @FXML
    protected TableColumn<SaleItem, SaleItem> itemSummaryColumn;

    @FXML
    protected TableColumn<SaleItem, String> itemIDColumn;
    @FXML
    protected TableColumn<SaleItem, String> titleColumn;
    @FXML
    protected TableColumn<SaleItem, String> priceColumn;
    @FXML
    protected TableColumn<SaleItem, String> bidsColumn;
    @FXML
    protected TableColumn<SaleItem, LocalDateTime> startTimeColumn;
    @FXML
    protected TableColumn<SaleItem, LocalDateTime> endTimeColumn;
    @FXML
    protected TableColumn<SaleItem, String> sellerColumn;
    @FXML
    protected TableColumn<SaleItem, String> imageColumn;

    /**
     * The data as an observable list of sale items.
     */
    protected final ObservableList<SaleItem> saleItemData = FXCollections.observableArrayList();

    protected Database db;

    RootLayoutController rootLayoutController;

    // boolean guard flag to ignore selection events when switching between watchlist views; don't refresh the browser page when switching views
    private boolean ignoreSelectionEvents = false;

    Preferences prefs = Preferences.userNodeForPackage(EBayToolsMain.class);

    @FXML
    public final void initialize() {

        // add action to display selected row in browser
        saleItemTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (ignoreSelectionEvents || newSelection == null) {
                return;
            }
            rootLayoutController.hideAddWatchlistButton();
            rootLayoutController.showItemBrowserPage(newSelection.getItemID());
            rootLayoutController.hideErrorPane();
        } );

        // sales item table row action bindings here
        saleItemTable.setRowFactory(new Callback<TableView<SaleItem>, TableRow<SaleItem>>() {

            private final String KEY_AUTO_REMOVE_ITEM = "KEY_AUTO_REMOVE_ITEM";

            @Override
            public TableRow<SaleItem> call(TableView<SaleItem> tableView) {
                final TableRow<SaleItem> row = new TableRow<>();
                final ContextMenu contextMenu = new ContextMenu();
                final String sRemoveFromWatchlist =  "Remove from watchlist";
                final MenuItem removeMenuItem = new MenuItem( sRemoveFromWatchlist );
                Alert alert = createAlertWithOptOut(Alert.AlertType.CONFIRMATION, sRemoveFromWatchlist,
                        null, "Are you sure you wish to remove this item from the watchlist?",
                        "Do not ask again",
                        param -> prefs.put(KEY_AUTO_REMOVE_ITEM, param ? "Always" : "Never"),
                        ButtonType.YES, ButtonType.NO);
                // todo: put this in the stylesheet file and retrieve somehow
                final String rightSelectedStyle = "-fx-border-color: deepskyblue; -fx-border-width: 2px; -fx-border-style: solid; -fx-border-radius: 8px";
                final String[] rowStyleBeforeRightClick = {""};

                row.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    if (event.getButton().equals(MouseButton.SECONDARY)) {
                        event.consume();
                    }
                });

                // Remove watchlist item from table and DB
                // Pop up confirmation menu item with checkbox option to not ask anymore
                //todo: add preference menu item with ability to enable confirm popup again
                removeMenuItem.setOnAction(event -> {
                    if (prefs.get(KEY_AUTO_REMOVE_ITEM, "").equals("Always")) {
                        saleItemTable.getItems().remove(row.getItem());
                        db.deleteSaleItemRow(row.getItem().getItemID());
                        return;
                    }

                    if (alert.showAndWait().filter(t -> t == ButtonType.YES).isPresent()) {
                        saleItemTable.getItems().remove(row.getItem());
                        db.deleteSaleItemRow(row.getItem().getItemID());
                    }
                    row.setStyle(rowStyleBeforeRightClick[0]);
                });

                contextMenu.getItems().add(removeMenuItem);

                // special styling for right-clicked rows
                contextMenu.setOnShowing(event -> {
                    rowStyleBeforeRightClick[0] = row.getStyle();
                    //row.getStyleClass().add("right-clicked-row");
                    row.setStyle(rightSelectedStyle);
                });

                contextMenu.setOnHidden(event -> {
                    if (prefs.get(KEY_AUTO_REMOVE_ITEM, "").equals("Always"))
                        //row.getStyleClass().remove("right-clicked-row");
                        row.setStyle(rowStyleBeforeRightClick[0]);
                    else if (!alert.isShowing())
                        row.setStyle(rowStyleBeforeRightClick[0]);
                    //row.getStyleClass().remove("right-clicked-row");
                });

                // Set context menu on row, but use a binding to make it only show for non-empty rows:
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu)null)
                                .otherwise(contextMenu)
                );
                return row ;
            }
        });

        onInitialize();
    }

    protected abstract void onInitialize();

    // add tooltip popups to table cells
    protected <T> void addTooltipToColumnCells(TableColumn<SaleItem,T> column) {

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

    // select a row in the saleItemTable without triggering listener which would refresh browser page
    public void selectRowSilently(int rowIndex) {
        ignoreSelectionEvents = true;
        try {
            saleItemTable.getSelectionModel().select(rowIndex);
        } finally {
            ignoreSelectionEvents = false;
        }
    }

    // Confirmation alert dialog with option to not ask to confirm action (i.e. delete item) again
    protected static Alert createAlertWithOptOut(Alert.AlertType type, String title, String headerText,
                                              String message, String optOutMessage, Consumer<Boolean> optOutAction,
                                              ButtonType... buttonTypes) {
        Alert alert = new Alert(type);
        // Need to force the alert to layout in order to grab the graphic,
        // as we are replacing the dialog pane with a custom pane
        alert.getDialogPane().applyCss();
        Node graphic = alert.getDialogPane().getGraphic();
        // Create a new dialog pane that has a checkbox instead of the hide/show details button
        // Use the supplied callback for the action of the checkbox
        alert.setDialogPane(new DialogPane() {
            @Override
            protected Node createDetailsButton() {
                CheckBox optOut = new CheckBox();
                optOut.setText(optOutMessage);
                optOut.setOnAction(e -> optOutAction.accept(optOut.isSelected()));
                return optOut;
            }
        });
        alert.getDialogPane().getButtonTypes().addAll(buttonTypes);
        alert.getDialogPane().setContentText(message);
        // Fool the dialog into thinking there is some expandable content
        // a Group won't take up any space if it has no children
        alert.getDialogPane().setExpandableContent(new Group());
        alert.getDialogPane().setExpanded(true);
        // Reset the dialog graphic using the default style
        alert.getDialogPane().setGraphic(graphic);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        return alert;
    }

    // get index of selected sale item row in saleItemTable
    public int getSelectedSaleItemRowIndex() {
        return saleItemTable.getSelectionModel().getSelectedIndex();
    }

    public void selectSaleItemRowByIndex(int index) {
        saleItemTable.getSelectionModel().select(index);
    }

    // insert new saleItem into the db and list
    public void addItemToSalesList(SaleItem saleItem) {
        saleItemData.add(saleItem);
        db.insertSaleItemRow(saleItem);
    }

    public boolean itemExists (String itemID) {
        for (SaleItem item : saleItemData) {
            if ( item.getItemID().equals(itemID))
                return true;
        }
        return false;
    }

    public void loadSalesWatchListFromDB(Database db) {
        this.db = db;
        saleItemData.addAll( db.getAllSalesItemRows() );
    }

    // backreference to root controller to access its members
    public void setRootLayoutControllerAccess(RootLayoutController rootLayoutController) {
        this.rootLayoutController = rootLayoutController;
    }

}