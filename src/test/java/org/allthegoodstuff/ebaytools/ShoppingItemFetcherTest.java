package org.allthegoodstuff.ebaytools;

import org.allthegoodstuff.ebaytools.model.SaleItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

class ShoppingItemFetcherTest {

    @Test
    void getSingleAuctionItemTest() {
        String itemID = "402323900275";
        String rs;

        try {
            SaleItem si = ShoppingItemFetcher.getSingleItem(itemID);

            Assertions.assertEquals(itemID, si.getItemID());
        }
        catch (Exception e) {
            System.err.println("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void getSingleAuctionItemBidsTest() {
        String itemID = "264787227715";
        String rs;

        try {
            SaleItem si = ShoppingItemFetcher.getSingleItem(itemID);

            Assertions.assertEquals(itemID, si.getItemID());
        }
        catch (Exception e) {
            System.err.println("Exception thrown: " + e.getMessage());
        }
    }

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
            System.err.println("Exception thrown: " + e.getMessage());
        }
    }
}