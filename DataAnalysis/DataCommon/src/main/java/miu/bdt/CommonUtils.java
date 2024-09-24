package miu.bdt;

import java.util.ArrayList;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CommonUtils {
	//private static final InputStream inputStream = CommonUtils.class.getClassLoader().getResourceAsStream("config.properties");
	
	public static String getKafkaServer() {
		// Create a Properties object
        Properties properties = new Properties();
        String server = "";
        try (InputStream inputStream = CommonUtils.class.getClassLoader().getResourceAsStream("config.properties")) {

            // Check if input stream is not null
            if (inputStream == null) {
                System.out.println("Sorry, unable to find config.properties");
                return null;
            }

            // Load the properties file
            properties.load(inputStream);

            // Read properties
            server = properties.getProperty("kafka.server");
            System.out.println("Kafka server===" + server);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return server;
	}
	
	public static String[] getHbaseInfo() {
		// Create a Properties object
        Properties properties = new Properties();
        String[] hbaseInfo = new String[2];
        try (InputStream inputStream = CommonUtils.class.getClassLoader().getResourceAsStream("config.properties")) {

            // Check if input stream is not null
            if (inputStream == null) {
                System.out.println("Sorry, unable to find config.properties");
                return null;
            }

            // Load the properties file
            properties.load(inputStream);

            // Read properties
            hbaseInfo[0] = properties.getProperty("hbase.zookeeper.quorum");
            hbaseInfo[1] = properties.getProperty("hbase.zookeeper.property.clientPort");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return hbaseInfo;
	}

	public static List<List<String>> chunkBySize(List<String> list, int size) {
        List<List<String>> chunks = new ArrayList<>();

        for (int i = 0; i < list.size(); i += size) {
            int end = Math.min(list.size(), i + size);
            chunks.add(new ArrayList<>(list.subList(i, end)));
        }

        return chunks;
    }
	
	public static long countRow(String table) {
		Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long rowCount = 0;
        
        try {
        	System.out.println("Loading class...");
        	// Load the Phoenix JDBC Driver
            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
            
            // Connect to the Phoenix JDBC driver
            connection = DriverManager.getConnection("jdbc:phoenix:quickstart.cloudera:2181:/hbase-unsecure");
            System.out.println("Connection succeed...");
            
            // SQL query to count the number of rows in the table
            String query = "SELECT COUNT(*) FROM " + table;

            // Prepare and execute the query
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            
            System.out.println("query succeed...");
            
            // Retrieve and print the count result
            if (resultSet.next()) {
                rowCount = resultSet.getLong(1);
                System.out.println("Total rows in the table: " + rowCount);
            }

        } catch (ClassNotFoundException ce) {
        	ce.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rowCount;
	}
}
