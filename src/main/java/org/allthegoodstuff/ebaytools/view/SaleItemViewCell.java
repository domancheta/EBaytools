package org.allthegoodstuff.ebaytools.view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.AnchorPane;
import org.allthegoodstuff.ebaytools.EBayToolsMain;
import org.allthegoodstuff.ebaytools.model.SaleItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class SaleItemViewCell extends TableCell<SaleItem, SaleItem> {

    @FXML
    private Label labelTitle;

    @FXML
    private Label labelItemId;

    @FXML
    private Label labelPrice;

    @FXML
    private Label labelSeller;

    @FXML
    private AnchorPane anchorPane;

    private FXMLLoader mLLoader;

    private final static Logger logger = LogManager.getLogger("GLOBAL");

    @Override
    protected void updateItem(SaleItem saleItem, boolean empty) {
        super.updateItem(saleItem, empty);

        if(empty || saleItem == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(EBayToolsMain.class.getResource("view/SaleItemCell.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }

            labelTitle.setText(saleItem.getTitle());
            //labelPrice.setText(String.valueOf(saleItem.getPrice()));
            labelPrice.setText(Bindings.format("%.2f", saleItem.getPrice()).getValue());
            labelItemId.setText(saleItem.getItemID());
            labelSeller.setText(saleItem.getSellerInfo());

            setText(null);
            setGraphic(anchorPane);
        }
    }
}
