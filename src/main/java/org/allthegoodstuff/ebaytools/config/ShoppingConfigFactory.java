package org.allthegoodstuff.ebaytools.config;

import dagger.Component;

@Component
public interface ShoppingConfigFactory {
    Cfg4JShoppingConfig configSettings();
}
