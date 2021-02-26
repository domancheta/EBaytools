package org.allthegoodstuff.ebaytools;

import org.allthegoodstuff.ebaytools.api.FetchItemResult;
import org.allthegoodstuff.ebaytools.api.ShoppingItemFetcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

class ShoppingItemFetcherTest {

    @Test
    void getSingleAuctionItemTest() {
        String itemID = "402323900275";
        FetchItemResult rs;

        try {
            rs = ShoppingItemFetcher.getSingleItem(itemID);

            Assertions.assertTrue(rs.fetchSucceeded());
            Assertions.assertEquals(itemID, rs.getSaleItem().getItemID());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getSingleAuctionItemBidsTest() {
        String itemID = "264787227715";
        FetchItemResult rs;

        try {
            rs = ShoppingItemFetcher.getSingleItem(itemID);
            Assertions.assertTrue(rs.fetchSucceeded());
            Assertions.assertEquals(itemID, rs.getSaleItem().getItemID());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void invalidItemTest() {
        String itemID = "invalid";
        FetchItemResult rs;

        try {
            rs = ShoppingItemFetcher.getSingleItem(itemID);
            Assertions.assertFalse(rs.fetchSucceeded());
            Assertions.assertNull(rs.getSaleItem());
            Assertions.assertNotNull(rs.getErrorMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: need to mock out scenario for a few non-200 responses
    //  will have to override what is set within config file
    //  page not found test
    //  server error test
    //  bad connection test

    // todo: add test for identifying expired sale items, i.e.,


    @Test
    // todo: convert following to follow format of synchronized format.
    //  call common code in separate private function
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