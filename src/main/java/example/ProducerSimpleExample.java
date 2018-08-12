package example;

import org.apache.kafka.clients.producer.ProducerConfig;
import producer.SimpleProducer;

import java.io.Serializable;
import java.util.Properties;

/**
 * <h1>Role</h1>
 * <p>This a very basic example of a Producer , in this example we only use a single broker with no explicit partitionning
 * of messages </p>
 * <b>Note:</b>
 *
 * @author Mohamed Fadlaoui
 * @version 1.0
 * @projet kafka
 * @since 20:45 05/08/2017
 */
public class ProducerSimpleExample {

    private final static String TOPIC = "test";

    public static void main(String[] args) {
        Boolean syncSend = true;
        long noOfMessages = 5;
        long delay = 0; //DELAY BETWEEN EACH MESSAGE
        String messageType = "string";

        Properties producerConfig = new Properties();
        producerConfig.put("bootstrap.servers", "localhost:9092");
        producerConfig.put("client.id", "basic-producer");
        producerConfig.put("acks", "all");
        producerConfig.put("retries", "3");
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");

        SimpleProducer<byte[], byte[]> producer = new SimpleProducer<byte[], byte[]>(producerConfig, syncSend);

        for (int i = 0; i < noOfMessages; i++) {
            producer.send(TOPIC, getKey(i), getEvent(messageType, i));
            try {
                Thread.sleep(delay);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        producer.close();


    }

    private static byte[] getEvent(String messageType, int i) {
        return serialize("message" + i);
    }


    private static byte[] getKey(int i) {
        return serialize(new Integer(i));
    }

    public static byte[] serialize(final Object obj) {
        return org.apache.commons.lang3.SerializationUtils.serialize((Serializable) obj);
    }
}
