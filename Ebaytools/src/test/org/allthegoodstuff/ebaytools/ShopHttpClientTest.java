package org.allthegoodstuff.ebaytools;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShopHttpClientTest {

    @Test
    void getTest1() {
            String uri = "http://api.zippopotam.us/us/90210";
            String rs;

            try {
                rs = ShopHttpClient.get(uri);

                System.out.println(rs);
            }
            catch (Exception e) {
                System.err.println("Exception thrown: " + e.getMessage());
            }
        }

    @Test
    void getSingleItemTest() {
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
}