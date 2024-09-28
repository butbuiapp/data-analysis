package miu.bdt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
	
	public static String convertToDateTime(long epochTimeMillis) {
		// Convert epoch milliseconds to LocalDateTime
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochTimeMillis), ZoneId.systemDefault());

        // Format the date as a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateTime.format(formatter);

        return formattedDate;
	}
	
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

}
