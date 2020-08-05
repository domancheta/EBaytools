package org.allthegoodstuff.ebaytools;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import org.allthegoodstuff.ebaytools.model.SaleItem;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

/*TODO: Fields from the item are retrieved on the perl app:
        itemID
        EndTime
        itemTitle
        image URLs ( There's a gallery url and list of picture urls being returned in api)
        item description - appears in api with extra params
        shipping info (multiple fields)
        -
        bid amount (one of) - in model, this is currentPrice:
        - current bid
        - winning bid
        - start price
        bid count
        highest bidder (one of):
        - high bidder
        - winning bidder
        - seller
*/

public class ShoppingItemFetcher {

    private static DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    // TODO: synchronous get call - use asynchronous instead?
    public static SaleItem getSingleItem(String itemID) throws Exception {

        String uri = ShoppingAPIUriBuilder.getSingleItemURI(itemID);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        // TODO: be able to handle exceptions correctly
        // handle non-200 errors: 4xx, 5xx
        // handle non-existent content - i.e. item id doesn't exists, sales on item expired
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        Any any = JsonIterator.deserialize(response.body());

        // todo: find best way to log errors and provide error feedback
        // todo: design issue - display the page item to a preview pane and allow user to add to the watchlist table
        return new SaleItem(
                itemID, any.toString("Item", "Title"),
                any.toString("Item", "Description"),
                any.toString("Item", "Seller", "UserID"),
                any.toBigDecimal("Item", "CurrentPrice", "Value"),
                LocalDateTime.parse(any.toString("Item", "EndTime"), dateFormatter),
                LocalDateTime.parse(any.toString("Item", "StartTime"), dateFormatter)
                );

    }

    public static CompletableFuture<String> getSingleItemAsync(String itemID) {
        String uri = ShoppingAPIUriBuilder.getSingleItemURI(itemID);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

}
