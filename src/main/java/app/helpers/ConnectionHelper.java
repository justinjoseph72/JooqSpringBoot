package app.helpers;

import org.postgresql.ds.PGPoolingDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Justin on 17/04/2017.
 */
public class ConnectionHelper {

    public static Connection getConnectionFromDriverManager()  {
        Connection conn = null;

        try {
            Class.forName("org.postgresql.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","justin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;

    }

    public static Connection getConnectionFromDataSource()
    {
        Connection conn = null;

        PGPoolingDataSource source = new PGPoolingDataSource();
        //commented out this line because this will cause error when
        //creating multiple datasource
//        source.setDataSourceName("PostGress datasource");
        source.setServerName("localhost");
        source.setDatabaseName("postgres");
        //source.setCurrentSchema("testSchema");
        source.setUser("postgres");
        source.setPassword("justin");
        source.setMaxConnections(10);
        try {
            conn = source.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
