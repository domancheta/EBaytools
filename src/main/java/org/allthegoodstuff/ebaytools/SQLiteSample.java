package org.allthegoodstuff.ebaytools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/* PERL code to convert:
        my $dbh = 'DBI'->connect( 'dbi:SQLite2:dbname=ebaytools.db', '', '', { 'RaiseError', 0 } );
        $dbh->{sqlite_handle_binary_nulls} = 1;
        $dbh->{LongReadLen} = 16384;
        $dbh->{LongTruncOk} = 1;

        printlog ("sqlite_version=".$dbh->{sqlite_version}."\n");

        # if db dont exist - create one with needed tables
        $dbh->{PrintError} = 0;    # disable printing of warnings
        $dbh->{RaiseError} = 0;

        my ($idTmp, $thumbTemp) = $dbh->do('select  ID,Thumbnail from Table1 LIMIT 1');
        if ( $dbh->errstr and $dbh->errstr =~ /no such table/ ) {
        $dbh->do(q{create table Table1 (ID PRIMARY KEY, Ended,ItemTitle, CurrentBid,Shipping,Account,MyBid,HiBidder,Seller,Image1,Image2,Image3,Image4,Image5,Image6,Image7,Thumbnail,Description,Comment)});
        $dbh->do(q{create table Settings (Param PRIMARY KEY, VALUE1, VALUE2, VALUE3)});
        $dbh->do(q{insert into Settings values('DB_Version','0.0.4',  ''   ,   ''  )});
        }elsif($dbh->errstr and $dbh->errstr =~ /no such column/){
        Win32::MsgBox("The database format was changed. Please rename or delete file ebaytools.db and start programm again.");
        exit;
        }else{

        printlog ("DB is ok!\n");
        }
        $dbh->{PrintError} = 1;    # disable printing of warnings
        $dbh->{RaiseError} = 1;
        my $sthSearchInDatabaseLike = $dbh->prepare("SELECT ID,Ended,ItemTitle,Description,Account,CurrentBid,Seller,HiBidder,Thumbnail FROM Table1 WHERE ID LIKE ? AND Ended BETWEEN ? AND ? AND ItemTitle LIKE ? AND Description LIKE ? AND Account LIKE ? AND CurrentBid LIKE ? AND Seller LIKE ? AND HiBidder LIKE ? ORDER BY Ended");
        my $sthMostFirstAuction     = $dbh->prepare("SELECT min(Ended) from Table1");
        my $sthMostLastAuction      = $dbh->prepare("SELECT max(Ended) from Table1");
        my $sthGetThumbnail         = $dbh->prepare("SELECT Thumbnail  from Table1 where ID = ?");
        my $sthSelectImages 	    = $dbh->prepare("SELECT Image1,Image2,Image3,Image4,Image5,Image6,Image7 FROM Table1 WHERE ID = ?");
        my $sthSelectCurrentAuctions= $dbh->prepare("SELECT ID,Ended,ItemTitle,CurrentBid,Shipping,Thumbnail,Description from Table1 where Ended >= ? ORDER BY Ended");
        my $sthDelId                = $dbh->prepare("DELETE FROM Table1 WHERE ID = ?");
 */
public class SQLiteSample
{
    public static void main(String[] args)
    {
        Connection connection = null;
        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:ebaytools.db");
            Statement s = connection.prepareStatement("select * from person");

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table person (id integer, name string)");
            statement.executeUpdate("insert into person values(1, 'leo')");
            statement.executeUpdate("insert into person values(2, 'yui')");
            ResultSet rs = statement.executeQuery("select * from person");
            while(rs.next())
            {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        finally
        {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }
}