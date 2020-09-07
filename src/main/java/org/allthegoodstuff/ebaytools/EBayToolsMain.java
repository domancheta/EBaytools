package org.allthegoodstuff.ebaytools;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.allthegoodstuff.ebaytools.db.*;
import org.allthegoodstuff.ebaytools.view.RootLayoutController;
import org.allthegoodstuff.ebaytools.view.SalesItemsViewController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class EBayToolsMain extends Application {

    private Stage primaryStage;
    private VBox rootLayout;
    private RootLayoutController rootLayoutController;
    private SalesItemsViewController salesItemsViewController;
    //todo: consider async logging for performant logging
    private final static Logger logger = LogManager.getLogger("GLOBAL");
    final private Database db;

    public EBayToolsMain() {
        db = DaggerDatabaseFactory.create().database();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        initRootLayout();
        showSalesItemsView();
        //todo: this is probably bad injecting whole controller to the other, but it's better than
        // static method littering
        salesItemsViewController.setRootLayoutControllerAccess(rootLayoutController);
        rootLayoutController.setSalesItemsViewController(salesItemsViewController);

    }


    public static void main(String[] args) {
        launch(args);
    }

    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader fxmlLoader = new FXMLLoader(EBayToolsMain.class
                    .getResource("view/RootLayout.fxml"));

            rootLayout = fxmlLoader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            rootLayoutController = fxmlLoader.getController();
            rootLayoutController.setMainApp(this);

            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows sales item view in the root layout
     */

    private void showSalesItemsView() {
        try {
            // Load sales item view.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(EBayToolsMain.class.getResource("view/SalesItemsView.fxml"));
            AnchorPane salesItemsView = loader.load();

            // Set sales list view into the location of the layout
            rootLayoutController.getSalesListPane().getChildren().add(salesItemsView);

            // Give the controller access to the main app.
            salesItemsViewController = loader.getController();
            salesItemsViewController.setDatabaseAccess(db);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return primaryStage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void stop () {
        // this is one of the last methods executed when the app exits
        // todo: find where else that the app may inadvertently exit and fail to close db connection
        db.shutdown();
        logger.info("Exitted this wonderful app!  Have a nice day!");
    }


}
