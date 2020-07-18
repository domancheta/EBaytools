package org.allthegoodstuff.ebaytools;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class ShopHttpClient {

    // TODO: synchronous get call - use asynchronous instead
    public static String getSingleItem(String itemID) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String uri = "https://open.api.ebay.com/shopping?callname=GetSingleItem&" +
               "responseencoding=JSON&appid=DominicA-eShopToo-PRD-8c8ee5576-77674917&" +
               "siteid=0&version=967&ItemID=" + itemID;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return (response.body());
    }



}
