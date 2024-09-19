package miu.bdt;

public class Constant {
	// Kafka config
	public static final String KAFKA_SERVER = "quickstart.cloudera:9092";
    public static final String TOPIC_NAME = "weather_topic";    
    public static final String MESSAGE_SIZE = "20971520";
    public static final int SIZE_CHUNK = 450;
    
    // Hive config
    public static final String HIVE_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String JDBC_DRIVER_NAME = "org.apache.hive.jdbc.HiveDriver";
    public static final String JDBC_HIVE_CONNECTION = "jdbc:hive2://quickstart.cloudera:10000/;ssl=false";
    
}
