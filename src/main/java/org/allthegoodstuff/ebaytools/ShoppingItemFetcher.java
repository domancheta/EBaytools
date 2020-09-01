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
import java.util.Iterator;
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

    private final static DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    // TODO: synchronous get call - use asynchronous instead?
    public static FetchResult getSingleItem(String itemID) throws Exception {

        String uri = ShoppingAPIUriBuilder.getSingleItemURI(itemID);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        //  handle non-200 errors: 4xx, 5xx
        int httpStatus = response.statusCode();
        String errorMessage = "";

        if (httpStatus != 200) {
            errorMessage = "HTTP status error " +  httpStatus + ": " + switch ( httpStatus ) {
                case 400 -> "Bad Request — Client sent an invalid request";
                case 401 -> "Unauthorized — Client failed to authenticate with the server";
                case 403 -> "Forbidden — Client doesn't access to the requested resource";
                case 404 -> "Not Found — The requested resource does not exist";
                case 412 -> "Precondition Failed — Condition(s) in the request header fields evaluated to false";
                case 500 -> "Internal Server Error — An error occurred on the server";
                case 503 -> "Service Unavailable — The requested service is not available";
                default -> "Unexpected HTTP response occurred";
            };

            return new FetchResult(FetchResult.Status.FAILED, errorMessage, null);
        }

        System.out.println ("response status code: " + response.statusCode() + "\n");
        System.out.println ("response headers: " + response.headers() + "\n");
        System.out.println ("response body: " + response.body() + "\n");
        System.out.println ("request url: " + response.request() + "\n");

        Any any = JsonIterator.deserialize(response.body());

        //  handle non-existent content - i.e. item id doesn't exists
        //  TODO: should expired sales item be checked for failure or warning before adding?
        if (any.toString("Ack").equals("Failure")) {
            Any errorList = any.get("Errors");
            Iterator<Any> errIter = errorList.iterator();
            while (errIter.hasNext()) {
                Any errMap = errIter.next();
                //TODO: is just the long message sufficient troubleshooting feedback ?
                errorMessage = errMap.toString("LongMessage") + "\n" + errorMessage;
            }
            return new FetchResult(FetchResult.Status.FAILED, errorMessage, null);
        }


        SaleItem saleitem = new SaleItem(
                itemID, any.toString("Item", "Title"),
                any.toString("Item", "Description"),
                any.toString("Item", "Seller", "UserID"),
                any.toBigDecimal("Item", "CurrentPrice", "Value"),
                LocalDateTime.parse(any.toString("Item", "EndTime"), dateFormatter),
                LocalDateTime.parse(any.toString("Item", "StartTime"), dateFormatter)
                );

        return new FetchResult(FetchResult.Status.SUCCEEDED, null, saleitem);

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

