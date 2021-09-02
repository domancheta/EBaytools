package org.allthegoodstuff.ebaytools.db;

import org.allthegoodstuff.ebaytools.config.ShoppingConfig;
import org.allthegoodstuff.ebaytools.model.SaleItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SQLiteDB implements Database{

    private Connection conn;
    private ShoppingConfig shoppingConfig;

    // retrieve following properties from config
    private String dbDriverName;
    private String mainTableName;
    private final static Logger logger = LogManager.getLogger("GLOBAL");

    // main table column names
    private final static String cItemID = "itemId",  cTitle = "title", cDescription = "description",
    cSellerInfo = "sellerInfo", cPrice = "price", cBids = "bids", cEndTime = "endTime", cStartTime = "startTime";

    private static PreparedStatement sthSelectAllSalesItems;
    private static PreparedStatement sthSelectSalesItem;
    private static PreparedStatement stInsertSalesItem;
    private static PreparedStatement sthDeleteSalesItem;

    @Inject
    public SQLiteDB () {
    }

    public void initialize(ShoppingConfig shoppingConfig) {
        this.shoppingConfig = shoppingConfig;

        dbDriverName = shoppingConfig.dbDriverName();
        mainTableName = shoppingConfig.mainTableName();
        try {
            conn = DriverManager.getConnection(dbDriverName);
        }
        catch(SQLException se) {
            // todo: handle output of all sql exceptions.  should output to log as well
            logger.error(se.getMessage());
        }
        validateMainTables();
        createPreparedStatements();
    }

    private void validateMainTables() {

        try {
            DatabaseMetaData metaData = conn.getMetaData();
            Statement createStatement = getStatement();

            logger.info("Using DB driver " + metaData.getDriverName() + " version " + metaData.getDriverVersion());

            // check existence of main table
            ResultSet rs = metaData.getTables(null, null, mainTableName, null );
            if (rs.next()) {
                do {
                    if (rs.getString(3).equals(mainTableName))
                        logger.info("Using existing table " + mainTableName );
                } while (rs.next());
            }
            else {
                logger.info("Table " + mainTableName + " doesn't exist.  Creating it...");

                String qMainTableCreate = "create table " + mainTableName + "(" + cItemID + " PRIMARY KEY," +
                        cTitle + ", " + cDescription + ", " + cSellerInfo + ", " + cPrice + " INTEGER, " +
                        cBids + " INTEGER, " + cEndTime + ", " + cStartTime + ")";
                if ( createStatement.execute( qMainTableCreate ) )
                    logger.info("Table " + mainTableName + " created successfully!");
            }

        } catch (SQLException se) {
            logger.error(se.getMessage());
        }
    }

    private void createPreparedStatements() {
        logger.info("Initializing DB prepared statements...");
        try {
            sthSelectAllSalesItems = conn.prepareStatement("SELECT * FROM " + mainTableName);
            sthSelectSalesItem = conn.prepareStatement("SELECT * FROM " + mainTableName + " WHERE itemID = ?");
            stInsertSalesItem = conn.prepareStatement("INSERT OR REPLACE INTO " + mainTableName
                                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            sthDeleteSalesItem = conn.prepareStatement("DELETE FROM " + mainTableName + " WHERE itemID = ?");
            logger.info("Prepared statements created successfully...");
        } catch (SQLException se) {
            logger.error(se.getMessage());
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public Statement getStatement() {
        Statement newStatement = null;

        try {
            if (conn != null ) newStatement = conn.createStatement();
        } catch (SQLException se) {
            logger.error(se.getMessage());
        }
        return newStatement;
    }


    public int insertSaleItemRow (String itemID, String title, String description, String sellerInfo,
                                  BigDecimal price, int bids, String endTime, String startTime) {
        try {
            stInsertSalesItem.setString(1, itemID);
            stInsertSalesItem.setString(2, title);
            stInsertSalesItem.setString(3, description);
            stInsertSalesItem.setString(4, sellerInfo);
            // convert big decimal to int
            int packedInt = price.scaleByPowerOfTen(4).intValue();
            stInsertSalesItem.setInt(5, packedInt);
            stInsertSalesItem.setInt(6, bids);
            stInsertSalesItem.setString(7, endTime);
            stInsertSalesItem.setString(8, startTime);

            return stInsertSalesItem.executeUpdate();

        } catch (SQLException se) {
            logger.error(se.getMessage());
        }

        return 0;

    }

    public int insertSaleItemRow (SaleItem saleItem) {
        try {
            stInsertSalesItem.setString(1, saleItem.getItemID());
            stInsertSalesItem.setString(2, saleItem.getTitle());
            stInsertSalesItem.setString(3, saleItem.getDescription());
            stInsertSalesItem.setString(4, saleItem.getSellerInfo());
            // convert big decimal to int
            int packedInt = saleItem.getPrice().scaleByPowerOfTen(4).intValue();
            stInsertSalesItem.setInt(5, packedInt);
            stInsertSalesItem.setInt(6, saleItem.getBids());
            stInsertSalesItem.setString(7, saleItem.getEndTime().toString());
            stInsertSalesItem.setString(8, saleItem.getStartTime().toString());

            return stInsertSalesItem.executeUpdate();

        } catch (SQLException se) {
            logger.error(se.getMessage());
        }

        return 0;

    }

    public int deleteSaleItemRow (String itemID) {
        try {
            sthDeleteSalesItem.setString(1, itemID);
            return sthDeleteSalesItem.executeUpdate();
        } catch (SQLException se) {
            logger.error(se.getMessage());
        }

        return 0;
    }

    public ArrayList<SaleItem> getAllSalesItemRows () {
        ArrayList<SaleItem> saleItems = new ArrayList<SaleItem>();

        String itemId, title, description, sellerInfo;
        BigDecimal price;
        int bids;
        LocalDateTime endTime, startTime;

        try {
            ResultSet rs = sthSelectAllSalesItems.executeQuery();

            while (rs.next()) {
                itemId = rs.getString(cItemID);
                title = rs.getString(cTitle);
                description = rs.getString(cDescription);
                sellerInfo = rs.getString(cSellerInfo);
                price = new BigDecimal(rs.getInt(cPrice)).scaleByPowerOfTen(-4);
                bids = rs.getInt(cBids);
                endTime = LocalDateTime.parse(rs.getString(cEndTime), DateTimeFormatter.ISO_DATE_TIME);
                startTime = LocalDateTime.parse(rs.getString(cStartTime), DateTimeFormatter.ISO_DATE_TIME);

                saleItems.add ( new SaleItem(itemId, title, description, sellerInfo, price, bids, endTime, startTime ));
            }
        } catch (SQLException se) {
            logger.error(se.getMessage());
        }

        return saleItems;
    }


    public boolean executeStatement(Statement mS, String statement) {
        try {
            return mS.execute(statement);
        } catch (SQLException se) {
            logger.error(se.getMessage());
            return false;
        }
    }

    public void commit() {
        try {
            conn.commit();
        } catch (SQLException se) {
            logger.error(se.getMessage());
        }
    }
    public void shutdown() {
        try {
            conn.close();
            logger.info("Successfully shutdown DB connection");
        } catch (SQLException se) {
            logger.error(se.getMessage());
        }
    }

}
