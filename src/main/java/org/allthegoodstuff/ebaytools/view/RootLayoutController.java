package org.allthegoodstuff.ebaytools.view;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.allthegoodstuff.ebaytools.EBayToolsMain;
import org.allthegoodstuff.ebaytools.FetchResult;
import org.allthegoodstuff.ebaytools.ShoppingItemFetcher;
import org.allthegoodstuff.ebaytools.model.SaleItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;

public class RootLayoutController {
    private final static Logger logger = LogManager.getLogger("GLOBAL");

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

    @FXML
    private GridPane errorPane;

    @FXML
    private Label errorText;

    @FXML
    private ImageView logoImage;

    private FetchItemService fetchItemService;

    private SalesItemsViewController salesItemsViewController;

    // flag searched item as one addable to the watchlist
    private boolean itemAddable = false;
    private SaleItem candidateWatchlistSaleItem;

    @FXML
    private void initialize() {

        try {
            FileInputStream input = new FileInputStream("src/main/resources/logo_200x200.png");
            Image logo = new Image(input);
            logoImage.setImage(logo);
        } catch (IOException ie) {
            System.err.println("Error loading image:");
            logger.error(ie.getMessage());
        }

        showErrorPane();

        browserEngine = browser.getEngine();

        fetchItemService = new FetchItemService();

        // bind the error label to fetch service error message
        errorText.textProperty().bind(fetchItemService.messageProperty());

        // bind browser view to a progress circle animation which appears only when loading
        browserEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                       if (newState == Worker.State.SUCCEEDED) {
                           // hide progress indicator when page is ready
                           progressCircle.setVisible(false);
                           if (itemAddable)
                                showAddWatchlistButton();
                           else
                               hideAddWatchlistButton();
                       }
        });

    }

    public void showItemBrowserPage (String itemID) {
        String itemUrl = "https://www.ebay.com/itm/" + itemID;
        progressCircle.setVisible(true);
        browserEngine.load(itemUrl);
    }

    public void showHTMLInBrowser (String html) {
        progressCircle.setVisible(true);
        browserEngine.loadContent(html) ;
    }

    public void showAddWatchlistButton() {
        watchlistButton.setVisible(true);
    }

    public void hideAddWatchlistButton() {
        itemAddable = false;
        watchlistButton.setVisible(false);
    }

    public void showErrorPane() {
        errorPane.setVisible(true);
    }

    public void hideErrorPane() {
        errorPane.setVisible(false);
        searchText.clear();
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
                    FetchResult result = ShoppingItemFetcher.getSingleItem(searchText.getText());
                    if (result.fetchSucceeded()) {
                        errorPane.setVisible(false);
                        candidateWatchlistSaleItem = result.getSaleItem();
                        // flag to show watchlist button if the item entered is valid
                        itemAddable = true;
                    }
                    else {
                        // display the error stackframe (do what to browser after dismissing error?)
                        // todo: add close button somewhere near error message whose click event hides the error pane
                        updateMessage(result.getErrorMessage());
                        showErrorPane();
                        itemAddable = false;
                    }

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

        try {
            fetchItemService.restart();
            // show browser page only if successful item fetch occurs
            fetchItemService.setOnSucceeded( e -> {
                if (itemAddable)
                    showItemBrowserPage(searchText.getText());
            });
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @FXML
    public void addToWatchlist() {
        // todo: fix up coloring on button; also add little animation once clicked
        salesItemsViewController.addItemToSalesList(candidateWatchlistSaleItem);
        candidateWatchlistSaleItem = null;
        searchText.clear();
        hideAddWatchlistButton();
    }

    @FXML
    public void closeEventHandler(ActionEvent e) {
        //todo: bring up confirmation box that can be opted out of for future sessions
        mainApp.getPrimaryStage().close();

    }

}
