package org.allthegoodstuff.ebaytools;

import org.allthegoodstuff.ebaytools.db.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class DatabaseTest {
    @Test
    public void createdTablesTest () {
        Database db = DaggerDatabaseFactory.create().database();
        Statement s = db.getStatement();
        try {
            Assertions.assertEquals(true, s.execute("select * from sale_items"));
        }
        catch (SQLException se) {
            System.err.println(se.getMessage());;
        }
        finally {
            db.shutdown();
        }
    }

    @Test
    public void insertRowTest() {
        Database db = DaggerDatabaseFactory.create().database();

        String testid = "345678";

        try {

            int rows = db.insertSaleItemRow( testid, "another thing", "stuf stuff", "mr. sales",
                    new BigDecimal(3.99), 5,LocalDateTime.now().toString(), LocalDateTime.now().toString());
            Assertions.assertEquals(1, rows);
        }
        finally {
            db.deleteSaleItemRow(testid);
            db.shutdown();

        }
    }

}
