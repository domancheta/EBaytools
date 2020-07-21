package org.allthegoodstuff.ebaytools;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

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

public class ShopHttpClient {

    // TODO: synchronous get call - use asynchronous instead?
    public static String getSingleItem(String itemID) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String uri = "https://open.api.ebay.com/shopping?callname=GetSingleItem&" +
               "responseencoding=XML&appid=DominicA-eShopToo-PRD-8c8ee5576-77674917&" +
               "siteid=0&version=967&ItemID=" + itemID +
               "&IncludeSelector=Description,Details";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return (response.body());
    }



}
