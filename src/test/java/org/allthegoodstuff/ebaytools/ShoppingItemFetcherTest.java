package org.allthegoodstuff.ebaytools;

import org.allthegoodstuff.ebaytools.model.SaleItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

class ShoppingItemFetcherTest {

    @Test
    void getSingleAuctionItemTest() {
        String itemID = "402323900275";
        FetchResult rs;

        try {
            rs = ShoppingItemFetcher.getSingleItem(itemID);

            Assertions.assertEquals(true,  rs.fetchSucceeded());
            Assertions.assertEquals(itemID, rs.getSaleItem().getItemID());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getSingleAuctionItemBidsTest() {
        String itemID = "264787227715";
        FetchResult rs;

        try {
            rs = ShoppingItemFetcher.getSingleItem(itemID);
            Assertions.assertEquals(true,  rs.fetchSucceeded());
            Assertions.assertEquals(itemID, rs.getSaleItem().getItemID());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void invalidItemTest() {
        String itemID = "invalid";
        FetchResult rs;

        try {
            rs = ShoppingItemFetcher.getSingleItem(itemID);
            Assertions.assertEquals(false,  rs.fetchSucceeded());
            Assertions.assertNull(rs.getSaleItem());
            Assertions.assertNotNull(rs.getErrorMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: need to mock out scenario for a few non-200  responses
    //  page not found test
    //  server error test
    //  bad connection test


    @Test
    void getSingleItemAsyncTest() {
        String itemID = "402323900275";
        CompletableFuture<String> rs;

        try {
            rs = ShoppingItemFetcher.getSingleItemAsync(itemID);
            rs.join();
            Assertions.assertEquals(true, rs.get().contains("\"ItemID\":\"" + itemID + "\""));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}