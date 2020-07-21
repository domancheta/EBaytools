package org.allthegoodstuff.ebaytools;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.allthegoodstuff.ebaytools.model.SaleItem;
import org.allthegoodstuff.ebaytools.view.SaleItemsViewController;

public class EBayToolsMain extends Application {

    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<SaleItem> saleItems = FXCollections.observableArrayList();

    public ObservableList<SaleItem> getSaleData() {
        return saleItems;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(EBayToolsMain.class
                .getResource("view/RootLayout.fxml"));
        Parent root= fxmlLoader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        final SaleItemsViewController controller = fxmlLoader.getController();
        controller.setStage(primaryStage);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
