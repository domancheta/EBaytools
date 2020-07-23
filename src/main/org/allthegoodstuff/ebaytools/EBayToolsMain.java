package org.allthegoodstuff.ebaytools;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.allthegoodstuff.ebaytools.model.SaleItem;
import org.allthegoodstuff.ebaytools.view.RootLayoutController;
import org.allthegoodstuff.ebaytools.view.SalesItemsViewController;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EBayToolsMain extends Application {

    private Stage primaryStage;
    VBox rootLayout;

    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<SaleItem> saleItemData = FXCollections.observableArrayList();

    //TODO: initialize table with something - remove after further development
    public EBayToolsMain() {
        // Add some sample data
        saleItemData.add(new SaleItem(12345, "ipod classic", "classic ipod",
                "apple_lover", new BigDecimal("99.98"), LocalDate.now(), LocalDate.now()));
    }

    public ObservableList<SaleItem> getSalesData() {
        return saleItemData;
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
            final RootLayoutController controller = fxmlLoader.getController();
            controller.setMainApp(this);

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
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.getChildren().addAll(personOverview);

            // Give the controller access to the main app.
            SalesItemsViewController controller = loader.getController();
            controller.setMainApp(this);

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
