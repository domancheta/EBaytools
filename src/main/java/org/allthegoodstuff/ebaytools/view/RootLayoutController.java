package org.allthegoodstuff.ebaytools.view;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
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

    private WebEngine browserEngine;

    @FXML
    private ProgressIndicator progressCircle;

    @FXML
    private Button watchlistButton;

    private FetchItemService fetchItemService;

    private SalesItemsViewController salesItemsViewController;

    private boolean itemAddable = false;

    @FXML
    private void initialize() {
        browserEngine = browser.getEngine();

        fetchItemService = new FetchItemService();


        // bind browser view to a progress circle animation which appears only when loading
        browserEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                       if (newState == Worker.State.SUCCEEDED) {
                           // hide progress indicator when page is ready
                           progressCircle.setVisible(false);
                           if (itemAddable)
                                showAddWatchlistButton();
                       }
        });

    }

    public void showItemBrowserPage (String itemID) {
        String itemUrl = "https://www.ebay.com/itm/" + itemID;
        progressCircle.setVisible(true);
        browser.getEngine().load(itemUrl);
    }

    public void showHTMLInBrowser (String html) {
        progressCircle.setVisible(true);
        browser.getEngine().loadContent(html) ;
    }

    public void showAddWatchlistButton() {
        watchlistButton.setVisible(true);
    }

    public void hideAddWatchlistButton() {
        itemAddable = false;
        watchlistButton.setVisible(false);
    }

    public AnchorPane getSalesListPane() {
        return SalesListPane;
    }

    // add fetched item info with JSON parser to the observable sales list
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
                        hideAddWatchlistButton();
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
    private void searchItem() {
        if (salesItemsViewController.itemExists(searchText.getText())) {
            return;
        }

        if (searchText.getText().trim().isBlank()) {
            searchText.clear();
            return;
        }

        // show watchlist button once page loads if item is not in watchlist
        // TODO: do not show watchlist button if the item displayed is not valid or response error (see return from ebay)
        itemAddable = true;
        showItemBrowserPage(searchText.getText());
    }

    @FXML
    public void addToWatchlist() {
        //todo: fix up coloring on button; also add little animation once clicked
        try {
            fetchItemService.restart();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void closeEventHandler(ActionEvent e) {
        //todo: bring up confirmation box that can be opted out of for future sessions
        mainApp.getPrimaryStage().close();

    }

}
