package miu.bdt.consumer.weather;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.StringJoiner;

import miu.bdt.dto.weather.Weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HiveService {
	private static Connection connection;
    private static HiveService INSTANCE = null;

    private static final Logger logger = LoggerFactory.getLogger(HiveService.class);
    private static final Gson gson = new Gson();

    public static HiveService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HiveService();
        }
        return INSTANCE;
    }

    private HiveService() {

        try {
            // 1. Load Hive JDBC driver
            Class.forName(HiveConfig.JDBC_DRIVER_NAME);

            // 2. Establish a connection to Hive
            connection = DriverManager.getConnection(HiveConfig.HIVE_URL, HiveConfig.HIVE_USERNAME, HiveConfig.HIVE_PASSWORD);
        } catch (Exception e) {
            logger.error("Cannot create Hive connection. " + e);
            e.printStackTrace();
            System.exit(1);
        }        
        
        Statement statement = null;
        try {
        	// 3. Create a statement object
        	statement = connection.createStatement();

            String sql = String.format(HiveConfig.CREATE_WEATHER_TABLE_SQL, HiveConfig.TABLE_NAME);
            statement.execute(sql);

            statement.close();
        } catch (SQLException e) {
            logger.error("Error occurs while creating table: " + e);
            e.printStackTrace();
            if (statement != null)
				try {
					statement.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            System.exit(1);
        }
    }

    public void insertWeathers(String values) {
    	List<Weather> weathers = gson.fromJson(values, new TypeToken<List<Weather>>(){}.getType());
    	
        if (weathers.isEmpty()) {
            return;
        }
        try {
        	Statement statement = connection.createStatement();
        	
            StringJoiner joiner = new StringJoiner(",");
            weathers.stream().map(r -> String.format("(\"%s\",\"%s\",%.2f,\"%s\")",
                    r.getZipcode(),
                    r.getCity(),
                    r.getTemp(),
                    r.getUpdatedDate())).forEach(joiner::add);
            String sql = String.format(HiveConfig.INSERT_WEATHER_TABLE_SQL, HiveConfig.TABLE_NAME, joiner);
            //logger.info("INSERT_WEATHER_TABLE_SQL: " + sql);
            statement.execute(sql);
        } catch (SQLException e) {
            logger.error("Exception occurs while inserting data: " + e);
            System.exit(1);
        }
    }
}
