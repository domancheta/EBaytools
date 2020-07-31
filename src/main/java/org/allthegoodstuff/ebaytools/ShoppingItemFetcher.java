package org.allthegoodstuff.ebaytools;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import org.allthegoodstuff.ebaytools.model.SaleItem;
import org.allthegoodstuff.ebaytools.view.SalesItemsViewController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

/*Fields from the item are retrieved on the perl app:
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
    public static String getSingleItem(String itemID) throws Exception {
        // TODO: will need to store the substrings for the api call somewhere
        String baseUri = "https://open.api.ebay.com/shopping?callname=GetSingleItem";
        String respEncoding = "responseencoding=JSON";
        //todo: is there a generic yet secure appID for any user
        String appID = "appid=DominicA-eShopToo-PRD-8c8ee5576-77674917";
        String siteID = "siteid=0";
        String version = "version=967";
        String incSelector = "IncludeSelector=Description,Details";

        HttpClient client = HttpClient.newHttpClient();
        StringBuilder sb = new StringBuilder();
        String uri = sb.append(baseUri).append("&").append(respEncoding).append("&")
                        .append(appID).append("&").append(siteID).append("&").append(version)
                        .append("&") .append("ItemID=").append(itemID).append("&").append(incSelector).toString();
        //System.out.println ("the built api call:\n" + uri);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        // TODO: be able to handle exception correctly

        Any any = JsonIterator.deserialize(response.body());

        // todo: find best way to log errors and provide error feedback
        // todo: design issue - display the page item to a preview pane and allow user to add to the watchlist table
        SalesItemsViewController.addItemToSalesList(new SaleItem(
                itemID, any.toString("Item", "Title"),
                any.toString("Item", "Description"),
                any.toString("Item", "Seller", "UserID"),
                any.toBigDecimal("Item", "CurrentPrice", "Value"),
                LocalDateTime.parse(any.toString("Item", "EndTime"), dateFormatter),
                LocalDateTime.parse(any.toString("Item", "StartTime"), dateFormatter)
        ));

        //todo: define proper return value if any.  some way to test this method
        return (response.body());
    }

    public static CompletableFuture<String> getSingleItemAsync(String itemID) {
        HttpClient client = HttpClient.newHttpClient();
        String uri = "https://open.api.ebay.com/shopping?callname=GetSingleItem&" +
                "responseencoding=JSON&appid=DominicA-eShopToo-PRD-8c8ee5576-77674917&" +
                "siteid=0&version=967&ItemID=" + itemID +
                "&IncludeSelector=Description,Details";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);

    }

}
