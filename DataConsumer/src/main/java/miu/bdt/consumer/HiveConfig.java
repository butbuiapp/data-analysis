package miu.bdt.consumer;

public class HiveConfig {
	public static final String CREATE_WEATHER_TABLE_SQL = "CREATE TABLE IF NOT EXISTS %s (zip_code STRING,city STRING,temperature FLOAT,updated_date TIMESTAMP) STORED AS PARQUET";
    public static final String INSERT_WEATHER_TABLE_SQL = "INSERT INTO %s VALUES  %s";
    public static final String TABLE_NAME = "bdt_weather";
}
