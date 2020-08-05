package org.allthegoodstuff.ebaytools.view;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import org.allthegoodstuff.ebaytools.EBayToolsMain;
import org.allthegoodstuff.ebaytools.ShoppingItemFetcher;
import org.allthegoodstuff.ebaytools.model.SaleItem;

public class RootLayoutController {


    @FXML
    private TextField searchText;

    @FXML
    private AnchorPane SalesListPane;

    @FXML
    private WebView browser;

    private FetchItemService fetchItemService;

    private SalesItemsViewController salesItemsViewController;

    @FXML
    private void initialize() {
        fetchItemService = new FetchItemService();

        // search textfield handler
        searchText.setOnAction((event) -> {
            if (salesItemsViewController.itemExists(searchText.getText())) {
                return;
            }

            showItemBrowserPage(searchText.getText());

            try {
                fetchItemService.restart();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    public void showItemBrowserPage (String itemID) {
        String itemUrl = "https://www.ebay.com/itm/" + itemID;
        browser.getEngine().load(itemUrl);
    }

    public AnchorPane getSalesListPane() {
        return SalesListPane;
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
                        SaleItem newSaleItem = ShoppingItemFetcher.getSingleItem(searchText.getText());
                        salesItemsViewController.addItemToSalesList(newSaleItem);
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

    public void setSalesItemsViewController(SalesItemsViewController salesItemsViewController) {
       this.salesItemsViewController = salesItemsViewController;
    }

    @FXML
    public void closeEventHandler(ActionEvent e) {
        //todo: bring up confirmation box that can be opted out of for future sessions
        mainApp.getPrimaryStage().close();

    }

}
