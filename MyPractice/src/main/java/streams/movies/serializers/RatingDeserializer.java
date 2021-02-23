package streams.movies.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import streams.movies.model.Rating;

import java.io.IOException;
import java.util.Map;

public class RatingDeserializer implements Deserializer<Rating> {

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {

  }

  @Override
  public Rating deserialize(String value, byte[] bytes) {

    ObjectMapper objectMapper = new ObjectMapper();
    Rating pojo = null;

    try {
      pojo = objectMapper.readValue(bytes, Rating.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return pojo;
  }

  @Override
  public void close() {

  }
}
