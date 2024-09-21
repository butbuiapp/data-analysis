package miu.bdt.consumer.weather;

public class HiveConfig {
	// Hive config
    
    public static final String JDBC_DRIVER_NAME = "org.apache.hive.jdbc.HiveDriver";
    public static final String HIVE_URL = "jdbc:hive2://quickstart.cloudera:10000/default";
    public static final String HIVE_USERNAME = "hdfs";
    public static final String HIVE_PASSWORD = "";
    
	public static final String CREATE_WEATHER_TABLE_SQL = "CREATE TABLE IF NOT EXISTS %s (zip_code STRING,city STRING,temperature FLOAT,updated_date TIMESTAMP) STORED AS PARQUET";
    public static final String INSERT_WEATHER_TABLE_SQL = "INSERT INTO %s VALUES  %s";
    public static final String TABLE_NAME = "bdt_weather";
}
