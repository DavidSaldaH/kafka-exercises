package streams.movies.utils;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import streams.movies.MovieTopology;
import streams.movies.model.Movie;

import java.time.Duration;
import java.util.Properties;

import static java.util.Collections.singleton;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

public class MoviesConsumer {

  static final String BOOTSTRAP_SERVERS = "127.0.0.1:9092, 127.0.0.1:9093, 127.0.0.1:9094";
  static final String  CONSUMER_GROUP_ID = "rated-movies-group";

  static KafkaConsumer<String, Movie> createKafkaConsumer() {
    Properties props = new Properties();
    props.put(BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(GROUP_ID_CONFIG, CONSUMER_GROUP_ID);
    props.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(KEY_DESERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringDeserializer");
    props.put(VALUE_DESERIALIZER_CLASS_CONFIG,
        "org.ogomez.practica.movies.serializers.MovieDeserializer");

    KafkaConsumer<String, Movie> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(singleton(MovieTopology.MOVIES_TOPIC
    ));
    return consumer;
  }

  public static void main(String[] args) {

    Logger logger = LoggerFactory.getLogger(MoviesConsumer.class.getName());

    KafkaConsumer<String, Movie> consumer = createKafkaConsumer();

    while (true) {
      ConsumerRecords<String, Movie> records =
          consumer.poll(Duration.ofMillis(100));

      for (ConsumerRecord<String, Movie> record : records) {
        logger.info("Key: " + record.key() + ", Value: " + record.value());
        logger.info("Partition: " + record.partition() + ", Offset:" + record.offset());
      }
    }

  }

}
