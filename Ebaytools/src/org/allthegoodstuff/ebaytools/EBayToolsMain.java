package org.allthegoodstuff.ebaytools;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.allthegoodstuff.ebaytools.model.SaleItem;

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
        Parent root = FXMLLoader.load(getClass().getResource("view/RootLayout.fxml"));
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
