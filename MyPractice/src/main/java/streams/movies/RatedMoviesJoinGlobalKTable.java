package streams.movies;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import streams.movies.model.Movie;
import streams.movies.model.RatedMovie;
import streams.movies.model.Rating;
import streams.movies.serializers.MovieCustomSerdes;

public class RatedMoviesJoinGlobalKTable {

  public static void main(String[] args) {

    MovieTopology.createTopics();

    final StreamsBuilder builder = new StreamsBuilder();

    KStream<String, Movie> movieStream = builder.stream(MovieTopology.MOVIES_TOPIC,
        Consumed.with(Serdes.String(), MovieCustomSerdes.Movie()));

    final GlobalKTable<String, Rating> ratings =
        builder.globalTable(MovieTopology.RATINGS_TOPIC,
            Materialized.<String, Rating, KeyValueStore<Bytes, byte[]>>as(
                MovieTopology.REKEYED_MOVIES_TOPIC)
                .withKeySerde(Serdes.String())
                .withValueSerde(MovieCustomSerdes.Rating()));


    KStream<String, RatedMovie> ratedMovies = movieStream.join(ratings,
        (movieId,movie) -> movie.getId(),
        (movie,rating) -> new RatedMovie(movie.getId(), movie.getTitle(), movie.getReleaseYear(),
            rating.getRating()));

    ratedMovies.print(Printed.toSysOut());
    ratedMovies.to(MovieTopology.RATED_MOVIES_TOPIC,
        Produced.with(Serdes.String(), MovieCustomSerdes.RatedMovie()));

    Topology topology = builder.build();
    final KafkaStreams streams = new KafkaStreams(topology,
        MovieTopology.createStreamsConfigProperties("moviesGlobalTable"));
    streams.cleanUp();

    streams.start();

    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
  }


}
