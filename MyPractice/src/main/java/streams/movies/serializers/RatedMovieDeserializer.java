package streams.movies.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import streams.movies.model.RatedMovie;

import java.io.IOException;
import java.util.Map;

public class RatedMovieDeserializer implements Deserializer<RatedMovie> {

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {

  }

  @Override
  public RatedMovie deserialize(String value, byte[] bytes) {

    ObjectMapper objectMapper = new ObjectMapper();
    RatedMovie pojo = null;

    try {
      pojo = objectMapper.readValue(bytes, RatedMovie.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return pojo;
  }

  @Override
  public void close() {

  }
}
