package org.allthegoodstuff.ebaytools;

import org.allthegoodstuff.ebaytools.config.Cfg4JShoppingConfig;
import org.allthegoodstuff.ebaytools.config.ShoppingConfig;

public class ShoppingAPIUriBuilder {

    private static ShoppingConfig shoppingConfig;

    static {
        shoppingConfig = new Cfg4JShoppingConfig();
    }

    public static String getSingleItemURI (String itemID) {
        StringBuilder builtFetchUri = new StringBuilder();

        String fetchUri = builtFetchUri.append(shoppingConfig.baseApiUri()).append("?")
                .append(shoppingConfig.callname()).append("&")
                .append(shoppingConfig.rsEncoding()).append("&")
                .append(shoppingConfig.appID()).append("&")
                .append(shoppingConfig.siteid()).append("&")
                .append(shoppingConfig.apiVersion()).append("&")
                .append("ItemID=").append(itemID)
                .append("&").append(shoppingConfig.includeSelector()).toString();

        return fetchUri;
    }

    public static String getItemStatusUri (String itemID) {
        StringBuilder builtFetchStatusUri = new StringBuilder();

        String fetchStatusUri =  builtFetchStatusUri.append(shoppingConfig.baseApiUri()).append("?")
                .append(shoppingConfig.itemStatusCallname()).append("&")
                .append(shoppingConfig.rsEncoding()).append("&")
                .append(shoppingConfig.appID()).append("&")
                .append(shoppingConfig.siteid()).append("&")
                .append(shoppingConfig.apiVersion()).append("&")
                .append("ItemID=").append(itemID)
                .append("&").append(shoppingConfig.includeSelector()).toString();

        return fetchStatusUri;

    }

}
