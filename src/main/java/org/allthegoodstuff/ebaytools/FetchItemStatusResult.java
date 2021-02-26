package org.allthegoodstuff.ebaytools;

import org.allthegoodstuff.ebaytools.model.SaleItem;
import org.eclipse.jgit.annotations.Nullable;

public final class FetchItemStatusResult {
    protected enum Status {FAILED, SUCCEEDED} ;

    private Status status;
    private String errorMessage;
    private SaleItem saleItem;

    //todo: get the info fields in ebay GetItemStatus response that's needed
    // tentatively these would be Errors/ShortMessage, BidCount, ListingStatus, EndTime
    // todo: new columns and/or tables in database to add
    //todo: make a Result interface and use that as base for the 2 result types
    //todo: make this generic so it can be non-specific to ebay
    public FetchItemStatusResult(Status status, @Nullable String errorMessage, @Nullable SaleItem saleItem) {
        this.status = status;
        if (status == Status.SUCCEEDED) {
            this.saleItem = saleItem;
        } else {
            this.errorMessage = errorMessage;
        }
    }

    public boolean fetchSucceeded() {
        if (status == Status.SUCCEEDED)
            return true;
        return false;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public SaleItem getSaleItem() {
        return saleItem;
    }
}

