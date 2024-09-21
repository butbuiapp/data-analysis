package miu.bdt.consumer.yahoofinance;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.hadoop.hbase.util.Bytes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import miu.bdt.dto.yahoofinance.Ticker;

public class HbaseService {
	private static final String TABLE_NAME = "ticker";
    private static final String COLUMN_FAMILY = "info";
    private static HbaseService INSTANCE = null;
    private Connection connection = null;
    private static final Gson gson = new Gson();
    
    private HbaseService() {
    	connection = createConnection();
    }
    
    public static HbaseService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new HbaseService();
		}
		return INSTANCE;
    }
    
    private Connection createConnection() {
    	// Create the configuration
        Configuration config = HBaseConfiguration.create();

        // Establish a connection
        Connection connection = null;
		try {
			
			connection = ConnectionFactory.createConnection(config);			
			Admin admin = connection.getAdmin();
			System.out.println("Connection created...");
			
	    	// create table
			HTableDescriptor table = new HTableDescriptor(TableName.valueOf(TABLE_NAME));
			// create Column Family
			table.addFamily(new HColumnDescriptor(COLUMN_FAMILY).setCompressionType(Algorithm.NONE));

			System.out.println("Checking table...");
	        // Create the table if it does not exist
			if (!admin.tableExists(table.getTableName())) {
	            admin.createTable(table);
	            System.out.println("Table 'ticker' with column family 'info' created.");
	        }
			System.out.println("Table was created...");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
        
    	return connection;
    }
    	
    
    public void insert(String values)  throws IOException {
    	// convert to Ticker
    	Ticker ticker = gson.fromJson(values, new TypeToken<Ticker>(){}.getType());
    	System.out.println(ticker);
    	
    	 // Create a Put object with the row key as the ticker id
        Put put = new Put(Bytes.toBytes(ticker.getId() + "_" + String.valueOf(ticker.getTime())));

        // Add columns to the Put object
        put.addColumn(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes("id"), convertToByte(ticker.getId()));
        put.addColumn(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes("price"), convertToByte(ticker.getPrice()));
        put.addColumn(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes("time"), convertToByte(ticker.getTime()));
        put.addColumn(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes("exchange"), convertToByte(ticker.getExchange()));
        put.addColumn(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes("quoteType"), convertToByte(ticker.getQuoteType()));
        put.addColumn(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes("marketHours"), convertToByte(ticker.getMarketHours()));
        put.addColumn(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes("changePercent"), convertToByte(ticker.getChangePercent()));
        put.addColumn(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes("change"), convertToByte(ticker.getChange()));

        try (Table tb = connection.getTable(TableName.valueOf(TABLE_NAME))) {
			tb.put(put);
			System.out.println("Ticker inserted to Hbase...");
		}

        System.out.println("Ticker stored successfully in HBase.");
    }
    
    private byte[] convertToByte(Object s) {
    	return Bytes.toBytes(String.valueOf(s));
    }
}
