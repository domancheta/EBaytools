package org.allthegoodstuff.ebaytools;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

class ShopHttpClientTest {

    @Test
    void getSingleAuctionItemTest() {
        String itemID = "402323900275";
        String rs;

        try {
            rs = ShopHttpClient.getSingleItem(itemID);

            System.out.println(rs);
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
            rs = ShopHttpClient.getSingleItem(itemID);

            System.out.println(rs);
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
            rs = ShopHttpClient.getSingleItemAsync(itemID);
            rs.join();

            System.out.println(rs.get());
        }
        catch (Exception e) {
            System.err.println("Exception thrown: " + e.getMessage());
        }
    }
}