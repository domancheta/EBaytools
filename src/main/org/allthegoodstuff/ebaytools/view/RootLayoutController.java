package org.allthegoodstuff.ebaytools.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.allthegoodstuff.ebaytools.EBayToolsMain;

public class RootLayoutController {
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
