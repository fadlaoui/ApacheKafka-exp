package consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.record.KafkaLZ4BlockInputStream;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <h1>Role</h1>
 * <p>Description </p>
 * <b>Note:</b>
 *
 * @author Mohamed Fadlaoui
 * @version 1.0
 * @projet kafka
 * @since 21:47 05/08/2017
 */
public class SimpleConsumer<K extends Serializable, V extends Serializable> {

    private final KafkaConsumer<K, V> consumer;

    public SimpleConsumer(Properties properties, String topic) {
        this.consumer = new KafkaConsumer<K, V>(properties);
        this.consumer.subscribe(Collections.singletonList(topic));

    }

    public void testConsumer() {
        while (true) {

            ConsumerRecords<K, V> records = consumer.poll(1000);
            for (ConsumerRecord<K, V> record : records) {

                System.out.printf("Received Message topic =%s, partition =%s, offset = %d, key = %s, value = %s\n", record.topic(), record.partition(), record.offset(), deserialize((byte[]) record.key()), deserialize((byte[]) record.value()));
            }

            consumer.commitSync();
        }
    }

    private static <V> V deserialize(final byte[] objectData) {
        return (V) org.apache.commons.lang3.SerializationUtils.deserialize(objectData);
    }
}
