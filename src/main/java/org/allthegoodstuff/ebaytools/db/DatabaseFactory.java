package org.allthegoodstuff.ebaytools.db;

import dagger.Component;

@Component
public interface DatabaseFactory {
    SQLiteDB database();
}
