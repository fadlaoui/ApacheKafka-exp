package example;

import consumer.SimpleConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;

/**
 * <h1>Role</h1>
 * <p>Description </p>
 * <b>Note:</b>
 *
 * @author Mohamed Fadlaoui
 * @version 1.0
 * @projet kafka
 * @since 21:30 05/08/2017
 */
public class ConsumerSimpleExample {

    private final static String TOPIC = "test";
    private final static String GROUP_ID = "group-test";
    private final static String BOOTSTRAP_SERVER = "localhost:9092";

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("bootstrap.servers", BOOTSTRAP_SERVER);
        props.put("group.id", GROUP_ID);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");


        SimpleConsumer<byte[], byte[]> consumer = new SimpleConsumer<byte[], byte[]>(props, TOPIC);

        consumer.testConsumer();
    }

}
