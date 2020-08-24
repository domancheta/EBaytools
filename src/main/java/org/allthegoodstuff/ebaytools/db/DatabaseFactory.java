package org.allthegoodstuff.ebaytools.db;

import dagger.Component;
import org.allthegoodstuff.ebaytools.db.SQLiteDB;

import javax.inject.Singleton;

@Component
public interface DatabaseFactory {
    SQLiteDB database();
}
