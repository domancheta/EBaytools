package org.allthegoodstuff.ebaytools;

import org.allthegoodstuff.ebaytools.model.SaleItem;
import org.eclipse.jgit.annotations.Nullable;

public final class FetchResult {
    protected enum Status {FAILED, SUCCEEDED} ;

    private Status status;
    private String errorMessage;
    private SaleItem saleItem;

    public FetchResult(Status status, @Nullable String errorMessage, @Nullable SaleItem saleItem) {
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
