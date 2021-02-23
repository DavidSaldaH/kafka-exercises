package streams.movies.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import streams.movies.MovieTopology;
import streams.movies.model.Movie;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

public class MoviesProducer {


  private final ObjectMapper mapper = new ObjectMapper();

  private static KafkaProducer<String, Movie> createKafkaProducer() {

    Properties props = new Properties();
    props.put(BOOTSTRAP_SERVERS_CONFIG, MovieTopology.BOOTSTRAP_SERVERS);
    props.put(KEY_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer");
    props.put(VALUE_SERIALIZER_CLASS_CONFIG,
        "org.ogomez.practica.movies.serializers.MovieSerializer");

    return new KafkaProducer<>(props);

  }

  public static void main(String[] args)
      throws ExecutionException, InterruptedException, IOException, URISyntaxException {

    MoviesProducer app = new MoviesProducer();

    MovieTopology.createTopics();

    final Logger logger = LoggerFactory.getLogger(MoviesProducer.class);

    KafkaProducer<String, Movie> producer = createKafkaProducer();

    List<Movie> movies = app.getMoviesFromFile();

    for (Movie movie : movies) {
      ProducerRecord<String, Movie> record = new ProducerRecord<>(MovieTopology.MOVIES_TOPIC,
          movie.getId(), movie);
      producer.send(record, (recordMetadata, e) -> {
        if (e == null) {
          logger.info("Received new metadata. \n" +
              "Topic:" + recordMetadata.topic() + "\n" +
              "msgSize:" + recordMetadata.serializedValueSize() + "\n" +
              "Partition: " + recordMetadata.partition() + "\n" +
              "Offset: " + recordMetadata.offset() + "\n" +
              "Timestamp: " + recordMetadata.timestamp());
        } else {
          logger.error("Error while producing", e);
        }
      }).get();
    }
  }

  private List<Movie> getMoviesFromFile() throws URISyntaxException, IOException {

    return mapper.readValue(getFileFromResource(), new TypeReference<>() {
    });
  }

  private File getFileFromResource() throws URISyntaxException {

    ClassLoader classLoader = getClass().getClassLoader();
    URL resource = classLoader.getResource("movies.json");

    if (resource == null) {
      throw new IllegalArgumentException("file not found! " + "movies.json");
    } else {
      return new File(resource.toURI());
    }
  }
}
