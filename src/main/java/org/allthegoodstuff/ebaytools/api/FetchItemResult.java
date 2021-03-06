package org.allthegoodstuff.ebaytools.api;

import org.allthegoodstuff.ebaytools.model.SaleItem;
import org.eclipse.jgit.annotations.Nullable;

// TODO: logic to just pull watchlisted item's status info (bids, expiration, etc.)
public final class FetchItemResult {
    protected enum Status {FAILED, SUCCEEDED} ;

    private Status status;
    private String errorMessage;
    private SaleItem saleItem;

    // todo: create new ItemStatus class to store only status info for an item?
    public FetchItemResult(Status status, @Nullable String errorMessage, @Nullable SaleItem saleItem) {
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
