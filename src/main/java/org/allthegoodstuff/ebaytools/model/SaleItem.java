package org.allthegoodstuff.ebaytools.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;


// TODO: tentative classname for item being auction or sold on ebay.  can look up naming in ebay if you want

/**
 * Model class for a sale item
 * @author domancheta
 */
public class SaleItem {
    private final StringProperty itemID;
    private final StringProperty title;
    private final StringProperty description;
    private final StringProperty sellerInfo;
    private final ObjectProperty<BigDecimal> price;
    private final ObjectProperty<LocalDateTime> endTime;
    private final ObjectProperty<LocalDateTime> startTime;
    // TODO: add properties for and add for db as well:
    // description
    // high/winning bidder (this will be hidden partially or completely for protection of users - maybe don't include)
    // bid count
    // image urls
    // shipping info
    // seller
    // others?

    public SaleItem(String itemID, String title, String description, String seller, BigDecimal price,
                    LocalDateTime endTime, LocalDateTime startTime) {
        this.itemID = new SimpleStringProperty(itemID);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty( description );
        this.sellerInfo = new SimpleStringProperty(seller);
        this.price = new SimpleObjectProperty<>(price);
        this.endTime = new SimpleObjectProperty<LocalDateTime>(endTime);
        this.startTime = new SimpleObjectProperty<LocalDateTime>(startTime);
    }

    public String getItemID() {
        return itemID.get();
    }

    public void setItemID(String itemID) {
        this.itemID.set(itemID);
    }

    public StringProperty itemIDProperty() {
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

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
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

    public LocalDateTime getEndTime() {
        return endTime.get();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime.set(endTime);
    }

    public ObjectProperty<LocalDateTime> endTimeProperty() {
        return endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime.get();
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime.set(startTime);
    }

    public ObjectProperty<LocalDateTime> startTimeProperty() {
        return startTime;
    }
}
