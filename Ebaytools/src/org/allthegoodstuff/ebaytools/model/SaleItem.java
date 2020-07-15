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
    private final IntegerProperty itemNumber;
    private final StringProperty itemTitle;
    private final StringProperty itemDescription;
    private final StringProperty sellerInfo;
    private final ObjectProperty<BigDecimal> price;
    private final ObjectProperty<LocalDate> endTime;
    private final ObjectProperty<LocalDate> startTime;

    public SaleItem(int itemNumber, String title, String description, String seller, BigDecimal price,
                    LocalDate endTime, LocalDate startTime) {
        this.itemNumber = new SimpleIntegerProperty(itemNumber);
        this.itemTitle = new SimpleStringProperty(title);
        this.itemDescription = new SimpleStringProperty( description );
        this.sellerInfo = new SimpleStringProperty(seller);
        this.price = new SimpleObjectProperty<BigDecimal>(price);
        this.endTime = new SimpleObjectProperty<LocalDate>(endTime);
        this.startTime = new SimpleObjectProperty<LocalDate>(startTime);
    }

    public int getItemNumber() {
        return itemNumber.get();
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber.set(itemNumber);
    }

    public IntegerProperty itemNumberProperty() {
        return itemNumber;
    }

    public String getItemTitle() {
        return itemTitle.get();
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle.set(itemTitle);
    }

    public StringProperty itemTitleProperty() {
        return itemTitle;
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

    public BigDecimal getPrice() {
        return price.get();
    }

    public void setPrice(BigDecimal price) {
        this.price.set(price);
    }

    public ObjectProperty<BigDecimal> priceProperty() {
        return price;
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
