package producer;


import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * <h1>Role</h1>
 * <p>Simple Producer </p>
 * <b>Note:</b>
 *
 * @author Mohamed Fadlaoui
 * @version 1.0
 * @projet kafka
 * @since 18:46 05/08/2017
 */
public class SimpleProducer<K extends Serializable, V extends Serializable> {

    private final Logger logger = LoggerFactory.getLogger(SimpleProducer.class);
    private boolean syncSend;
    private volatile boolean shutDown = false;
    private KafkaProducer<K, V> producer;

    public SimpleProducer(Properties producerConfig) {
        this(producerConfig, true);
    }

    public SimpleProducer(Properties producerConfig, boolean syncSend) {
        this.producer = new KafkaProducer<K, V>(producerConfig);
        this.syncSend = syncSend;
        this.logger.info("Started Producer Sync : {}", syncSend);
    }

    public void send(String topic, V v) {
        send(topic, -1, null, v, new DummyCallback());
    }

    public void send(String topic, K k, V v) {
        send(topic, -1, k, v, new DummyCallback());
    }

    public void send(String topic, int partition, V v) {
        send(topic, partition, null, v, new DummyCallback());
    }

    public void send(String topic, int partition, K k, V v) {
        send(topic, partition, k, v, new DummyCallback());
    }

    public void send(String topic, int partition, K k, V v, Callback callback) {
        if (shutDown) {
            throw new RuntimeException("Producer is closed");
        }

        try {
            ProducerRecord producerRecord;
            if (partition < 0)
                producerRecord = new ProducerRecord<K, V>(topic, k, v);
            else
                producerRecord = new ProducerRecord<K, V>(topic, partition, k, v);

            Future<RecordMetadata> future = producer.send(producerRecord, callback);
            if (!syncSend) return;
            future.get();
        } catch (Exception e) {
            logger.error("Error while producing event for topic : {}", topic, e);

        }
    }


    public void close() {
        shutDown = true;
        try {
            producer.close();
        } catch (Exception e) {
            logger.error("Exception occurred while stopping the producer", e);
        }

    }


    private class DummyCallback implements Callback {

        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if(e != null) {
                logger.error("Error while producing message to topic : {}" , recordMetadata.topic());
            }else {
                logger.debug("sent message to topic :{} partition : {} offset : {} ",recordMetadata.topic() , recordMetadata.partition() , recordMetadata.offset());
            }
        }
    }


}
