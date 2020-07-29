package org.allthegoodstuff.ebaytools;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.allthegoodstuff.ebaytools.view.RootLayoutController;
import org.allthegoodstuff.ebaytools.view.SalesItemsViewController;

import java.io.IOException;

public class EBayToolsMain extends Application {

    private Stage primaryStage;
    private VBox rootLayout;
    private RootLayoutController rootLayoutController;

    public EBayToolsMain() {

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
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(EBayToolsMain.class.getResource("view/SalesItemsView.fxml"));
            AnchorPane salesItemsView = (AnchorPane) loader.load();

            // Set person overview into the location of the layout
            rootLayoutController.getSalesListPane().getChildren().add(salesItemsView);

            // Give the controller access to the main app.
            SalesItemsViewController controller = loader.getController();

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
}
