package org.allthegoodstuff.ebaytools.view;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.allthegoodstuff.ebaytools.model.SaleItem;

public class SalesItemsDefaultViewController extends SalesItemsViewController {

    @Override
    protected void onInitialize()
    {
        //todo:  fix up this.  the SaleItemViewCell passed is null as observed in debugging.
        itemSummaryColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue()));
        itemSummaryColumn.setCellFactory((TableColumn<SaleItem, SaleItem> celldata) -> new SaleItemViewCell());

        bidsColumn.setCellValueFactory(celldata -> celldata.getValue().bidsProperty().asString());
        startTimeColumn.setCellValueFactory(celldata -> celldata.getValue().startTimeProperty());
        endTimeColumn.setCellValueFactory(celldata -> celldata.getValue().endTimeProperty());

        saleItemTable.setItems(saleItemData);

        // show tooltips for cells whose content is too long to fit in certain columns
        ObservableList<TableColumn> tippedColumns = FXCollections.observableArrayList( startTimeColumn, endTimeColumn);
        tippedColumns.forEach(this::addTooltipToColumnCells);
    }

}