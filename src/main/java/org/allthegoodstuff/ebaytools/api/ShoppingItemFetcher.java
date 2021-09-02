package org.allthegoodstuff.ebaytools.api;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import org.allthegoodstuff.ebaytools.config.ShoppingConfig;
import org.allthegoodstuff.ebaytools.model.SaleItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

/*TODO: More fields to be retrieved - using perl app as reference:
        - image URLs ( There's a gallery url and list of picture urls being returned in api)
        - shipping info (multiple fields)
        bid amount (one of) - in model, this is currentPrice:
        - current bid
        - winning bid
        - start price
        highest bidder (one of):
        - high bidder
        - winning bidder
*/

// todo: make ShoppingItemFetcher an interface and make implemented class - ebayItemFetcher
public class ShoppingItemFetcher {

    private final DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private final Logger logger = LogManager.getLogger("GLOBAL");

    private ShoppingConfig shoppingConfig;

    public ShoppingItemFetcher(ShoppingConfig shoppingConfig) {
        this.shoppingConfig = shoppingConfig;
    }

    public FetchItemResult getSingleItem(String itemID) throws Exception {

        String uri = ShoppingAPIUriBuilder.getSingleItemURI(itemID);

        String clientID = shoppingConfig.appID();
        String clientSecret = shoppingConfig.certID();

        OAuth20Service service = new ServiceBuilder(clientID)
                .apiSecret(clientSecret)
                .defaultScope("https://api.ebay.com/oauth/api_scope") // replace with desired scope
                .build(EbayApi20.instance());

        //todo: re-use current acces token if not expired else fetch new one
        OAuth2AccessToken accessToken = service.getAccessTokenClientCredentialsGrant();

        OAuthRequest request = new OAuthRequest(Verb.GET, uri);
        request.addHeader("X-EBAY-API-IAF-TOKEN", accessToken.getRawResponse());
        service.signRequest(accessToken, request);

        String responseBody;
        String responseHeaders;
        String errorMessage = "";

        try (Response response = service.execute(request)) {
            int httpStatus = response.getCode();
            responseHeaders = response.getHeaders().toString();

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

                logger.error(errorMessage);
                logger.error("response status code: " + httpStatus + "\n");
                logger.error("response headers: " + responseHeaders + "\n");

                return new FetchItemResult(FetchItemResult.Status.FAILED, errorMessage, null);
            }
            else
                responseBody = response.getBody();

        }

        /*
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        //  handle non-200 errors: 4xx, 5xx
        int httpStatus = response.statusCode();
         */


        Any any = JsonIterator.deserialize(responseBody);

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

            logger.error(errorMessage);
            logger.error("response request: " + uri);
            logger.error("response body: " + responseBody + "\n");

            return new FetchItemResult(FetchItemResult.Status.FAILED, errorMessage, null);
        }


        SaleItem saleitem = new SaleItem(
                itemID, any.toString("Item", "Title"),
                any.toString("Item", "Description"),
                any.toString("Item", "Seller", "UserID"),
                any.toBigDecimal("Item", "CurrentPrice", "Value"),
                any.toInt("Item", "BidCount"),
                LocalDateTime.parse(any.toString("Item", "EndTime"), dateFormatter),
                LocalDateTime.parse(any.toString("Item", "StartTime"), dateFormatter)
                );

        logger.trace("response headers: " + responseHeaders + "\n");
        logger.trace("response body: " + responseBody + "\n");
        logger.trace("request url: " + uri + "\n");

        return new FetchItemResult(FetchItemResult.Status.SUCCEEDED, null, saleitem);

    }

    //todo: convert following async call to conform to above synchronous call
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

