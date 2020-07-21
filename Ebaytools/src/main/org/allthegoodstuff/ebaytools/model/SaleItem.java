package org.allthegoodstuff.ebaytools.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


// TODO: tentative classname for item being auction or sold on ebay.  can look up naming in ebay if you want

/**
 * Model class for a sale item
 * @author domancheta
 */
public class SaleItem {
    private final IntegerProperty itemID;
    private final StringProperty title;
    private final StringProperty itemDescription;
    private final StringProperty sellerInfo;
    private final ObjectProperty<BigDecimal> currentPrice;
    private final ObjectProperty<LocalDate> endTime;
    private final ObjectProperty<LocalDate> startTime;
    // TODO: add properties for:
    // description
    // high/winning bidder
    // bid count
    // image urls
    // shipping info
    // seller

    public SaleItem(int itemID, String title, String description, String seller, BigDecimal currentPrice,
                    LocalDate endTime, LocalDate startTime) {
        this.itemID = new SimpleIntegerProperty(itemID);
        this.title = new SimpleStringProperty(title);
        this.itemDescription = new SimpleStringProperty( description );
        this.sellerInfo = new SimpleStringProperty(seller);
        this.currentPrice = new SimpleObjectProperty<>(currentPrice);
        this.endTime = new SimpleObjectProperty<>(endTime);
        this.startTime = new SimpleObjectProperty<>(startTime);
    }

    public int getItemID() {
        return itemID.get();
    }

    public void setItemID(int itemID) {
        this.itemID.set(itemID);
    }

    public IntegerProperty itemIDProperty() {
        return itemID;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getItemDescription() {
        return itemDescription.get();
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription.set(itemDescription);
    }

    public StringProperty itemDescriptionProperty() {
        return itemDescription;
    }

    public String getSellerInfo() {
        return sellerInfo.get();
    }

    public void setSellerInfo(String sellerInfo) {
        this.sellerInfo.set(sellerInfo);
    }

    public StringProperty sellerInfoProperty() {
        return sellerInfo;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice.get();
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice.set(currentPrice);
    }

    public ObjectProperty<BigDecimal> currentPriceProperty() {
        return currentPrice;
    }

    public LocalDate getEndTime() {
        return endTime.get();
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime.set(endTime);
    }

    public ObjectProperty<LocalDate> endTimeProperty() {
        return endTime;
    }

    public LocalDate getStartTime() {
        return startTime.get();
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime.set(startTime);
    }

    public ObjectProperty<LocalDate> startTimeProperty() {
        return startTime;
    }
}
