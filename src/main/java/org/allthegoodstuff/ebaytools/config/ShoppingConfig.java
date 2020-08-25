package org.allthegoodstuff.ebaytools.config;

import javax.inject.Singleton;

@Singleton
public interface ShoppingConfig {

    // property strings to be used to build out fetch API URIs
    public String baseApiUri();

    public String apiVersion();

    public String appID();

    // call to retrieve info on single item
    public String callname();

    // call to retrieve status info on one or more item - use for quick retrieval of bidding info
    public String itemStatusCallname();

    public String rsEncoding();

    public String siteid();

    public String includeSelector();

    // generic method to retrieve specified property from the config files
    public String getProperty(String key);
}
