package org.allthegoodstuff.ebaytools.view;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.allthegoodstuff.ebaytools.EBayToolsMain;
import org.allthegoodstuff.ebaytools.ShopHttpClient;

public class RootLayoutController {


    @FXML
    private TextField searchText;

    private FetchItemService fetchItemService;

    @FXML
    private void initialize() {
        fetchItemService = new FetchItemService();

        // search textfield handler
        searchText.setOnAction((event) -> {
            try {
                fetchItemService.restart();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // TODO: add fetched item info with JSON parser to the observable sales list
    private class FetchItemService extends Service<Void> {

        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() throws Exception{
                        // TODO: review validity of handling search textfield in the task
                        // TODO: should asynchronous version of http call be used?
                        String rawEbayResponse = ShopHttpClient.getSingleItem(searchText.getText());
                        System.out.println(rawEbayResponse);
                        searchText.clear();
                    return null;
                }
            };
        }
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
