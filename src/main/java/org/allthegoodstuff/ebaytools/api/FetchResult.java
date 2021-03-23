package org.allthegoodstuff.ebaytools.api;

//TODO: finish up making this a parent of FetchItemResult and FetchResult classes.
//  determine what classes remain abstract and implemented
public abstract class FetchResult {
    enum Status {FAILED, SUCCEEDED} ;

    Status status;
    private String errorMessage;

    public boolean fetchSucceeded() {
        if (status == FetchResult.Status.SUCCEEDED)
            return true;
        return false;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
