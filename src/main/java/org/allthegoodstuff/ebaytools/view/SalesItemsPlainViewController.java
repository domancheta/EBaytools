package org.allthegoodstuff.ebaytools.view;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

public class SalesItemsPlainViewController extends SalesItemsViewController {

    @Override
    protected void onInitialize()
    {
        itemIDColumn.setCellValueFactory(celldata -> celldata.getValue().itemIDProperty());
        titleColumn.setCellValueFactory(celldata -> celldata.getValue().titleProperty());
        priceColumn.setCellValueFactory(celldata -> Bindings.format("%.2f", celldata.getValue().priceProperty()));
        bidsColumn.setCellValueFactory(celldata -> celldata.getValue().bidsProperty().asString());
        startTimeColumn.setCellValueFactory(celldata -> celldata.getValue().startTimeProperty());
        endTimeColumn.setCellValueFactory(celldata -> celldata.getValue().endTimeProperty());
        sellerColumn.setCellValueFactory(celldata -> celldata.getValue().sellerInfoProperty());

        saleItemTable.setItems(saleItemData);

        // show tooltips for cells whose content is too long to fit in certain columns
        ObservableList<TableColumn> tippedColumns = FXCollections.observableArrayList(
                titleColumn, startTimeColumn, endTimeColumn, sellerColumn);
        tippedColumns.forEach(this::addTooltipToColumnCells);
    }

}