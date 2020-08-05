package org.allthegoodstuff.ebaytools;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.allthegoodstuff.ebaytools.db.Database;
import org.allthegoodstuff.ebaytools.db.SQLiteDB;
import org.allthegoodstuff.ebaytools.view.RootLayoutController;
import org.allthegoodstuff.ebaytools.view.SalesItemsViewController;

import java.io.IOException;

public class EBayToolsMain extends Application {

    private Stage primaryStage;
    private VBox rootLayout;
    private RootLayoutController rootLayoutController;
    final private Database db;

    public EBayToolsMain() {
        // local db singleton allowing only one connection during app lifetime
        // todo: use a DI framework
        db = SQLiteDB.getInstance();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        initRootLayout();
        showSalesItemsView();

    }


    public static void main(String[] args) {
        launch(args);
    }

    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader fxmlLoader = new FXMLLoader(EBayToolsMain.class
                    .getResource("view/RootLayout.fxml"));

            rootLayout = (VBox) fxmlLoader.load();

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
            AnchorPane salesItemsView = (AnchorPane) loader.load();

            // Set sales list view into the location of the layout
            rootLayoutController.getSalesListPane().getChildren().add(salesItemsView);

            db.getAllSalesItemRows();
            // Give the controller access to the main app.
            SalesItemsViewController controller = loader.getController();
            controller.setDBAccess(db);

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
        System.out.println ("Exitted this wonderful app!  Have a nice day!");
    }


}
