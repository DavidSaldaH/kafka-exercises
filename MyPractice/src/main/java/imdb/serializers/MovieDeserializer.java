package imdb.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import imdb.model.movie.Movie;

import java.io.IOException;
import java.util.Map;

public class MovieDeserializer implements Deserializer<Movie> {

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {

  }

  @Override
  public Movie deserialize(String value, byte[] bytes) {

    ObjectMapper objectMapper = new ObjectMapper();
    Movie pojo = null;

    try {
      pojo = objectMapper.readValue(bytes, Movie.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return pojo;
  }

  @Override
  public void close() {

  }
}
