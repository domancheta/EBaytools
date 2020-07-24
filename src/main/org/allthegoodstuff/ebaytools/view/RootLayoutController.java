package org.allthegoodstuff.ebaytools.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.allthegoodstuff.ebaytools.EBayToolsMain;
import org.allthegoodstuff.ebaytools.ShopHttpClient;

public class RootLayoutController {


    @FXML
    private TextField searchText;

    @FXML
    private void initialize() {
        searchText.setOnAction((event) -> {
            try {
                String rawEbayResponse = ShopHttpClient.getSingleItem(searchText.getText());
                System.out.println(rawEbayResponse);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            searchText.clear();
        });
    }

    /**
     * Is called by the main application to give a reference back to itself.
     */
    private EBayToolsMain mainApp;

    public void setMainApp(EBayToolsMain mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void closeEventHandler(ActionEvent e) {
        mainApp.getPrimaryStage().close();

    }
}
