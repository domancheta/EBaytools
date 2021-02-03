package org.allthegoodstuff.ebaytools.db;

import org.allthegoodstuff.ebaytools.model.SaleItem;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

@Singleton
public interface Database {

    public Connection getConnection();
    public Statement getStatement();
    public boolean executeStatement(Statement mS, String statement);
    public void commit();
    public void shutdown();

    public int insertSaleItemRow (SaleItem saleItem);
    public int insertSaleItemRow (String itemID, String title, String description, String sellerInfo,
                                  BigDecimal price, int bids, String endTime, String startTime);
    public int deleteSaleItemRow (String itemID);

    public ArrayList<SaleItem> getAllSalesItemRows ();

}
